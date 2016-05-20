package dev4a.competitor;

import java.util.concurrent.atomic.AtomicInteger;

import dev4a.exceptions.BadParametersException;

public class IndividualCompetitor implements Competitor {

	/* attributes */
	private static AtomicInteger uniqueId = new AtomicInteger();
	/* Id for the DB */
	private int id;
	/* type = 1 --> IndividualCompetitor */
	private int type;
	/* Competitor surname */
	private String lastName;
	/* Competitor name */
	private String firstName;
	/* Competitor born date */
	private String bornDate;
	/* Id of the team, if the competitor belongs to a team */
	private int id_team;
		
	/* constructor */
	public IndividualCompetitor(){
		/* empty for hibernate */
	}
	/* proper constructor for a competitor */
	public IndividualCompetitor(String firstName, String lastName, String bornDate){
		/* initialize */
		this.id = uniqueId.getAndIncrement();
		this.type = TYPE_INDIVIDUAL; 
		this.lastName = lastName;
		this.firstName = firstName;
		this.bornDate = bornDate;
	}
	/* proper constructor for a competitor in team */
	public IndividualCompetitor(String firstName, String lastName, String bornDate, int id_team){
		/* initialize */
		this.id = uniqueId.getAndIncrement();
		this.type = TYPE_INDIVIDUAL; 
		this.lastName = lastName;
		this.firstName = firstName;
		this.bornDate = bornDate;
		this.id_team = id_team;
	}
	
	public int getId() {
		return id;
	}
	
	public int getType() {
		return type;
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
	public int getIdTeam() {
		return id_team;
	}
	public void setIdTeam(int id_team) {
		this.id_team = id_team;
	}
	@Override
	public boolean hasValidName() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public void addMember(Competitor member) throws ExistingCompetitorException, BadParametersException {
		
	}
	
	@Override
	public void deleteMember(Competitor member) throws BadParametersException, ExistingCompetitorException {
		
	}
	
			
	@Override 
	public String toString() {
		/* return the full name */
		return this.firstName + " " + this.lastName;
	}
	
}
