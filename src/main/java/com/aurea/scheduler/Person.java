package com.aurea.scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Person {
    public String name;

    public List<Project> knows;
    public List<Project> navigate;

    public Person(String name, List<Project> navigate) {
        this.name = name;
        knows = new ArrayList<>(navigate);
        this.navigate = new ArrayList<>(navigate);
    }

    public Person(String name, Project... navigate) {
        this.name = name;
        knows = Arrays.asList(navigate);
        this.navigate = Arrays.asList(navigate);
    }

    @Override
    public String toString() {
        return name;
    }
}
