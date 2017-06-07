package org.stackit.database.dao;

import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface DAO {

    @SqlUpdate
    void createTable();

    void close();
}
