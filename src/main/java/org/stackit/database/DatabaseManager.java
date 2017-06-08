package org.stackit.database;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.stackit.config.StackItConfiguration;
import org.stackit.database.dao.proxy.LogsProxy;
import org.stackit.database.dao.proxy.QueueProxy;
import org.stackit.database.dao.proxy.UsersProxy;
import org.stackit.database.dao.templates.LogsDAO;
import org.stackit.database.dao.templates.QueueDAO;
import org.stackit.database.dao.templates.UsersDAO;
import org.stackit.src.StackIt;


public class DatabaseManager {
	private static QueueDAO queueDAO;
	private static UsersDAO usersDAO;
	private static LogsDAO logsDAO;

	private static QueueProxy queueProxy;
	private static UsersProxy usersProxy;
	private static LogsProxy logsProxy;

	/**
	 * Initiate the database connection and check if the type of database is supported
	 * by the plugin.
	 * @return boolean
	 */
	public static void init() throws Exception {
		ClassLoader loader = StackIt.class.getClassLoader();

		try {

			queueDAO = (QueueDAO) loader.loadClass(StackItConfiguration.getQueueDAOClassName()).newInstance();
			usersDAO = (UsersDAO) loader.loadClass(StackItConfiguration.getUsersDAOClassName()).newInstance();
			logsDAO = (LogsDAO) loader.loadClass(StackItConfiguration.getLogsDAOClassName()).newInstance();

			queueProxy = new QueueProxy(queueDAO);
			usersProxy = new UsersProxy(usersDAO);
			logsProxy = new LogsProxy(logsDAO);

		} catch (InstantiationException | IllegalAccessException e){
			e.printStackTrace();
			StackIt.disable();
		}

	}

    public static QueueDAO getQueue(){
	    return queueProxy;
    }

    public static UsersDAO getUsers(){
        return usersProxy;
    }

    public static LogsDAO getLogs(){
        return logsProxy;
    }

    public static Handle getDatabaseHandle(){
    	String url = "jdbc:"
                + StackItConfiguration.getDatabaseType()
                + "://" + StackItConfiguration.getDatabaseHost()
                + ":"
                + StackItConfiguration.getDatabasePort()
                + "/"
                + StackItConfiguration.getDatabaseName()
                + "?user=" + StackItConfiguration.getDatabaseUser()
                + "&password=" + StackItConfiguration.getDatabasePassword();
    	return new DBI(url).open();
	}

}
