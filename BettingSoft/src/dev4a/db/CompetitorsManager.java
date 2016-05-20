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

	public static Competitor findByiD(int id) throws SQLException
		{
			Connection c = DatabaseConnection.getConnection(); 
			PreparedStatement psSelect = c.prepareStatement("select * from competitor where id=?");

			psSelect.setInt(1, id);
			ResultSet resultSet = psSelect.executeQuery();

			Competitor competitor = null;
			while(resultSet.next())
			{
				if(resultSet.getInt("type") == Competitor.TYPE_INDIVIDUAL) {
					competitor = new IndividualCompetitor(resultSet.getString("first_name"), 
							resultSet.getString("last_name"), resultSet.getDate("born_date").toString());
				} else if(resultSet.getInt("type") == Competitor.TYPE_TEAM) {
					competitor = new Team(resultSet.getString("team_name"));
				}
			}

			resultSet.close();
			psSelect.close();
			c.close();

			return competitor;
		}

}
