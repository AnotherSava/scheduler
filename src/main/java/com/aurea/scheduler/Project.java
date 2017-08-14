package com.aurea.scheduler;

public class Project {
    public int id;
    public String name;

    public Project(int id) {
        this(id, "P" + id);
    }

    public Project(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
