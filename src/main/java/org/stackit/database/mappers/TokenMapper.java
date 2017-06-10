package org.stackit.database.mappers;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.stackit.database.entities.Token;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TokenMapper implements ResultSetMapper<Token> {
    @Override
    public Token map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new Token(r.getInt("id"), r.getLong("time"), r.getString("value"));
    }
}
