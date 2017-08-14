package com.aurea.scheduler;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import one.util.streamex.StreamEx;

public class Projects implements Comparable<Projects> {
    /** Sorted by priority */
    public List<Project> projects;

    public Projects(Collection<Project> projects) {
        this.projects = StreamEx.of(projects).sortedByInt(Project::getPriority).toList();
    }

    @Override
    public int compareTo(Projects otherProjects) {
        Iterator<Project> iterator = projects.iterator();
        Iterator<Project> anotherIterator = otherProjects.projects.iterator();
        while (iterator.hasNext() || anotherIterator.hasNext()) {
            if (iterator.hasNext() && !anotherIterator.hasNext())
                return 1;
            if (!iterator.hasNext() && anotherIterator.hasNext())
                return -1;
            Project project = iterator.next();
            Project anotherProject = anotherIterator.next();
            if (project.priority != anotherProject.priority)
                return anotherProject.priority - project.priority;
        }
        return 0;
    }

    @Override
    public String toString() {
        return StreamEx.of(projects).joining(" | ");
    }

    public List<Project> get(String... names) {
        List<String> namesList = Arrays.asList(names);
        namesList.forEach(this::checkName);
        return StreamEx.of(projects).filter(project -> namesList.contains(project.name)).toList();
    }

    private void checkName(String name) {
        if (StreamEx.of(projects).noneMatch(project -> project.name.equals(name))) {
            System.out.println("Invalid name: '" + name + "'");
        }
    }
}
