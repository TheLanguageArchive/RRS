/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mpi.rrs.model.user;

/**
 *
 * @author kees
 */
import nl.mpi.common.util.Text;
import nl.mpi.common.util.spring.SpringContextLoader;
import nl.mpi.lat.ams.Constants;
import nl.mpi.lat.auth.authentication.AuthenticationService;
import nl.mpi.lat.auth.principal.PrincipalService;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class TestUserGenerator2 {
    TestUserGenerator2() {
        
    }
    
    public static void main(String[] args) {
        TestUserGenerator2 me = new TestUserGenerator2();
        
        me.test(null,null,null);
        
    }
    
    public void test(String springConfigPaths, String principalSrv, String authenticationSrv) {
        SpringContextLoader spring = new SpringContextLoader();
        spring.init(Text.notEmpty(springConfigPaths)
                ? springConfigPaths
                : "spring-ams2-auth.xml");
        UserGenerator2 ug2 = new UserGenerator2(
                (PrincipalService) spring.getBean(
                Text.notEmpty(principalSrv)
                ? principalSrv
                : Constants.BEAN_PRINCIPAL_SRV),
                (AuthenticationService) spring.getBean(
                Text.notEmpty(authenticationSrv)
                ? authenticationSrv
                : Constants.BEAN_INTEGRATED_AUTHENTICATION_SRV));
    
        ug2.addMemberToGroup("kees", "all registered users");
        
    }
}
