package dev4a.graphicalview;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import dev4a.system.BettingSystem;

public class ClientMenu extends Menu {
	/**
	 * The main manu of the manager
	 * @param bs
	 * @param pass
	 */
	public ClientMenu (BettingSystem bs, String pass) {
		super(bs, pass);
		/* the parent */
		this.parentMenu = this;
	}

	@Override
	public void showMenu() {
		System.out.println("");

		System.out.println("Client Menu");

		System.out.println("---------------------------");

		System.out.println("1. Bet on winner");

		System.out.println("2. Bet on podium");

		System.out.println("3. View my info");

		System.out.println("4. Consult winners on competition");

		System.out.println("5. Change password");

		System.out.println("6. List all competitions");
		
		System.out.println("7. List all competitiors in competition");
		
		System.out.println("*. Exit system");

		System.out.println("----------------------------");

		System.out.println("");

		System.out.print("Please select an option from 1-7");

		System.out.println("");

		System.out.println("");

	}
	/**
	 * Take aciton based on the 'selected' integer
	 */
	@Override
	protected int takeAction(int selected) {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		switch (selected) {
		case 1:
			try {
				System.out.println("Insert username");
				String username = br.readLine();
				/* insert the winner bet data */
				System.out.println("Insert number of tokens");
				long numberTokens = Long.parseLong(br.readLine());
				System.out.println("Insert competition name");
				String competition = br.readLine();
				System.out.println("Insert winner id");
				int winner = Integer.parseInt(br.readLine());
				/* persist */
				this.bettingSystem.betOnWinner(
						numberTokens, 
						competition, 
						bettingSystem.getCompetitorById(winner), 
						username, 
						this.storedPass
						);

			} catch (Exception ex) {
				ex.printStackTrace();
			}

			break;

		case 2:
			try {
				System.out.println("Insert username");
				String username = br.readLine();
				/* insert the winner bet data */
				System.out.println("Insert number of tokens");
				long numberTokens = Long.parseLong(br.readLine());
				System.out.println("Insert competition name");
				String competition = br.readLine();
				System.out.println("Insert winner id");
				int winner = Integer.parseInt(br.readLine());
				System.out.println("Insert second id");
				int second = Integer.parseInt(br.readLine());
				System.out.println("Insert third id");
				int third = Integer.parseInt(br.readLine());
				/* persist */
				this.bettingSystem.betOnPodium(
						numberTokens, 
						competition, 
						bettingSystem.getCompetitorById(winner), 
						bettingSystem.getCompetitorById(second), 
						bettingSystem.getCompetitorById(third), 
						username, 
						this.storedPass
						);

			} catch (Exception ex) {
				ex.printStackTrace();
			}

			break;
		case 3:
			try {
				System.out.println("Insert username");
				String username = br.readLine();
				
				this.bettingSystem.infosSubscriber(username, this.storedPass);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			break;
			
		case 4:
			try {
				/* insert the data */
				System.out.println("Insert competition name");

				String competition = br.readLine();
				
				this.bettingSystem.consultResultsCompetition(competition);
				
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			break;
		
		case 5:
			try {
				/* insert username */
				System.out.println("Insert username");
				String username = br.readLine();
				/* the new pass */
				System.out.println("Insert new password");
				String newPassword = br.readLine();
				/* change pass */
				bettingSystem.changeSubsPwd(username, this.storedPass, newPassword);
			} catch (Exception e) {
				System.out.println("Something went wrong..");
			}
			break;	
		case 6:
			try {

				this.bettingSystem.printCompetitions();

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			break;
		case 7:
			try {

				/* insert the data */
				System.out.println("Insert competition name");

				String competition = br.readLine();
				/* print */
				this.bettingSystem.printCompetitors(competition);

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
