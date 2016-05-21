package dev4a.graphicalview;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import dev4a.system.BettingSystem;

public class CompetitorsManagerMenu extends Menu {

	public CompetitorsManagerMenu(BettingSystem bs, String storredPass) {
		super(bs, storredPass);
		
	}

	@Override
	public void showMenu() {
		System.out.println("");

		System.out.println("Competitors Menu");

		System.out.println("---------------------------");

		System.out.println("1. Create individual competitor");
		
		System.out.println("2. Create team");
		
		System.out.println("3. Delete competitor from comptition");

		System.out.println("4. Add to competition");
		
		System.out.println("5. Add member to team");
		
		System.out.println("6. List competitors in competition");
		
		System.out.println("----------------------------");

		System.out.println("");

		System.out.print("Please select an option from 1-6");

		System.out.println("");

		System.out.println("");
		
	}

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
				System.out.println("Insert born date");
				String borndate = br.readLine();
				if(this.bettingSystem != null)
					this.bettingSystem.createCompetitor(lastName, firstName, borndate, this.storedPass);

			} catch (Exception ex) {
				ex.printStackTrace();
			}

			break;

		case 2:
			try {

				System.out.println("Insert team name");

				String teamName = br.readLine();
				
				this.bettingSystem.createCompetitor(teamName, this.storedPass);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			break;
		case 3:
			try {

				System.out.println("Insert id");

				String id = br.readLine();
				
				System.out.println("Insert competition name");

				String competition = br.readLine();
				
				this.bettingSystem.deleteCompetitor(competition, bettingSystem.getCompetitorById(Integer.valueOf(id)), this.storedPass);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			break;
			
		case 4:
			try {

				System.out.println("Insert competition name");

				String competition = br.readLine();
				
				System.out.println("Insert id of the competitor");

				int competitor = Integer.parseInt(br.readLine());
				
				this.bettingSystem.addCompetitor(competition, bettingSystem.getCompetitorById(competitor), this.storedPass);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			break;
		case 5:
			try {
				
				System.out.println("Insert team id");

				int teamId = Integer.parseInt(br.readLine());
				
				System.out.println("Insert individual competitor id");

				int competitorId = Integer.parseInt(br.readLine());
				
				System.out.println("For the moment this does not work..");

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			break;	
		case 6:
			try {
				
				System.out.println("Insert competition name");

				String competition = br.readLine();
				
				this.bettingSystem.printCompetitors(competition);

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
			break;
		}
		
	}
	
}
