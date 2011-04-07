/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.mpi.rrs.model.ams;

/**
 *
 * @author kees
 */
public class AmsServicesSingleton {
    private static AmsServices services = new AmsServices();
    

    private AmsServicesSingleton() {
        
    }
    
    public synchronized static AmsServices getInstance() {
        return services;
    }

}
