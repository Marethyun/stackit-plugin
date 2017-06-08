package org.stackit.database.dao.proxy;

import org.skife.jdbi.v2.Handle;
import org.stackit.database.DatabaseManager;
import org.stackit.database.dao.Proxy;
import org.stackit.database.dao.templates.UsersDAO;
import org.stackit.database.entities.User;

public class UsersProxy implements UsersDAO, Proxy {

    private UsersDAO substitued;

    public UsersProxy(UsersDAO substitued) {
        this.substitued = substitued;
    }

    @Override
    public User getById(int id) {
        Handle h = DatabaseManager.getDatabaseHandle();
        User user = h.attach(substitued.getClass()).getById(id);
        h.close();
        return user;
    }

    @Override
    public void createTable() {
        Handle h = DatabaseManager.getDatabaseHandle();
        h.attach(substitued.getClass()).createTable();
        h.close();
    }

}
