package com.aurea.scheduler

import groovy.transform.TupleConstructor

@TupleConstructor
class PlannedSession {
    Session session
    int day
}
