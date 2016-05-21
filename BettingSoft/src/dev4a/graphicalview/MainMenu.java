package dev4a.graphicalview;

import dev4a.system.BettingSystem;

public class MainMenu extends Menu {
	
	public MainMenu(BettingSystem bs, String pass) {
		
		super(bs, pass);	
		this.possibleMenus.add(this);
		this.possibleMenus.add(new ManagerMenu(bs, pass, this));
		
		
		
	}
	
	@Override
	public void showMenu() {
		System.out.println("");

		System.out.println("Main Menu");

		System.out.println("---------------------------");

		System.out.println("1. Authenticate manager");

		System.out.println("2. Exit the program");

		System.out.println("----------------------------");

		System.out.println("");

		System.out.print("Please select an option from 1-2");

		System.out.println("");

		System.out.println("");		
	}

	@Override
	protected int takeAction(int selected) {
		return 0;
		
	}

	
	
}
