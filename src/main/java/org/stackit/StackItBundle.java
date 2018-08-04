package org.stackit;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class StackItBundle extends JavaPlugin {
    protected String name;
    protected String description = "No description";
    protected String author = "No author";
    protected String version = "No version";

    public StackItBundle(String name, String description, String author, String version){
        this.name = name;
        this.description = description;
        this.author = author;
        this.version = version;
    }

    public StackItBundle(String name){
        this.name = name;
    }
}
