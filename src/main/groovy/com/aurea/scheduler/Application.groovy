package com.aurea.scheduler

import one.util.streamex.StreamEx

class Application {
    private static final String SPREADSHEET_ID = "1oqG81V2ozBDRr75rJ146bvD58ydsQ6bCh4PjNOOk4dE"

    private static final int DAYS_PER_SESSION = 2
    public static final int NAVIGATOR_PER_PROJECT = 1
    private static final int DRIVERS_LEVEL = 3
    private static final int NAVIGATORS_LEVEL = 3

    private Scheduler scheduler
    private ArrayList<PlannedSession> plannedSessions
    private GoogleIOProcessor ioProcessor

    private Application() {
        ioProcessor = new GoogleIOProcessor(SPREADSHEET_ID)
        scheduler = ioProcessor.read()
    }

    private void schedule() {
        System.out.println("Projects: \n\t" + StreamEx.of(scheduler.projects.projects).map { "$it.name ($it.priority)" }.joining("\n\t"))

        plannedSessions = new ArrayList<>()
        int day = 1
        while (true) {

            System.out.println()
            Reporter.reportState(scheduler)
            System.out.println()

            def sessions = scheduler.schedule(DRIVERS_LEVEL, NAVIGATORS_LEVEL)

            if (sessions.isEmpty())
                break

            sessions.each {
                def plannedSession = new PlannedSession(it, day, day + DAYS_PER_SESSION - 1)
                Reporter.reportSession(plannedSession)
                plannedSessions.add(plannedSession)
            }

            day += DAYS_PER_SESSION
        }

        ioProcessor.write(plannedSessions)
    }

    static void main(String[] args) {
        Application application = new Application()

        application.schedule()
    }
}
