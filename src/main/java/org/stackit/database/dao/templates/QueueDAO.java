package org.stackit.database.dao.templates;

import org.stackit.database.dao.DAO;
import org.stackit.database.entities.QueueElement;

import java.util.List;

public interface QueueDAO extends DAO {

    List<QueueElement> getByUserUuid(String uuid);

    List<QueueElement> getAll();

    @Override
    void createTable();

    void delete(String uuid);
}
