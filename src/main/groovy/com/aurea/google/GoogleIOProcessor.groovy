package com.aurea.google

import com.aurea.scheduler.*
import com.google.api.services.sheets.v4.model.ValueRange
import one.util.streamex.StreamEx

import static com.aurea.google.Auth.sheetsService

class GoogleIOProcessor {
    private def spreadsheetId
    private def service

    GoogleIOProcessor(String spreadsheetId) {
        service = sheetsService
        this.spreadsheetId = spreadsheetId
    }

    void write(List<PlannedSession> plannedSessions) throws IOException {
        List<List<Object>> values = StreamEx.of(plannedSessions).map{[it.session.toString(), it.session.driver.toString(), it.session.navigator.toString(), it.day]}.toList()
        values.add(["", "", "", ""])

        ValueRange body = new ValueRange().setValues(values)
        String range = "Output!A2:D"

        service.spreadsheets().values().update(spreadsheetId, range, body)
                .setValueInputOption("RAW")
                .execute()
    }

    Scheduler read() throws IOException {
        String range = "Input!A1:Z"
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute()
        List<List<Object>> values = response.getValues()

        def projects = new Projects(StreamEx.of(values).skip(1).map {
            new Project(String.valueOf(it.get(0)), Integer.parseInt(String.valueOf(it.get(1))))
        }.toList())

        List<Person> people = new ArrayList<>()
        for (int i = 2; i < values.get(0).size(); i++) {
            def name = String.valueOf(values.get(0).get(i))
            def canNavigate = StreamEx.of(values)
                    .skip(1)
                    .filter { it.size() > i }
                    .filter { !String.valueOf(it.get(i)).isEmpty() }
                    .map { String.valueOf(it.get(0)) }
                    .toList()
            people.add(new Person(name, projects.get(canNavigate)))
        }

        new Scheduler(projects, people)
    }
}
