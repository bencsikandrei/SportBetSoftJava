package dev4a.bets;
import dev4a.competitor.*;

import java.util.Date;
/**
 * 
 * @author Group 4A
 * @version 0.1 (BETA)
 * 
 * POJO !
 * This class serves the purpose of modeling
 * bets in the BETTINGSOFT application
 * for the Fil Rouge project (Spring 2016)
 * 
 */
public class PodiumBet extends Bet {
	
	/* the winner competitor of the competition */
	protected Competitor winner;
	/* the second competitor of the competition */
	protected Competitor second;
	/* the third competitor of the competition */
	protected Competitor third;
	
	/* all getters and setters are form the supper class
	 * we need a constructor
	 */
	
	/* constructor */
	public PodiumBet(long nbOfTokens, String competition, 
			Competitor winner, Competitor second, Competitor third, 
			String username, Date betDate) {
		this.numberOfTokens = nbOfTokens;
		
		this.identifier = 0;
		
		this.betDate = betDate;
		
		this.state = Betstate.INPROGRESS;
		
		this.userName = username;
				
	}
	
	/* getters and setters */
	
	public Competitor getWinner() {
		return winner;
	}
	public void setWinner(Competitor winner) {
		this.winner = winner;
	}
	public Competitor getSecond() {
		return second;
	}
	public void setSecond(Competitor second) {
		this.second = second;
	}
	public Competitor getThird() {
		return third;
	}
	public void setThird(Competitor third) {
		this.third = third;
	}
	
	@Override
	public String toString() {
		/* generate a complete string for the bet */
		String betDetails = "Bet no. " + this.identifier + "\nDate: " + this.betDate; 
		return betDetails;
	}
}
