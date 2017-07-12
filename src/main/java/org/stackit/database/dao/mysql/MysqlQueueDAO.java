package org.stackit.database.dao.mysql;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.stackit.database.dao.templates.QueueDAO;
import org.stackit.database.entities.QueueElement;
import org.stackit.database.mappers.QueueMapper;

import java.util.List;

public interface MysqlQueueDAO extends QueueDAO {

    @Override
    @Mapper(QueueMapper.class)
    @SqlQuery("SELECT * FROM stackit_queue WHERE player_uuid = :uuid")
    List<QueueElement> getByUserUuid(@Bind("uuid") String uuid);

    @Override @SqlUpdate("CREATE TABLE IF NOT EXISTS `stackit_queue`( `id` INT(11) NOT NULL AUTO_INCREMENT, `uid` VARCHAR(8) NULL, `player_uuid` VARCHAR(255) NOT NULL, `commands` VARCHAR(1500) NOT NULL, `slotnumber` INT(11) UNSIGNED NOT NULL, `name` VARCHAR(255) NOT NULL, PRIMARY KEY (`id`)) COLLATE='utf8_general_ci' ENGINE=InnoDB AUTO_INCREMENT=4 ; ")
    void createTable();

    @Mapper(QueueMapper.class)
    @SqlQuery("SELECT * FROM stackit_queue")
    List<QueueElement> getAll();

    @Override
    @SqlUpdate("DELETE FROM stackit_queue WHERE uid = :uid")
    void delete(@Bind("uid") String uid);
}
