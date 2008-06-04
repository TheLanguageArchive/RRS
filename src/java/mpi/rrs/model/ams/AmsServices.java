/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mpi.rrs.model.ams;

import nl.mpi.common.util.spring.SpringContextLoader;
import nl.mpi.lat.ams.Constants;
import nl.mpi.lat.auth.authorization.AdvAuthorizationService;
import nl.mpi.lat.auth.principal.PrincipalService;
import nl.mpi.lat.fabric.FabricService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author kees
 */
public class AmsServices {
    
    private static Log logger = LogFactory.getLog(AmsServices.class);
    private PrincipalService pcplSrv;
    private AdvAuthorizationService authzSrv;
    private FabricService fabricSrv;

    public AmsServices() {
        this.loadAms2Services();
    }
    
    /**
     * example method to load spring-configured services from ams2 api
     */
    public void loadAms2Services() {

        // 1st create the spring-bean-loader instance 
        // you might consider it as kind of "bean factory"
        SpringContextLoader spring = new SpringContextLoader();
        
        // initialize the bean loader
        // with the (spring-beans)config file it should use 
        // to lookup the bean definitions
        spring.init("spring-ams2-core.xml");
        
        logger.info("Constants.BEAN_PRINCIPAL_SRV: " + Constants.BEAN_PRINCIPAL_SRV);
        logger.info("Constants.BEAN_AUTHORIZATION_SRV: " + Constants.BEAN_AUTHORIZATION_SRV);
        logger.info("Constants.BEAN_FABRIC_SRV: " + Constants.BEAN_FABRIC_SRV);
        
        
        // load a bean by its id/name:
        // use the service interface as reference!
        pcplSrv = (PrincipalService) spring.getBean(Constants.BEAN_PRINCIPAL_SRV);

        // just another ams2 service as example: the "advanced authorization service" (managing user-node-rules information)
        authzSrv = (AdvAuthorizationService) spring.getBean(Constants.BEAN_AUTHORIZATION_SRV);

        // just another ams2 service as example: the "rule service" (providing rules/roles information)
        //RuleService ruleSrv = (RuleService) spring.getBean(Constants.BEAN_RULE_SRV);

        // load the needed service: FabricService (designed to wrap further functionality arround corpusstructure api)
        fabricSrv =  (FabricService) spring.getBean(Constants.BEAN_FABRIC_SRV);

    }

    public PrincipalService getPcplSrv() {
        return pcplSrv;
    }

    public AdvAuthorizationService getAuthzSrv() {
        return authzSrv;
    }

    public FabricService getFabricSrv() {
        return fabricSrv;
    }
}
