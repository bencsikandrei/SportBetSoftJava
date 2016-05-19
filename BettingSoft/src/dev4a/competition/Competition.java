package dev4a.competition;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dev4a.competitor.*;
import dev4a.bets.*;
/**
 * 
 * @author Group 4A
 * @version 0.1 (BETA)
 * 
 * POJO !
 * This class serves the purpose of modeling
 * competitions in the BETTINGSOFT application
 * for the Fil Rouge project (Spring 2016)
 * 
 */
public class Competition {
	
	public static States STATE;
	/* the name of the competition 
	 * UTF-8 string
	 */
	private String name;
	/* the starting date of the competition 
	 * 
	 */
	private Calendar startDate;
	/* the closing date of the competition 
	 * 
	 */
	private Calendar closingDate;
	/* the progress status -> @ENUM States 
	 * 
	 */
	private States progress;
	/* the sport played in the competition 
	 * UTF-8 string
	 */
	private String sport;
	/* the competitors of this competition
	 * 
	 */
	private List<Competitor> allCompetitors = new ArrayList();
	/* the winners of this competition
	 * 
	 */
	private List<Competitor> winners;
	/* the types of bets allowed of the competition 
	 * UTF-8 string
	 * possible values:
	 * 			- w: only winner allowed
	 * 			- p: only podium allowed 
	 * 			- wp: both winner and podium allowed
	 */
	private String betType;
	/* the bets done in this competition
	 * 
	 */
	private List<Bet> bets;
	
	/* constructors */
	public Competition() {
		/**
		 * Empty constructor for hibernate
		 */
	}
	/* constructor with some params */
	public Competition(String name, Calendar startDate, Calendar closingDate, States inProgress, List<Competitor> allCompetitors){
		this.name = name;
		this.startDate = startDate;
		this.closingDate = closingDate;
		this.progress = this.STATE.STARTED;
		this.allCompetitors = allCompetitors;
	}
	/* constructor with all params */
	public Competition(String name, Calendar startDate, Calendar closingDate, States inProgress, String sport, List<Competitor> allCompetitors, String betType){
		this.name = name;
		this.startDate = startDate;
		this.closingDate = closingDate;
		this.progress = this.STATE.STARTED;
		this.sport = sport;
		this.allCompetitors = allCompetitors;
		this.betType = betType;
	}
	
	/* getters and setters */
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Calendar getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}
	
	public Calendar getClosingDate() {
		return closingDate;
	}
	
	public void setClosingDate(Calendar closingDate) {
		this.closingDate = closingDate;
	}
	
	public States getInProgress() {
		return this.progress;
	}
	
	public void setInProgress(States newProgress) {
		this.progress = newProgress;
	}
	
	public String getSport() {
		return sport;
	}
	
	public void setSport(String sport) {
		this.sport = sport;
	}
	
	public List<Competitor> getAllCompetitors() {
		return allCompetitors;
	}
	
	public void setAllCompetitors(List<Competitor> allCompetitors) {
		this.allCompetitors = allCompetitors;
	}
	
	public void addCompetitor(Competitor competitor){
		this.allCompetitors.add(competitor);
	}
	
	public void removeCompetitor(Competitor competitor){
		this.allCompetitors.remove(competitor);
	}
	
	public List<Competitor> getWinners() {
		return winners;
	}
	
	public void setWinners(List<Competitor> winners) {
		this.winners = winners;
	}
	
	public List<Bet> getBets() {
		return bets;
	}
	
	public void addBet(Bet bet){
		this.bets.add(bet);
	}
	
	public void removeBet(Bet bet){
		this.bets.remove(bet);
	}
	
	public String getBetType() {
		return betType;
	}
	
	public void setBetType(String betType) {
		this.betType = betType;
	}
	
	public long getTotalNumberOfTokens() {
		long sum = 0;
		for (int i = 0; i<bets.size(); i++){
			sum += bets.get(i).getNumberOfTokens();
		}
		return sum;
	}
	
	public int getTotalNumberOfBets() {
		return bets.size();
	}
	/*
	 * Checks if a competitor is in the competition 
	 */
	public boolean isCompetitor(Competitor competitor) {
		return this.allCompetitors.contains(competitor);
	}
	
	@Override
	public String toString() {
		/* return only the name */
		return this.name;
	}
	
}
