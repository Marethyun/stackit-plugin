package org.stackit.database.dao.templates;

import org.stackit.database.dao.DAO;
import org.stackit.database.entities.Log;

import java.sql.Date;
import java.util.List;

public interface LogsDAO extends DAO {

    @Override
    void createTable();

    List<Log> getAll();

    void addLog(Date date, String log, String handler);
}
