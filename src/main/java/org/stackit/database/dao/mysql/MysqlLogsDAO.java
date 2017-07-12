package org.stackit.database.dao.mysql;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.stackit.database.dao.templates.LogsDAO;
import org.stackit.database.entities.Log;
import org.stackit.database.mappers.LogMapper;

import java.sql.Date;
import java.util.List;

public interface MysqlLogsDAO extends LogsDAO {
    @Override @SqlUpdate("CREATE TABLE IF NOT EXISTS `stackit_log`( `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT, `date` DATE NOT NULL, `log` VARCHAR(255) NOT NULL, `handler` VARCHAR(255) NOT NULL, PRIMARY KEY (`id`)) COLLATE='utf8_general_ci' ENGINE=InnoDB AUTO_INCREMENT=17 ; ")
    void createTable();

    @SqlQuery("SELECT id, date, handler, log FROM stackit_log")
    @Mapper(LogMapper.class)
    List<Log> getAll();

    @Override
    @SqlUpdate("INSERT INTO stackit_log (date, log, handler) VALUES (:date, :log, :handler)")
    void addLog(@Bind("date") Date date, @Bind("log") String log, @Bind("handler") String handler);
}
