/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mpi.rrs.model.ams;

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
    
    
    
    

}
