package org.stackit.database.dao.proxy;

import org.skife.jdbi.v2.Handle;
import org.stackit.database.DatabaseManager;
import org.stackit.database.dao.Proxy;
import org.stackit.database.dao.templates.LogsDAO;
import org.stackit.database.entities.Log;

import java.sql.Date;
import java.util.List;

public class LogsProxy implements LogsDAO, Proxy {

    private Class<LogsDAO> substituted;

    public LogsProxy(Class<LogsDAO> substituted) {
        this.substituted = substituted;
    }

    @Override
    public void createTable() {
        Handle h = DatabaseManager.getDatabaseHandle();
        h.attach(substituted).createTable();
        h.close();
    }

    @Override
    public List<Log> getAll() {
        Handle h = DatabaseManager.getDatabaseHandle();
        List<Log> logs = h.attach(substituted).getAll();
        h.close();
        return logs;
    }

    @Override
    public void addLog(Date date, String log, String handler) {
        Handle h = DatabaseManager.getDatabaseHandle();
        h.attach(substituted).addLog(date, log, handler);
        h.close();
    }
}
