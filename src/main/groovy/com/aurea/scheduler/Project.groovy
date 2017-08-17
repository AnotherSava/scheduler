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
    int hashCode() {
        return name.toLowerCase().hashCode() + priority
    }

    @Override
    boolean equals(Object obj) {
        return obj instanceof  Project && name.equalsIgnoreCase(obj.name) && priority == obj.priority
    }

    @Override
    String toString() {
        name
    }
}
