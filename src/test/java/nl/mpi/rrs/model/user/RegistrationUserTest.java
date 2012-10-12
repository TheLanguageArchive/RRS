package nl.mpi.rrs.model.user;

import java.util.Random;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class RegistrationUserTest {
    
    private static Random random = new Random();

    /**
     * Creates 50 random passwords, encodes them, decodes the result, and checks
     * for equality to original password
     */
    @Test
    public void encodeDecodePassword() {
	for (int i = 0; i < 50; i++) {
	    String password = getRandomString(1 + random.nextInt(255));
	    String encoded = RegistrationUser.encodePassword(password);
	    String decoded = RegistrationUser.decodePassword(encoded);
	    
	    assertEquals(password, decoded);
	}
    }
    
    @Test
    public void validateUser() {
	// Should fail with any of the value null or empty
	RegistrationUser user = getRegistrationUser();
	user.setUserName(null);
	assertFalse(user.validate());
	user.setUserName("");
	assertFalse(user.validate());
	
	user = getRegistrationUser();
	user.setFirstName(null);
	assertFalse(user.validate());
	user.setFirstName("");
	assertFalse(user.validate());
	
	user = getRegistrationUser();
	user.setLastName(null);
	assertFalse(user.validate());
	user.setLastName("");
	assertFalse(user.validate());
	
	user = getRegistrationUser();
	user.setOrganization(null);
	assertFalse(user.validate());
	user.setOrganization("");
	assertFalse(user.validate());
	
	user = getRegistrationUser();
	user.setEmail(null);
	assertFalse(user.validate());
	user.setEmail("");
	assertFalse(user.validate());
    }
    
    @Test
    public void validateUserName() {
	RegistrationUser user = getRegistrationUser();
	
	// too short
	user.setUserName("ab");
	assertFalse(user.isCustomUsernameValid());
	
	// too long
	user.setUserName("a234567890b234567890c234567890d");
	assertFalse(user.isCustomUsernameValid());
	
	// illegal first character
	user.setUserName("9abcd");
	assertFalse(user.isCustomUsernameValid());

	// illegal character
	user.setUserName("a%cd");
	assertFalse(user.isCustomUsernameValid());
    }

    /**
     *
     * @return registration user with (asserted) valid properties
     */
    private RegistrationUser getRegistrationUser() {
	RegistrationUser user = new RegistrationUser();
	user.setUserName("john.doe");
	user.setFirstName("John");
	user.setLastName("Doe");
	user.setOrganization("Acme Co.");
	user.setEmail("john.doe@acme.com");
	assertTrue(user.validate());
	return user;
    }
    
    private String getRandomString(final int length) {
	byte[] randomBytes = new byte[length];
	random.nextBytes(randomBytes);
	return new String(randomBytes);
    }
}
