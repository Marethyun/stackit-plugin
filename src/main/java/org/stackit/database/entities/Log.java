package org.stackit.database.entities;

import java.sql.Date;

public class Log {
    private Integer id;
    private Date date;
    private String handler;
    private String log;

    public Log(Integer id, Date date, String handler, String log){
        this.id = id;
        this.date = date;
        this.handler = handler;
        this.log = log;
    }

    public Integer getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getHandler() {
        return handler;
    }

    public String getLog() {
        return log;
    }
}
