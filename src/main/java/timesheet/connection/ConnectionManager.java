package timesheet.connection;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * TODO proper connection pool
 *
 */
public class ConnectionManager {
	// a.damlmeadows@googlemail.com
	// GAKrWsosj0Us

	// sql8123946
	// P3QCtz2eHy

	private static final BasicDataSource dataSource = new BasicDataSource();

	public static int connectionCount = 0;

	static {
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://sql8.freemysqlhosting.net:3306/sql8123946");
		dataSource.setUsername("sql8123946");
		dataSource.setPassword("P3QCtz2eHy");
		dataSource.setMaxWaitMillis(10000);
		dataSource.setMaxIdle(10);
		dataSource.setMaxConnLifetimeMillis(7000);
		dataSource.setMaxTotal(30);
	}

	private ConnectionManager() {
	}

	public static Connection getConnection() throws SQLException {
		System.out.print("start ");
		Connection connection = dataSource.getConnection();
		connectionCount++;
		connection.setAutoCommit(false);
		System.out.println(connectionCount + " - end");

		return connection;
	}
	
	
}
