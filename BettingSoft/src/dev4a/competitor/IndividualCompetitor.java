package dev4a.competitor;

import dev4a.exceptions.BadParametersException;

public class IndividualCompetitor implements Competitor {
	/* attributes */
	private String lastName;
	private String firstName;
	private String bornDate;
	
	/* constructor */
	public IndividualCompetitor(){
		/* empty for hibernate */
	}
	/* proper constructor */
	public IndividualCompetitor(String firstName, String lastName, String bornDate){
		/* initialize */
		this.lastName = lastName;
		this.firstName = firstName;
		this.bornDate = bornDate;
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
