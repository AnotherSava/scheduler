package com.aurea.google

import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.ValueRange

import static com.aurea.google.Auth.getSheetsService

class Quickstart {

    public static final String SPREADSHEET_ID = "1oqG81V2ozBDRr75rJ146bvD58ydsQ6bCh4PjNOOk4dE"

    static void main(String[] args) throws IOException {
        // Build a new authorized API client service.
        Sheets service = getSheetsService

        sampleRead(service)
        sampleWrite(service)
    }

    /**
     * Prints the names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
     */
    private static void sampleRead(Sheets service) throws IOException {
        String range = "Class Data!A2:E"
        ValueRange response = service.spreadsheets().values()
                .get(SPREADSHEET_ID, range)
                .execute()
        List<List<Object>> values = response.getValues()
        if (values == null || values.size() == 0) {
            System.out.println("No data found.")
        } else {
            System.out.println("Name, Major")
            for (List row : values) {
                // Print columns A and E, which correspond to indices 0 and 4.
                System.out.printf("%s, %s\n", row.get(0), row.get(4))
            }
        }
    }

    private static void sampleWrite(Sheets service) throws IOException {
        List<List<Object>> values = Arrays.asList(
                Arrays.asList("Top left", "Top right"),
                Arrays.asList("Bottom left", "Bottom right")
        )
        ValueRange body = new ValueRange().setValues(values)
        String range = "Class Data!I10:J11"

        service.spreadsheets().values().update(SPREADSHEET_ID, range, body)
                        .setValueInputOption("RAW")
                        .execute()
    }
}
