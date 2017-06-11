package org.stackit.database.dao.proxy;

import org.skife.jdbi.v2.Handle;
import org.stackit.database.DatabaseManager;
import org.stackit.database.dao.Proxy;
import org.stackit.database.dao.templates.TokensDAO;
import org.stackit.database.entities.Token;

import java.util.List;

public class TokensProxy implements Proxy, TokensDAO {
    private Class<TokensDAO> substituted;

    public TokensProxy(Class<TokensDAO> substituted) {
        this.substituted = substituted;
    }

    @Override
    public void createTable() {
        Handle h = DatabaseManager.getDatabaseHandle();
        h.attach(substituted).createTable();
        h.close();
    }

    @Override
    public Token getByValue(String value) {
        Handle h = DatabaseManager.getDatabaseHandle();
        Token token = h.attach(substituted).getByValue(value);
        h.close();
        return token;
    }

    @Override
    public void add(long time, String value) {
        Handle h = DatabaseManager.getDatabaseHandle();
        h.attach(substituted).add(time, value);
        h.close();
    }

    @Override
    public void deleteByValue(String value) {
        Handle h = DatabaseManager.getDatabaseHandle();
        h.attach(substituted).deleteByValue(value);
        h.close();
    }

    @Override
    public List<Token> getAll() {
        Handle h = DatabaseManager.getDatabaseHandle();
        List<Token> tokenList = h.attach(substituted).getAll();
        h.close();
        return tokenList;
    }
}
