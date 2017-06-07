package org.stackit.database.dao.templates;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.stackit.database.dao.DAO;

public interface QueueDAO extends DAO {

    @Override @SqlUpdate
    void createTable();

    @Override
    void close();

    @SqlUpdate
    void delete(@Bind("id") int id);
}
