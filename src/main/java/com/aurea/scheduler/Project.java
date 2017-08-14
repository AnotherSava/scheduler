package com.aurea.scheduler;

public class Project {
    public String name;

    /** Less is more prioritized */
    public int priority;

    public Project(String name, int priority) {
        this.priority = priority;
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return name;
    }
}
