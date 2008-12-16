/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mpi.rrs.model.ams;

import nl.mpi.lat.auth.authentication.AuthenticationException;
import nl.mpi.lat.auth.federation.FedUID;
import nl.mpi.lat.fabric.NodeID;

/**
 *
 * @author kees
 */
public class AmsUtils {
    
    public AmsUtils() {
        
    }
    
    public static NodeID getCorpusRootNode() {
        return AmsServicesSingleton.getInstance().getFabricSrv().getRootNode();    
    }
    
    
    /**
     * transcribes given userName to FedUID signature,
     * ensures valid fed-id part (using default in case)
     * @param userName
     * @return
     */
    private FedUID toFedUid(String userName) {
        FedUID result = AmsServicesSingleton.getInstance().getPcplSrv().newDamLrID(userName);
        return result;
    }
    
    /**
     * @see mpi.rrs.model.user.UserGenerator#isValidPasswordForUsername(java.lang.String, java.lang.String)
     */
    public boolean isValidPasswordForUsername(String userName, String passWord) {
        try {
            FedUID uid = this.toFedUid(userName);
            AmsServicesSingleton.getInstance().getAuthSrv().authenticate(uid, passWord);
            return true;
        } catch (AuthenticationException aE) {
            return false;
        }
    }
    
    
    

}
