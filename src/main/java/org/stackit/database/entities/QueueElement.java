package org.stackit.database.entities;

public class QueueElement {
    private Integer id;
    private String uid;
    private String playerUuid;
    private String commands;
    private int slotNumber;
    private String name;

    public QueueElement(Integer id, String uid, String playerUuid, String commands, Integer slotNumber, String name){
        this.id = id;
        this.uid = uid;
        this.playerUuid = playerUuid;
        this.commands = commands;
        this.slotNumber = slotNumber;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getPlayerUuid() {
        return playerUuid;
    }

    public String getCommands() {
        return commands;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public String getName() {
        return name;
    }
}
