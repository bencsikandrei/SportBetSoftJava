package dev4a.bets;
import java.util.ArrayList;
import java.util.Date;

import dev4a.competitor.*;
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
public class Bet {
	/* the id of this bet -> serves for DB */
	protected int identifier;
	/* state in which the bet is 
	 * in progress, won , lost */
	protected Betstate state;
	/* how many tokens have been bet on this */
	protected long numberOfTokens;
	/* the type of the bet in which is
	 * 1 winner, 2 podium */
	protected int type;
	/* the date it was placed on */
	protected Date betDate;
	/* the username of the subscriber */
	protected String userName;
	/* the username of the subscriber */
	protected String competition;
	/* the winner competitor of the competition */
	protected Competitor winner;
	/* the second competitor of the competition */
	protected Competitor second;
	/* the third competitor of the competition */
	protected Competitor third;
	
	/* constructors */
	/* winner bet */
	public Bet(long nbOfTokens, String competition, 
			Competitor winner,
			String username, Date betDate) {
		this.state = Betstate.INPROGRESS;
		this.numberOfTokens = nbOfTokens;
		this.type = 1;
		this.betDate = betDate;
		this.userName = username;
		this.competition = competition;
		this.winner = winner;
	}
	/* podium bet */
	public Bet(long nbOfTokens, String competition, 
			Competitor winner, Competitor second, Competitor third, 
			String username, Date betDate) {
		this.state = Betstate.INPROGRESS;
		this.numberOfTokens = nbOfTokens;
		this.type = 2;
		this.betDate = betDate;
		this.userName = username;
		this.competition = competition;
		this.winner = winner;
		this.second = second;
		this.third = third;
	}
	
	/* getters and setters */
	
	public int getIdentifier() {
		return identifier;
	}
	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}
	public Betstate getState() {
		return state;
	}
	public void setState(Betstate state) {
		this.state = state;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public long getNumberOfTokens() {
		return numberOfTokens;
	}
	public void setNumberOfTokens(long numberOfTokens) {
		this.numberOfTokens = numberOfTokens;
	}
	public Date getBetDate() {
		return betDate;
	}
	public void setBetDate(Date betDate) {
		this.betDate = betDate;
	}
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
	public String getCompetition() {
		return competition;
	}
	public void setCompetition(String competition) {
		this.competition = competition;
	}
	
	/* get list of ID of competitior */
	public ArrayList<Integer> getListCompetitor(){
		ArrayList<Integer> listCompetitor = new ArrayList<Integer>();
		if(this.type == 1)
			listCompetitor.add(winner.getId());
		else{
			listCompetitor.add(winner.getId());
			listCompetitor.add(second.getId());
			listCompetitor.add(third.getId());
		}
		return listCompetitor;
	}
	
	public String toString() {
		/* generate a complete string for the bet */
		String betDetails = "Bet no. " + this.identifier + "\nDate: " + this.betDate; 
		return betDetails;
	}
}
