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
import dev4a.db.CompetitionsManager;
import dev4a.db.CompetitorsManager;
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
	private Map<Integer, Competitor> allCompetitors = new HashMap<>();
	/* all the competitions in the system */
	private Map<String,Competition> allCompetitions = new HashMap<>();
	/* all the subscribers in the System */
	private Map<String,Subscriber> allSubscribers = new HashMap<>();


	/* constructor */
	public System(String mgrPassword) {
		/* setting the pass */
		this.mgrPassword = mgrPassword; 
	}

	private String getMgrPassword() {
		return mgrPassword;
	}

	public void setMgrPassword(String mgrPassword) {
		// TODO add some logic for the setting of pswd -> maybe check OLD pass -> New PASS
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
		/* we created him, now add him to the collection */
		addSubscriberToList(temporarySubscriber, password);		
		/* return the new password */
		return password;
	}

	private void addSubscriberToList(Subscriber temporarySubscriber, String password) {
		/* 
		 *  hide the implementation
		 *  add the subscirber to any collection 
		 */
		try {
			SubscribersManager.persist(temporarySubscriber, password);
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
		this.allSubscribers.put(temporarySubscriber.getUserName(), temporarySubscriber);
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
		removeSubscriberFromList(toBeRemoved);
		/* return the number of tokens he had left */
		return toBeRemoved.getNumberOfTokens();
	}

	private void removeSubscriberFromList(Subscriber toRemove) {
		/* 
		 *  hide the implementation
		 *  remove the subscirber from any collection 
		 */
		try {
			SubscribersManager.delete(toRemove);
		} catch (SQLException sqlex ) {
			sqlex.printStackTrace();
		}
		java.lang.System.out.println("removing " + toRemove.getUserName());
		this.allSubscribers.remove(toRemove.getUserName());
	}

	@Override
	public List<List<String>> listSubscribers(String managerPwd) throws AuthenticationException {
		/* first authenticate the manager */
		authenticateMngr(managerPwd);
		/* iterate the container and get all names and attributes */
		List<List<String>> printableSubs = new ArrayList<>();
		/* get the updated list */
		try {
			this.allSubscribers = SubscribersManager.findAll();
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
		for( Subscriber sub : this.allSubscribers.values() ) {
			/* store the details for each subscriber */
			List<String> subDetails = new ArrayList<>();
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
		if (closingDate.before(Calendar.getInstance()) || competitors.size() < 2) {
			throw new CompetitionException();
		}
		/* create the Map */
		Map<Integer, Competitor> tempCompetitors = new HashMap<>();

		/* create it ! */
		tempCompetition = new Competition(
				competition,
				Calendar.getInstance(),
				closingDate,
				"sport",
				tempCompetitors,
				"pw"
				);
		/* freshly created add it to our collection */
		addCompetitionToList(tempCompetition);		
		//TODO check BadParametresException
	}

	private void addCompetitionToList(Competition tempCompetition) {
		// hide the implementation
		try {
			CompetitionsManager.persist(tempCompetition);
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
		this.allCompetitions.put(tempCompetition.getName(), tempCompetition);
	}


	private void removeCompetitionFromList(Competition competition) {
		// hide the implementation
		try {
			CompetitionsManager.delete(competition);
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
		this.allCompetitions.remove(competition.getName());
	}

	private Competition getCompetitionByName(String competition) {
		// hide the implementation
		Competition temp = null;
		try {
			temp = CompetitionsManager.findByName(competition);
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
		return temp;
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
		for (int i = 0; i < listBets.size(); i++){
			Subscriber subscriber = getSubscriberByUserName(listBets.get(i).getUserName());
			/* returns tokens */
			subscriber.credit(listBets.get(i).getNumberOfTokens());
			/* deletes the bets from subscriber */
			subscriber.getBets().remove(listBets.get(i));
		}
		/* deletes the bets from Competition */
		toBeCanceled.getBets().removeAll(listBets);
		/* cancels it */
		toBeCanceled.setStatus(Competition.CANCELED);
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
		removeCompetitionFromList(toBeRemoved);
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
			throw new CompetitionException();
		/* check if the competition is in a proper state */
		else if( !(myCompetition.getStatus() == Competition.STARTED) ) {
			throw new CompetitionException();
		}
		/* check if the competitor is already in the competition */
		if(myCompetition.hasCompetitor(competitor)){
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
		try {
			CompetitorsManager.persist(competitor);
		} catch( SQLException sqlex ) {
			sqlex.printStackTrace();
		}
		this.allCompetitors.put(new Integer(competitor.getId()), competitor);

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
		else if( (myCompetition.getStatus() != Competition.STARTED) || myCompetition.getAllCompetitors().size() == 2) {
			throw new CompetitionException();
		}
		/* check if the competitor is in the competition */
		if(!myCompetition.hasCompetitor(competitor)){
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
		try {
			SubscribersManager.update(tempSubscriber);
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
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
		try {
			SubscribersManager.update(tempSubscriber);
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
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
		else if( myCompetition.getStatus() != Competition.FINISHED)  {
			throw new CompetitionException();
		}
		/* check if the competitor is in the competition */
		if(!myCompetition.hasCompetitor(winner)){
			throw new CompetitionException();
		}
		/* now we can set the winner */  
		Map<Integer, Competitor> listOfWinners = new HashMap<>();
		listOfWinners.put(new Integer(winner.getId()), winner);
		myCompetition.setWinners(listOfWinners); 
		// TODO check type of competition?		
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
		else if(myCompetition.getStatus() != (Competition.FINISHED)) {
			throw new CompetitionException();
		}
		/* check if the competitor is in the competition */
		if(!myCompetition.hasCompetitor(winner) || !myCompetition.hasCompetitor(second) || !myCompetition.hasCompetitor(third)){
			throw new CompetitionException();
		}
		/* now we can set the winner */  
		Map<Integer, Competitor> listOfWinners = new HashMap<>();
		listOfWinners.put(new Integer(winner.getId()), winner);
		listOfWinners.put(new Integer(second.getId()), winner);
		listOfWinners.put(new Integer(third.getId()), winner);

		myCompetition.setWinners(listOfWinners); 
		// TODO check type of competition?

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
	public ArrayList<String> consultBetsCompetition(String competition) throws ExistingCompetitionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Competitor> consultResultsCompetition(String competition) throws ExistingCompetitionException {
		// TODO Auto-generated method stub
		return null;
	}

}
