package nl.mpi.rrs.authentication;

import de.mpg.aai.shhaa.context.AuthenticationContext;
import de.mpg.aai.shhaa.context.AuthenticationContextHolder;
import de.mpg.aai.shhaa.model.AuthAttribute;
import de.mpg.aai.shhaa.model.AuthAttributes;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import nl.mpi.rrs.model.user.RegistrationUser;

/**
 * Shibboleth implementation of AuthenticationProvider
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class ShibbolethAuthenticationProvider implements AuthenticationProvider {

    public final static String SHIB_ANONYMOUS_USER = "anonymous";

    public boolean isUserLoggedIn(HttpServletRequest request) {
	String user = request.getRemoteUser();
	return user != null
		&& !user.isEmpty()
		&& !SHIB_ANONYMOUS_USER.equals(user);
    }

    public String getLoggedInUser(HttpServletRequest request) {
	if (isUserLoggedIn(request)) {
	    return request.getRemoteUser();
	} else {
	    return null;
	}
    }

    /**
     *
     * @param request Current HttpServletRequest
     * @return IdentityProviderID according to the current authentication context
     * @see de.mpg.aai.shhaa.context.AuthenticationContext
     */
    public String getIdentityProviderId(HttpServletRequest request) {
	AuthenticationContext context = AuthenticationContextHolder.get(request);
	return context.getIdentiyProviderID();
    }

    public RegistrationUser createRegistrationUser(HttpServletRequest request) {
	AuthenticationContext context = AuthenticationContextHolder.get(request);
	RegistrationUser regUser = new RegistrationUser();
	setAttributesFromShibbolethContext(context, regUser);
	return regUser;
    }

    /**
     * Sets the user attributes (name, email, organization) from the attributes provided
     * by the shibboleth context. Uses the collections nameAttributes, emailAttributes, organizationAttributes
     * as allowed values
     */
    public void setAttributesFromShibbolethContext(AuthenticationContext authContext, RegistrationUser user) {
	final AuthAttributes attributes = authContext.getAuthPrincipal().getAttribues();

	if (emailAttributes != null) {
	    user.setEmail(getAttributeValue(emailAttributes, attributes));
	}
	if (organizationAttributes != null) {
	    user.setOrganization(getAttributeValue(organizationAttributes, attributes));
	}
	if (firstNameAttributes != null) {
	    user.setFirstName(getAttributeValue(firstNameAttributes, attributes));
	}
	if (lastNameAttributes != null) {
	    user.setLastName(getAttributeValue(lastNameAttributes, attributes));
	}

	// Set entityID of IdP as hosting institution
	user.setHostingInstitute(authContext.getIdentiyProviderID());
    }

    /**
     * Searches the provided attributes for an attribute with any of the allowed id's (list
     * gets enumerated in the provided order)
     *
     * @param allowedIds Ids to match against
     * @param attributes Provided attributes
     * @return First matching string value encountered. If none found, null
     */
    private String getAttributeValue(List<String> allowedIds, AuthAttributes attributes) {
	Collection<String> attributeIds = attributes.getIDs();

	// Enumerate allowed id's in provided order
	for (String id : allowedIds) {
	    // Check if auth context contains attribute id
	    if (attributeIds.contains(id)) {
		// Get attribute value...
		AuthAttribute<?> attribute = attributes.get(id);
		Object value = attribute.getValue();
		// ..and return it if it is a string
		if (value instanceof String) {
		    return (String) value;
		}
	    }
	}
	return null;
    }
    private List<String> firstNameAttributes;
    private List<String> lastNameAttributes;
    private List<String> organizationAttributes;
    private List<String> emailAttributes;
    private boolean federated = true;

    /**
     * @param nameAttributes the nameAttributes to set
     */
    public void setFirstNameAttributes(List<String> nameAttributes) {
	this.firstNameAttributes = nameAttributes;
    }

    /**
     * @param lastNameAttributes the lastNameAttributes to set
     */
    public void setLastNameAttributes(List<String> lastNameAttributes) {
	this.lastNameAttributes = lastNameAttributes;
    }

    /**
     * @param organizationAttributes the organizationAttributes to set
     */
    public void setOrganizationAttributes(List<String> organizationAttributes) {
	this.organizationAttributes = organizationAttributes;
    }

    /**
     * @param emailAttributes the emailAttributes to set
     */
    public void setEmailAttributes(List<String> emailAttributes) {
	this.emailAttributes = emailAttributes;
    }

    /**
     * Gets whether federation is supported. Defaults to true.
     *
     * @return whether federation is supported
     */
    public boolean isFederated() {
	return federated;
    }

    /**
     * Sets whether federation should be supported. Defaults to true.
     *
     * @param federated the federated to set
     */
    public void setFederated(boolean federated) {
	this.federated = federated;
    }
}
