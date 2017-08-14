package com.aurea.scheduler

class Project {
    public String name

    /** Less is more prioritized */
    public int priority

    Project(String name, int priority) {
        this.priority = priority
        this.name = name
    }

    @Override
    String toString() {
        name
    }
}
