package dev4a.graphicalview;

import dev4a.system.BettingSystem;

public class BetsManagerMenu extends Menu {
	
	
	
	public BetsManagerMenu(BettingSystem bs, String storredPass) {
		super(bs, storredPass);
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
		
	}
	
}
