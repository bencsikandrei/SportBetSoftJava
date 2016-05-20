package dev4a.system;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import javax.rmi.CORBA.Util;
import javax.xml.transform.Templates;

import java.util.List;

import dev4a.bets.*;
import dev4a.competition.*;
import dev4a.competitor.*;
import dev4a.exceptions.AuthenticationException;
import dev4a.exceptions.BadParametersException;
import dev4a.subscriber.*;
import dev4a.utils.Utils;
import dev4a.db.SubscribersManager;
/**
 * 
 * @author Group 4A
 * @version 0.1 (BETA)
 * 
 * POJO !
 * This class serves the purpose of modeling
 * the system in the BETTINGSOFT application
 * for the Fil Rouge project (Spring 2016)
 * 
 */
public class System implements Betting {
	/* attributes */
	/* the one and only managerPassword */
	private String mgrPassword = null;
	/* the utilities */
	Utils utility = new Utils();
	/* the list of all competitors in the System */
	private List<Competitor> allCompetitors = new ArrayList();
	/* all the competitions in the system */
	private Map<String,Competition> allCompetitions = new HashMap();
	/* all the subscribers in the System */
	private Map<String,Subscriber> allSubscribers = new HashMap();
	/* all the bets in the system */
	private List<Bet> allBets = new ArrayList();
	
	
	/* constructor */
	public System(String mgrPassword) {
		/* setting the pass */
		this.mgrPassword = mgrPassword; 
	}
	
	private String getMgrPassword() {
		return mgrPassword;
	}

	public void setMgrPassword(String mgrPassword) {
		// TODO add some logic for the setting opf pswd -> maybe check OLD pass -> New PASS
		this.mgrPassword = mgrPassword;
		
	}

	@Override
	public void authenticateMngr(String managerPwd) throws AuthenticationException {
		// TODO Auto-generated method stub
		if ( this.mgrPassword != managerPwd ) {
			throw new AuthenticationException();
		}
	}
	
	@Override
	public String subscribe(String lastName, String firstName, String username, String borndate, String managerPwd)
			throws AuthenticationException, ExistingSubscriberException, SubscriberException, BadParametersException {
		// TODO Auto-generated method stub
		/* first authenticate the manager */
		authenticateMngr(managerPwd);
		/* valid name ? */
		if ( ! (utility.checkValidName(lastName) && utility.checkValidName(firstName) ) )
			throw new BadParametersException();
		/* then check for the username (which is unique) */
		if ( getSubscriberByUserName(username) != null )
			throw new ExistingSubscriberException();
		/* give him a new password */
		String password = utility.randomString(8);
		/* if he does not exist, we can create him */
		Subscriber temporarySubscriber = new Subscriber(lastName, firstName, username, borndate); 
		try {
			SubscribersManager.persist(temporarySubscriber, password);
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
		/* we created him, now add him to the collection */
		addSubscriberToList(temporarySubscriber);		
		/* return the new password */
		return password;
	}

	private void addSubscriberToList(Subscriber temporarySubscriber) {
		/* 
		 *  hide the implementation
		 *  add the subscirber to any collection 
		 */
		
		this.allSubscribers.put(temporarySubscriber.getUserName(), temporarySubscriber);
	}
	
	private void removeSubscriberFromList(String username) {
		/* 
		 *  hide the implementation
		 *  remove the subscirber from any collection 
		 */
		this.allSubscribers.remove(username);
	}
	
	public Subscriber getSubscriberByUserName(String username) {
		// TODO make a method to get the sub from the List
		Subscriber sub = null;
		try {
			sub = SubscribersManager.findSubscriberByUserName(username);
		} catch(SQLException sqlex) {
			sqlex.printStackTrace();
		}
		return sub;
	}

	@Override
	public long unsubscribe(String username, String managerPwd)
			throws AuthenticationException, ExistingSubscriberException {
		/* first authenticate the manager */
		authenticateMngr(managerPwd);
		/* proceed to remove him */
		Subscriber toBeRemoved = getSubscriberByUserName(username);
		/* check if the username exists */
		if ( toBeRemoved == null )
			throw new ExistingSubscriberException("The user does not exist!");
		/* remove him from the collection */
		removeSubscriberFromList(username);
		/* return the number of tokens he had left */
		return toBeRemoved.getNumberOfTokens();
	}

	@Override
	public List<List<String>> listSubscribers(String managerPwd) throws AuthenticationException {
		/* first authenticate the manager */
		authenticateMngr(managerPwd);
		/* iterate the container and get all names and attributes */
		List<List<String>> printableSubs = new ArrayList();
		/* get the updated list */
		try {
			this.allSubscribers = SubscribersManager.findAll();
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
		for( Subscriber sub : this.allSubscribers.values() ) {
			/* store the details for each subscriber */
			List<String> subDetails = new ArrayList();
			/* */
			subDetails.add(sub.getFirstName());
			subDetails.add(sub.getLastName());
			subDetails.add(sub.getBornDate());
			subDetails.add( String.valueOf( sub.getNumberOfTokens() ) );
			/* insert it in the big list */
			printableSubs.add(subDetails);
		}
		return printableSubs;
	}
	
	public void printSubscribers(String mgrPassword) throws AuthenticationException {
		this.utility.printList(this.listSubscribers(mgrPassword));
	}

	@Override
	public void addCompetition(String competition, Calendar closingDate, Collection<Competitor> competitors,
			String managerPwd) throws AuthenticationException, ExistingCompetitionException, CompetitionException,
					BadParametersException {
		/* first authenticate the manager */
		authenticateMngr(managerPwd);
		/* valid name, date ? */
		if ( ! (utility.checkValidCompetition(competition) ) || competitors == null ) 
			// we do not validate competitor because we already did it when we created it.
			throw new BadParametersException();
		/* now check if it exists */
		Competition tempCompetition = getCompetitionByName(competition);
		/* the test */
		if( tempCompetition != null )
			throw new ExistingCompetitionException();
		/* check if the date is correct */
		if (closingDate.before(Calendar.getInstance()) || competitors.size()<2){
			throw new CompetitionException();
		}
		/* are there repeated competitors? */
		Set<Competitor> set = new HashSet<Competitor>(competitors);
		if (set.size() < competitors.size()){
			throw new CompetitionException();
		}		
		/* create it ! */
		tempCompetition = new Competition(competition, Calendar.getInstance(), closingDate,Competition.STATE.STARTED, new ArrayList(competitors));
		/* freshly created add it to our collection */
		addCompetitionToList(tempCompetition);		
		//TODO check BadParametresException
	}
	
	private void addCompetitionToList(Competition tempCompetition) {
		// hide the implementation
		this.allCompetitions.put(tempCompetition.getName(), tempCompetition);
	}
	

	private void removeCompetitionFromList(String compName) {
		// hide the implementation
		this.allCompetitions.remove(compName);
	}

	private Competition getCompetitionByName(String competition) {
		// hide the implementation
		return this.allCompetitions.get(competition);
	}

	@Override
	public void cancelCompetition(String competition, String managerPwd)
			throws AuthenticationException, ExistingCompetitionException, CompetitionException {
		/* first authenticate the manager */
		authenticateMngr(managerPwd);
		/* now checks if it exists */
		Competition toBeCanceled = getCompetitionByName(competition);
		/* checks if it exists */
		if( toBeCanceled == null )
			/* does not exist */
			throw new ExistingCompetitionException();
		/* checks closing date */
		if (toBeCanceled.getClosingDate().before(Calendar.getInstance()))
			throw new CompetitionException();
		/* take care of all the ongoing bets ! */
		List<Bet> listBets = toBeCanceled.getBets();
		for (int i = 0; i<listBets.size(); i++){
			Subscriber subscriber = getSubscriberByUserName(listBets.get(i).getUserName());
			/* returns tokens */
			subscriber.credit(listBets.get(i).getNumberOfTokens());
			/* deletes the bets from subscriber */
			subscriber.getBets().remove(listBets.get(i));
		}
		/* deletes the bets from System */
		allBets.removeAll(listBets);
		/* deletes the bets from Competition */
		toBeCanceled.getBets().removeAll(listBets);
		/* cancels it */
		toBeCanceled.setInProgress(Competition.STATE.CANCELED);
	}


	@Override
	public void deleteCompetition(String competition, String managerPwd)
			throws AuthenticationException, ExistingCompetitionException, CompetitionException {
		/* first authenticate the manager */
		authenticateMngr(managerPwd);
		/* now check if it exists */
		Competition toBeRemoved = getCompetitionByName(competition);
		/* check if it exists */
		if( toBeRemoved == null )
			/* does not exist */
			throw new ExistingCompetitionException();
		/* check if the competition is in a proper state */
		if (toBeRemoved.getClosingDate().after(Calendar.getInstance()))
			throw new CompetitionException();
		//if(!toBeRemoved.getInProgress().equals(Competition.STATE.SOLDOUT)) 
		/* now we can safely delete */
		removeCompetitionFromList(competition);
	}

	@Override
	public void addCompetitor(String competition, Competitor competitor, String managerPwd)
			throws AuthenticationException, ExistingCompetitionException, CompetitionException,
			ExistingCompetitorException, BadParametersException {
		/* first authenticate the manager */
		authenticateMngr(managerPwd);
		/* competition where the competitor is to be added */
		Competition myCompetition = getCompetitionByName(competition);
		/* check if it exists */
		if( myCompetition == null )
			/* does not exist */
			throw new ExistingCompetitionException();
		/* check if the competition is in a proper state */
		else if(!myCompetition.getInProgress().equals(Competition.STATE.STARTED)) {
			throw new CompetitionException();
		}
		/* check if the competitor is already in the competition */
		if(myCompetition.isCompetitor(competitor)){
			throw new CompetitionException();
		}
		/* now we can add the competitor */  
		myCompetition.addCompetitor(competitor); 
	}

		
	@Override
	public Competitor createCompetitor(String lastName, String firstName, String borndate, String managerPwd)
			throws AuthenticationException, BadParametersException {
		/* first authenticate the manager */
		authenticateMngr(managerPwd);
		/* check validity of the name */
		if( !(utility.checkValidName(lastName) && utility.checkValidName(firstName) && utility.checkValidDate(borndate) ) ) {
			throw new BadParametersException();
		}
		/* create the competitor */
		Competitor tempCompetitor = new IndividualCompetitor(firstName,lastName,borndate);
		/* add him to the list */
		addCompetitorToList(tempCompetitor);
		
		return tempCompetitor;
	}

	private void addCompetitorToList(Competitor competitor) {
		/* hide implementation */
		this.allCompetitors.add(competitor);
		
	}

	@Override
	public Competitor createCompetitor(String name, String managerPwd)
			throws AuthenticationException, BadParametersException {
		/* first authenticate the manager */
		authenticateMngr(managerPwd);
		/* check validity of the name */
		if( (utility.checkValidName(name) ) ) {
			throw new BadParametersException();
		}
		/* create the competitor */
		Competitor tempCompetitor = new Team(name);
		/* add him to the list */
		addCompetitorToList(tempCompetitor);
		return tempCompetitor;
	}

	@Override
	public void deleteCompetitor(String competition, Competitor competitor, String managerPwd)
			throws AuthenticationException, ExistingCompetitionException, CompetitionException,
			ExistingCompetitorException {
		/* first authenticate the manager */
		authenticateMngr(managerPwd);
		/* competition to remove competitor from */
		Competition myCompetition = getCompetitionByName(competition);
		/* check if it exists */
		if( myCompetition == null )
			/* does not exist */
			throw new ExistingCompetitionException();
		/* check if the competition is in a proper state */
		else if(!myCompetition.getInProgress().equals(Competition.STATE.STARTED) || myCompetition.getAllCompetitors().size() == 2) {
			throw new CompetitionException();
		}
		/* check if the competitor is in the competition */
		if(!myCompetition.isCompetitor(competitor)){
			throw new ExistingCompetitorException();
		}
		/* now we can delete the competitor */  
		myCompetition.removeCompetitor(competitor); 
		// TODO return tokens
	}

	@Override
	public void creditSubscriber(String username, long numberTokens, String managerPwd)
			throws AuthenticationException, ExistingSubscriberException, BadParametersException {
		/* first authenticate the manager */
		authenticateMngr(managerPwd);
		/* check existence */
		Subscriber tempSubscriber = getSubscriberByUserName(username);
		/* if he does not exist */
		if ( tempSubscriber == null ){
			/* does not exist */
			throw new ExistingSubscriberException("Subscriber does not exist!");
			
		}
		if ( numberTokens < 0 ) {
			/* can't credit with negative value */
			throw new BadParametersException();
		}
		/* credit the money */
		tempSubscriber.credit(numberTokens);
		
	}

	@Override
	public void debitSubscriber(String username, long numberTokens, String managerPwd)
			throws AuthenticationException, ExistingSubscriberException, SubscriberException, BadParametersException {
		/* first authenticate the manager */
		authenticateMngr(managerPwd);
		/* check existence */
		Subscriber tempSubscriber = getSubscriberByUserName(username);
		/* if he does not exist */
		if ( tempSubscriber == null ){
			/* does not exist */
			throw new ExistingSubscriberException("Subscriber does not exist!");
			
		}
		if ( numberTokens < 0 || numberTokens > tempSubscriber.getNumberOfTokens()) {
			/* can't credit with negative value */
			throw new BadParametersException();
		}
		/* credit the money */
		tempSubscriber.debit(numberTokens);		
	}

	@Override
	public void settleWinner(String competition, Competitor winner, String managerPwd)
			throws AuthenticationException, ExistingCompetitionException, CompetitionException {
		/* first authenticate the manager */
		authenticateMngr(managerPwd);
		/* competition where the competitor should be winner */
		Competition myCompetition = getCompetitionByName(competition);
		/* check if it exists */
		if( myCompetition == null )
			/* does not exist */
			throw new CompetitionException();
		/* check if the competition is in a proper state */
		else if(!myCompetition.getInProgress().equals(Competition.STATE.FINISHED)) {
			throw new CompetitionException();
		}
		/* check if the competitor is in the competition */
		if(!myCompetition.isCompetitor(winner)){
			throw new CompetitionException();
		}
		/* now we can set the winner */  
		ArrayList<Competitor> listOfWinners = new ArrayList<Competitor>();
		listOfWinners.add(winner);
		myCompetition.setWinners(listOfWinners); 
		// TODO check type of competition? in this case the type of bet should be w
		pay(myCompetition);
		myCompetition.setInProgress(Competition.STATE.SOLDOUT);
	}

	@Override
	public void settlePodium(String competition, Competitor winner, Competitor second, Competitor third,
			String managerPwd) throws AuthenticationException, ExistingCompetitionException, CompetitionException {
		/* first authenticate the manager */
		authenticateMngr(managerPwd);
		/* competition where the competitor should be winner */
		Competition myCompetition = getCompetitionByName(competition);
		/* check if it exists */
		if( myCompetition == null )
			/* does not exist */
			throw new CompetitionException();
		/* check if the competition is in a proper state */
		else if(!myCompetition.getInProgress().equals(Competition.STATE.FINISHED)) {
			throw new CompetitionException();
		}
		/* check if the competitor is in the competition */
		if(!myCompetition.isCompetitor(winner) || !myCompetition.isCompetitor(second) || !myCompetition.isCompetitor(third)){
			throw new CompetitionException();
		}
		/* now we can set the winner */  
		ArrayList<Competitor> listOfWinners = new ArrayList<Competitor>();
		listOfWinners.add(winner);
		listOfWinners.add(second);
		listOfWinners.add(third);
		myCompetition.setWinners(listOfWinners); 
		// TODO check type of competition?
		pay(myCompetition);
		myCompetition.setInProgress(Competition.STATE.SOLDOUT);	
	}

	@Override
	public void betOnWinner(long numberTokens, String competition, Competitor winner, String username, String pwdSubs)
			throws AuthenticationException, CompetitionException, ExistingCompetitionException, SubscriberException,
			BadParametersException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void betOnPodium(long numberTokens, String competition, Competitor winner, Competitor second,
			Competitor third, String username, String pwdSubs) throws AuthenticationException, CompetitionException,
					ExistingCompetitionException, SubscriberException, BadParametersException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeSubsPwd(String username, String newPwd, String currentPwd)
			throws AuthenticationException, BadParametersException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<String> infosSubscriber(String username, String pwdSubs) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteBetsCompetition(String competition, String username, String pwdSubs)
			throws AuthenticationException, CompetitionException, ExistingCompetitionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<List<String>> listCompetitions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Competitor> listCompetitors(String competition)
			throws ExistingCompetitionException, CompetitionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> consultBetsCompetition(String competition) 
			throws ExistingCompetitionException {
		/* competition where the competitor is to be added */
		Competition myCompetition = getCompetitionByName(competition);
		/* check if it exists */
		if( myCompetition == null )
			/* does not exist */
			throw new ExistingCompetitionException();
		ArrayList<String> listOfStrings = new ArrayList<String>();
		List<Bet> listOfBets = getCompetitionByName(competition).getBets();
		String string = "";
		String names = "";
		for(Bet b:listOfBets){
			if (b.getType()==1){ //type winner
				names = "the winner: " + b.getWinner().toString();
			}
			if (b.getType()==2){ //type podium
				names = "the podium: " + b.getWinner().toString() + ", " + b.getSecond().toString() + " and " + b.getThird().toString();
			}
			string = b.getUserName() + " has bet " + b.getNumberOfTokens() + " tokens on " + names + " in " + competition;
			listOfStrings.add(string);
		}
		return listOfStrings;
	}

	@Override
	public ArrayList<Competitor> consultResultsCompetition(String competition) throws ExistingCompetitionException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* distribution of tokens for a competition */
	private void pay(Competition competition){
		List<Bet> listOfBets = competition.getBets();
		/* list of subscribers that bet on winner and won */
		List<Subscriber> winningSubscribersOnWinner = new ArrayList<Subscriber>();
		/* list of tokens that the winning subscribers bet on winner */
		List<Long> tokensBetOnWinner = new ArrayList();
		/* list of subscribers that bet on podium and won */
		List<Subscriber> winningSubscribersOnPodium = new ArrayList<Subscriber>();
		/* list of tokens that the winning subscribers bet on podium */
		List<Long> tokensBetOnPodium = new ArrayList();
		long winningTokensOnWinner=0;
		long winningTokensOnPodium=0;
		long payMe;
		/* search in every bet done on the competition */
		/* this will work no matter what type of bets the competition accepts*/
		for (Bet b:listOfBets){
			if(b.getType()==1){ // type winner
				if(competition.getWinners()==b.getWinner()){
					winningSubscribersOnWinner.add(getSubscriberByUserName(b.getUserName()));
					tokensBetOnWinner.add(b.getNumberOfTokens());
					winningTokensOnWinner += b.getNumberOfTokens();
				}				
			}
			else if(b.getType()==2){
				if(competition.getWinners().get(0)==b.getWinner() && 
						competition.getWinners().get(1)==b.getSecond() && 
						competition.getWinners().get(2)==b.getThird()){
					winningSubscribersOnPodium.add(getSubscriberByUserName(b.getUserName()));
					tokensBetOnPodium.add(b.getNumberOfTokens());
					winningTokensOnPodium += b.getNumberOfTokens();
				}
			}
		}
		for (int i=0; i<winningSubscribersOnWinner.size();i++){
			payMe = competition.getTotalNumberOfTokens(1)*tokensBetOnWinner.get(i)/winningTokensOnWinner;  
			winningSubscribersOnWinner.get(i).credit(payMe);
		}
		
		for (int i=0; i<winningSubscribersOnPodium.size();i++){
			payMe = competition.getTotalNumberOfTokens(1)*tokensBetOnPodium.get(i)/winningTokensOnPodium;  
			winningSubscribersOnPodium.get(i).credit(payMe);
		}
	}
}
