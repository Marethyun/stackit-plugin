package org.stackit.database.entities;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LogMapper implements ResultSetMapper<Log> {
    @Override
    public Log map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new Log(r.getInt("id"), r.getDate("date"), r.getString("handler"), r.getString("log"));
    }
}
