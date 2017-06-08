package org.stackit.database.dao.mysql;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.stackit.database.dao.templates.UsersDAO;
import org.stackit.database.entities.User;
import org.stackit.database.mappers.UserMapper;

public interface MysqlUsersDAO extends UsersDAO{
    @Override
    @Deprecated
    void createTable();

    @Override
    @SqlQuery("SELECT * FROM users WHERE id = :id")
    @Mapper(UserMapper.class)
    User getById(@Bind("id") int id);
}
