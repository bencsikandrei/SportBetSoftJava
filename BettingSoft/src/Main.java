import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;

import dev4a.competition.Competition;
import dev4a.competition.CompetitionException;
import dev4a.competition.ExistingCompetitionException;
import dev4a.competitor.Competitor;
import dev4a.competitor.ExistingCompetitorException;
import dev4a.exceptions.AuthenticationException;
import dev4a.exceptions.BadParametersException;
import dev4a.subscriber.ExistingSubscriberException;
import dev4a.subscriber.Subscriber;
import dev4a.subscriber.SubscriberException;
import dev4a.system.System;
import dev4a.utils.DatabaseConnection;

public class Main {

	public static void main(String[] args) {
		System bettingSystem = new System("1234");

		try {
			java.lang.System.out.println("Truncating ...");
			initializeDatabase();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		addSubscribers(bettingSystem);
		
		addCompetitions(bettingSystem);

		//addCompetitor(bettingSystem);
	}

	private static void initializeDatabase() throws SQLException {
		/* open the connection */
		Connection conn = DatabaseConnection.getConnection();
		/* create the delete query */
		Statement sTruncate = conn.createStatement();
		/* clean up */
		sTruncate.executeUpdate("TRUNCATE subscriber CASCADE");
		sTruncate.executeUpdate("TRUNCATE bet CASCADE");
		sTruncate.executeUpdate("TRUNCATE competition CASCADE");
		sTruncate.executeUpdate("TRUNCATE competitor CASCADE");
		sTruncate.executeUpdate("TRUNCATE participant CASCADE");
		sTruncate.executeUpdate("TRUNCATE manager CASCADE");
		
		sTruncate.close();
		conn.close();

	}

	private static void addCompetitions(System bettingSystem) {
		try {
			/* make some competitiors */
			java.util.ArrayList<Competitor> listOfCompetitors = new java.util.ArrayList<Competitor>();
			
			listOfCompetitors.add(bettingSystem.createCompetitor("Ronaldo","Cristiano", "1985-02-05", "1234"));
			listOfCompetitors.add(bettingSystem.createCompetitor("Iniesta","Andres", "1984-05-11", "1234"));

			bettingSystem.addCompetition("Real_Madrid_-_Barcelona_Primera_Division", Calendar.getInstance(), listOfCompetitors, "1234");
			bettingSystem.addCompetition("Real_Madrid_-_Barcelona_Primera_Division_1", Calendar.getInstance(), listOfCompetitors, "1234");
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} 

	}

	private static void addSubscribers(System bettingSystem) {
		try {
			/* adding some subscribers */
			bettingSystem.subscribe("Andrei", "Bencsik", "afbencsi", "1992-08-12", "1234");
			bettingSystem.subscribe("Andrei", "Bencsik", "bencsan", "1992-08-12", "1234");
			bettingSystem.subscribe("Andrei", "Bencsik", "florinben", "1992-08-12", "1234");
			bettingSystem.subscribe("Andrei", "Bencsik", "andrei1", "1992-08-12", "1234");
			bettingSystem.subscribe("Andrei", "Bencsik", "andrei92", "1992-08-12", "1234");
			bettingSystem.subscribe("Andrei", "Bencsik", "andrusca", "1992-08-12", "1234");
			/* also showing the password */
			java.lang.System.out.println( "Password : " + bettingSystem.subscribe("Ahmed", "Sami-Mohamed", "asamimoh", "1992-08-12", "1234") );

			java.lang.System.out.println( "Password : " + bettingSystem.subscribe("Florian", "Dumbovski", "fdumbov", "1992-08-12", "1234") );
			/* testing the to String method */
			java.lang.System.out.println(bettingSystem.getSubscriberByUserName("afbencsi").toString());
			
			/* some crediting */
			bettingSystem.creditSubscriber("bencsan", 567, "1234");
			bettingSystem.creditSubscriber("asamimoh", 9999, "1234");
			bettingSystem.creditSubscriber("andrei1", 1000, "1234");
			bettingSystem.creditSubscriber("afbencsi", 20, "1234");
			bettingSystem.creditSubscriber("fdumbov", 100, "1234");
			
			/* some debiting */
			bettingSystem.debitSubscriber("afbencsi", 10,"1234");
			bettingSystem.debitSubscriber("afbencsi", 10,"1234");
			bettingSystem.debitSubscriber("fdumbov", 10,"1234");
			

		} catch (BadParametersException ex) {
			ex.printStackTrace();
		} catch (AuthenticationException auth) {
			auth.printStackTrace();
		} catch (ExistingSubscriberException subEx) {
			subEx.printStackTrace();
			java.lang.System.out.println("Username already exists!");
		} catch( SubscriberException ex1) {
			ex1.printStackTrace();
		} catch (Exception gen) {
			gen.printStackTrace();
		}

		try {
			/* some unsubscribtions */
			java.lang.System.out.println( bettingSystem.unsubscribe("afbencsi", "1234") );
			

		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			bettingSystem.printSubscribers("1234");
		} catch (Exception ex) {

		}
	}

	private static void addCompetitor(System bettingSystem){
		try{
			//bettingSystem.createCompetitor("Messi","Lionel", "1987-06-24", "1234");
			/* adding Lionel Messi to the match, he does not want to miss it! and we don't want him to miss it either */
			//bettingSystem.addCompetitor("Real_Madrid_-_Barcelona_Primera_Division",bettingSystem.createCompetitor("Messi","Lionel", "1987-06-24", "1234"),"1234");
			Competitor player =bettingSystem.createCompetitor("Messi","Lionel", "1987-06-24", "1234");
			Competitor player1 = bettingSystem.createCompetitor("Rodriguez","James", "1991-07-12", "1234");
			bettingSystem.addCompetitor("Real_Madrid_-_Barcelona_Primera_Division",player,"1234");	
			
			/* deleting poor James :( */
			//bettingSystem.deleteCompetitor("Real_Madrid_-_Barcelona_Primera_Division",player,"1234");

		} catch (AuthenticationException auth){
			auth.printStackTrace();
		} catch (ExistingCompetitionException compEx){
			compEx.printStackTrace();
		} catch (CompetitionException comp){
			comp.printStackTrace();
		} catch (ExistingCompetitorException comptrEx){
			comptrEx.printStackTrace();
		} catch (BadParametersException ex){
			ex.printStackTrace();
		}	
	}

}
