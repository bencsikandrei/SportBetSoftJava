package dev4a.competition;
import java.util.Calendar;
import java.util.List;
import dev4a.competitor.*;
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
	/* the status of the competition 
	 * UTF-8 string
	 */
	private boolean inProgress;
	/* the sport played in the competition 
	 * UTF-8 string
	 */
	private String sport;
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
	/* constructors */
	public Competition() {
		/**
		 * Empty constructor for hibernate
		 */
	}
	/* constructor with all params */
	public Competition(String name, Calendar startDate, Calendar closingDate, boolean inProgress){
		this.name = name;
		this.startDate = startDate;
		this.closingDate = closingDate;
		this.inProgress = inProgress;
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
	
	public boolean isInProgress() {
		return inProgress;
	}
	
	public void setInProgress(boolean inProgress) {
		this.inProgress = inProgress;
	}
	
	public String getSport() {
		return sport;
	}
	
	public void setSport(String sport) {
		this.sport = sport;
	}
	
	public List<Competitor> getWinners() {
		return winners;
	}
	
	public void setWinners(List<Competitor> winners) {
		this.winners = winners;
	}
	
	public String getBetType() {
		return betType;
	}
	
	public void setBetType(String betType) {
		this.betType = betType;
	}
	
	public long getTotalNumberOfTokens() {
		return 0l;
	}
	
	public int getTotalNumberOfBets() {
		return 0;
	}
	
	@Override
	public String toString() {
		/* return only the name */
		return this.name;
	}
	
}