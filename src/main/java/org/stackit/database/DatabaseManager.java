package org.stackit.database;

import org.stackit.config.StackItConfiguration;
import org.stackit.src.StackIt;


public class DatabaseManager {
	private static QueueDAO queueDAO;
	private static UsersDAO usersDAO;
	private static LogsDAO logsDAO;

	/**
	 * Initiate the database connection and check if the type of database is supported
	 * by the plugin.
	 * @return boolean
	 */
	public static boolean init() throws Exception {
		ClassLoader loader = StackIt.class.getClassLoader();

		try {

			queueDAO = (QueueDAO) loader.loadClass(StackItConfiguration.getQueueDAOClassName()).newInstance();
			usersDAO = (UsersDAO) loader.loadClass(StackItConfiguration.getUsersDAOClassName()).newInstance();

		} catch (InstantiationException | IllegalAccessException e){
			e.printStackTrace();
			StackIt.disable();
		}
		return false;
	}

    public static QueueDAO getQueue(){
	    return queueDAO;
    }

    public static UsersDAO getUsers(){
        return usersDAO;
    }

    public static LogsDAO getLogs(){
        return logsDAO;
    }
}
