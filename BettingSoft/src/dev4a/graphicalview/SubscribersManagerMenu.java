package dev4a.graphicalview;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import dev4a.system.BettingSystem;

public class SubscribersManagerMenu extends Menu {
	/**
	 * Initialize the menu and set up the parent
	 * @param bs
	 * @param storredPass
	 */
	public SubscribersManagerMenu(BettingSystem bs, String pass, Menu parentMenu) {
		super(bs, pass);
		this.parentMenu = parentMenu;
	}
	/**
	 * This method shows the appropriate menu for each type 
	 * we have the options printed in order and the user can 
	 * select one of them or a higher number to obtain a different
	 * behavior
	 * 
	 */
	@Override
	public void showMenu() {
		System.out.println("");

		System.out.println("Subscribers Menu");

		System.out.println("---------------------------");

		System.out.println("1. Add subscriber");

		System.out.println("2. Delete subscriber");

		System.out.println("3. Credit subscriber");

		System.out.println("4. Debit subscriber");

		System.out.println("5. List subscribers");
		
		System.out.println("*. Go back");		
		
		
		System.out.println("----------------------------");

		System.out.println("");

		System.out.print("Please select an option from 1-4");

		System.out.println("");

		System.out.println("");

	}
	
	/**
	 * This method uses a simple choice selector (i.e. a switch statement)
	 * to chose the acction that is happening given the selected number
	 * 
	 * Uses the functions in the betting system given as a param to the class
	 * 
	 * @param selected (int) - the choice of the user
	 */
	@Override
	protected int takeAction(int selected) {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		switch (selected) {
		case 1:
			try {

				System.out.println("Insert last name");
				String lastName = br.readLine();
				System.out.println("Insert first name");
				String firstName = br.readLine();
				System.out.println("Insert username");
				String username = br.readLine();
				System.out.println("Insert born date");
				String borndate = br.readLine();
				if(this.bettingSystem != null)
					this.bettingSystem.subscribe(lastName, firstName, username, borndate, this.storedPass);

			} catch (Exception ex) {
				ex.printStackTrace();
			}

			break;

		case 2:
			try {

				System.out.println("Insert username");

				String username = br.readLine();
				
				this.bettingSystem.unsubscribe(username, this.storedPass);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			break;
		case 3:
			try {

				System.out.println("Insert username");

				String username = br.readLine();
				
				System.out.println("Insert number of tokens");

				long numberOfTokens = Long.parseLong(br.readLine());
				
				this.bettingSystem.creditSubscriber(username, numberOfTokens, this.storedPass);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			break;
			
		case 4:
			try {

				System.out.println("Insert username");

				String username = br.readLine();
				
				System.out.println("Insert number of tokens");

				long numberOfTokens = Long.parseLong(br.readLine());
				
				this.bettingSystem.debitSubscriber(username, numberOfTokens, this.storedPass);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			break;
		case 5:
			try {

				this.bettingSystem.printSubscribers(this.storedPass);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			break;
		default:
			return -1;
		}
		return 0;
	}
	
}
