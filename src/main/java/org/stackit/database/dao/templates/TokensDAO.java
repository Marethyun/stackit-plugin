package org.stackit.database.dao.templates;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.stackit.database.dao.DAO;
import org.stackit.database.entities.Token;

import java.util.List;

public interface TokensDAO extends DAO {
    @Override
    void createTable();

    @SqlQuery
    Token getByValue(@Bind("value") String value);

    @SqlQuery
    void add(@Bind("time") long time, @Bind("value") String value);

    @SqlUpdate
    void deleteByValue(@Bind("value") String value);

    @SqlQuery
    List<Token> getAll();
}
