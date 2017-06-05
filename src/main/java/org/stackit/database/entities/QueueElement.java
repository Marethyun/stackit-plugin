package org.stackit.database.entities;

public class QueueElement {
    private Integer id;
    private Integer user_id;
    private String commands;

    public QueueElement(Integer id, Integer user_id, String commands){
        this.id = id;
        this.user_id = id;
        this.commands = commands;
    }
}
