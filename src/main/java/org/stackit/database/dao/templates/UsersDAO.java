package org.stackit.database.dao.templates;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.stackit.database.dao.DAO;
import org.stackit.database.entities.User;

public interface UsersDAO extends DAO {

    User getById(@Bind("id") int id);

    @Override @SqlUpdate
    void createTable();

    @Override
    void close();
}
