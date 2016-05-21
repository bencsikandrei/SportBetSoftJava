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
				if( selected == 1 && currentMenu instanceof MainMenu && !askForAuthentication() ) { 
					break;
				}
				if( currentMenu.possibleMenus.size() < 2 && !(currentMenu instanceof MainMenu) ) {
					
					if( currentMenu.takeAction(selected) == -1)
						currentMenu = currentMenu.possibleMenus.get(0);
					
				}
				else 
					if ( selected <= currentMenu.possibleMenus.size() )
						this.currentMenu = this.currentMenu.possibleMenus.get(selected);
				else 
					if( currentMenu.takeAction(selected) == -1)
						currentMenu = currentMenu.possibleMenus.get(0);

			} catch (Exception ioe) {

				System.out.println("IO error trying to read your input." + ioe);

				System.exit(1);

			}


		}
	}

	private void showCurrentMenu() {
		this.currentMenu.showMenu();
		
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
			System.out.println("Password you entered was :" + new String(passwordArray));
			System.out.println("Password expected was :" + bettingSystem.getPassword());
			
			this.bettingSystem.authenticateMngr(new String(passwordArray)); 
			this.storedPassword = new String(passwordArray);
			return true;
		} catch (AuthenticationException ex) {
			System.out.println("Authentication failed");
			return false;
		}
	}

}
