package com.aurea.google

import com.aurea.scheduler.PlannedSession
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.ValueRange
import one.util.streamex.StreamEx

import static com.aurea.google.Auth.sheetsService

class GoogleOutputProcessor {
    public static final String SPREADSHEET_ID = "1oqG81V2ozBDRr75rJ146bvD58ydsQ6bCh4PjNOOk4dE"

    static void write(List<PlannedSession> plannedSessions) throws IOException {
        Sheets service = sheetsService

        List<List<Object>> values = StreamEx.of(plannedSessions).map{[it.session.toString(), it.session.driver.toString(), it.session.navigator.toString(), it.day]}.toList()
        values.add(["", "", "", ""])

        ValueRange body = new ValueRange().setValues(values)
        String range = "Output!A2:D"

        service.spreadsheets().values().update(SPREADSHEET_ID, range, body)
                .setValueInputOption("RAW")
                .execute()
    }
}
