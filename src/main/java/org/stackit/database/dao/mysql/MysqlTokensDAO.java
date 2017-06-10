package org.stackit.database.dao.mysql;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.stackit.database.dao.templates.TokensDAO;
import org.stackit.database.entities.Token;
import org.stackit.database.mappers.TokenMapper;

import java.util.List;

public interface MysqlTokensDAO extends TokensDAO {
    @Override
    @SqlUpdate("CREATE TABLE `stackit_tokens`(`id` INT(11) NOT NULL AUTO_INCREMENT,`time` BIGINT(20) NULL DEFAULT NULL,`value` VARCHAR(255) NULL DEFAULT NULL,PRIMARY KEY (`id`))ENGINE=InnoDB")
    void createTable();

    @Override
    @SqlQuery("SELECT * FROM stackit_tokens WHERE value = :value")
    @Mapper(TokenMapper.class)
    Token getByValue(@Bind("value")String value);

    @Override
    @SqlQuery("SELECT * FROM stackit_tokens")
    @Mapper(TokenMapper.class)
    List<Token> getAll();
}
