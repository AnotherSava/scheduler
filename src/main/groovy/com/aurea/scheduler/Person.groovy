package com.aurea.scheduler

import groovy.transform.TupleConstructor

@TupleConstructor
class Person {
    String name
    int level
    List<Project> navigate
    List<Project> knows

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
