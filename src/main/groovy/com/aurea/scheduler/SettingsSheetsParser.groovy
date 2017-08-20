package com.aurea.scheduler

import com.google.api.services.sheets.v4.Sheets

class SettingsSheetsParser {
    private static final String SETTINGS_LENGTH_RANGE = "Settings: Training length!D5:F7"

    private Sheets service
    private String spreadsheetId

    SettingsSheetsParser(Sheets service, String spreadsheetId) {
        this.service = service
        this.spreadsheetId = spreadsheetId
    }

    Settings load() throws IOException {
        List<List<Object>> values = readValues(SETTINGS_LENGTH_RANGE)

        int [][] sessionLength = new int[3][3]

        for (int di = 0; di < 3; di++) {
            for (int ni = 0; ni < 3; ni++) {
                sessionLength[di][ni] = values.size() < di + 1 || values[di].size() < ni + 1 || String.valueOf(values[di][ni]).empty ? Settings.SESSION_LENGTH_ERROR : Integer.parseInt(String.valueOf(values[di][ni]))
           }
        }

        new Settings(sessionLength)
    }

    private List<List<Object>> readValues(String range) {
        service.spreadsheets().values().get(spreadsheetId, range).execute().getValues()
    }
}
