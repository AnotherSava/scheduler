package com.aurea.scheduler

class Settings {
    public static final int SESSION_LENGTH_ERROR = -1

    private int [][] sessionLength

    Settings(int[][] sessionLength) {
        this.sessionLength = sessionLength
    }

    int getSessionLength(int driversLevel, int navigatorsLevel)
    {
        return sessionLength[driversLevel - 1][navigatorsLevel - 1]
    }
}
