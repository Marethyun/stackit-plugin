package org.stackit.database.dao.templates;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.stackit.database.dao.DAO;
import org.stackit.database.entities.QueueElement;

import java.util.List;

public interface QueueDAO extends DAO {

    @SqlQuery
    List<QueueElement> getByUserId(@Bind("user_id") int user_id);

    @Override @SqlUpdate
    void createTable();

    @Override
    void close();

    @SqlUpdate
    void delete(@Bind("id") int id);
}
