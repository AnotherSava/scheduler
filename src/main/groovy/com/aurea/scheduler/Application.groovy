package com.aurea.scheduler

import com.google.api.services.sheets.v4.Sheets
import one.util.streamex.StreamEx

import static com.aurea.common.GoogleAuth.getSheetsService

class Application {
    private static final String SPREADSHEET_ID = "1oqG81V2ozBDRr75rJ146bvD58ydsQ6bCh4PjNOOk4dE"

    public static final int NAVIGATOR_PER_PROJECT = 1

    private ArrayList<PlannedSession> plannedSessions
    private GoogleIOProcessor ioProcessor
    private Settings settings
    private Sheets service

    private Application() {
        service = sheetsService
        ioProcessor = new GoogleIOProcessor(service, SPREADSHEET_ID)
        settings = new SettingsSheetsParser(service, SPREADSHEET_ID).load()
    }

    private void schedule(String tabName, int driversLevel, int navigatorsLevel) {
        def daysPerSession = settings.getSessionLength(driversLevel, navigatorsLevel)
        assert daysPerSession > 0

        def scheduler = ioProcessor.read()
        scheduler.init(driversLevel, navigatorsLevel)

        System.out.println("Projects: \n\t" + StreamEx.of(scheduler.projects.projects).map { "$it.name ($it.priority)" }.joining("\n\t"))

        plannedSessions = new ArrayList<>()
        int day = 1
        while (true) {

            System.out.println()
            Reporter.reportState(scheduler)
            System.out.println()

            def sessions = scheduler.schedule()

            if (sessions.isEmpty())
                break

            sessions.each {
                def plannedSession = new PlannedSession(it, day, day + daysPerSession - 1)
                Reporter.reportSession(plannedSession)
                plannedSessions.add(plannedSession)
            }

            day += daysPerSession
        }

        ioProcessor.write(plannedSessions, tabName)
    }

    static void main(String[] args) {
        Application application = new Application()

        application.schedule("Output L1", 1, 1)
        application.schedule("Output L1-L2", 1, 2)
        application.schedule("Output L2", 2, 2)
        application.schedule("Output L2-L3", 2, 3)
        application.schedule("Output L3", 3, 3)
    }
}
