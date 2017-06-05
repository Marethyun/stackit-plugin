package org.stackit.database.mysql;

import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.stackit.database.LogsDAO;

public interface MysqlLogsDAO extends LogsDAO {
    @Override @SqlUpdate("CREATE TABLE `stackit_log` (`id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,`date` DATE NOT NULL,`handler` VARCHAR(255) NOT NULL,`log` VARCHAR(255) NOT NULL,PRIMARY KEY (`id`)ENGINE=InnoDB")
    void createTable();

    @Override
    void close();

    @SqlQuery("SELECT id, date, handler, log FROM stackit_log")
    void getAll();
}
