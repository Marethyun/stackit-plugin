package org.stackit.database;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.stackit.Logger;
import org.stackit.StackIt;
import org.stackit.config.StackItConfiguration;
import org.stackit.database.dao.proxy.LogsProxy;
import org.stackit.database.dao.proxy.QueueProxy;
import org.stackit.database.dao.proxy.TokensProxy;
import org.stackit.database.dao.proxy.UsersProxy;
import org.stackit.database.dao.templates.LogsDAO;
import org.stackit.database.dao.templates.QueueDAO;
import org.stackit.database.dao.templates.TokensDAO;
import org.stackit.database.dao.templates.UsersDAO;


public class DatabaseManager {
	private static Class<QueueDAO> queueDAO;
	private static Class<UsersDAO> usersDAO;
	private static Class<LogsDAO> logsDAO;
	private static Class<TokensDAO> tokensDAO;

	private static QueueProxy queueProxy;
	private static UsersProxy usersProxy;
	private static LogsProxy logsProxy;
	private static TokensProxy tokensProxy;

	/**
	 * Initiate the database connection and check if the type of database is supported
	 * by the plugin.
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public static void init() throws Exception {

        Logger.info("Initializing database interface..");

		ClassLoader loader = StackIt.class.getClassLoader();

		try {

			queueDAO = (Class<QueueDAO>) loader.loadClass(StackItConfiguration.getQueueDAOClassName());
			usersDAO = (Class<UsersDAO>) loader.loadClass(StackItConfiguration.getUsersDAOClassName());
			logsDAO = (Class<LogsDAO>) loader.loadClass(StackItConfiguration.getLogsDAOClassName());
			tokensDAO = (Class<TokensDAO>) loader.loadClass(StackItConfiguration.getTokensDAOClassName());

			queueProxy = new QueueProxy(queueDAO);
			usersProxy = new UsersProxy(usersDAO);
			logsProxy = new LogsProxy(logsDAO);
			tokensProxy = new TokensProxy(tokensDAO);

		} catch (Exception e){
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

    public static TokensDAO getTokens(){
        return tokensProxy;
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
