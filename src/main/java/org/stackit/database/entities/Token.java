package org.stackit.database.entities;

public class Token {
    private int id;
    private long time;
    private String value;

    public Token(int id, long time, String value) {
        this.id = id;
        this.time = time;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public String getValue() {
        return value;
    }
}
