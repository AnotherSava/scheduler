package com.aurea.scheduler;

import one.util.streamex.StreamEx;

public class Session implements Comparable<Session> {
    public Projects projects;
    public Person navigator;
    public Person driver;

    public Session(Projects projects, Person navigator, Person driver) {
        this.projects = new Projects(StreamEx.of(projects.projects).limit(3).toList());
        this.navigator = navigator;
        this.driver = driver;
    }

    @Override
    public String toString() {
        return projects + "," + driver + "," + navigator;
    }

    @Override
    public int compareTo(Session anotherSession) {
        return projects.compareTo(anotherSession.projects);
    }
}
