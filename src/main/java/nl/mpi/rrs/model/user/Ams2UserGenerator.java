package nl.mpi.rrs.model.user;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import nl.mpi.lat.auth.authentication.AuthenticationException;
import nl.mpi.lat.auth.authentication.AuthenticationService;
import nl.mpi.lat.auth.federation.FedUID;
import nl.mpi.lat.auth.principal.LatUser;
import nl.mpi.lat.auth.principal.LatGroup;
import nl.mpi.lat.auth.principal.LatPrincipal;
import nl.mpi.lat.auth.principal.PrincipalService;

import nl.mpi.lat.dao.DataSourceException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * $Id$
 * implementation of UserGenerator using ams2 api
 *  
 * @author	last modified by $Author$, created by mategg
 * @version	$Revision$
 */
public class Ams2UserGenerator implements UserGenerator {

    private static Log _log = LogFactory.getLog(Ams2UserGenerator.class);
    /** provides access to principals' data */
    private PrincipalService mPcplSrv;
    /** handles authentication */
    private AuthenticationService mAuthSrv;

    public Ams2UserGenerator(PrincipalService pcplSrv, AuthenticationService authSrv) {
        this.setPcplSrv(pcplSrv);
        this.setAuthSrv(authSrv);
    }

    /**
     * transcribes given userName to FedUID signature,
     * ensures valid fed-id part (using default in case)
     * @param userName
     * @return
     */
    private FedUID toFedUid(String userName) {
        FedUID result = this.getPcplSrv().newDamLrID(userName);
        return result;
    }

    /**
     * @see nl.mpi.rrs.model.user.UserGenerator#getUserInfoByUserName(java.lang.String)
     */
    public User getUserInfoByUserName(String userName) {
        User result = new User();

        try {
            FedUID uid = this.toFedUid(userName);
            LatUser latUser = this.getPcplSrv().getUser(uid);

            // TODO: transcribe latUser to rrsUser
            result.setUserName(userName);
            result.setEmail(latUser.getEmail());
            result.setAddress(latUser.getAddress());
            result.setOrganization(latUser.getOrganisation());
            result.setFirstName(latUser.getFirstName());
            result.setLastName(latUser.getName());
            result.setPhone("");
            result.setMiddleName("");
            result.setStatus("");
            result.setFax("");

        // ...
        } catch (DataSourceException ex) {
            _log.info("username: " + userName + "does not exist!");
            result = null;
        }

        return result;
    }

    /**
     * load an user entity from ams2 db by his username
     * @param userName string representation of the destined user's username or fed-uid
     */
    public LatUser getUserByUserName(String userName) {
        // translate username string to its class representation
        FedUID uid = this.toFedUid(userName);

        // load the user from ams2 db via PrincipalService instance
        LatUser user = this.getPcplSrv().getUser(uid);
        return user;
    }

    /**
     * load a group entity from ams2 db by its groupname
     * @param groupname string representation of the destined group's groupname or fed-uid
     */
    public LatGroup getGroupByName(String groupName) {
        // translate name string to its class representation
        FedUID gid = this.toFedUid(groupName);

        // load the user from ams2 db via PrincipalService instance
        LatGroup group = this.getPcplSrv().getGroup(gid);
        return group;
    }

    public boolean isExistingUserName(String userName) {
        boolean result = false;
        try {
            FedUID uid = this.toFedUid(userName);
            LatUser latUser = this.getPcplSrv().getUser(uid);

            if (latUser != null) {
                result = true;
            }
        } catch (DataSourceException ex) {
            _log.error("User not found: " + userName);
            return false;
        }

        return result;
    }

    /**
     * @see nl.mpi.rrs.model.user.UserGenerator#isValidPasswordForUsername(java.lang.String, java.lang.String)
     */
    public boolean isValidPasswordForUsername(String userName, String passWord) {
        try {
            FedUID uid = this.toFedUid(userName);
            this.getAuthSrv().authenticate(uid, passWord);
            return true;
        } catch (AuthenticationException aE) {
            _log.info("failed authentication for userName: " + userName);
            return false;
        }
    }

    /**
     * example: CREATE a new user
     * @throws DataSourceException if save fails
     */
    public boolean addNewUser(User userinfo) {
        boolean result = true;
        String userName = userinfo.getUserName();

        try {
            // create new fed-uid:
            FedUID uid = this.toFedUid(userName);

            // load the needed services:
            PrincipalService pcplSrv = this.getPcplSrv();

            // create new instance:
            LatUser user = pcplSrv.newUser();

            user.setFedUID(uid);

            // fill in user attributes
            user.setFirstName(userinfo.getFirstName());
            user.setName(userinfo.getLastName());
            user.setEmail(userinfo.getEmail());
            user.setOrganisation(userinfo.getOrganization());
            //user.setPasswd(userinfo.getPassword()); (deprecated; sets passwords unencrypted)

            // note: update (new) datasets' creator-/last-modifier-fields:
            // use the current user
            //LatUser modifier = this.getCurrentUser(); // you should have it from somewhere
            // or if it's an automated process: use the system-user who is always there:
            LatUser modifier = pcplSrv.getSystemUser();
            user.setModifier(modifier);

            // ...and save new data and store plain text password encrypted in DB
            pcplSrv.save(user, userinfo.getPassword());  // throws DataSourceException (unchecked)
        } catch (DataSourceException aE) {
            result = false;
            _log.warn("addNewUser: failed adding to ANMS DB for userName: " + userName, aE);
        }

        return result;
    }

    /**
     * example: modify GROUP-MEMBERSHIPs
     * @throws DataSourceException if save fails
     */
    public boolean addMemberToGroup(String userName, String groupName) {
        LatUser user;
        LatGroup group;

        try {
            user = this.getUserByUserName(userName);
        } catch (DataSourceException ex) {
            _log.info("username: " + userName + " does not exist.");
            return false;
        }

        try {
            group = this.getGroupByName(groupName);
        } catch (DataSourceException ex) {
            _log.info("group: " + groupName + " does not exist.");
            return false;
        }

        try {
            // add member to group
            // 1st: need to wrap target members into a set
            Set<LatPrincipal> members = Collections.synchronizedSet(new LinkedHashSet<LatPrincipal>());
            members.add(user);
            // 2nd: add members via setter method!
            group.addMembers(members);

            // 3rd: save new relations:
            this.getPcplSrv().save(group);

        } catch (DataSourceException ex) {
            _log.info("username: " + userName + "has not been added to " + groupName);
            return false;
        }

        return true;

    }

    /**
     * example: modify GROUP-MEMBERSHIPs
     * @throws DataSourceException if save fails
     */
    public String getMembersOfGroup(String groupName) {
        LatGroup group;

        try {
            group = this.getGroupByName(groupName);
        } catch (DataSourceException ex) {
            _log.info("group: " + groupName + " does not exist.");
            return null;
        }


        Set<LatPrincipal> members = group.getMembers();
        StringBuilder result = new StringBuilder();
        for (Iterator<LatPrincipal> iter = members.iterator(); iter.hasNext();) {
            LatPrincipal npl = iter.next();
            result.append("Name: ")
                    .append(npl.getName())
                    .append(" ::: Id: ")
                    .append(npl.getUid())
                    .append(" ::: \n");
        }

        return result.toString();

    }

    /**
     * example: modify GROUP-MEMBERSHIPs
     * @throws DataSourceException if save fails
     */
    public boolean isMemberOfGroup(String userName, String groupName) {

        LatPrincipal user;
        LatGroup group;

        try {
            user = this.getUserByUserName(userName);
        } catch (DataSourceException ex) {
            _log.info("username: " + userName + " does not exist.");
            return false;
        }

        try {
            group = this.getGroupByName(groupName);
        } catch (DataSourceException ex) {
            _log.info("group: " + groupName + " does not exist.");
            return false;
        }


        // 1st: need to wrap target members into a set
        Set<LatPrincipal> members = group.getMembers();
        for (Iterator<LatPrincipal> iter = members.iterator(); iter.hasNext();) {
            LatPrincipal npl = iter.next();
            if (npl.getUid().equals(user.getUid())) {
                return true;
            }

        }

        return false;

    }

    /**
     * @return the authSrv
     */
    public AuthenticationService getAuthSrv() {
        return mAuthSrv;
    }

    /**
     * @param authSrv the authSrv to set
     */
    final public void setAuthSrv(AuthenticationService authSrv) {
        mAuthSrv = authSrv;
    }

    /**
     * @return the pcplSrv
     */
    public PrincipalService getPcplSrv() {
        return mPcplSrv;
    }

    /**
     * @param pcplSrv the pcplSrv to set
     */
    final public void setPcplSrv(PrincipalService pcplSrv) {
        mPcplSrv = pcplSrv;
    }

    /**
     * @see nl.mpi.rrs.model.user.UserGenerator#isValidUserName(java.lang.String)
     */
//	public boolean isValidUserName(String userName) {
//	}
    public String getInfo() {
        return "ams2 user generator";
    }
}
