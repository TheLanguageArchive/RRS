/*
 * RrsContextListener.java
 *
 * Created on February 5, 2007, 2:34 PM
 */
package nl.mpi.rrs;

/**
 *
 * @author kees
 */
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import nl.mpi.corpusstructure.CorpusStructureDBImpl;

import nl.mpi.rrs.model.registrations.RegisFileIO;
import nl.mpi.rrs.model.user.UserGenerator;
import nl.mpi.rrs.model.user.Ams2UserGenerator;
import nl.mpi.common.util.Text;
import nl.mpi.common.util.spring.SpringContextLoader;
import nl.mpi.lat.ams.Constants;
import nl.mpi.lat.auth.authentication.AuthenticationService;
import nl.mpi.lat.auth.principal.PrincipalService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class RrsContextListener implements ServletContextListener {

    static Log logger = LogFactory.getLog(RrsContextListener.class);
    private String corpusJdbcURL;
    private String corpusUser;
    private String corpusPass;
    private CorpusStructureDBImpl corpusDbConnection;
    private UserGenerator mUserGenerator;
    private String rrsRegistrationFileName;
    private RegisFileIO regisFileIO;

    /** Creates a new instance of RrsContextListener */
    public RrsContextListener() {
    }

    public void contextInitialized(ServletContextEvent sce) {

        ServletContext sc = sce.getServletContext();

        rrsRegistrationFileName = sc.getInitParameter("REGISTRATION_FILENAME");

        String maxFormNodeIds = sc.getInitParameter("MAX_FORM_NODE_IDS");
        sc.setAttribute("maxFormNodeIds", maxFormNodeIds);
        
        String amsInterfaceLink = sc.getInitParameter("AMS_INTERFACE_LINK");
        sc.setAttribute("amsInterfaceLink", amsInterfaceLink);

        String emailAddressCorpman = sc.getInitParameter("EMAIL_ADDRESS_CORPMAN");
        sc.setAttribute("emailAddressCorpman", emailAddressCorpman);        
        
        String emailHost = sc.getInitParameter("EMAIL_HOST");
        sc.setAttribute("emailHost", emailHost);

        logger.debug("RrsContextListener contextInitialized");

        try {
            Class cl = Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            logger.error("RRS_V1 ***** contextInitialized: No org.postgresql.Driver", ex);
        //ex.printStackTrace();
        }

        corpusJdbcURL = sc.getInitParameter("RRS_V1_CORPUS_SERVER_JDBC_URL");
        corpusUser = sc.getInitParameter("RRS_V1_CORPUS_SERVER_DB_USER");
        corpusPass = sc.getInitParameter("RRS_V1_CORPUS_SERVER_DB_PASS");


        //try {
        boolean bootstrapMode = false;
        corpusDbConnection = new CorpusStructureDBImpl(corpusJdbcURL, bootstrapMode, corpusUser, corpusPass);
        if (corpusDbConnection == null) {
            logger.fatal("************ corpusDbConnection is null");
            logger.fatal("corpusJdbcURL: " + corpusJdbcURL);
            logger.fatal("corpusUser: " + corpusUser);
        }

        RegisFileIO rfio = this.getRegisFileIO();
        sc.setAttribute("regisFileIO", rfio);

        UserGenerator ug = this.getUserGenerator(null, null, null);	// ams2 : using defaults
        logger.info("using UserGenerator " + ug.getInfo());

        sc.setAttribute(RrsConstants.AMS2_DB_CONNECTION_ATTRIBUTE, ug);
        sc.setAttribute(RrsConstants.CORPUS_DB_CONNECTION_ATTRIBUTE, corpusDbConnection);
        sc.setAttribute(RrsConstants.ARCHIVE_OBJECTS_DB_CONNECTION_ATTRIBUTE, corpusDbConnection);

    }

    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("contextDestroyed");
        if (corpusDbConnection != null) {
            logger.info("Closing Corpus DB");
            corpusDbConnection.close();
        }

    }

    /**
     * @return the userGenerator
     */
    /**
     * initializes utilized services and their configuration (springConfig files).
     * note:  all parameters can be null, 
     * 	if no value is given (null), the default config is used.
     * 
     * spring-ams2-auth.xml is used as default config
     * you can overwrite/extend... this config
     * by adding further spring-servlets (comma-separated) to the springConfigPaths
     * the actual beans which will be instantiated are the ones which have been defined in the LATEST given file
     * => by this way you can also easily overwrite some settings e.g. datasource strings etc...
     * (just define same bean with adapted settings in a new config file and add this file to this list)
     * 
     * @param springConfigPath commaseparated relative paths to multiple spring-config files defining the utilized services
     * @param principalSrv NAME of the (spring-bean) service which implements {@link PrincipalService}
     * @param authenticationSrv NAME of the (spring-bean) service which implements {@link AuthenticationService}
     */
    private UserGenerator getUserGenerator(String springConfigPaths, String principalSrv, String authenticationSrv) {
        if (mUserGenerator != null) {
            return mUserGenerator;
        }

        logger.info("initializing new (ams2)user-generator...");
        SpringContextLoader spring = new SpringContextLoader();
        spring.init(Text.notEmpty(springConfigPaths)
                ? springConfigPaths
                : "spring-ams2-auth.xml");
        Ams2UserGenerator userGenerator = new Ams2UserGenerator(
                (PrincipalService) spring.getBean(
                Text.notEmpty(principalSrv)
                ? principalSrv
                : Constants.BEAN_PRINCIPAL_SRV),
                (AuthenticationService) spring.getBean(
                Text.notEmpty(authenticationSrv)
                ? authenticationSrv
                : Constants.BEAN_INTEGRATED_AUTHENTICATION_SRV));

        this.setUserGenerator(userGenerator);
        //mUserGenerator = ug2;
        return mUserGenerator;
    }

    /**
     * @param userGenerator the userGenerator to set
     */
    private void setUserGenerator(UserGenerator userGenerator) {
        mUserGenerator = userGenerator;
    }

    public RegisFileIO getRegisFileIO() {
        if (this.regisFileIO != null) {
            return this.regisFileIO;
        } else {
            RegisFileIO rf = new RegisFileIO(rrsRegistrationFileName);
            this.setRegisFileIO(rf);
            return this.regisFileIO;
        }
    }

    public void setRegisFileIO(RegisFileIO regisFileIO) {
        this.regisFileIO = regisFileIO;
    }
}
