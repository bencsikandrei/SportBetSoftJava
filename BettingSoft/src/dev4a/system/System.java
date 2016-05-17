package dev4a.system;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.rmi.CORBA.Util;

import java.util.List;

import dev4a.bets.*;
import dev4a.competition.*;
import dev4a.competitor.*;
import dev4a.exceptions.AuthenticationException;
import dev4a.exceptions.BadParametersException;
import dev4a.subscriber.*;
import dev4a.utils.Utils;
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
	private List<Competition> allCompetitions = new ArrayList();
	/* all the subscribers in the System */
	private Map<String,Subscriber> allSubscribers = new HashMap();
	/* all the bets in the system */
	private List<Bet> allBets = new ArrayList();
	
	
	/* constructor */
	public System(String mgrPassword) {
		/* setting the pass */
		this.mgrPassword = mgrPassword; 
	}
	
	public String getMgrPassword() {
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
	
	private String generateRandomPassword(int len) {
		/* use the utility method */
		return utility.randomString(len);		
	}
	
	@Override
	public String subscribe(String lastName, String firstName, String username, String borndate, String managerPwd)
			throws AuthenticationException, ExistingSubscriberException, SubscriberException, BadParametersException {
		// TODO Auto-generated method stub
		/* first authenticate the manager */
		authenticateMngr(managerPwd);
		/* then check for the username (which is unique) */
		if ( getSubscriberByUserName(username) != null )
			throw new ExistingSubscriberException();
		/* if he does not exist, we can create him */
		Subscriber temporarySubscriber = new Subscriber(lastName, firstName, username, borndate);
		/* generate a password */
		temporarySubscriber.setPassword( this.generateRandomPassword(8) );		
		/* we created him, now add him to the collection */
		addSubscriberToList(temporarySubscriber);		
		/* return the new password */
		return temporarySubscriber.getPassword();
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
	
	private Subscriber getSubscriberByUserName(String username) {
		// TODO make a method to get the sub from the List
		return this.allSubscribers.get(username);
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
		
		return toBeRemoved.getNumberOfTokens();
	}

	

	@Override
	public List<List<String>> listSubscribers(String managerPwd) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addCompetition(String competition, Calendar closingDate, Collection<Competitor> competitors,
			String managerPwd) throws AuthenticationException, ExistingCompetitionException, CompetitionException,
					BadParametersException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelCompetition(String competition, String managerPwd)
			throws AuthenticationException, ExistingCompetitionException, CompetitionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteCompetition(String competition, String managerPwd)
			throws AuthenticationException, ExistingCompetitionException, CompetitionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCompetitor(String competition, Competitor competitor, String managerPwd)
			throws AuthenticationException, ExistingCompetitionException, CompetitionException,
			ExistingCompetitorException, BadParametersException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Competitor createCompetitor(String lastName, String firstName, String borndate, String managerPwd)
			throws AuthenticationException, BadParametersException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Competitor createCompetitor(String name, String managerPwd)
			throws AuthenticationException, BadParametersException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteCompetitor(String competition, Competitor competitor, String managerPwd)
			throws AuthenticationException, ExistingCompetitionException, CompetitionException,
			ExistingCompetitorException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void creditSubscriber(String username, long numberTokens, String managerPwd)
			throws AuthenticationException, ExistingSubscriberException, BadParametersException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void debitSubscriber(String username, long numberTokens, String managerPwd)
			throws AuthenticationException, ExistingSubscriberException, SubscriberException, BadParametersException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void settleWinner(String competition, Competitor winner, String managerPwd)
			throws AuthenticationException, ExistingCompetitionException, CompetitionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void settlePodium(String competition, Competitor winner, Competitor second, Competitor third,
			String managerPwd) throws AuthenticationException, ExistingCompetitionException, CompetitionException {
		// TODO Auto-generated method stub
		
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
