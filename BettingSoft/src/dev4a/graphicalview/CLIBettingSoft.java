package dev4a.graphicalview;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

import dev4a.exceptions.AuthenticationException;
import dev4a.system.BettingSystem;

public class CLIBettingSoft {
	/* the system */
	private BettingSystem bettingSystem;
	/* the choice */
	private int selected;
	
	private String storedPassword = "1234";
	
	private BufferedReader br;
	
	private Menu currentMenu;
	
	public CLIBettingSoft(BettingSystem bettingSys) {
		/* our new system */
		this.bettingSystem = bettingSys;
		
		/* to read the input */
		br = new BufferedReader(new InputStreamReader(System.in));
		
		this.currentMenu = new MainMenu(bettingSys, storedPassword);
		
		while(true) {
			
			showCurrentMenu();

			try {

				selected = Integer.parseInt(br.readLine());
				if( currentMenu.possibleMenus.size() < 1) {
					
					currentMenu.takeAction(selected);
					
				}
				else 
					this.currentMenu = this.currentMenu.possibleMenus.get(selected-1);


			} catch (Exception ioe) {

				System.out.println("IO error trying to read your input." + ioe);

				System.exit(1);

			}


		}
	}

	private void showCurrentMenu() {
		this.currentMenu.showMenu();
		
	}

	private void showSubscribersOptions() {

		

	}

	private void showCompetitorsOptions() {

		System.out.println("");

		System.out.println("Competitors Menu");

		System.out.println("---------------------------");

		System.out.println("1. Create individual competitor");

		System.out.println("2. Create team competitor");

		System.out.println("3. Delete competitor");

		System.out.println("4. Add individual competitor to team");

		System.out.println("----------------------------");

		System.out.println("");

		System.out.print("Please select an option from 1-4");

		System.out.println("");

		System.out.println("");

	}

	private void showCompetitionsOptions() {

		System.out.println("");

		System.out.println("Main Menu");

		System.out.println("---------------------------");

		System.out.println("1. Add subscriber");

		System.out.println("2. Delete subscriber");

		System.out.println("3. Credit subscriber");

		System.out.println("4. Debit subscriber");

		System.out.println("----------------------------");

		System.out.println("");

		System.out.print("Please select an option from 1-4");

		System.out.println("");

		System.out.println("");

	}

	private void showManagerOptions(int selected) throws IOException {
		

		switch (selected) {

		case 1:
			/* first authenticatte */
			if(true) { // askForAuthentication()
				/* manager options */


				selected = Integer.parseInt(br.readLine());


				switch (selected) {

				case 1:

					showSubscribersOptions();

					break;

				case 2:
					
					showCompetitorsOptions();
					
					break;

				case 3:
					
					showCompetitionsOptions();
					
					break;

				case 4:
					
					showBetsOptions();
					
					break;

				case 5:
					
					
					
					break;

				case 6:
					try {
						bettingSystem.printSubscribers(this.storedPassword);
					} catch (AuthenticationException e) {
						e.printStackTrace();
					}
					break;

				case 7:
					
					bettingSystem.printCompetitions();
					
					break;
				}

			}
			else {
				System.out.println("Authentication failed!");
			}
			break;

		case 2:



			break;

		case 3:

			break;

		}
	}

	private void showBetsOptions() {
		// TODO Auto-generated method stub
		
	}

	private boolean askForAuthentication() {
		/* get the console */
		Console console = System.console();

		if (console == null) {
			System.out.println("Couldn't get Console instance");
			System.exit(0);
		}

		char [] passwordArray = console.readPassword("Enter your password: ");

		try {
			this.bettingSystem.authenticateMngr(new String(passwordArray)); 
			this.storedPassword = new String(passwordArray);
			return true;
		} catch (AuthenticationException ex) {
			return false;
		}
	}

}
