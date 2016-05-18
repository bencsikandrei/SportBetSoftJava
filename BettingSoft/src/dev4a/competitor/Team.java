package dev4a.competitor;

import java.util.HashMap;
import java.util.Map;

import dev4a.competition.Competition;
import dev4a.exceptions.BadParametersException;

public class Team implements Competitor {
	/* attributes */
	private String name;
	/* competitions where this competitor participates */
	private Map<String,Competition> competitions = new HashMap();
	
	/* constructor */
	public Team(){
		/* empty for hibernate */
	}
	/* proper constructor */
	public Team(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
	private void addCompetitionsToList(Competition temporaryCompetition) {

		this.competitions.put(temporaryCompetition.getName(), temporaryCompetition);
	}
	
	private void removeCompetitionFromList(String competitionName) {

		this.competitions.remove(competitionName);
	}
	
	private Competition getCompetitionByName(String competitionName) {

		return this.competitions.get(competitionName);
	}
	
	@Override 
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override 
	public String toString() {
		/* just return the name of the team */
		return this.name;
	}

}
