package dev4a.utils;

import java.security.SecureRandom;

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
}
