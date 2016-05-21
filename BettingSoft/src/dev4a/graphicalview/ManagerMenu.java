package dev4a.graphicalview;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import dev4a.system.BettingSystem;

public class ManagerMenu extends Menu {
	
	public ManagerMenu (BettingSystem bs, String pass, Menu parentMenu) {
		super(bs, pass);

		this.possibleMenus.add(parentMenu);
		this.possibleMenus.add(new SubscribersManagerMenu(bs, pass, this));
		this.possibleMenus.add(new CompetitorsManagerMenu(bs, pass));
		this.possibleMenus.add(new CompetitionsManagerMenu(bs, pass));
		this.possibleMenus.add(new BetsManagerMenu(bs, pass));
		
	}
	
	@Override
	public void showMenu() {
		System.out.println("");

		System.out.println("Main Menu");

		System.out.println("---------------------------");

		System.out.println("1. Manage subscribers");

		System.out.println("2. Manage competitors");

		System.out.println("3. Manage competitions");

		System.out.println("4. Manage bets");

		System.out.println("5. Change password");

		System.out.println("6. List all subscribers");

		System.out.println("7. List all competitions");

		System.out.println("----------------------------");

		System.out.println("");

		System.out.print("Please select an option from 1-7");

		System.out.println("");

		System.out.println("");
		
	}

	@Override
	protected int takeAction(int selected) {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		switch (selected) {
		case 5:
				
				System.out.println("Sorry this is not yet implemented :)");

			break;	
		case 6:
			try {

				this.bettingSystem.printSubscribers(storedPass);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			break;
		case 7:
			try {
				
				this.bettingSystem.printCompetitions();

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
