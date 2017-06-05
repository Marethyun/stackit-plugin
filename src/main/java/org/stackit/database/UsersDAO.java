package org.stackit.database;

import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface UsersDAO extends DAO {

    @Override @SqlUpdate
    void createTable();

    @Override
    void close();
}
