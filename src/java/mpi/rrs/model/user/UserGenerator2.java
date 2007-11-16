package mpi.rrs.model.user;

import nl.mpi.lat.auth.authentication.AuthenticationException;
import nl.mpi.lat.auth.authentication.AuthenticationService;
import nl.mpi.lat.auth.federation.FedUID;
import nl.mpi.lat.auth.principal.LatUser;
import nl.mpi.lat.auth.principal.PrincipalService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * $Id$
 * implementation of UserGenerator using ams2 api
 *  
 * @author	last modified by $Author$, created by mategg
 * @version	$Revision$
 */

public class UserGenerator2 implements UserGenerator {
	private static Log	_log = LogFactory.getLog(UserGenerator2.class);
	
	/** provides access to principals' data */
	private PrincipalService			mPcplSrv;
	/** handles authentication */
	private AuthenticationService	mAuthSrv;

	
	
	public UserGenerator2(PrincipalService pcplSrv, AuthenticationService authSrv) {
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
	 * @see mpi.rrs.model.user.UserGenerator#getUserInfoByUserName(java.lang.String)
	 */
	public User getUserInfoByUserName(String userName) {
		FedUID uid = this.toFedUid(userName);
		LatUser latUser = this.getPcplSrv().getUser(uid);
		User result = new User();
		// TODO: transcribe latUser to rrsUser
                result.setUserName(userName);
		result.setEmail(latUser.getEmail());
                result.setAddress(latUser.getAddress());
                result.setAffiliation(latUser.getOrganisation());
               
                result.setFirstName(latUser.getFirstName());
                result.setLastName(latUser.getName());
                result.setPhone("");
                result.setMiddleName("");
                result.setStatus("");
                result.setFax("");
		// ...
		return result;
	}



	/**
	 * @see mpi.rrs.model.user.UserGenerator#isValidPasswordForUsername(java.lang.String, java.lang.String)
	 */
	public boolean isValidPasswordForUsername(String userName, String passWord) {
		try {
			FedUID uid = this.toFedUid(userName);
			this.getAuthSrv().authenticate(uid, passWord);
			return true;
		} catch(AuthenticationException aE) {
			_log.info("failed authentication for userName: " + userName);
			return false;
		}
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
	public void setAuthSrv(AuthenticationService authSrv) {
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
	public void setPcplSrv(PrincipalService pcplSrv) {
		mPcplSrv = pcplSrv;
	}



	/**
	 * @see mpi.rrs.model.user.UserGenerator#isValidUserName(java.lang.String)
	 */
//	public boolean isValidUserName(String userName) {
//	}
	
	public String getInfo() {
		return "ams2 user generator";
	}
}
