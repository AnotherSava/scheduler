package com.aurea.scheduler;

import java.util.ArrayList;
import java.util.List;

public class Person {
    public int id;
    public String name;

    public List<Project> knows;
    public List<Project> navigate;

    public Person(int id, List<Project> navigate) {
        this(id, navigate, "R" + id);
    }

    public Person(int id, List<Project> navigate, String name) {
        this.id = id;
        this.name = name;
        knows = new ArrayList<>(navigate);
        this.navigate = new ArrayList<>(navigate);
    }

    @Override
    public String toString() {
        return name;
    }
}
