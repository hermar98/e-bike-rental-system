package main.java.com.team15.ebrs.util;

import org.apache.commons.codec.binary.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;

/**
 * Class used to hash password with salt before storing them in the database.
 * @author Team 15
 */
public class Password {
	
	private static final Random RANDOM = new SecureRandom();
	private static final int ITERATIONS = 10000;
	private static final int KEY_LENGTH = 256;
	private static final int SALT_SIZE = 32;

	/**
	 * Generates a salt.
	 * @return Byte[] containting the generated salt.
	 */
	public static byte[] genNextSalt() {
		byte[] salt = new byte[SALT_SIZE];
		RANDOM.nextBytes(salt);
		return salt;
	}

	/**
	 * Hashes the provided password using the salt.
	 * @param password Character[] with the password being hashed.
	 * @param salt Byte[] containing the salt.
	 * @return Byte[] containing the hashed password.
	 */
	public static byte[] hash(char[] password, byte[] salt) {
	    PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
	    Arrays.fill(password, Character.MIN_VALUE);
	    try {
	    	SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	    	return skf.generateSecret(spec).getEncoded();
	    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
	    	throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
	    } finally {
	    	spec.clearPassword();
	    }
	}

	/**
	 * Checks a password and a salt according to a expected byte[] hash.
	 * @param password Char[] with the password.
	 * @param salt Salt used to hash the provided password.
	 * @param expectedHash Expected hash from the password and salt.
	 * @return Boolean stating whether the two hashes are equal.
	 */
	public static boolean isExpectedPassword(char[] password, byte[] salt, byte[] expectedHash) {
	    byte[] pwdHash = hash(password, salt);
	    Arrays.fill(password, Character.MIN_VALUE);
	    if (pwdHash.length != expectedHash.length) return false;
	    for (int i = 0; i < pwdHash.length; i++) {
	    	if (pwdHash[i] != expectedHash[i]) return false;
	    }
	    return true;
	}

	/**
	 * Generates a random string with characters, uppercase and lowercase.
	 * @param length Int desired length of the string.
	 * @return String with random characters.
	 */
	public static String generateRandomPassword(int length) {
	    StringBuilder sb = new StringBuilder(length);
	    for (int i = 0; i < length; i++) {
	    	int c = RANDOM.nextInt(62);
	    	if (c <= 9) {
	    		sb.append(String.valueOf(c));
	    	} else if (c < 36) {
	    		sb.append((char) ('a' + c - 10));
	    	} else {
	    		sb.append((char) ('A' + c - 36));
	    	}
	    }
	    return sb.toString();
	}
}
