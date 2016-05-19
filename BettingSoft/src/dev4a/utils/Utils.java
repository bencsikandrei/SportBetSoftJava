package dev4a.utils;

import java.security.SecureRandom;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class Utils {
	/* create a string with all alphanumerical chars and some symbols */
	static final String ALPHANUM = "!@?0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	/* use SecureRandom for extra safety */
	static SecureRandom rnd = new SecureRandom();
	/**
	 * This method allows us to create a password
	 * made out of 'len' characters
	 * 
	 * The pass can contain all the chars in the
	 * ALPHANUM string.
	 * 
	 * @param len
	 * @return generatePassword = sb.toString()
	 */
	public String randomString( int len ) {
		/* use string builder for more optimization */
		StringBuilder sb = new StringBuilder( len );
		/* loop through and get a random char */
		for( int i = 0; i < len; i++ ) 
			sb.append( ALPHANUM.charAt( rnd.nextInt(ALPHANUM.length()) ) );
		/* return the final string */
		return sb.toString();
	}
	/**
	 * This method takes care of printing a complex list
	 * @param printable
	 */
	public void printList(List<List<String>> printable) {
		/* count */
		int count = 0;
		
		for (List<String> pList : printable ) {
			System.out.print("Subscriber " + count + " : ");
			for ( String pString : pList ) {
				System.out.print(pString + "\t| ");
			}
			++count;
			System.out.println();
		}
	}
	/**
	 * Parses the regular expression
	 * Returns true if match is found
	 * @param pattern
	 * @param string
	 * @return true for match / false for no match
	 */
	private boolean regexpParser(String pattern, String string) {
		/* create the regexp objects */
		Pattern pat = Pattern.compile(pattern);
		/* also the matcher for the line */
		Matcher mat = pat.matcher(string);
		/* check validity */
		if ( !mat.matches())
			return false;
		/* it matches */
		return true;
	}
	/**
	 * 
	 * @param name
	 * @return
	 */
	public boolean checkValidName(String name) {
		/* the pattern to be respected by the string */
		String pattern = "^[A-Za-z][A-Za-z-]*";
		/* */
		return regexpParser(pattern, name);
	}
	/**
	 * 
	 * @param uname
	 * @return
	 */
	public boolean checkValidUserName(String uname) {
		/* the pattern to be respected by the string */
		String pattern = "^[A-Za-z][A-Za-z-]*";
		/* */
		return regexpParser(pattern, uname);
	}
	/**
	 * 
	 * @param date
	 * @return
	 */
	public boolean checkValidDate(String date) {
		/* the pattern to be respected by the string */
		String pattern = "^((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$";
		/* */
		return regexpParser(pattern, date);
	}
	/**
	 * 
	 * @param competition
	 * @return
	 */
	public boolean checkValidCompetition(String competition) {
		/* the pattern to be respected by the string */
		String pattern = "^[A-Za-z0-9_-]{1,50}$";
		/* */
		return regexpParser(pattern, competition);
	}
	/**
	 * 
	 * @param competition
	 * @return
	 */
	public boolean checkValidCalendar(Calendar date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-DD");  
		String stringDate = formatter.format(date); 
		/* the pattern to be respected by the string */
		String pattern = "^((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$";
		/* */
		return regexpParser(pattern, stringDate);
	}
	
}
