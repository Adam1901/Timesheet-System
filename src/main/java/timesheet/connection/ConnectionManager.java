package timesheet.connection;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

import timesheet.utils.Props;

public class ConnectionManager {
	private static final BasicDataSource dataSource = new BasicDataSource();
	// private final static Logger LOGGER =
	// Logger.getLogger(ConnectionManager.class.getName());

	public static int connectionCount = 0;

	static {
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl(Props.getProperty("connectionString"));
		dataSource.setUsername(Props.getProperty("dbusername"));
		dataSource.setPassword(Props.getProperty("dbpassword"));
		dataSource.setMaxWaitMillis(10000);
		dataSource.setMaxIdle(10);
		dataSource.setMaxConnLifetimeMillis(0);
		dataSource.setMaxTotal(-1);
	}

	private ConnectionManager() {
	}

	public static Connection getConnection() throws SQLException {
		Connection connection = dataSource.getConnection();
		connectionCount++;
		connection.setAutoCommit(false);
		return connection;
	}
}
