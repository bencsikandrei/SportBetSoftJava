import java.util.Calendar;

import dev4a.exceptions.AuthenticationException;
import dev4a.exceptions.BadParametersException;
import dev4a.subscriber.ExistingSubscriberException;
import dev4a.subscriber.SubscriberException;
import dev4a.system.System;

public class Main {
	
	
	
	public static void main(String[] args) {
		System bettingSystem = new System("1234");
		
		addSubscribers(bettingSystem);
		
		
		
		addCompetitions(bettingSystem);
		
		
		
		
	}

	private static void addCompetitions(System bettingSystem) {
		try {
			bettingSystem.addCompetition("Real Madrid - Barcelona Primera Division", Calendar.getInstance(), new java.util.ArrayList<dev4a.competitor.Competitor>(), "1234");
			// bettingSystem.addCompetition("Real Madrid - Barcelona Primera Division", Calendar.getInstance(), new java.util.ArrayList<dev4a.competitor.Competitor>(), "1234");
		
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}

	private static void addSubscribers(System bettingSystem) {
		try {
			java.lang.System.out.println( "Password : " + bettingSystem.subscribe("Andrei", "Bencsik", "afbencsi", "1992-08-12", "1234") );
			bettingSystem.creditSubscriber("afbencsi", 100, "1234");
			bettingSystem.debitSubscriber("afbencsi", 10,"1234");
			java.lang.System.out.println( "Password : " + bettingSystem.subscribe("Ahmed", "Sami-Mohamed", "asamimoh", "1992-08-12", "1234") );
						
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
			//java.lang.System.out.println( bettingSystem.unsubscribe("afbencsi", "1234") );
			
			java.lang.System.out.println( "Password : " + bettingSystem.subscribe("Florian", "Dumbovski", "fdumbov", "1992-08-12", "1234") );
		
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		try {
			bettingSystem.printSubscribers("1234");
		} catch (Exception ex) {
			
		}
	}
	
}
