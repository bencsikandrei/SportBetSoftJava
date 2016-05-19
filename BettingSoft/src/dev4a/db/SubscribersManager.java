package dev4a.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dev4a.utils.DatabaseConnection;
import dev4a.bets.Bet;
import dev4a.subscriber.*;

public class SubscribersManager {
	public static Subscriber persist(Subscriber subscriber, String password) throws SQLException {

		/* get the connection using the class we defined */
		Connection conn = DatabaseConnection.getConnection();

		try {
			/* we need to leave the system in a stable state so we turn off autocommit */
			conn.setAutoCommit(false);
			/* the insert statement for the subscriber 
			 *     	username varchar(30) primary key,
				    first_name varchar(30) NOT NULL,
				    last_name varchar(30) NOT NULL,
				    password varchar(30) NOT NULL,
				    born_date date NOT NULL,
				    credit integer NOT NULL
			 * */
			PreparedStatement psPersist = conn
					.prepareStatement("insert into subscriber(username, first_name, "
							+ "last_name,"
							+ "password, born_date, credit "
							+ "values (?, ?, ? , ? , ? , ?, ?, ? , ? , ?)");

			psPersist.setString(1, subscriber.getUserName());
			psPersist.setString(2, subscriber.getFirstName());
			psPersist.setString(3, subscriber.getLastName());
			psPersist.setString(4, password );
			psPersist.setString(5, subscriber.getBornDate());
			psPersist.setLong(6, subscriber.getNumberOfTokens());
			
			/* do the updare */
			psPersist.executeUpdate();
			/* run the prepared statement that contains the subscribers data */
			psPersist.close();
			/* now commit the changes we made */
			conn.commit();
			
		} catch (SQLException e) {
			try {
				/* if something occured do not commit anything and rollback !*/
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			/* reset the state to the default */
			conn.setAutoCommit(true);
			throw e;
		}

		conn.setAutoCommit(true);
		conn.close();

		return subscriber;
	}
}