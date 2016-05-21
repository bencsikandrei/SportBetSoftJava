package dev4a.subscriber;
import dev4a.bets.*;

import java.util.List;
import java.util.Map;
/**
 * 
 * 
 * @author Group 4A
 * @version 0.1 (BETA)
 * 
 * POJO !
 * This class serves the purpose of modeling
 * subscribers in the BETTINGSOFT application
 * for the Fil Rouge project (Spring 2016)
 * 
 */
public class Subscriber {

	/* The last name of the Subscriber 
	 * UTF-8 String 
	 */
	private String lastName;

	/* The first name of the Subscriber 
	 * UTF-8 String 
	 */
	private String firstName;

	/* The username of the Subscriber 
	 * UTF-8 String used to identify him UNIQUELY
	 */
	private String userName;

	/* The password of the Subscriber 
	 * UTF-8 String TODO HASH this!!
	 */
	private String password = "";

	/* The date of birth of the Subscriber 
	 * UTF-8 String ! Serves for checking age restrictions
	 */
	private String bornDate;

	/* The amount in the account of the Subscriber 
	 * a long integer ( he can be really rich if he bets right )
	 */
	private long numberOfTokens;
	/* list of bets
	 */
	private Map<Integer, Bet> bets;
	
	
	/* Constructors of this class */
	public Subscriber(){
		/*
		 * Empty constructor for Hibernate
		 */
	}

	public Subscriber( String lastName, String firstName, String userName, String bornDate) {
		/* the constructor with all params */
		this.lastName = lastName;
		this.firstName = firstName;
		this.userName = userName;
		this.bornDate = bornDate;
		this.numberOfTokens = 0l;
		this.password = "";
	}
	public  Subscriber( String lastName, String firstName, String userName ) {
		this(lastName, firstName, userName, null);
	}
	public  Subscriber( String lastName, String firstName, String userName, String password, String bornDate, long credit) {
		this(lastName, firstName, userName, bornDate);
		this.setNumberOfTokens(credit);
		this.changePassword(this.password, password);
	}
	/* account balancing functions */
	public long credit(long amount) {
		if (amount > 0) 
			this.numberOfTokens += amount;
		else 
			System.out.println("Wrong amount. Account unchanged.");
		return this.numberOfTokens;
	}
	/* debiting */
	public long debit(long amount) {
		if (amount > 0 && amount <= this.numberOfTokens ) 
			this.numberOfTokens -= amount;
		
		return this.numberOfTokens;
	}
	/* password management */
	public boolean changePassword(String oldPassword, String newPassword) {
		/* check validity of old pass */
		if ( oldPassword != getPassword() )
			return false;
		/* now set the new one */
		setPassword(newPassword);
		return true;
	}
	/* check password */
	public boolean checkPassword(String pass) {
		return this.password == pass;
	}
	
	/* getters and setters REQUIRED for the POJO  (hibernate as well) */
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getBornDate() {
		return bornDate;
	}
	
	public void setBornDate(String bornDate) {
		this.bornDate = bornDate;
	}
	
	public long getNumberOfTokens() {
		return numberOfTokens;
	}
	
	public void setNumberOfTokens(long numberOfTokens) {
		if( numberOfTokens > 0)
			this.numberOfTokens = numberOfTokens;
	}
	
	private String getPassword() {
		return password;
	}
	
	private void setPassword(String password) {
		this.password = password;
	}
	
	public Map<Integer, Bet> getBets() {
		return bets;
	}

	public void setBets(Map<Integer, Bet> bets) {
		this.bets = bets;
	}
	/* place a bet */
	public void placeBet(Bet bet) {
		/* add the bet to the list */
		this.bets.put(bet.getIdentifier(), bet);
		
	}
	/* cancel a bet */
	public long cancelBet(Bet betToCancel) {

		return 0l;
	}
	
	@Override
	public String toString() {
		/* simply return the username, since it is unique */
		return this.userName;
	}
	
	@Override 
	public boolean equals(Object obj) {
		/* check if it's instance of the subscriber class */
		if (!(obj instanceof Subscriber))
			return false;
		if ( ((Subscriber) obj).getUserName() == this.getUserName() )
			return true;
		return false;
	}
}
