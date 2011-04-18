package nl.mpi.rrs.authentication;

import javax.servlet.http.HttpServletRequest;
import nl.mpi.rrs.model.user.RegistrationUser;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class DummyAuthenticationProvider implements AuthenticationProvider {

    private boolean loggedIn;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String organization;

    public RegistrationUser createRegistrationUser(HttpServletRequest request) {
        RegistrationUser ru = new RegistrationUser();
        ru.setUserName(userName);
        ru.setFirstName(firstName);
        ru.setLastName(lastName);
        ru.setOrganization(organization);
        ru.setEmail(email);
        
        return ru;
    }

    public String getLoggedInUser(HttpServletRequest request) {
        return loggedIn?userName:null;
    }

    public boolean isUserLoggedIn(HttpServletRequest request) {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdentityProviderId(HttpServletRequest request) {
        return "RRS DUMMY PROVIDER!!!";
    }

}
