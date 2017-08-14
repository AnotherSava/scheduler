package com.aurea.scheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import one.util.streamex.StreamEx;

public class Scheduler {
    List<Project> projects;
    List<Person> persons;

    public Scheduler(List<Project> projects, List<Person> persons) {
        this.projects = projects;
        this.persons = persons;
    }

    private int leftToLearn(Person person) {
        return projects.size() - person.knows.size();
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

    public void schedule(Collection<Person> people, int day) {
        scheduleSorted(StreamEx.of(people).sortedByDouble(p -> -remainingWorkload(p)).toList(), day);
    }

    public Optional<Project> canNavigate(Person navigator, Person driver) {
        return StreamEx.of(navigator.navigate).filter(project -> !driver.knows.contains(project)).sortedByDouble(
                project1 -> -remainingWorkload(project1)).findFirst();
    }

    private Optional<Session> createSession(Optional<Project> project, Person navigator, Person driver) {
        return project.map(project1 -> new Session(project1, navigator, driver));
    }

    public Optional<Session> findProject(Person person1, Person person2) {
        Optional<Project> project1 = canNavigate(person1, person2);
        Optional<Project> project2 = canNavigate(person2, person1);
        if (!project1.isPresent())
            return createSession(project2, person2, person1);
        if (!project2.isPresent())
            return createSession(project1, person1, person2);

        return remainingWorkload(project1.get()) > remainingWorkload(project2.get()) ?
                createSession(project1, person1, person2) : createSession(project2, person2, person1);
    }

    public void scheduleSorted(List<Person> people, int day) {
        if (people.size() < 2)
            return;

        Person mostBusy = people.get(0);
        for (Person person: people.subList(1, people.size())) {
            Optional<Session> session = findProject(mostBusy, person);
            if (!session.isPresent())
                continue;

            System.out.println(session.get().toString() + "," + day);
            session.get().driver.knows.add(session.get().project);
            List<Person> remainingPeople = new ArrayList<>(people);
            remainingPeople.remove(session.get().driver);
            remainingPeople.remove(session.get().navigator);
            scheduleSorted(remainingPeople, day);
            break;
        }
    }

    private String getPersonStatusWithTheProject(Person person, Project project) {
        return  person.navigate.contains(project) ? "N" : person.knows.contains(project) ? "+" : "-";
    }

    private String createPersonStatusWithProjects(Person person) {
        return StreamEx.of(projects).map(project -> getPersonStatusWithTheProject(person, project)).joining("  ");
    }

    public void reportState() {
        persons.forEach(person -> System.out.println(person + ":   " + createPersonStatusWithProjects(person) + "   remaining workload: " + remainingWorkload(person)));
    }

    public double getTotalRemainingWorkload() {
        return StreamEx.of(persons).mapToDouble(this::remainingWorkload).sum();
    }
}
