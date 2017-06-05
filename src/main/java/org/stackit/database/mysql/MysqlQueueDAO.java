package org.stackit.database.mysql;

import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.stackit.database.QueueDAO;
import org.stackit.database.entities.QueueElement;

import java.util.List;

public interface MysqlQueueDAO extends QueueDAO {
    @Override @SqlUpdate("CREATE TABLE `stackit_queue` (`id` INT(11) NOT NULL AUTO_INCREMENT,`user_id` INT(11) NOT NULL,`commands` TEXT NOT NULL,PRIMARY KEY (`id`))COLLATE='utf8_general_ci'ENGINE=InnoDB")
    void createTable();

    @Override
    void close();

    @SqlQuery("SELECT id, user_id, commands FROM stackit_queue")
    List<QueueElement> getAll();
}
