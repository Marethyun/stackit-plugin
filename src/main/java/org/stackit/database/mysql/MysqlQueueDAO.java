package org.stackit.database.mysql;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.stackit.database.QueueDAO;
import org.stackit.database.entities.QueueElement;
import org.stackit.database.entities.QueueMapper;

import java.util.List;

public interface MysqlQueueDAO extends QueueDAO {
    @Override @SqlUpdate("CREATE TABLE `stackit_queue` (`id` INT(11) NOT NULL AUTO_INCREMENT,`user_id` INT(11) NOT NULL,`commands` TEXT NOT NULL,PRIMARY KEY (`id`))COLLATE='utf8_general_ci'ENGINE=InnoDB")
    void createTable();

    @Override
    void close();

    @Mapper(QueueMapper.class)
    @SqlQuery("SELECT id, user_id, commands FROM stackit_queue")
    List<QueueElement> getAll();

    @Override
    @SqlUpdate("DELETE FROM stackit_queue WHERE id = :id")
    void delete(@Bind("id") int id);
}
