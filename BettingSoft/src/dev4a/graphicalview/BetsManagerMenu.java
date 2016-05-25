package dev4a.graphicalview;

import dev4a.system.BettingSystem;

public class BetsManagerMenu extends Menu {
	
	
	/**
	 * Initialize the menu and set up the parent
	 * @param bs
	 * @param storredPass
	 */
	public BetsManagerMenu(BettingSystem bs, String storredPass, Menu parentMenu) {
		super(bs, storredPass);
		this.parentMenu = parentMenu;
	}

	@Override
	public void showMenu() {
		System.out.println("");

		System.out.println("Main Menu");

		System.out.println("---------------------------");

		System.out.println("1. See bets for subscriber");

		System.out.println("2. List all subscribers");

		System.out.println("3. List all competitions");
		
		System.out.println("*. Go back");

		System.out.println("----------------------------");

		System.out.println("");

		System.out.print("Please select an option from 1-3");

		System.out.println("");

		System.out.println("");
		
	}

	@Override
	protected int takeAction(int selected) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
