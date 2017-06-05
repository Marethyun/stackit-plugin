package org.stackit.database;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface QueueDAO extends DAO {

    @Override @SqlUpdate
    void createTable();

    @Override
    void close();

    @SqlUpdate
    void delete(@Bind("id") int id);
}
