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
 * @author Group 4A
 */
import java.sql.*;
import java.util.*;

import dev4a.competition.Competition;
import dev4a.competition.States;
import dev4a.competitor.Competitor;
import dev4a.utils.DatabaseConnection;

public class CompetitionsManager {
	

	/**
	 * Find a competition by his id.
	 * 
	 * @param id the id of the competition to retrieve.
	 * @return the competition or null if the id does not exist in the database.
	 * @throws SQLException
	 */
	public static Competition findByName(String name) throws SQLException
	{
	  // 1 - Get a database connection from the class 'DatabaseConnection' 
		Connection c = DatabaseConnection.getConnection();

	  // 2 - Creating a Prepared Statement with the SQL instruction.
	  //     The parameters are represented by question marks. 
		PreparedStatement psSelect = c.prepareStatement("select * from competitions where name=?");

	  // 3 - Supplying values for the prepared statement parameters (question marks).
		psSelect.setString(1, name);

	  // 4 - Executing Prepared Statement object among the database.
	  //     The return value is a Result Set containing the data.
		ResultSet resultSet = psSelect.executeQuery();

	  // 5 - Retrieving values from the Result Set.
		Competition competition = null;
		while(resultSet.next())
		{
			// Get competitor 1, 2 and 3 from Ids
			// resultSet.getInt("id_winner");
			// resultSet.getInt("id_second");
			// resultSet.getInt("id_third");
			
			// store it in a new List<Competitor> 
			List<Competitor> competitors;
			
			
//			competition = new Competition(resultSet.getString("name"), 
//			resultSet.getDate("start_date"), resultSet.getDate("end_date"),
//			resultSet.getString("status"), resultSet.getString("sport"), 
//			competitors, resultSet.getString("bet_type"));
		}

	  // 6 - Closing the Result Set
		resultSet.close();

	  // 7 - Closing the Prepared Statement.
		psSelect.close();

	  // 8 - Closing the database connection.
		c.close();

		return competition;
	}

}