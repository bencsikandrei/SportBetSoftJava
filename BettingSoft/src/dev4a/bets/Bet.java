package dev4a.bets;
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
public abstract class Bet {
	/* the id of this bet -> serves for DB */
	protected int identifier;
	/* state in which the bet is 
	 * sold out, in progress, won , lost
	 */
	protected Betstate state;
	/* how many tokens have been bet on this */
	protected long numberOfTokens;
	/* the date it was placed on */
	protected Date betDate;
	
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
	public void betstate(Betstate state) {
		this.state = state;
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
	
}
