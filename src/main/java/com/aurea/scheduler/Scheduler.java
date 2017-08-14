package com.aurea.scheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import one.util.streamex.StreamEx;

public class Scheduler {

    Projects projects;
    List<Person> persons;

    public Scheduler(Projects projects, List<Person> persons) {
        this.projects = projects;
        this.persons = persons;
    }

    private int leftToLearn(Person person) {
        return projects.projects.size() - person.knows.size();
    }

    private long leftToTeach(Project project) {
        return persons.stream().filter(p -> !p.knows.contains(project)).count();
    }

    private long numberOfNavigators(Project project) {
        return persons.stream().filter(p -> p.navigate.contains(project)).count();
    }

    private double remainingWorkload(Person p) {
        return leftToLearn(p) + p.navigate.stream().mapToDouble(this::remainingWorkload).sum();
    }

    private double remainingWorkload(Project project) {
        return leftToTeach(project) * 1.0 / numberOfNavigators(project);
    }

    public Optional<Project> canNavigate(Person navigator, Person driver) {
        return StreamEx.of(navigator.navigate).filter(project -> !driver.knows.contains(project)).sortedByDouble(
                project1 -> -remainingWorkload(project1)).findFirst();
    }

    private String getPersonStatusWithTheProject(Person person, Project project) {
        return person.navigate.contains(project) ? "N" : person.knows.contains(project) ? "+" : "-";
    }

    private String createPersonStatusWithProjects(Person person) {
        return StreamEx.of(projects.projects).map(project -> getPersonStatusWithTheProject(person, project)).joining("  ");
    }

    public void reportState() {
        persons.forEach(person -> System.out.println(
                createPersonStatusWithProjects(person) + "     " + person + ",  remaining workload: "
                        + remainingWorkload(person)));
    }

    public double getTotalRemainingWorkload() {
        return StreamEx.of(persons).mapToDouble(this::remainingWorkload).sum();
    }

    public void scheduleAll(int day) {
        Collection<Person> availablePeople = new ArrayList<>(persons);
        projects.projects.forEach(project -> scheduleAll(project, availablePeople, day));
    }

    public void scheduleAll(Project project, Collection<Person> availablePeople, int day) {
        Optional<Session> session = schedule(project, availablePeople);
        while (session.isPresent()) {
            System.out.println(session.get() + "," + day);
            availablePeople.remove(session.get().navigator);
            session.get().driver.knows.addAll(session.get().projects.projects);
            availablePeople.remove(session.get().driver);
            session = schedule(project, availablePeople);
        }
    }

    public Optional<Session> schedule(Project project, Collection<Person> availablePeople) {
        return StreamEx.of(availablePeople).filter(person -> person.navigate.contains(project)).flatMap(navigator -> StreamEx.of(availablePeople).filter(person -> !person.knows.contains(project)).map(person -> createSession(navigator, person))).sorted().findFirst();
    }

    private Session createSession(Person navigator, Person driver) {
        return new Session(createNavigationList(navigator, driver), navigator, driver);
    }

    private Projects createNavigationList(Person navigator, Person driver) {
        return new Projects(StreamEx.of(navigator.navigate).filter(project -> !driver.knows.contains(project)).toList());
    }
}