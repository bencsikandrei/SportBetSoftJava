package dev4a.competitor;

import java.util.HashMap;
import java.util.Map;

import dev4a.competition.Competition;
import dev4a.exceptions.BadParametersException;
import dev4a.subscriber.Subscriber;

public class IndividualCompetitor implements Competitor {

	/* attributes */
	private int id;
	private int type;
	private String lastName;
	private String firstName;
	private String bornDate;
		
	/* constructor */
	public IndividualCompetitor(){
		/* empty for hibernate */
	}
	/* proper constructor */
	public IndividualCompetitor(int id, int type, String firstName, String lastName, String bornDate){
		/* initialize */
		this.lastName = lastName;
		this.firstName = firstName;
		this.bornDate = bornDate;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
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
	public String getBornDate() {
		return bornDate;
	}
	public void setBornDate(String bornDate) {
		this.bornDate = bornDate;
	}
	@Override
	public boolean hasValidName() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void addMember(Competitor member) throws ExistingCompetitorException, BadParametersException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deleteMember(Competitor member) throws BadParametersException, ExistingCompetitorException {
		// TODO Auto-generated method stub
		
	}
	
			
	@Override 
	public String toString() {
		/* return the full name */
		return this.firstName + " " + this.lastName;
	}
	
}
