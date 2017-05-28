package org.stackit.dtb;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import org.apache.commons.codec.digest.DigestUtils;
import org.stackit.config.StackItConfiguration;
import org.stackit.src.Language;
import org.stackit.src.Logger;

public class MySQLClient implements DatabaseType {
	public java.sql.Connection mysql = null;
	
	@Override
	public Object getDatabase() {
		return this.mysql;
	}

	@Override
	public Object Connect() {
		try {
			mysql = DriverManager.getConnection("jdbc:mysql://" + StackItConfiguration.getDatabaseHost() + ":" + StackItConfiguration.getDatabasePort() + "/" + 
					StackItConfiguration.getDatabaseName() + "?" + 
					"user=" + StackItConfiguration.getDatabaseUser() + 
					"&password=" + StackItConfiguration.getDatabasePassword());
			
			Logger.info(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_connected")));
			return mysql;
		} catch(SQLException e) {
			Logger.critical(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_error_accessing")));
			Logger.critical(e.getMessage());
			return null;
		}
	}
	
	@Override
	public void CheckDatabase() {
		DatabaseMetaData dbm = null;
		
		try {
			dbm = mysql.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// API Users table check
		try {
			ResultSet table = dbm.getTables(null, null, StackItConfiguration.getDatabaseTablePrefix() + "api_users", null);
			
			if(table.next()) { // If table exist
				if(StackItConfiguration.isLogEnabled()) Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_table_exist")), "TABLE", StackItConfiguration.getDatabaseTablePrefix() + "api_users"));
			} else { // Table does not exist, let's create it.
				if(StackItConfiguration.isLogEnabled()) Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_table_doesnt_exist")), "TABLE", StackItConfiguration.getDatabaseTablePrefix() + "api_users"));
				Statement req = mysql.createStatement();
				
				req.executeUpdate("CREATE TABLE `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_users` ( `row` INT NOT NULL AUTO_INCREMENT , `id` INT NOT NULL , `name` TEXT NOT NULL , `pass` TEXT NOT NULL , `role` INT NOT NULL , `enabled` BOOLEAN NOT NULL , PRIMARY KEY (`row`)) ENGINE = InnoDB;");
				if(StackItConfiguration.isLogEnabled()) Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_table_created")), "TABLE", StackItConfiguration.getDatabaseTablePrefix() + "api_users"));
			}
			
			table = dbm.getTables(null, null, StackItConfiguration.getDatabaseTablePrefix() + "api_tokens", null);
				
			if(table.next()) { // If table exist
				if(StackItConfiguration.isLogEnabled()) Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_table_exist")), "TABLE", StackItConfiguration.getDatabaseTablePrefix() + "api_tokens"));
			} else { // Table does not exist, let's create it.
				if(StackItConfiguration.isLogEnabled()) Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_table_doesnt_exist")), "TABLE", StackItConfiguration.getDatabaseTablePrefix() + "api_tokens"));
				Statement req = mysql.createStatement();

				req.executeUpdate("CREATE TABLE `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_tokens` ( `row` INT NOT NULL AUTO_INCREMENT , `id` INT NOT NULL , `token` TEXT NOT NULL , `user` TEXT NOT NULL , `expire` TEXT NOT NULL , `enabled` BOOLEAN NOT NULL , PRIMARY KEY (`row`)) ENGINE = InnoDB;");
				if(StackItConfiguration.isLogEnabled()) Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_table_created")), "TABLE", StackItConfiguration.getDatabaseTablePrefix() + "api_tokens"));
			}
			
			table = dbm.getTables(null, null, StackItConfiguration.getDatabaseTablePrefix() + "api_roles", null);
				
			if(table.next()) { // If table exist
				if(StackItConfiguration.isLogEnabled()) Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_table_exist")), "TABLE", StackItConfiguration.getDatabaseTablePrefix() + "api_roles"));
			} else { // Table does not exist, let's create it.
				if(StackItConfiguration.isLogEnabled()) Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_table_doesnt_exist")), "TABLE", StackItConfiguration.getDatabaseTablePrefix() + "api_roles"));
				Statement req = mysql.createStatement();
				
				req.executeUpdate("CREATE TABLE `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_roles` ( `row` INT NOT NULL AUTO_INCREMENT , `id` INT NOT NULL , `permissions` TEXT NOT NULL , `enabled` BOOLEAN NOT NULL , PRIMARY KEY (`row`)) ENGINE = InnoDB;");
				if(StackItConfiguration.isLogEnabled()) Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_table_created")), "TABLE", StackItConfiguration.getDatabaseTablePrefix() + "api_roles"));
			}
			
			table = dbm.getTables(null, null, StackItConfiguration.getDatabaseTablePrefix() + "api_contexts_perms", null);
				
			if(table.next()) { // If table exist
				if(StackItConfiguration.isLogEnabled()) Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_table_exist")), "TABLE", StackItConfiguration.getDatabaseTablePrefix() + "api_contexts_perms"));
			} else { // Table does not exist, let's create it.
				if(StackItConfiguration.isLogEnabled()) Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_table_doesnt_exist")), "TABLE", StackItConfiguration.getDatabaseTablePrefix() + "api_contexts_perms"));
				Statement req = mysql.createStatement();
				
				req.executeUpdate("CREATE TABLE `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_contexts_perms` ( `row` INT NOT NULL AUTO_INCREMENT , `id` INT NOT NULL , `context` TEXT NOT NULL , `required_permission` TEXT NOT NULL , `enabled` BOOLEAN NOT NULL , PRIMARY KEY (`row`)) ENGINE = InnoDB;");
				if(StackItConfiguration.isLogEnabled()) Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_table_created")), "TABLE", StackItConfiguration.getDatabaseTablePrefix() + "api_contexts_perms"));
			}
			
			table = dbm.getTables(null, null, StackItConfiguration.getDatabaseTablePrefix() + "api_logs", null);
				
			if(table.next()) { // If table exist
				if(StackItConfiguration.isLogEnabled()) Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_table_exist")), "TABLE", StackItConfiguration.getDatabaseTablePrefix() + "api_logs"));
			} else { // Table does not exist, let's create it.
				if(StackItConfiguration.isLogEnabled()) Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_table_doesnt_exist")), "TABLE", StackItConfiguration.getDatabaseTablePrefix() + "api_logs"));
				Statement req = mysql.createStatement();
				
				req.executeUpdate("CREATE TABLE `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_logs` ( `row` INT NOT NULL AUTO_INCREMENT , `id` INT NOT NULL , `date` TEXT NOT NULL , `time` TEXT NOT NULL , `log` TEXT NOT NULL , `executor` TEXT NOT NULL , `context` TEXT NOT NULL , `remote` TEXT NOT NULL , PRIMARY KEY (`row`)) ENGINE = InnoDB;");
				if(StackItConfiguration.isLogEnabled()) Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_table_created")), "TABLE", StackItConfiguration.getDatabaseTablePrefix() + "api_logs"));
			}
			
			table = dbm.getTables(null, null, StackItConfiguration.getDatabaseTablePrefix() + "buys_history", null);
				
			if(table.next()) { // If table exist
				if(StackItConfiguration.isLogEnabled()) Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_table_exist")), "TABLE", StackItConfiguration.getDatabaseTablePrefix() + "buys_history"));
			} else { // Table does not exist, let's create it.
				if(StackItConfiguration.isLogEnabled()) Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_table_doesnt_exist")), "TABLE", StackItConfiguration.getDatabaseTablePrefix() + "buys_history"));
				Statement req = mysql.createStatement();
				
				req.executeUpdate("CREATE TABLE `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "buys_history` ( `row` INT NOT NULL AUTO_INCREMENT , `id` TEXT NOT NULL , `buy` TEXT NOT NULL , `buy_sid` TEXT NOT NULL , `quantity` INT NOT NULL , `buyer` TEXT NOT NULL , `message` TEXT NOT NULL , `buy_for` TEXT NOT NULL , `pay_id` TEXT NOT NULL , `type` TEXT NOT NULL , `buy_date` TEXT NOT NULL , `buy_time` TEXT NOT NULL , `args` TEXT NOT NULL , `gift` BOOLEAN NOT NULL, `history_date` TEXT NOT NULL , `history_time` TEXT NOT NULL , PRIMARY KEY (`row`)) ENGINE = InnoDB;");
				if(StackItConfiguration.isLogEnabled()) Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_table_created")), "TABLE", StackItConfiguration.getDatabaseTablePrefix() + "buys_history"));
			}
			
			table = dbm.getTables(null, null, StackItConfiguration.getDatabaseTablePrefix() + "buys", null);
				
			if(table.next()) { // If table exist
				if(StackItConfiguration.isLogEnabled()) Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_table_exist")), "TABLE", StackItConfiguration.getDatabaseTablePrefix() + "buys"));
			} else { // Table does not exist, let's create it.
				if(StackItConfiguration.isLogEnabled()) Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_table_doesnt_exist")), "TABLE", StackItConfiguration.getDatabaseTablePrefix() + "buys"));
				Statement req = mysql.createStatement();
				
				req.executeUpdate("CREATE TABLE `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "buys` ( `row` INT NOT NULL AUTO_INCREMENT , `id` TEXT NOT NULL , `buy` TEXT NOT NULL , `buy_sid` TEXT NOT NULL , `quantity` INT NOT NULL , `buyer` TEXT NOT NULL , `buy_for` TEXT NOT NULL , `message` TEXT NOT NULL , `pay_id` TEXT NOT NULL , `type` TEXT NOT NULL , `buy_date` TEXT NOT NULL , `buy_time` TEXT NOT NULL , `args` TEXT NOT NULL , `gift` BOOLEAN NOT NULL, PRIMARY KEY (`row`)) ENGINE = InnoDB;");
				if(StackItConfiguration.isLogEnabled()) Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_table_created")), "TABLE", StackItConfiguration.getDatabaseTablePrefix() + "buys"));
			}
			
			table = dbm.getTables(null, null, StackItConfiguration.getDatabaseTablePrefix() + "buys_waiting", null);
				
			if(table.next()) { // If table 
				if(StackItConfiguration.isLogEnabled()) Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_table_exist")), "TABLE", StackItConfiguration.getDatabaseTablePrefix() + "buys_waiting"));
			} else { // Table does not exist, let's create it.
				if(StackItConfiguration.isLogEnabled()) Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_table_doesnt_exist")), "TABLE", StackItConfiguration.getDatabaseTablePrefix() + "buys_waiting"));
				Statement req = mysql.createStatement();
				
				req.executeUpdate("CREATE TABLE `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "buys_waiting` ( `row` INT NOT NULL AUTO_INCREMENT , `id` INT NOT NULL , `buy_id` TEXT NOT NULL , PRIMARY KEY (`row`)) ENGINE = InnoDB;");
				if(StackItConfiguration.isLogEnabled()) Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_table_created")), "TABLE", StackItConfiguration.getDatabaseTablePrefix() + "buys_waiting"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public HashMap<String, Object> GetContext(String context) {
		Statement req;
		HashMap<String, Object> dtb_context = new HashMap<String, Object>();
		
		try {
			req = mysql.createStatement();
			ResultSet rs = req.executeQuery("SELECT * FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_contexts_perms` WHERE context='" + context.toString().replace("'", "''") + "'");

			while(rs.next()) {
				for(Integer i = 1 ; i < rs.getMetaData().getColumnCount() ; i++) {
					dtb_context.put(rs.getMetaData().getColumnName(i), rs.getString(i));
				}
			}
			
			return dtb_context;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public HashMap<String, Object> GetRole(Integer role) {
		Statement req;
		HashMap<String, Object> dtb_role = new HashMap<String, Object>();
		
		try {
			req = mysql.createStatement();
			ResultSet rs = req.executeQuery("SELECT * FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_roles` WHERE id='" + role.toString().replace("'", "''") + "'");

			while(rs.next()) {
				for(Integer i = 1 ; i < rs.getMetaData().getColumnCount() ; i++) {
					dtb_role.put(rs.getMetaData().getColumnName(i), rs.getString(i));
				}
			}
			
			return dtb_role;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public HashMap<String, Object> GetToken(String token) {
		Statement req;
		HashMap<String, Object> dtb_token = new HashMap<String, Object>();
		
		try {
			req = mysql.createStatement();
			ResultSet rs = req.executeQuery("SELECT * FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_tokens` WHERE token='" + token.toString().replace("'", "''") + "'");

			while(rs.next()) {
				for(Integer i = 1 ; i < rs.getMetaData().getColumnCount() ; i++) {
					dtb_token.put(rs.getMetaData().getColumnName(i), rs.getString(i));
				}
			}
			
			return dtb_token;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public HashMap<String, Object> GetUser(String user) {
		Statement req;
		HashMap<String, Object> dtb_user = new HashMap<String, Object>();
		
		try {
			req = mysql.createStatement();
			ResultSet rs = req.executeQuery("SELECT * FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_users` WHERE name='" + user.toString().replace("'", "''") + "'");
			
			while(rs.next()) {
				for(Integer i = 1 ; i < rs.getMetaData().getColumnCount() ; i++) {
					if(!rs.getMetaData().getColumnLabel(i).equalsIgnoreCase("pass")) { // Security
						dtb_user.put(rs.getMetaData().getColumnName(i), rs.getString(i));
					}
				}
			}
			
			return dtb_user;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public HashMap<String, Object> GetPurchase(String purchaseId) {
		Statement req;
		HashMap<String, Object> purchase = new HashMap<String, Object>();
		
		try {
			req = mysql.createStatement();
			ResultSet rs = req.executeQuery("SELECT * FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "buys` WHERE id='" + purchaseId.toString().replace("'", "''") + "'");
			
			while(rs.next()) {
				for(Integer i = 1 ; i <= rs.getMetaData().getColumnCount() ; i++) {
					purchase.put(rs.getMetaData().getColumnName(i), rs.getString(i));
				}
			}
			
			return purchase;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String AuthUser(String user, String pass) {
		Statement req;
		
		try {
			req = mysql.createStatement();
			ResultSet rs = req.executeQuery("SELECT * FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_users` WHERE name='" + user.toString().replace("'", "''") + "'");
			rs.next();
			
			if(UserExist(user)) {
				if(rs.getString("pass").equalsIgnoreCase(DigestUtils.sha1Hex(DigestUtils.md5Hex(pass)))) {
					if(rs.getBoolean("enabled") == true) {
						return "success";
					} else {
						return "account_not_enabled";
					}
				} else {
					return "invalid_password";
				}
			} else {
				return "unknown_user";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "internal_server_error";
		}
	}
	
	@Override
	public String AuthToken(String token) {
		Statement req;
		
		try {
			req = mysql.createStatement();
			ResultSet rs = req.executeQuery("SELECT * FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_tokens` WHERE token='" + token.toString().replace("'", "''") + "'");
			rs.next();
			
			if(TokenExist(token)) {
				// If time hasn't expired
				if(Calendar.getInstance().getTimeInMillis() < Long.parseLong(rs.getString("expire"))) {
					if(rs.getBoolean("enabled") == true) {
						return "success";
					} else {
						return "token_disabled";
					}
				} else {
					return "token_expired";
				}
			} else {
				return "unknown_token";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "internal_server_error";
		}
	}
	
	@Override
	public void AddLog(String date, String time, String log, String executor, String context, String remoteAddress) {
		try {
			Statement req = mysql.createStatement();
			
			ResultSet id = req.executeQuery("SELECT COUNT(*) FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_logs`");
			id.next();
			
			req.executeUpdate("INSERT INTO `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_logs` VALUES (0, " + id.getInt(1) + ", '" + date.toString().replace("'", "''") + "', '" + time.toString().replace("'", "''") + "', '" + log.toString().replace("'", "''") + "', '" + executor.toString().replace("'", "''") + "', '" + context.toString().replace("'", "''") + "', '" + remoteAddress.toString().replace("'", "''") + "')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void AddPurchaseToWaitingList(String user, String buy_id) {
		try {
			Statement req = mysql.createStatement();
			
			ResultSet id = req.executeQuery("SELECT COUNT(*) FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_logs`");
			id.next();
			
			req.executeUpdate("INSERT INTO `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "buys_waiting` VALUES (0, " + id.getInt(1) + ", '" + buy_id.toString().replace("'", "''") + "')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Boolean HasPurchaseInWaitingList(String user) {
		Statement req, req2;
		
		try {
			req = mysql.createStatement();
			ResultSet rs = req.executeQuery("SELECT * FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "buys_waiting`");

			while(rs.next()) {
				req2 = mysql.createStatement();
				ResultSet rs2 = req2.executeQuery("SELECT * FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "buys` WHERE id=\'" + rs.getString("buy_id").toString().replace("'", "''") + "\'");

				while(rs2.next()) {
					if(rs2.getString("buy_for").equalsIgnoreCase(user)) {
						return true;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	@Override
	public Boolean HasPurchaseMessage(String purchaseId) {
		Statement req;
		
		try {
			req = mysql.createStatement();
			ResultSet rs = req.executeQuery("SELECT * FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "buys` WHERE id='" + purchaseId.toString().replace("'", "''") + "'");
			rs.next();
			
			if(rs.getString("message").replaceAll(" ", "") != "") {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	@Override
	public Boolean IsAGift(String purchaseId) {
		Statement req;
		
		try {
			req = mysql.createStatement();
			ResultSet rs = req.executeQuery("SELECT * FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "buys` WHERE id='" + purchaseId.toString().replace("'", "''") + "'");
			rs.next();
			
			if(rs.getBoolean("gift")) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	@Override
	public String GetPurchaseMessage(String purchaseId) {
		Statement req;
		
		try {
			req = mysql.createStatement();
			ResultSet rs = req.executeQuery("SELECT * FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "buys` WHERE id='" + purchaseId.toString().replace("'", "''") + "'");
			rs.next();
			
			return rs.getString("message");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Boolean IsPurchaseInWaitingList(String purchaseId) {
		Statement req;
		
		try {
			req = mysql.createStatement();
			ResultSet rs = req.executeQuery("SELECT COUNT(*) FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "buys_waiting` WHERE buy_id='" + purchaseId.toString().replace("'", "''") + "'");
			rs.next();
			int count = rs.getInt(1);
			
			if(count >= 1) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	@Override
	public HashMap<Integer, String> GetPurchasesInWaitingList(String user, String uuid) {
		Statement req, req2;
		HashMap<Integer, String> purchases = new HashMap<Integer, String>();
		
		try {
			req = mysql.createStatement();
			ResultSet rs = req.executeQuery("SELECT * FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "buys_waiting`");

			while(rs.next()) {
				req2 = mysql.createStatement();
				ResultSet rs2 = req2.executeQuery("SELECT * FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "buys` WHERE id=\'" + rs.getString("buy_id").toString().replace("'", "''") + "\'");

				while(rs2.next()) {
					if(rs2.getString("buy_for").equalsIgnoreCase(user) || rs2.getString("buy_for").equalsIgnoreCase(uuid)) {
						purchases.put(purchases.size(), rs.getString("buy_id"));
					}
				}
			}
			
			return purchases;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public HashMap<String, Object> GetPurchaseArguments(String purchaseId) {
		Statement req;
		HashMap<String, Object> args = new HashMap<String, Object>();
		
		try {
			req = mysql.createStatement();
			ResultSet rs = req.executeQuery("SELECT * FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "buys` WHERE id='" + purchaseId.toString().replace("'", "''") + "'");
			
			if(rs.next()) {
				String argsString = rs.getString("args");
				String[] splitted1 = argsString.split("&");
				
				for(Integer i = 0 ; i < splitted1.length ; i++) {
					String[] splitted2 = splitted1[i].split("=");
					args.put(splitted2[0], splitted2[1]);
				}
			}
			
			return args;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void RemovePurchase(String purchaseId) {
		Statement req, req2, req3;
		HashMap<String, Object> purchase = GetPurchase(purchaseId);
		
		try {
			req = mysql.createStatement();
			req.executeUpdate("DELETE FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "buys` WHERE id='" + purchaseId.toString().replace("'", "''") + "'");

			req2 = mysql.createStatement();
			req2.executeUpdate("DELETE FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "buys_waiting` WHERE buy_id='" + purchaseId.toString().replace("'", "''") + "'");

			Date date = new Date();
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			
			String dateOutput = dateFormat.format(date);
			String timeOutput = timeFormat.format(date);
			
			req3 = mysql.createStatement();
			req3.executeUpdate("INSERT INTO `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "buys_history` VALUES (0, '" + purchase.get("id") + "', '" + purchase.get("buy").toString().replace("'", "''") + "', '" + purchase.get("buy_sid").toString().replace("'", "''") + "', " + purchase.get("quantity").toString().replace("'", "''") + ", '" + purchase.get("buyer").toString().replace("'", "''") + "', '" + purchase.get("message").toString().replace("'", "''") + "', '" + purchase.get("buy_for").toString().replace("'", "''") + "', '" + purchase.get("pay_id").toString().replace("'", "''") + "', '" + purchase.get("type").toString().replace("'", "''") + "', '" + purchase.get("buy_date").toString().replace("'", "''") + "', '" + purchase.get("buy_time").toString().replace("'", "''") + "', '" + purchase.get("args").toString().replace("'", "''") + "', " + purchase.get("gift").toString().replace("'", "''") + ", '" + dateOutput.toString().replace("'", "''") + "', '" + timeOutput.toString().replace("'", "''") + "')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Boolean ContextExist(String context) {
		Statement req;
		
		try {
			req = mysql.createStatement();
			ResultSet rs = req.executeQuery("SELECT COUNT(*) FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_contexts_perms` WHERE context='" + context.toString().replace("'", "''") + "'");
			rs.next();
			int count = rs.getInt(1);
			
			if(count >= 1) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public Boolean UserExist(String user) {
		Statement req;
		
		try {
			req = mysql.createStatement();
			ResultSet rs = req.executeQuery("SELECT COUNT(*) FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_users` WHERE name='" + user.toString().replace("'", "''") + "'");
			rs.next();
			int count = rs.getInt(1);
			
			if(count >= 1) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public String GenerateToken(String user) {
		String token = new BigInteger(130, new SecureRandom()).toString(32);
		
		while(TokenExist(token)) {
			token = new BigInteger(130, new SecureRandom()).toString(32);
		}

		try {
			Statement req = mysql.createStatement();
			
			ResultSet id = req.executeQuery("SELECT COUNT(*) FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_tokens`");
			id.next();
			
			Calendar expiration = Calendar.getInstance();
			expiration.add(Calendar.MINUTE, 30);
			
			req.executeUpdate("INSERT INTO `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_tokens` VALUES (0, " + id.getInt(1) + ", '" + token.toString().replace("'", "''") + "', '" + user.toString().replace("'", "''") + "', '" + expiration.getTimeInMillis() + "', 1)");
			return token;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String GetContextRequiredPermission(String context) {
		HashMap<String, Object> dtb_context = GetContext(context);
		
		if(dtb_context.size() > 0) {
			return dtb_context.get("required_permission").toString();
		}
		
		return null;
	}
	
	@Override
	public String GetRolePermissions(Integer role) {
		HashMap<String, Object> dtb_role = GetRole(role);
		
		if(dtb_role.size() > 0) {
			return dtb_role.get("permissions").toString();
		}
		
		return null;
	}
	
	@Override
	public Boolean TokenExist(String token) {
		Statement req;
		try {
			req = mysql.createStatement();
			ResultSet rs = req.executeQuery("SELECT COUNT(*) FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_tokens` WHERE token='" + token.toString().replace("'", "''") + "'");
			rs.next();
			int count = rs.getInt(1);
			
			if(count >= 1) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public String GetTokenUser(String token) {
		HashMap<String, Object> dtb_token = GetToken(token);
		
		if(dtb_token.size() > 0) {
			return dtb_token.get("user").toString();
		}
		
		return null;
	}
	
	@Override
	public String GetTokenPermissions(String token) {
		HashMap<String, Object> dtb_token = Database.getActiveDatabase().GetToken(token);
		
		if(dtb_token.size() > 0) {
			HashMap<String, Object> dtb_user = Database.getActiveDatabase().GetUser(dtb_token.get("user").toString());
			
			if(dtb_user.size() > 0) {
				return Database.getActiveDatabase().GetRolePermissions(Integer.parseInt(dtb_user.get("role").toString()));
			}
		}
		
		return null;
	}
	
	@Override
	public void CreateContext(String context, String permission) {
		if(!ContextExist(context)) {
			try {
				Statement req = mysql.createStatement();
				
				ResultSet id = req.executeQuery("SELECT COUNT(*) FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_contexts_perms`");
				id.next();
				
				req.executeUpdate("INSERT INTO `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_contexts_perms` VALUES (0, " + id.getInt(1) + ", '" + context.toString().replace("'", "''") + "', '" + permission.toString().replace("'", "''") + "', 1)");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_create_context")), "CONTEXT", context));
		} else {
			Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_create_context")), "CONTEXT", context));
		}
	}
	
	@Override
	public String CreateUser(String user, String pass) {
		if(!UserExist(user)) {
			try {
				Statement req = mysql.createStatement();
				
				ResultSet id = req.executeQuery("SELECT COUNT(*) FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_users`");
				id.next();
				
				req.executeUpdate("INSERT INTO `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_users` VALUES (0, " + id.getInt(1) + ", '" + user + "', '" + DigestUtils.sha1Hex(DigestUtils.md5Hex(pass)) + "', 1, 1)");
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
			
			Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_user_created")), "USER", user));
			return "success";
		} else {
			Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "dtb_user_already_exist")), "USER", user));
			return "already_exist";
		}
	}
	
	@Override
	public String DeleteUser(String user) {
		if(UserExist(user)) {
			Statement req;
			
			try {
				req = mysql.createStatement();
				
				req.executeUpdate("DELETE FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_users` WHERE name='" + user.toString().replace("'", "''") + "'");
				return "success";
			} catch (SQLException e) {
				e.printStackTrace();
				return "null";
			}
		} else {
			return "not_found";
		}
	}
	
	@Override
	public String GetTokenExpiration(String token) {
		Statement req;
		
		try {
			req = mysql.createStatement();
			ResultSet rs = req.executeQuery("SELECT * FROM `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_tokens` WHERE token='" + token.toString().replace("'", "''") + "'");
			rs.next();
			
			return rs.getString("expire");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String GetUserPermissions(String user) {
		HashMap<String, Object> dtb_user = GetUser(user);
		
		if(dtb_user.size() > 0) {
			HashMap<String, Object> dtb_role = GetRole(Integer.parseInt(dtb_user.get("role").toString()));
			
			if(dtb_role.size() > 0) {
				return dtb_role.get("permissions").toString();
			}
		}
		
		return null;
	}
	
	@Override
	public void SetUserRole(String user, Integer role) {
	      try {
		      Statement req = mysql.createStatement();
			req.executeUpdate("UPDATE `" + StackItConfiguration.getDatabaseName() + "`.`" + StackItConfiguration.getDatabaseTablePrefix() + "api_users` SET role=" + role + " WHERE name='" + user.toString().replace("'", "''") + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override 
	public void Disconnect() {
		try {
			if(mysql != null) {
				mysql.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
