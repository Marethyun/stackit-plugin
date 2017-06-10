package org.stackit.database.dao.proxy;

import org.skife.jdbi.v2.Handle;
import org.stackit.database.DatabaseManager;
import org.stackit.database.dao.Proxy;
import org.stackit.database.dao.templates.TokensDAO;
import org.stackit.database.entities.Token;

import java.util.List;

public class TokensProxy implements Proxy, TokensDAO {
    private TokensDAO substituted;

    public TokensProxy(TokensDAO substituted) {
        this.substituted = substituted;
    }

    @Override
    public void createTable() {
        Handle h = DatabaseManager.getDatabaseHandle();
        h.attach(substituted.getClass()).createTable();
        h.close();
    }

    @Override
    public Token getByValue(String value) {
        Handle h = DatabaseManager.getDatabaseHandle();
        Token token = h.attach(substituted.getClass()).getByValue(value);
        h.close();
        return token;
    }

    @Override
    public List<Token> getAll() {
        Handle h = DatabaseManager.getDatabaseHandle();
        List<Token> tokenList = h.attach(substituted.getClass()).getAll();
        h.close();
        return tokenList;
    }
}
