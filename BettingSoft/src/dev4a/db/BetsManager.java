package dev4a.db;
/**
 * DAO class (<i>Data Access Object</i>) for the {@link Bet} class. This class
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
import java.util.ArrayList;
import java.util.List;

import dev4a.utils.DatabaseConnection;
import dev4a.bets.*;

public class BetsManager {

	public static Bet persist(Bet bet) throws SQLException {
		/* 	Two steps in this method which must be managed in an atomic
			(unique) transaction:
			1 - insert the new bet;
			2 - once the insertion is OK, in order to set up the value
			of the id, a request is done to get this value by
			requesting the sequence (bets_id_seq) in the
			database.
		 */
		/* get the connection */
		Connection c = DatabaseConnection.getConnection();
		/*
		 * 	id serial PRIMARY KEY,
	username VARCHAR(30) REFERENCES Subscriber(username),
	name_comp VARCHAR(50) REFERENCES Competition(name),    
	type integer,
	id_winner integer references Competitor(id),
	id_second integer references Competitor(id),
	id_third integer references Competitor(id),
	nb_tokens integer,    
	status integer,
	earnings integer
		 */
		try {
			c.setAutoCommit(false);
			PreparedStatement psPersist = c
					.prepareStatement("insert into bets(id, username, "
							+ "name_comp, type, id_winner, "
							+ "id_second, id_third, nb_tokens, "
							+ "status, earnings)  "
							+ "values (?, ?, ? , ? , ? , ?, ?, ? , ? , ?)");

			psPersist.setInt(1, bet.getIdentifier());
			psPersist.setString(2, "" );
			
			/* do the updare */
			psPersist.executeUpdate();
			/* */
			psPersist.close();

			// Retrieving the value of the id with a request on the
			// sequence (subscribers_id_seq).
			PreparedStatement psIdValue = c
					.prepareStatement("select currval('bets_id_seq') as value_id");
			ResultSet resultSet = psIdValue.executeQuery();
			Integer id = null;
			while (resultSet.next()) {
				id = resultSet.getInt("value_id");
			}
			resultSet.close();
			psIdValue.close();
			c.commit();
			bet.setIdentifier(id);
		} catch (SQLException e) {
			try {
				c.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			c.setAutoCommit(true);
			throw e;
		}

		c.setAutoCommit(true);
		c.close();

		return bet;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Find a bet by his id.
	 * 
	 * @param id
	 *            the id of the bet to retrieve.
	 * @return the bet or null if the id does not exist in the database.
	 * @throws SQLException
	 */
	public static Bet findById(Integer id) throws SQLException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from bets where id=?");
		ResultSet resultSet = psSelect.executeQuery();
		Bet bet = null;
		while (resultSet.next()) {
			bet = new Bet(resultSet.getInt("id"),
					resultSet.getInt("number_of_tokens"),
					resultSet.getInt("id_subscriber"));
		}
		resultSet.close();
		psSelect.close();
		c.close();

		return bet;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Find all the bets for a specific subscriber in the database.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static List<Bet> findBySubscriber(Subscriber subscriber)
			throws SQLException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from bets where id_subscriber=? order by id");
		psSelect.setInt(1, subscriber.getId());
		ResultSet resultSet = psSelect.executeQuery();
		List<Bet> bets = new ArrayList<Bet>();
		while (resultSet.next()) {
			bets.add(new Bet(resultSet.getInt("id"), resultSet
					.getInt("number_of_tokens"), resultSet
					.getInt("id_subscriber")));
		}
		resultSet.close();
		psSelect.close();
		c.close();

		return bets;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Find all the bets in the database.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static List<Bet> findAll() throws SQLException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from bets order by id_subscriber,id");
		ResultSet resultSet = psSelect.executeQuery();
		List<Bet> bets = new ArrayList<Bet>();
		while (resultSet.next()) {
			bets.add(new Bet(resultSet.getInt("id"), resultSet
					.getInt("number_of_tokens"), resultSet
					.getInt("id_subscriber")));
		}
		resultSet.close();
		psSelect.close();
		c.close();

		return bets;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Update on the database the values from a bet. Useful?
	 * 
	 * @param bet
	 *            the bet to be updated.
	 * @throws SQLException
	 */
	public static void update(Bet bet) throws SQLException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psUpdate = c
				.prepareStatement("update bets set number_of_tokens=?, id_subscriber=? where id=?");
		psUpdate.setInt(1, bet.getNumberOfTokens());
		psUpdate.setInt(2, bet.getIdSubscriber());
		psUpdate.setInt(3, bet.getId());
		psUpdate.executeUpdate();
		psUpdate.close();
		c.close();
	}

	// -----------------------------------------------------------------------------
	/**
	 * Delete from the database a specific bet.
	 * 
	 * @param bet
	 *            the bet to be deleted.
	 * @throws SQLException
	 */
	public static void delete(Bet bet) throws SQLException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psUpdate = c
				.prepareStatement("delete from bets where id=?");
		psUpdate.setInt(1, bet.getId());
		psUpdate.executeUpdate();
		psUpdate.close();
		c.close();
	}
	// -----------------------------------------------------------------------------
}

// -----------------------------------------------------------------------------
/**
 * Store a bet in the database for a specific subscriber (the subscriber is
 * included inside the Bet object). This bet is not stored yet, so his
 * <code>id</code> value is <code>NULL</code>. Once the bet is stored, the
 * method returns the bet with the <code>id</code> value setted.
 * 
 * @param bet
 *            the bet to be stored.
 * @return the bet with the updated value for the id.
 * @throws SQLException
 */

}
