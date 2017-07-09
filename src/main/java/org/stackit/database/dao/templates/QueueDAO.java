package org.stackit.database.dao.templates;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.stackit.database.dao.DAO;
import org.stackit.database.entities.QueueElement;

import java.util.List;

public interface QueueDAO extends DAO {

    List<QueueElement> getByUserUuid(@Bind("user_uuid") String uuid);

    @Override
    void createTable();

    void delete(@Bind("id") String uuid);
}
