package com.aurea.scheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import one.util.streamex.StreamEx;

public class Scheduler {

    Projects projects;
    private List<Person> persons;
    private List<Session> sessions;

    public Scheduler(Projects projects, List<Person> persons) {
        this.projects = projects;
        this.persons = persons;
        sessions = new ArrayList<>();
    }

    private int leftToLearn(Person person) {
        return projects.projects.size() - person.knows.size();
    }

    private long leftToTeach(Project project) {
        return StreamEx.of(persons).filter(person -> person.needNavigation(project)).count();
    }

    private long numberOfNavigators(Project project) {
        return StreamEx.of(persons).filter(person -> person.canNavigate(project)).count();
    }

    private double remainingWorkload(Person person) {
        return leftToLearn(person) + StreamEx.of(person.navigate).mapToDouble(this::remainingWorkload).sum();
    }

    private double remainingWorkload(Project project) {
        return ((double) leftToTeach(project)) / numberOfNavigators(project);
    }

    private String getPersonStatusWithTheProject(Person person, Project project) {
        return person.canNavigate(project) ? "N" : person.needNavigation(project) ? "-" : "+";
    }

    private String createPersonStatusWithProjects(Person person) {
        return StreamEx.of(projects.projects).map(project -> getPersonStatusWithTheProject(person, project)).joining("  ");
    }

    public void reportState() {
        persons.forEach(person -> System.out.println(
                createPersonStatusWithProjects(person) + "     " + person + ",  remaining workload: " + remainingWorkload(person)));
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
            sessions.add(session.get());
            System.out.println(session.get() + "," + day);
            availablePeople.remove(session.get().navigator);
            session.get().driver.knows.addAll(session.get().projects.projects);
            availablePeople.remove(session.get().driver);
            session = schedule(project, availablePeople);
        }
    }

    public Optional<Session> schedule(Project project, Collection<Person> availablePeople) {
        return StreamEx.of(availablePeople)
                .filter(person -> person.canNavigate(project))
                .flatMap(navigator -> StreamEx.of(availablePeople)
                        .filter(person -> person.needNavigation(project))
                        .map(person -> createSession(navigator, person)))
                .sorted()
                .findFirst();
    }

    private Session createSession(Person navigator, Person driver) {
        Projects projects = new Projects(StreamEx.of(navigator.navigate).filter(driver::needNavigation).toList());
        return new Session(projects, navigator, driver);
    }

}