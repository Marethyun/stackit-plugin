package org.stackit.database.mappers;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.stackit.database.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements ResultSetMapper<User> {

    @Override
    public User map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new User(r.getInt("id"), r.getString("uuid"), r.getString("email"), r.getInt("ingot"));
    }
}
