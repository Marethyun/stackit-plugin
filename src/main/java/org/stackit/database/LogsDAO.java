package org.stackit.database;

import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.stackit.database.entities.Log;

import java.sql.Date;
import java.util.List;

public interface LogsDAO extends DAO {

    @Override @SqlUpdate
    void createTable();

    @Override
    void close();

    @SqlQuery
    List<Log> getAll();

    @SqlUpdate
    void addLog(Date date, String log, String handler);
}
