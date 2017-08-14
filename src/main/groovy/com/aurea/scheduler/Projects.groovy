package com.aurea.scheduler

import one.util.streamex.StreamEx

class Projects implements Comparable<Projects> {
    /** Sorted by priority */
    public List<Project> projects

    Projects(Collection<Project> projects) {
        this.projects = StreamEx.of(projects).sortedByInt { it.priority }.toList()
    }

    @Override
    int compareTo(Projects otherProjects) {
        Iterator<Project> iterator = projects.iterator()
        Iterator<Project> anotherIterator = otherProjects.projects.iterator()
        while (iterator.hasNext() || anotherIterator.hasNext()) {
            if (iterator.hasNext() && !anotherIterator.hasNext())
                return 1
            if (!iterator.hasNext() && anotherIterator.hasNext())
                return -1
            Project project = iterator.next()
            Project anotherProject = anotherIterator.next()
            if (project.priority != anotherProject.priority)
                return anotherProject.priority - project.priority
        }

        0
    }

    @Override
    String toString() {
        StreamEx.of(projects).joining(" | ")
    }

    List<Project> get(String... names) {
        List<String> namesList = Arrays.asList(names)
        namesList.forEach {checkName(it)}
        StreamEx.of(projects).filter { namesList.contains(it.name) }.toList()
    }

    private void checkName(String name) {
        if (StreamEx.of(projects).noneMatch { it.name == name }) {
            System.out.println("Invalid name: '" + name + "'")
        }
    }
}
