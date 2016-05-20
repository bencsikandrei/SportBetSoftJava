package dev4a.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import dev4a.competitor.Competitor;
import dev4a.competitor.IndividualCompetitor;
import dev4a.competitor.Team;
import dev4a.utils.DatabaseConnection;

public class CompetitorsManager {

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
	public static Competitor persist(Competitor competitor) throws SQLException {

		/* get the connection using the class we defined */
		Connection conn = DatabaseConnection.getConnection();

		try {
			/* we need to leave the system in a stable state so we turn off autocommit */
			conn.setAutoCommit(false);
			/* the insert statement for the subscriber 
			 *     	id integer primary key,
			 		type integer NOT NULL,
				    first_name varchar(30),
				    last_name varchar(30),
				    born_date date,
				    team_name varchar(30),
				    id_team integer foreign key
			 * */
			/* this statement prepares all the values to insert into the subscriber table 
			 * the structure of this table is up above for easier access
			 * */
			PreparedStatement psPersist = conn
			.prepareStatement("INSERT INTO competitor(type, first_name, "
				+ "last_name, born_date, team_name, id_team)"
				+ "values (?, ?, ?, ?, ?, ?)");

			psPersist.setInt(1, competitor.getType());
			if (competitor.getType() == Competitor.TYPE_INDIVIDUAL) {
				psPersist.setString(2, ((IndividualCompetitor) competitor).getFirstName());
				psPersist.setString(3, ((IndividualCompetitor) competitor).getLastName());
				psPersist.setDate(4, Date.valueOf(((IndividualCompetitor) competitor).getBornDate()));
				psPersist.setString(5, null);
				psPersist.setInt(6, ((Team) competitor).getIdTeam());
			} else if (competitor.getType() == Competitor.TYPE_TEAM) {
				psPersist.setString(2, null);
				psPersist.setString(3, null);
				psPersist.setString(4, null);
				psPersist.setString(5, ((Team) competitor).getName());
				psPersist.setInt(6, 0); // 0 for null
			}
		
			psPersist.executeUpdate();
			psPersist.close();
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
		return competitor;
	}

	public static Competitor findByid(int id) throws SQLException
	{
		Connection c = DatabaseConnection.getConnection(); 
		PreparedStatement psSelect = c.prepareStatement("select * from competitor where id=?");

		psSelect.setInt(1, id);
		ResultSet resultSet = psSelect.executeQuery();

		Competitor competitor = null;
		while(resultSet.next())
		{
			if(resultSet.getInt("type") == Competitor.TYPE_INDIVIDUAL) {
				competitor = new IndividualCompetitor(id, resultSet.getInt("type"), resultSet.getString("first_name"), 
					resultSet.getString("last_name"), resultSet.getDate("born_date").toString(), resultSet.getInt("id_team"));
			} else if(resultSet.getInt("type") == Competitor.TYPE_TEAM) {
				competitor = new Team(id, resultSet.getInt("type"), resultSet.getString("team_name"));
			}
		}

		resultSet.close();
		psSelect.close();
		c.close();

		return competitor;
	}

		/**
		 * This method finds all the individual competitors in the system
		 * @return
		 * @throws SQLException
		 */
		public static Map<Integer, IndividualCompetitor> findAllIndividualCompetitors() throws SQLException {
			/* open the connection */
			Connection conn = DatabaseConnection.getConnection();
			/* prepare the query */
			PreparedStatement psSelect = conn
			.prepareStatement("SELECT * FROM competitor ORDER BY id WHERE type=" + Integer.toString(Competitor.TYPE_INDIVIDUAL));
			/* the results are here */
			ResultSet resultSet = psSelect.executeQuery();
			/* a container for them all */
			Map<Integer, IndividualCompetitor> competitors = new HashMap<Integer, IndividualCompetitor>();
			
			/* reference for temporary competitor */
			IndividualCompetitor competitor = null;
			while (resultSet.next()) {
				competitor = new IndividualCompetitor(resultSet.getInt("id"),
						resultSet.getInt("type"), resultSet.getString("first_name"), 
						resultSet.getString("last_name"), resultSet.getDate("born_date").toString(),
						resultSet.getInt("id_team"));
				competitors.put(competitor.getId(), competitor);
			}
			
			/* clean up */
			resultSet.close();
			psSelect.close();
			conn.close();

			return competitors;
		}
		
		/**
		 * This method finds all the teams in the system
		 * @return
		 * @throws SQLException
		 */
		public static Map<Integer, Team> findAllTeams() throws SQLException {
			/* open the connection */
			Connection conn = DatabaseConnection.getConnection();
			/* prepare the query */
			PreparedStatement psSelect = conn
			.prepareStatement("SELECT * FROM competitor ORDER BY id WHERE type=" + Integer.toString(Competitor.TYPE_TEAM));
			/* the results are here */
			ResultSet resultSet = psSelect.executeQuery();
			/* a container for them all */
			Map<Integer, Team> competitors = new HashMap<Integer, Team>();
			
			/* reference for temporary competitor */
			Team competitor = null;
			while (resultSet.next()) {
				competitor = new Team(resultSet.getInt("id"), resultSet.getInt("type"), resultSet.getString("team_name"));
				competitors.put(competitor.getId(), competitor);
			}
			
			/* clean up */
			resultSet.close();
			psSelect.close();
			conn.close();

			return competitors;
		}

	}
