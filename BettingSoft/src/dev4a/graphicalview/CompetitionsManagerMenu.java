package dev4a.graphicalview;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import dev4a.competitor.Competitor;
import dev4a.system.BettingSystem;

public class CompetitionsManagerMenu extends Menu {

	public CompetitionsManagerMenu(BettingSystem bs, String storredPass) {
		super(bs, storredPass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void showMenu() {
		System.out.println("");

		System.out.println("Competitions Menu");

		System.out.println("---------------------------");

		System.out.println("1. Add competition");

		System.out.println("2. Cancel competition");

		System.out.println("3. Delete competition");

		System.out.println("4. List all subscribers");

		System.out.println("5. List all competitions");
		
		System.out.println("*. Go back");		

		System.out.println("----------------------------");

		System.out.println("");

		System.out.print("Please select an option from 1-5");

		System.out.println("");

		System.out.println("");
		
	}

	@Override
	protected int takeAction(int selected) {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		switch (selected) {
		case 1:
			try {

				System.out.println("Insert competition name");
				String competition = br.readLine();
				System.out.println("Insert closing date (format yyyy-MM-dd)");
				String closingDate = br.readLine();
				/* to get the calendar from a string ..*/
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date = sdf.parse(closingDate);
				Calendar cal = sdf.getCalendar();
				
				System.out.println("Insert competitor id");
				int compId = Integer.parseInt(br.readLine());
				ArrayList<Competitor> competitors = new ArrayList<>();
				do {
					competitors.add(bettingSystem.getCompetitorById(compId));
					System.out.println("Insert competitor id");
				} while( (compId = Integer.parseInt(br.readLine())) != -1 );
				
				if(this.bettingSystem != null)
					this.bettingSystem.addCompetition(competition, cal, competitors, this.storedPass);

			} catch (Exception ex) {
				ex.printStackTrace();
			}

			break;

		case 2:
			try {

				System.out.println("Insert competition name");

				String competition = br.readLine();
				
				this.bettingSystem.cancelCompetition(competition, this.storedPass);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			break;
		case 3:
			try {

				System.out.println("Insert competition name");

				String competition = br.readLine();
				
				this.bettingSystem.deleteCompetition(competition, this.storedPass);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			break;
			
		case 4:
			try {

				this.bettingSystem.printSubscribers(this.storedPass);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			break;
		case 5:
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
