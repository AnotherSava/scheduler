package com.aurea.scheduler

import com.google.api.services.sheets.v4.model.ValueRange
import one.util.streamex.IntStreamEx
import one.util.streamex.StreamEx

import static com.aurea.common.GoogleAuth.sheetsService

class GoogleIOProcessor {
    private def spreadsheetId
    private def service

    GoogleIOProcessor(String spreadsheetId) {
        service = sheetsService
        this.spreadsheetId = spreadsheetId
    }

    void write(List<PlannedSession> plannedSessions) throws IOException {
        List<List<Object>> values = StreamEx.of(plannedSessions).map{[it.session.projects.toString(), it.session.driver.toString(), it.session.navigator.toString(), "$it.dayStart-$it.dayFinish".toString()]}.toList()
        values.add(["", "", "", ""])

        ValueRange body = new ValueRange().setValues(values)
        String range = "Output!A2:D"

        service.spreadsheets().values().update(spreadsheetId, range, body)
                .setValueInputOption("RAW")
                .execute()
    }

    Scheduler read() throws IOException {
        def data = IntStreamEx.ints()
                .limit(3)
                .mapToObj { readValues("Input L" + (it + 1) + "!A1:Z") }
                .toList()

        def projects = new Projects(parseProjects(data))

        List<Person> people = new ArrayList<>()

        for (def level = 1; level <= 3; level++) {
            def values = data[level - 1]
            for (int i = 2; i < values[0].size(); i++) {
                def name = String.valueOf(values[0][i])
                def canNavigate = StreamEx.of(values)
                        .skip(1)
                        .filter { it.size() > i }
                        .filter { !String.valueOf(it[i]).isEmpty() }
                        .map { String.valueOf(it[0]) }
                        .toList()
                people.add(new Person(name, level, projects.get(canNavigate)))
            }
        }

        new Scheduler(projects, people)
    }

    private static Set<Project> parseProjects(List<List<List<Object>>> data) {
        StreamEx.of(data)
                .flatMap{ StreamEx.of(it) }
                .filter { String.valueOf(it[1]) != "Priority" }
                .map { new Project(String.valueOf(it[0]), Integer.parseInt(String.valueOf(it[1]))) }
                .toSet()
    }

    private List<List<Object>> readValues(String range) {
        service.spreadsheets().values().get(spreadsheetId, range).execute().getValues()
    }
}
