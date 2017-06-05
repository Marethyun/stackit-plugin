package org.stackit.database.entities;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QueueMapper implements ResultSetMapper<QueueElement> {

    @Override
    public QueueElement map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new QueueElement(r.getInt("id"), r.getInt("user_id"), r.getString("commands"));
    }
}
