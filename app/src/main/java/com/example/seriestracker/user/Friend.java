package com.example.seriestracker.user;

public class Friend {
    String name;
    int numberOfMatches;
    String status;

    public Friend(String name, int numberOfMatches) {
        this.name = name;
        this.numberOfMatches = numberOfMatches;
        this.status = "";
    }

    public String getName() {
        return name;
    }

    public int getNumberOfMatches() {
        return numberOfMatches;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }
}
