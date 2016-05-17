import dev4a.exceptions.AuthenticationException;
import dev4a.exceptions.BadParametersException;
import dev4a.subscriber.ExistingSubscriberException;
import dev4a.subscriber.SubscriberException;
import dev4a.system.System;

public class Main {

	public static void main(String[] args) {
		System bettingSystem = new System("1234");
		
		try {
			java.lang.System.out.println( bettingSystem.subscribe("Andrei", "Bencsik", "afbencsi", "1992-08-12", "1234") );
			
			java.lang.System.out.println( bettingSystem.subscribe("Andrei", "Bencsik", "afbencsi", "1992-08-12", "1234") );
			
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
	}

}
