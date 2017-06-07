package org.stackit.database.dao.templates;

import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.stackit.database.dao.DAO;

public interface UsersDAO extends DAO {

    @Override @SqlUpdate
    void createTable();

    @Override
    void close();
}
