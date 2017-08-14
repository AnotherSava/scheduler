package com.aurea.scheduler;

public class Session {
    public Project project;
    public Person navigator;
    public Person driver;

    public Session(Project project, Person navigator, Person driver) {
        this.project = project;
        this.navigator = navigator;
        this.driver = driver;
    }

    @Override
    public String toString() {
//        return "session for project '" + project + "', navigator: '" + navigator + "', driver: '" + driver + "'";
        return project + "," + driver + "," + navigator;
    }
}
