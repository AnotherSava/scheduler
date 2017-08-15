package com.aurea.scheduler

import one.util.streamex.StreamEx

class Scheduler {

    protected Projects projects
    protected List<Person> persons
    protected List<Session> sessions

    Scheduler(Projects projects, List<Person> persons) {
        this.projects = projects
        this.persons = persons
        sessions = new ArrayList<>()
    }

    private int leftToLearn(Person person) {
        projects.projects.size() - person.knows.size()
    }

    private long leftToTeach(Project project) {
        StreamEx.of(persons).filter{ it.needNavigation(project) }.count()
    }

    private long numberOfNavigators(Project project) {
        StreamEx.of(persons).filter{ it.canNavigate(project) }.count()
    }

    protected double remainingWorkloadForPerson(Person person) {
        leftToLearn(person) + StreamEx.of(person.navigate).mapToDouble{remainingWorkload(it)}.sum()
    }

    private double remainingWorkload(Project project) {
        ((double) leftToTeach(project)) / numberOfNavigators(project)
    }

    List<Session> schedule() {
        def newSessions = new ArrayList<>()
        def availablePeople = new ArrayList<>(persons)

        projects.projects.each { newSessions.addAll(scheduleAll(it, availablePeople)) }

        sessions.addAll(newSessions)

        newSessions
    }

    private static List<Session> scheduleAll(Project project, Collection<Person> availablePeople) {
        def newSessions = new ArrayList<>()
        def session
        def numberOfSessions = 0

        while (session = scheduleOne(project, availablePeople).orElse(null)) {
            newSessions.add(session)
            availablePeople.remove(session.navigator)
            session.driver.knows.addAll(session.projects.projects)
            availablePeople.remove(session.driver)
            if (++numberOfSessions >= Application.NAVIGATOR_PER_PROJECT) {
                break
            }
        }

        newSessions
    }

    private static Optional<Session> scheduleOne(Project project, Collection<Person> availablePeople) {
        StreamEx.of(availablePeople)
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
        new Session(projects, navigator, driver)
    }
}