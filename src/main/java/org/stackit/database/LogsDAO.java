package org.stackit.database;

import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface LogsDAO extends DAO {
    @Override @SqlUpdate
    void createTable();

    @Override
    void close();
}
