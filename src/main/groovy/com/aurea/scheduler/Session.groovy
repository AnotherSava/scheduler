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
        projects <=> anotherSession.projects
    }
}
