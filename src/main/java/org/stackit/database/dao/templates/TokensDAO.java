package org.stackit.database.dao.templates;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.stackit.database.dao.DAO;
import org.stackit.database.entities.Token;

import java.util.List;

public interface TokensDAO extends DAO {
    @Override
    void createTable();

    Token getByValue(@Bind("value") String value);

    void add(@Bind("time") long time, @Bind("value") String value);

    void deleteByValue(@Bind("value") String value);

    List<Token> getAll();
}
