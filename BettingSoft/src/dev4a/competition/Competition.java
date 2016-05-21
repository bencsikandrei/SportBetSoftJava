package dev4a.competition;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	public static final int STARTED = 1;
	public static final int FINISHED = 1;
	public static final int SOLDOUT = 1;
	public static final int CANCELED = 1;
	
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
	private int status;
	/* the sport played in the competition 
	 * UTF-8 string
	 */
	private String sport;
	/* the competitors of this competition
	 * 
	 */
	private Map<Integer, Competitor> allCompetitors = new HashMap<>();
	/* the winners of this competition
	 * 
	 */
	private Map<Integer, Competitor> winners = new HashMap<>();
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
	public Competition(String name, Calendar startDate, Calendar closingDate, States inProgress, Map<Integer, Competitor> allCompetitors){
		this.name = name;
		this.startDate = startDate;
		this.closingDate = closingDate;
		this.status = STARTED;
		this.allCompetitors = allCompetitors;
	}
	/* constructor with all params */
	public Competition(String name, Calendar startDate, Calendar closingDate, String sport, Map<Integer, Competitor> allCompetitors, String betType){
		this.name = name;
		this.startDate = startDate;
		this.closingDate = closingDate;
		this.status = STARTED;
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
	
	public int getStatus() {
		return this.status;
	}
	
	public void setStatus(int newStatus) {
		this.status = newStatus;
	}
	
	public String getSport() {
		return sport;
	}
	
	public void setSport(String sport) {
		this.sport = sport;
	}
	
	public Map<Integer, Competitor> getAllCompetitors() {
		return allCompetitors;
	}
	
	public void setAllCompetitors(Map<Integer, Competitor> allCompetitors) {
		this.allCompetitors = allCompetitors;
	}
	
	public void addCompetitor(Competitor competitor) {
		System.out.println(competitor.getId() + " " + competitor.toString());
		this.allCompetitors.put(new Integer(competitor.getId()), competitor);
	}
	
	public void removeCompetitor(Competitor competitor){
		this.allCompetitors.remove(competitor);
	}
	
	public Map<Integer, Competitor> getWinners() {
		return winners;
	}
	
	public void setWinners(Map<Integer, Competitor> winners) {
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
	
	/* return the number of tokens of a given type bet on this competition */
	public long getTotalNumberOfTokens(int betType) {
		long sum = 0;
		for(Bet b:this.bets){
			if(b.getType()==betType)
				sum += b.getNumberOfTokens();
		}
		return sum;
	}
	
	/* return the number of bets of a given type bet on this competition */
	public int getTotalNumberOfBets(int betType) {
		int count = 0;
		for(Bet b:this.bets){
			if(b.getType()==betType)
				count++;
		}
		return count;
	}
	/*
	 * Checks if a competitor is in the competition 
	 */
	public boolean hasCompetitor(Competitor competitor) {
		return this.allCompetitors.containsKey(competitor.getId());
	}
	
	@Override
	public String toString() {
		/* return only the name */
		return this.name;
	}
	
}
