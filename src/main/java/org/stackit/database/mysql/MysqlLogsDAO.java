package org.stackit.database.mysql;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.stackit.database.LogsDAO;
import org.stackit.database.entities.Log;
import org.stackit.database.entities.LogMapper;

import java.sql.Date;
import java.util.List;

public interface MysqlLogsDAO extends LogsDAO {
    @Override @SqlUpdate("CREATE TABLE `stackit_log` (`id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,`date` DATE NOT NULL,`handler` VARCHAR(255) NOT NULL,`log` VARCHAR(255) NOT NULL,PRIMARY KEY (`id`)ENGINE=InnoDB")
    void createTable();

    @Override
    void close();

    @SqlQuery("SELECT id, date, handler, log FROM stackit_log")
    @Mapper(LogMapper.class)
    List<Log> getAll();

    @Override
    @SqlQuery("INSERT INTO stackit_log (date, log, handler) VALUES (:date, :log, :handler)")
    void addLog(@Bind("date") Date date, @Bind("log") String log, @Bind("handler") String handler);
}
