package dev4a.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
				psPersist.setString(4, ((IndividualCompetitor) competitor).getBornDate());
			} else if (competitor.getType() == Competitor.TYPE_TEAM) {
				psPersist.setString(2, ((Team) competitor).getName());
				psPersist.setInt(3, ((Team) competitor).getIdTeam());
			}

			/* all fields in order */			
			psPersist.setDate(5, Date.valueOf(competitor.getBornDate()));
			psPersist.setLong(6, competitor.getNumberOfTokens());			
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
					resultSet.getString("last_name"), resultSet.getDate("born_date").toString());
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
		 * This method finds all the subscribers in the system
		 * @return
		 * @throws SQLException
		 */
		public static Map<int, Subscriber> findAll() throws SQLException {
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
