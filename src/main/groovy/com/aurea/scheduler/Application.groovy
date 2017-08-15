package com.aurea.scheduler

import com.aurea.google.GoogleIOProcessor
import one.util.streamex.StreamEx

class Application {
    private static final String SPREADSHEET_ID = "1oqG81V2ozBDRr75rJ146bvD58ydsQ6bCh4PjNOOk4dE"

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

            def sessions = scheduler.schedule()

            if (sessions.isEmpty())
                break

            def newPlannedSessions = StreamEx.of(sessions).map { new PlannedSession(it, day) }.toList()

            Reporter.reportSessions(newPlannedSessions)
            plannedSessions.addAll(newPlannedSessions)
            day++
        }

        ioProcessor.write(plannedSessions)
    }

    static void main(String[] args) {
        Application application = new Application()

        application.schedule()
    }
}
