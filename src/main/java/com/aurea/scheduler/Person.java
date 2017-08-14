package com.aurea.scheduler;

import java.util.ArrayList;
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

    public boolean canNavigate(Project project)
    {
        return navigate.contains(project);
    }

    public boolean needNavigation(Project project) {
        return !knows.contains(project);
    }

    @Override
    public String toString() {
        return name;
    }
}
