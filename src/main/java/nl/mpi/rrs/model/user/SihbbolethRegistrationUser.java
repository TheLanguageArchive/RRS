/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.mpi.rrs.model.user;

import de.mpg.aai.shhaa.context.AuthenticationContext;
import de.mpg.aai.shhaa.model.AuthAttributes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class SihbbolethRegistrationUser extends RegistrationUser {

    private static Log _log = LogFactory.getLog(RegistrationUser.class);
    private AuthenticationContext authContext;

    public SihbbolethRegistrationUser(AuthenticationContext context) {
        super();
        authContext = context;
    }

    public void setAttributesFromShibbolethContext() {
        AuthAttributes attributes = authContext.getAuthPrincipal().getAttribues();
        for (String id : attributes.getIDs()) {
            //request.setAttribute("att_" + id, attributes.get(id));
        }
    }
}
