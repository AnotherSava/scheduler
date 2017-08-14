package com.aurea.scheduler;

import java.util.Arrays;

public class Application {
    public static void main(String[] args) {
        Project project1 = new Project(1);
        Project project2 = new Project(2);
        Project project3 = new Project(3);
        Project project4 = new Project(4);
        Project project5 = new Project(5);
        Project project6 = new Project(6);

        Person person1 = new Person(1, Arrays.asList(project2, project5));
        Person person2 = new Person(2, Arrays.asList(project1, project4));
        Person person3 = new Person(3, Arrays.asList(project2, project6));
        Person person4 = new Person(4, Arrays.asList(project3));
        Person person5 = new Person(5, Arrays.asList(project1, project5));

        Scheduler scheduler = new Scheduler(Arrays.asList(project1, project2, project3, project4, project5, project6), Arrays.asList(person1, person2, person3, person4, person5));

        double totalWorkload;
        int day = 1;
        do {
            totalWorkload = scheduler.getTotalRemainingWorkload();
            scheduler.reportState();
//            scheduler.reportWorkload();
            System.out.println();
            scheduler.schedule(scheduler.persons, day++);
            System.out.println();
        } while (totalWorkload > scheduler.getTotalRemainingWorkload());
    }
}
