package com.aurea.scheduler

class Person {
    public String name

    List<Project> knows
    List<Project> navigate
    int level

    Person(String name, int level, List<Project> navigate) {
        this.name = name
        this.level = level
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
        "$name (L$level)"
    }
}
