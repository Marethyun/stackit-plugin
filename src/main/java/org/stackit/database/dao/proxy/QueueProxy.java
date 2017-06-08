package org.stackit.database.dao.proxy;

import org.skife.jdbi.v2.Handle;
import org.stackit.database.DatabaseManager;
import org.stackit.database.dao.Proxy;
import org.stackit.database.dao.templates.QueueDAO;
import org.stackit.database.entities.QueueElement;

import java.util.List;

public class QueueProxy implements QueueDAO, Proxy {

    private QueueDAO substituted;

    public QueueProxy(QueueDAO substituted) {
        this.substituted = substituted;
    }

    @Override
    public List<QueueElement> getByUserId(int user_id) {
        Handle h = DatabaseManager.getDatabaseHandle();
        List<QueueElement> elements = h.attach(substituted.getClass()).getByUserId(user_id);
        h.close();
        return elements;
    }

    @Override
    public void createTable() {
        Handle h = DatabaseManager.getDatabaseHandle();
        h.attach(substituted.getClass()).createTable();
        h.close();
    }

    @Override
    public void delete(int id) {
        Handle h = DatabaseManager.getDatabaseHandle();
        h.attach(substituted.getClass()).delete(id);
        h.close();
    }
}
