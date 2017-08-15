package com.aurea.scheduler

import com.aurea.google.GoogleOutputProcessor
import one.util.streamex.StreamEx

class Application {

    private Scheduler scheduler
    private List<Person> people
    private ArrayList<PlannedSession> plannedSessions

    private Application() {
        prepareData()
    }

    private void prepareData() {
        Projects projects = new Projects([
                new Project("Accept", 17),
                new Project("Acorn", 9),
                new Project("Agentek", 16),
                new Project("AlterPoint", 26),
                new Project("Artemis A7", 8),
                new Project("Artemis Views", 8),
                new Project("Artemis Finland", 27),
                new Project("Auto-trol TI", 14),
                new Project("Auto-trol Konfig", 14),
                new Project("Clear", 28),
                new Project("Corizon", 21),
                new Project("Ecora", 18),
                new Project("EPM Live", 5),
                new Project("ETI", 25),
                new Project("Everest", 1),
                new Project("Gensym", 6),
                new Project("Ignite", 7),
                new Project("Infobright", 12),
                new Project("NuView", 2),
                new Project("ObjectStore", 10),
                new Project("Placeable", 24),
                new Project("Prologic", 4),
                new Project("Purchasingnet", 13),
                new Project("Ravenflow", 23),
                new Project("Right90", 19),
                new Project("SenSage", 11),
                new Project("Smartform Design", 20),
                new Project("StillSecure", 29),
                new Project("TenFold", 22),
                new Project("TriActive", 15)
        ])

        people = [
                new Person("Chethan", projects.get("Prologic")),
                new Person("Ganapati", projects.get("EPM Live", "Everest", "Infobright")),
                new Person("Grace", projects.get("Everest", "Infobright")),
                new Person("Isabela", projects.get("Prologic")),
                new Person("Radu", projects.get("Accept", "Agentek", "AlterPoint", "Artemis Views", "Artemis A7", "Artemis Finland", "Auto-trol TI", "Auto-trol Konfig", "Ecora", "EPM Live", "Prologic", "Purchasingnet", "Right90", "Smartform Design")),
                new Person("AJ", projects.get("Ignite", "NuView", "Acorn", "Gensym", "ObjectStore", "SenSage", "Clear", "Corizon", "ETI", "Ravenflow", "StillSecure", "TenFold")),
                new Person("Marino", projects.get("Ignite", "NuView", "Acorn", "Gensym", "ObjectStore", "SenSage", "Clear", "Corizon", "ETI", "Ravenflow", "StillSecure", "TenFold"))
        ]

        scheduler = new Scheduler(projects, people)
    }

    /* Prints all information */
    private void fullReport() {
        System.out.println("Projects: \n\t" + StreamEx.of(scheduler.projects.projects).map { it.name }.joining("\n\t"))

        execute(true)
    }

    /* Prints report in CSV format, can be opened with Excel */
    private void csvReport() {
        execute(false)
    }

    private void execute(boolean fullReport) {
        plannedSessions = new ArrayList<>()
        int day = 1
        while (true) {

            if (fullReport) {
                System.out.println()
                Reporter.reportState(scheduler)
                System.out.println()
            }
            def sessions = scheduler.schedule()

            if (sessions.isEmpty())
                break

            def newPlannedSessions = StreamEx.of(sessions).map { new PlannedSession(it, day) }.toList()

            Reporter.reportSessions(newPlannedSessions)
            plannedSessions.addAll(newPlannedSessions)
            day++
        }
    }

    static void main(String[] args) {
        Application application = new Application()

        /* Prints all information */
        application.fullReport()

        /* Write output to google document */
        GoogleOutputProcessor.write(application.plannedSessions)

        /* Prints report in CSV format, can be opened with Excel */
//        application.csvReport()
    }
}
