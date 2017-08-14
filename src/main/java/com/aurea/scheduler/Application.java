package com.aurea.scheduler;

import java.util.Arrays;
import java.util.List;
import one.util.streamex.StreamEx;

public class Application {
    private Scheduler scheduler;

    private Application() {
        prepareData();
    }

    /* Prints all information */
    private void fullReport() {
        System.out.println("Projects: \n\t" + StreamEx.of(scheduler.projects.projects).map(project -> project.name).joining("\n\t"));

        execute(true);
    }

    /* Prints report in CSV format, can be opened with Excel */
    private void csvReport() {
        execute(false);
    }

    private void execute(boolean fullReport) {
        double totalWorkload;
        int day = 1;
        do {
            totalWorkload = scheduler.getTotalRemainingWorkload();
            if (fullReport) {
                System.out.println();
                scheduler.reportState();
                System.out.println();
            }
            scheduler.scheduleAll(day++);
        } while (totalWorkload > scheduler.getTotalRemainingWorkload());
    }

    private void prepareData() {
        Projects projects = new Projects(Arrays.asList(
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
        ));

        List<Person> persons = Arrays.asList(
                new Person("Chethan", projects.get("Prologic")),
                new Person("Ganapati", projects.get("EPM Live", "Everest", "Infobright")),
                new Person("Grace", projects.get("Everest", "Infobright")),
                new Person("Isabela", projects.get("Prologic")),
                new Person("Radu", projects.get("Accept", "Agentek", "AlterPoint", "Artemis Views", "Artemis A7", "Artemis Finland", "Auto-trol TI", "Auto-trol Konfig", "Ecora", "EPM Live", "Prologic", "Purchasingnet", "Right90", "Smartform Design")),
                new Person("AJ", projects.get("Ignite", "NuView", "Acorn", "Gensym", "ObjectStore", "SenSage", "Clear", "Corizon", "ETI", "Ravenflow", "StillSecure", "TenFold")),
                new Person("Marino", projects.get("Ignite", "NuView", "Acorn", "Gensym", "ObjectStore", "SenSage", "Clear", "Corizon", "ETI", "Ravenflow", "StillSecure", "TenFold")));

        scheduler = new Scheduler(projects, persons);
    }

    public static void main(String[] args) {
        Application application = new Application();

        /* Prints all information */
        application.fullReport();

        /* Prints report in CSV format, can be opened with Excel */
//        application.csvReport();
    }
}
