package dev4a.db;
/**
 * DAO class (<i>Data Access Object</i>) for the {@link Subscriber} class. This class
 * provides the CRUD functionalities :<br>
 * <ul>
 * <li><b>C</b>: create a new bet in the database.
 * <li><b>R</b>: retrieve (or read) a (list of)bet(s) from the database.
 * <li><b>U</b>: update the values stored in the database for a bet.
 * <li><b>D</b>: delete a bet in the database.
 * </ul>
 * 
 * @author dev4a
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev4a.bets.Bet;
import dev4a.subscriber.Subscriber;
import dev4a.utils.DatabaseConnection;

public class SubscribersManager {
	/**
	 * This method takes care of inserting subscribers in the table
	 * It takes the password of the new subscriber and inserts the
	 * 
	 * values in the table
	 * @param subscriber - the new object (in memory)
	 * @param password - the generated password
	 * @return the subscriber object
	 * @throws SQLException
	 */
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
			/* this statement prepares all the values to insert into the subscriber table 
			 * the structure of this table is up above for easier access
			 * */
			PreparedStatement psPersist = conn
					.prepareStatement("INSERT INTO subscriber(username, first_name, "
							+ "last_name,"
							+ "password, born_date, credit) "
							+ "values (?, ?, ? , ? , ? , ?)");
			/* all fields in order */
			psPersist.setString(1, subscriber.getUserName());
			psPersist.setString(2, subscriber.getFirstName());
			psPersist.setString(3, subscriber.getLastName());
			psPersist.setString(4, password );			
			psPersist.setDate(5, java.sql.Date.valueOf(subscriber.getBornDate()));
			psPersist.setLong(6, subscriber.getNumberOfTokens());			
			/* do the update */
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
		/* back to default */
		conn.setAutoCommit(true);
		conn.close();
		/* return if for convinience */
		return subscriber;
	}
	
	/** 
	 * Find a subscriber by his username 
	 * @param username
	 * @return
	 * @throws SQLException
	 */
	public static Subscriber findSubscriberByUserName(String username) throws SQLException {
		/* open the connection */
		Connection conn = DatabaseConnection.getConnection();
		/* make a querry */
		PreparedStatement psSelect = conn
				.prepareStatement("SELECT * FROM subscriber WHERE username LIKE ?");
		psSelect.setString(1, username);
		/* the result we get from the querry */
		ResultSet resultSet = psSelect.executeQuery();
		/* initialize */
		Subscriber sub = null;
		/* search in the result set */
		while (resultSet.next()) {
			/* create the new sub */
			sub = new Subscriber(
					resultSet.getString("username"),
					resultSet.getString("first_name"),
					resultSet.getString("last_name"),
					resultSet.getString("password"),
					resultSet.getDate("born_date").toString(),
					resultSet.getLong("credit")
					);
		}
		/* clean up */
		resultSet.close();
		psSelect.close();
		conn.close();
		
		return sub;
	}

	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Map<String, Subscriber> findAll() throws SQLException {
		/* open the connection */
		Connection conn = DatabaseConnection.getConnection();
		/* prepare the query */
		PreparedStatement psSelect = conn
				.prepareStatement("SELECT * FROM subscriber ORDER BY username, first_name, last_name");
		/* the results are here */
		ResultSet resultSet = psSelect.executeQuery();
		/* a container for them all */
		Map<String,Subscriber> subs = new HashMap();
		/* refference for temp subscriber */
		Subscriber sub = null;
		while (resultSet.next()) {
			sub = new Subscriber(
					resultSet.getString("username"),
					resultSet.getString("first_name"),
					resultSet.getString("last_name"),
					resultSet.getString("password"),
					resultSet.getDate("born_date").toString(),
					resultSet.getLong("credit")
					);
			subs.put(sub.getUserName(), sub);
		}
		/* clean up */
		resultSet.close();
		psSelect.close();
		conn.close();

		return subs;
	}
}