package com.aurea.scheduler

import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.ValueRange
import one.util.streamex.IntStreamEx
import one.util.streamex.StreamEx
import org.apache.commons.lang3.StringUtils

class GoogleIOProcessor {
    private def spreadsheetId
    private def service

    GoogleIOProcessor(Sheets service, String spreadsheetId) {
        this.service = service
        this.spreadsheetId = spreadsheetId
    }

    void write(List<PlannedSession> plannedSessions, String tabName) throws IOException {
        List<List<Object>> values = StreamEx.of(plannedSessions).map{[it.session.projects.toString(), it.session.driver.toString(), it.session.navigator.toString(), "$it.dayStart-$it.dayFinish".toString()]}.toList()
        values.add(["", "", "", ""])

        ValueRange body = new ValueRange().setValues(values)
        String range = "$tabName!A2:D"

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
                def navigate = createProjectList(values, i, "Navigator", "Primary", "Secondary")
                def knows = createProjectList(values, i, "Driver")
                people.add(new Person(name, level, projects.get(navigate), projects.get(knows)))
            }
        }

        new Scheduler(projects, people)
    }

    private static List<String> createProjectList(List<List<Object>> values, int column, String... condition) {
        StreamEx.of(values)
                .skip(1)
                .filter { it.size() > column }
                .filter { StringUtils.containsAny(String.valueOf(it[column]), condition) }
                .map { String.valueOf(it[0]) }
                .toList()
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
