package dev4a.competitor;

import java.util.HashMap;
import java.util.Map;

import dev4a.competition.Competition;
import dev4a.exceptions.BadParametersException;

public class Team implements Competitor {
	/* attributes */
	private int id;
	private int type;
	private String name;
	private int id_team;
	
	/* constructor */
	public Team(){
		/* empty for hibernate */
	}
	/* proper constructor */
	public Team(int id, int type, String name) {
		this.name = name;
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
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override 
	public String toString() {
		/* just return the name of the team */
		return this.name;
	}

}
