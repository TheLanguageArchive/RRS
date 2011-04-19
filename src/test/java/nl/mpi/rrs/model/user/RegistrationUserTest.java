package nl.mpi.rrs.model.user;

import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
    public void validateUser(){
        RegistrationUser user = new RegistrationUser();
        user.setUserName("john.doe");
        assertFalse(user.validate());
        user.setFirstName("John");
        assertFalse(user.validate());
        user.setLastName("Doe");
        assertFalse(user.validate());
        user.setOrganization("Acme Co.");
        assertFalse(user.validate());
        user.setEmail("john.doe@acme.com");
        assertTrue(user.validate());
    }

    private String getRandomString(final int length) {
        byte[] randomBytes = new byte[length];
        random.nextBytes(randomBytes);
        return new String(randomBytes);
    }
}
