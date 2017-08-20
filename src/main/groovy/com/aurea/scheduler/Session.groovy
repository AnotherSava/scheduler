package com.aurea.scheduler

import one.util.streamex.StreamEx

class Session implements Comparable<Session> {
    Projects projects
    Person navigator
    Person driver

    Session(Projects projects, Person navigator, Person driver) {
        this.projects = new Projects(StreamEx.of(projects.projects).limit(3).toList())
        this.navigator = navigator
        this.driver = driver
    }

    @Override
    String toString() {
        projects.toString() + "," + driver + "," + navigator
    }

    @Override
    int compareTo(Session anotherSession) {
        Iterator<Project> iterator = projects.projects.iterator()
        Iterator<Project> anotherIterator = anotherSession.projects.projects.iterator()
        while (iterator.hasNext() || anotherIterator.hasNext()) {
            if (iterator.hasNext() && !anotherIterator.hasNext())
                return 1
            if (!iterator.hasNext() && anotherIterator.hasNext())
                return -1
            Project project = iterator.next()
            Project anotherProject = anotherIterator.next()
            if (project.priority != anotherProject.priority)
                return anotherProject.priority - project.priority
            if (driver.canNavigate(project) && !anotherSession.driver.canNavigate(anotherProject))
                return 1
            if (!driver.canNavigate(project) && anotherSession.driver.canNavigate(anotherProject))
                return -1
        }

        0
    }
}
