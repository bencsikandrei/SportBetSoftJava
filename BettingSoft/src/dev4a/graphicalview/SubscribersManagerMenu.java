package dev4a.graphicalview;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import dev4a.system.BettingSystem;

public class SubscribersManagerMenu extends Menu {
	
	public SubscribersManagerMenu(BettingSystem bs, String pass) {
		super(bs, pass);
	}
	
	@Override
	public void showMenu() {
		System.out.println("");

		System.out.println("Subscribers Menu");

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

	@Override
	protected void takeAction(int selected) {

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

		default:
			break;
		}

	}

}
