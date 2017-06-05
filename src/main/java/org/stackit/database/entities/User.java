package org.stackit.database.entities;

public class User {
    private Integer id;
    private String UUID;
    private String email;
    private int ingots;

    public User(Integer id, String uuid, String email, int ingots){
        this.id = id;
        this.UUID = uuid;
        this.email = email;
        this.ingots = ingots;
    }
}
