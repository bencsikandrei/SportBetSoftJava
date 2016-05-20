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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev4a.subscriber.Subscriber;
import dev4a.utils.DatabaseConnection;
import dev4a.bets.*;
import dev4a.competitor.Competitor;

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
		
		try {
			c.setAutoCommit(false);
			PreparedStatement psPersist = c
					.prepareStatement("insert into bet(username, "
							+ "name_comp, type, id_winner, "
							+ "id_second, id_third, nb_tokens, "
							+ "status, earnings)  "
							+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?)");

			/* */
			psPersist.setString(1, bet.getUserName());
			psPersist.setString(2, bet.competitionName());
			psPersist.setString(3, bet.getType());
			/* */
			psPersist.setInt(4, bet.getWinner());
			psPersist.setInt(5, bet.getSecond());
			psPersist.setInt(6, bet.getThird());
			psPersist.setLong(7, bet.getNumberOfTokens());
			psPersist.setInt(8, bet.getState());
			psPersist.setLong(9, bet.getEarnings());
						
			/* do the updare */
			psPersist.executeUpdate();
			/* */
			psPersist.close();

			// Retrieving the value of the id with a request on the
			// sequence (subscribers_id_seq).
			PreparedStatement psIdValue = c
					.prepareStatement("SELECT currval('bets_id_seq') AS value_id");
			
			ResultSet resultSet = psIdValue.executeQuery();
			
			Integer id = null;
			
			while (resultSet.next()) {
				id = resultSet.getInt("id");
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
		/* open the connection */
		Connection conn = DatabaseConnection.getConnection();
		
		PreparedStatement psSelect = conn
				.prepareStatement("SELECT * FROM bet WHERE id=?");
		
		psSelect.setInt(1, id.intValue());
		
		ResultSet resultSet = psSelect.executeQuery();
		
		Bet bet = null;
		
		while (resultSet.next()) {
			/* the id */
			int tempId = resultSet.getInt("id");
			
			String tempUserName = resultSet.getString("username");
			
			String tempCompName = resultSet.getString("comp_name");
			
			String tempDate = (resultSet.getDate("bet_date")).toString();
			
			int tempType = resultSet.getInt("type");
			
			long tempNbTokens = resultSet.getLong("nb_tokens");
			
			int tempStatus = resultSet.getInt("status");
			
			long tempEarnings = resultSet.getLong("earnings");
			
			int tempIdWinner = resultSet.getInt("id_winner");
			
			if( tempType == 1 ) {
				/* this is a winner bet */
				bet = new WinnerBet(tempNbTokens, tempCompName, CompetitorsManager.findById(tempIdWinner), tempUserName, tempDate);
				/* */
				
			} 
			else {
				/* */
				int tempSecond = resultSet.getInt("id_second");
				/**/
				int tempThird = resultSet.getInt("id_third");
				
				bet = new PodiumBet(tempNbTokens, tempCompName, CompetitorsManager.findById(tempIdWinner), CompetitorsManager.findById(tempSecond), CompetitorsManager.findById(tempThird), tempUserName, tempDate);
			}			
		}
		/* clean up */
		resultSet.close();
		psSelect.close();
		conn.close();

		return bet;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Find all the bets for a specific subscriber in the database.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Map<Integer, Bet> findBySubscriber(Subscriber subscriber)
			throws SQLException {
		
		Connection conn = DatabaseConnection.getConnection();
		
		PreparedStatement psSelect = conn
				.prepareStatement("SELECT * FROM bet WHERE username=? order by username");
		
		psSelect.setString(1, subscriber.getUserName());
		
		ResultSet resultSet = psSelect.executeQuery();
		
		Map<Integer, Bet> bets = new HashMap();
		
		while (resultSet.next()) {
			
			
			
			bets.put(, );
		}
		resultSet.close();
		psSelect.close();
		conn.close();

		return bets;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Find all the bets in the database.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Map<Integer, Bet> findAll() throws SQLException {
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
		/* open the connection */
		Connection conn = DatabaseConnection.getConnection();
		/* */
		PreparedStatement psUpdate = conn
				.prepareStatement("DELETE FROM bet WHERE id=?");
		psUpdate.setInt(1, bet.getIdentifier());
		/* clean up */
		psUpdate.executeUpdate();
		psUpdate.close();
		conn.close();
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
