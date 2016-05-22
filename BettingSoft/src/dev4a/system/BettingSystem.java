package dev4a.system;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
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
import dev4a.db.BetsManager;
import dev4a.db.CompetitionsManager;
import dev4a.db.CompetitorsManager;
import dev4a.db.ParticipantsManager;
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
public class BettingSystem implements Betting {
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

	public String getPassword(){
		return this.mgrPassword;
	}
	/* constructor */
	public BettingSystem(String mgrPassword) {
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
		/* get the password from the DB and then compare it 
		 * TODO
		 */
		if ( this.mgrPassword != managerPwd ) {
			throw new AuthenticationException();
		}
	}

	public Subscriber authenticateSub(String username, String subPwd) throws AuthenticationException , SubscriberException {
		/* get him and verify pass */
		Subscriber tempSub = getSubscriberByUserName(username);

		if(tempSub == null)
			throw new SubscriberException();

		boolean correct = tempSub.checkPassword(subPwd);

		if( correct == false ) 
			throw new AuthenticationException();

		return tempSub;
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
		/* remove all his bets */
		try {

			for ( Bet bet : BetsManager.findBySubscriber(toBeRemoved).values()) {
				System.out.println("deleting his bets.." + bet);
				BetsManager.delete(bet);
			}

		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
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
			/* adding the details about them */
			subDetails.add(sub.getUserName());
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
		/* valid name? */
		if ( ! (utility.checkValidCompetition(competition) ) || competitors == null ) 
			// we do not validate competitor because we already did it when we created it.
			throw new BadParametersException();
		/* now check if it exists */
		Competition tempCompetition = getCompetitionByName(competition);
		/* the test */
		if( tempCompetition != null )
			throw new ExistingCompetitionException();
		/* check if the date is correct, if the number of competitors is >2 and if there is no repeated competitors */
		Set<Competitor> set = new HashSet<Competitor>(competitors);
		if ( competitors.size() < 2
				|| set.size()!=competitors.size() 
				|| closingDate.before(Calendar.getInstance()) ) {
			throw new CompetitionException();
		}
		/* create the Map */
		Map<Integer, Competitor> tempCompetitors = new HashMap<>();

		/* create it ! */
		tempCompetition = new Competition(
				competition,
				Calendar.getInstance(),
				closingDate,
				"other",
				tempCompetitors,
				"pw"
				);
		/* freshly created add it to our collection */
		addCompetitionToList(tempCompetition);		
		//TODO check BadParametresException
	}
	/**
	 * 
	 * @param tempCompetition
	 */
	private void addCompetitionToList(Competition tempCompetition) {
		// hide the implementation
		try {
			CompetitionsManager.persist(tempCompetition);
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
		this.allCompetitions.put(tempCompetition.getName(), tempCompetition);
	}


	/**
	 * 
	 * @param competition
	 */
	private void removeCompetitionFromList(Competition competition) {
		// hide the implementation
		try {
			CompetitionsManager.delete(competition);
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
		this.allCompetitions.remove(competition.getName());
	}
	/**
	 * 
	 * @param competition
	 * @return
	 */
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
			throw new ExistingCompetitionException();
		/* check if the competition is in a proper state */
		if( myCompetition.getClosingDate().before(Calendar.getInstance()) ){
		//if( !(myCompetition.getStatus() == Competition.STARTED) ) {
			throw new CompetitionException();
		}
		/* check if the competitor is already in the competition */
		if(myCompetition.hasCompetitor(competitor)){
			throw new ExistingCompetitorException();
		}
		/* now we can add the competitor */  
		myCompetition.addCompetitor(competitor); 
		addCompetitorToList(competitor);
		try {
			ParticipantsManager.persist(competitor, myCompetition);
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
		// TODO BadParametersExcaption
	}


	@Override
	public Competitor createCompetitor(String lastName, String firstName, String borndate, String managerPwd)
			throws AuthenticationException, BadParametersException {
		/* first authenticate the manager */
		authenticateMngr(managerPwd);
		/* check validity of the name */
		if( !(utility.checkValidFirstLastName(lastName) && utility.checkValidFirstLastName(firstName) && utility.checkValidDate(borndate) ) ) {
			throw new BadParametersException();
		}
		/* Checks if the competitor already exists */
		List<Competitor> listAllCompetitors = new ArrayList<Competitor>(allCompetitors.values());
		for(Competitor c: listAllCompetitors){
			if(c.toString() == firstName + " " + lastName + " born " + borndate)
				// It exists! Returned!
				return c;
		}
		/* It does not exist so the competitor is created  */
		Competitor tempCompetitor = new IndividualCompetitor(firstName,lastName,borndate);
		/* add him to the list */
		this.allCompetitors.put(new Integer(tempCompetitor.getId()), tempCompetitor);
		// WE DON'T HAVE TO DO IT HERE!
		//addCompetitorToList(tempCompetitor);
		/* return the object we created in memory */
		return tempCompetitor;
	}
	/**
	 * 
	 */
	@Override
	public Competitor createCompetitor(String name, String managerPwd)
			throws AuthenticationException, BadParametersException {
		/* first authenticate the manager */
		authenticateMngr(managerPwd);
		/* check validity of the name */
		if( !(utility.checkValidTeamName(name)) ) {
			throw new BadParametersException();
		}
		/* Checks if the competitor already exists */
		List<Competitor> listAllCompetitors = new ArrayList<Competitor>(allCompetitors.values());
		for(Competitor c: listAllCompetitors){
			if(c.toString() == name)
				// It exists! Returned!
				return c;
		}
		/* It does not exist so the competitor is created  */
		Competitor tempCompetitor = new Team(name);
		/* add him to the list */
		this.allCompetitors.put(new Integer(tempCompetitor.getId()), tempCompetitor);
		// WE DON'T HAVE TO DO IT HERE!
		//addCompetitorToList(tempCompetitor);
		return tempCompetitor;
	}	
	/**
	 * 
	 * @param competitor
	 */
	private void addCompetitorToList(Competitor competitor) {
		/* hide implementation */
		try {
			CompetitorsManager.persist(competitor);
		} catch( SQLException sqlex ) {
			sqlex.printStackTrace();
		}
	}

	@Override
	public void deleteCompetitor(String competition, Competitor competitor, String managerPwd)
			throws AuthenticationException, ExistingCompetitionException, CompetitionException,
			ExistingCompetitorException {
		/* first authenticate the manager */
		authenticateMngr(managerPwd);
		/* competition to remove competitor from */
		Competition myCompetition = getCompetitionByName(competition);
		/* checks if it exists */
		if( myCompetition == null )
			/* does not exist */
			throw new ExistingCompetitionException();
		/* checks if the competition is in a proper state */
		if( myCompetition.getClosingDate().before(Calendar.getInstance()) || myCompetition.getAllCompetitors().size() == 2) {
		// if((myCompetition.getStatus() != Competition.STARTED))
			throw new CompetitionException();
		}
		/* checks if the competitor is in the competition */
		if(!myCompetition.hasCompetitor(competitor)){
			throw new ExistingCompetitorException();
		}
		/* Returns tokens */
		List<Bet> listBets = myCompetition.getBets();
		for (int i = 0; i < listBets.size(); i++){
			/* Looks if the bet was done to the competitor to remove */
			if(listBets.get(i).getListCompetitor().contains(competitor.getId())){
				Subscriber subscriber = getSubscriberByUserName(listBets.get(i).getUserName());
				/* returns tokens */
				subscriber.credit(listBets.get(i).getNumberOfTokens());
				/* deletes the bet from subscriber */
				subscriber.getBets().remove(listBets.get(i));
				/* deletes the bet from the competition */
				myCompetition.getBets().remove(listBets.get(i));
			}
		}
		/* now we can delete the competitor */  
		myCompetition.removeCompetitor(competitor); 
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
			/* update his account in the DB */
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
			/* update his account in the DB */
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
			throw new ExistingCompetitionException();
		/* checks if the competition is in a proper state */
		if(myCompetition.getClosingDate().after(Calendar.getInstance())){
		//if( myCompetition.getStatus() != Competition.FINISHED)  {
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
		// TODO check type of competition? in this case the type of bet should be w
		pay(myCompetition);
		myCompetition.setStatus(Competition.SOLDOUT);
		/* Deletes the competition */
		deleteCompetition(competition, managerPwd);
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
			throw new ExistingCompetitionException();
		/* check if the competition is in a proper state */
		if(myCompetition.getClosingDate().after(Calendar.getInstance())){
		//if(myCompetition.getStatus() != (Competition.FINISHED)) {
			throw new CompetitionException();
		}
		/* check if the competitor is in the competition */
		if(!myCompetition.hasCompetitor(winner) || !myCompetition.hasCompetitor(second) || !myCompetition.hasCompetitor(third)){
			throw new CompetitionException();
		}
		if (winner==second || second==third || winner==third)
			throw new CompetitionException();
		/* now we can set the winner */  
		Map<Integer, Competitor> listOfWinners = new HashMap<>();
		listOfWinners.put(new Integer(winner.getId()), winner);
		listOfWinners.put(new Integer(second.getId()), winner);
		listOfWinners.put(new Integer(third.getId()), winner);

		myCompetition.setWinners(listOfWinners); 
		// TODO check type of competition?
		pay(myCompetition);
		myCompetition.setStatus(Competition.SOLDOUT);
		/* Deletes the competition */
		deleteCompetition(competition, managerPwd);
	}

	@Override
	public void betOnWinner(long numberTokens, String competition, Competitor winner, String username, String pwdSubs)
			throws AuthenticationException, CompetitionException, ExistingCompetitionException, SubscriberException,
			BadParametersException {
		/* first authenticate the subscriber */
		Subscriber tempSubscriber = authenticateSub(username, pwdSubs);
		/* get the competition */
		Competition tempComp = getCompetitionByName(competition);
		/* if the competition does not exist */
		if( tempComp == null ) {
			throw new ExistingCompetitionException();
		}
		/* if we have given wrong values */
		if( (numberTokens < 0))// || (tempComp.hasCompetitor(winner) == false) )
			throw new BadParametersException();
		/* the time of the bet */
		java.util.Date currentTime = new java.util.Date();

		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		String betDate = formatter.format(currentTime);
	
		/* place the bet */
		
		Bet tempBet = new Bet(numberTokens, competition, winner, username, betDate);
		/* add it to the player */
		tempSubscriber.placeBet( tempBet );
		/* */
		this.addBetToList(tempBet);
	}

	private void addBetToList(Bet bet) {
		try {
			BetsManager.persist(bet);
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}

	}

	@Override
	public void betOnPodium(long numberTokens, String competition, Competitor winner, Competitor second,
			Competitor third, String username, String pwdSubs) throws AuthenticationException, CompetitionException,
			ExistingCompetitionException, SubscriberException, BadParametersException {
		/* first authenticate the subscriber */
		Subscriber tempSubscriber = authenticateSub(username, pwdSubs);
		/* get the competition */
		Competition tempComp = getCompetitionByName(competition);
		/* if the competition does not exist */
		if( tempComp == null ) {
			throw new ExistingCompetitionException();
		}
		/* if we have given wrong values */
		if( (numberTokens < 0) || (tempComp.hasCompetitor(winner) == false) || (tempComp.hasCompetitor(second) == false) || (tempComp.hasCompetitor(third) == false))
			throw new BadParametersException();
		/* the time of the bet */
		String currentTime = new java.util.Date().toString();
		/* place the bet */
		Bet tempBet = new Bet(numberTokens, competition, winner, second, third, username, currentTime);
		/* add it to the player */
		tempSubscriber.placeBet( tempBet );
		/* add it to our lists  */
		this.addBetToList(tempBet);

	}

	@Override
	public void changeSubsPwd(String username, String newPwd, String currentPwd)
			throws AuthenticationException, BadParametersException {
		/* first authenticate the subscriber */
		Subscriber tempSubscriber = null;
		try {
			tempSubscriber = authenticateSub(username, currentPwd);
		} catch(SubscriberException subex){
			subex.printStackTrace();
		}

		tempSubscriber.changePassword(currentPwd, newPwd);

		try {
			SubscribersManager.update(tempSubscriber);
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
	}

	@Override
	public ArrayList<String> infosSubscriber(String username, String pwdSubs) throws AuthenticationException {
		/* first authenticate the subscriber */
		Subscriber tempSubscriber = null;
		try {
			tempSubscriber = authenticateSub(username, pwdSubs);
		} catch(SubscriberException subex){
			subex.printStackTrace();
		}
		/* then get the info */
		ArrayList<String> tempList = new ArrayList<>();

		tempList.add(tempSubscriber.getUserName());
		tempList.add(tempSubscriber.getLastName());
		tempList.add(tempSubscriber.getFirstName());
		tempList.add(tempSubscriber.getBornDate());

		return tempList;
	}

	@Override
	public void deleteBetsCompetition(String competition, String username, String pwdSubs)
			throws AuthenticationException, CompetitionException, ExistingCompetitionException {
		/* first authenticate the subscriber */
		Subscriber tempSubscriber = null;
		try {
			tempSubscriber = authenticateSub(username, pwdSubs);
		} catch(SubscriberException subex){
			subex.printStackTrace();
		}
		/* get the bets */
		for ( Bet b : tempSubscriber.getBets().values() ) {
			/* if it's the right competition */
			if( b.getCompetition() == competition )
				try {
					BetsManager.delete(b);
				} catch (SQLException sqlex) {
					sqlex.printStackTrace();
				}
		}

	}

	public void printCompetitions() {
		this.utility.printList(this.listCompetitions());
	}
	@Override
	public List<List<String>> listCompetitions() {
		/* iterate the container and get all names and attributes */
		List<List<String>> printableCompetitions = new ArrayList<>();
		/* get the updated list */
		try {
			this.allCompetitions = CompetitionsManager.findAll();
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
		for( Competition comp : this.allCompetitions.values() ) {
			/* store the details for each subscriber */
			List<String> compDetails = new ArrayList<>();
			/* get the competition b*/
			compDetails.add(comp.getName());
			compDetails.add(comp.getSport());
			compDetails.add(String.valueOf(comp.getStatus()));
			compDetails.add( String.valueOf( comp.getStartDate().getTime() ) );
			/* insert it in the big list */
			printableCompetitions.add(compDetails);
		}
		return printableCompetitions;
	}

	public void printCompetitors(String competition) {
		try {
			this.utility.printList(this.listCompetitors(competition));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public Collection<Competitor> listCompetitors(String competition)
			throws ExistingCompetitionException, CompetitionException {
		/* get the compeition with that name */
		Competition tempCompetition = getCompetitionByName(competition);
		/* check if competition exists */
		if ( tempCompetition == null ) {
			throw new ExistingCompetitionException();
		}
		Collection<Competitor> competitors = new ArrayList<>();
		try {
			competitors = ParticipantsManager.findAllByCompetition(competition).values();
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
		return competitors;
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
		/* */
		Competition tempCompetition = getCompetitionByName(competition);

		if (tempCompetition == null )
			throw new ExistingCompetitionException();

		return new ArrayList<Competitor> (tempCompetition.getWinners().values());
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
					b.setState(b.WON);
				}
				else
					b.setState(b.LOST);
			}
			else if(b.getType()==2){
				if(competition.getWinners().get(0)==b.getWinner() && 
						competition.getWinners().get(1)==b.getSecond() && 
						competition.getWinners().get(2)==b.getThird()){
					winningSubscribersOnPodium.add(getSubscriberByUserName(b.getUserName()));
					tokensBetOnPodium.add(b.getNumberOfTokens());
					winningTokensOnPodium += b.getNumberOfTokens();
					b.setState(b.WON);
				}
				else
					b.setState(b.LOST);
			}
		}
		/* pays to the winners */
		if(winningSubscribersOnWinner.size()!=0){
			for (int i=0; i<winningSubscribersOnWinner.size();i++){
				payMe = competition.getTotalNumberOfTokens(1)*tokensBetOnWinner.get(i)/winningTokensOnWinner;  
				winningSubscribersOnWinner.get(i).credit(payMe);
			}
		}
		/* Since it is a nonprofit organization... money is returned if nobody won */
		else{
			for (Bet b:listOfBets){
				getSubscriberByUserName(b.getUserName()).credit(b.getNumberOfTokens());
			}
		}
		/* pays to the winners */
		if(winningSubscribersOnPodium.size()!=0){
			for (int i=0; i<winningSubscribersOnPodium.size();i++){
				payMe = competition.getTotalNumberOfTokens(1)*tokensBetOnPodium.get(i)/winningTokensOnPodium;  
				winningSubscribersOnPodium.get(i).credit(payMe);
			}
		}
		/* Since it is a nonprofit organization... money is returned if nobody won */
		else{
			for (Bet b:listOfBets){
				getSubscriberByUserName(b.getUserName()).credit(b.getNumberOfTokens());
			}
		}
	}
	
	public Competitor getCompetitorById(Integer id) {
		Competitor tempComp = null;
		try {
			tempComp = CompetitorsManager.findById(id);
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
		return tempComp;
	}
}
