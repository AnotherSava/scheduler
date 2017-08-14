package com.aurea.scheduler;

public class Project {
    /** Less is more prioritized */
    public int priority;
    public String name;

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
