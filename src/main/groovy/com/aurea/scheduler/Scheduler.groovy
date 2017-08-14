package com.aurea.scheduler

import one.util.streamex.StreamEx

class Scheduler {

    protected Projects projects
    private List<Person> persons
    private List<Session> sessions

    Scheduler(Projects projects, List<Person> persons) {
        this.projects = projects
        this.persons = persons
        sessions = new ArrayList<>()
    }

    private int leftToLearn(Person person) {
        return projects.projects.size() - person.knows.size()
    }

    private long leftToTeach(Project project) {
        return StreamEx.of(persons).filter{ it.needNavigation(project) }.count()
    }

    private long numberOfNavigators(Project project) {
        return StreamEx.of(persons).filter{ it.canNavigate(project) }.count()
    }

    private double remainingWorkload(Person person) {
        return leftToLearn(person) + StreamEx.of(person.navigate).mapToDouble{remainingWorkload(it)}.sum()
    }

    private double remainingWorkload(Project project) {
        return ((double) leftToTeach(project)) / numberOfNavigators(project)
    }

    private static String getPersonStatusWithTheProject(Person person, Project project) {
        return person.canNavigate(project) ? "N" : person.needNavigation(project) ? "-" : "+"
    }

    private String createPersonStatusWithProjects(Person person) {
        return StreamEx.of(projects.projects).map { getPersonStatusWithTheProject(person, it) }.joining("  ")
    }

    void reportState() {
        persons.forEach { System.out.println(
                createPersonStatusWithProjects(it) + "     " + it + ",  remaining workload: " + remainingWorkload(it))}
    }

    double getTotalRemainingWorkload() {
        return StreamEx.of(persons).mapToDouble{remainingWorkload(it)}.sum()
    }

    void schedule(int day) {
        Collection<Person> availablePeople = new ArrayList<>(persons)
        projects.projects.forEach { scheduleAll(it, availablePeople, day) }
    }

    private void scheduleAll(Project project, Collection<Person> availablePeople, int day) {
        Optional<Session> session = scheduleAll(project, availablePeople)
        while (session.isPresent()) {
            sessions.add(session.get())
            System.out.println(session.get().toString() + "," + day)
            availablePeople.remove(session.get().navigator)
            session.get().driver.knows.addAll(session.get().projects.projects)
            availablePeople.remove(session.get().driver)
            session = scheduleAll(project, availablePeople)
        }
    }

    private static Optional<Session> scheduleAll(Project project, Collection<Person> availablePeople) {
        return StreamEx.of(availablePeople)
                .filter { it.canNavigate(project) }
                .flatMap {
                    navigator -> StreamEx.of(availablePeople)
                        .filter { it.needNavigation(project) }
                        .map { createSession(navigator, it) }
                }
                .sorted()
                .findFirst()
    }

    private static Session createSession(Person navigator, Person driver) {
        Projects projects = new Projects(StreamEx.of(navigator.navigate).filter{driver.needNavigation(it) }.toList())
        return new Session(projects, navigator, driver)
    }
}