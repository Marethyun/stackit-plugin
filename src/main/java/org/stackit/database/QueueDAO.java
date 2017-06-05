package org.stackit.database;

import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface QueueDAO extends DAO {

    @Override @SqlUpdate
    void createTable();

    @Override
    void close();
}
