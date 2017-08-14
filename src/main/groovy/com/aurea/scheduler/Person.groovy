package com.aurea.scheduler

class Person {
    public String name

    public List<Project> knows
    public List<Project> navigate

    Person(String name, List<Project> navigate) {
        this.name = name
        knows = new ArrayList<>(navigate)
        this.navigate = new ArrayList<>(navigate)
    }

    boolean canNavigate(Project project) {
        navigate.contains(project)
    }

    boolean needNavigation(Project project) {
        !knows.contains(project)
    }

    @Override
    String toString() {
        name
    }
}
