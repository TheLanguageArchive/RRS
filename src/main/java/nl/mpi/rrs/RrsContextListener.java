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
    private CorpusStructureDBImpl corpusDbConnection;
    private UserGenerator mUserGenerator;
    private String rrsRegistrationFileName;
    private RegisFileIO regisFileIO;

    /** Creates a new instance of RrsContextListener */
    public RrsContextListener() {
    }

    public void contextInitialized(ServletContextEvent sce) {

	ServletContext sc = sce.getServletContext();

	setAttributeFromContextParam(sc, RrsConstants.AMS_INTERFACE_LINK_ATTRIBUTE);
	setAttributeFromContextParam(sc, RrsConstants.EMAIL_ADDRESS_CORPMAN_ATTRIBUTE);
	setAttributeFromContextParam(sc, RrsConstants.EMAIL_HOST_ATTRIBUTE);
	setAttributeFromContextParam(sc, RrsConstants.DOBES_COC_LINK_ATTRIBUTE);
	setAttributeFromContextParam(sc, RrsConstants.DOBES_COC_LICENSE_NAME_ATTRIBUTE);
	setAttributeFromContextParam(sc, RrsConstants.OPEN_PATH_PREFIX_ATTRIBUTE);
	setAttributeFromContextParam(sc, RrsConstants.REGISTRATION_STATIC_PAGE_ATTRIBUTE);
	setBooleanAttributeFromContextParam(sc, RrsConstants.ALLOW_NEW_INTERNAL_USERS_ATTRIBUTE);

	logger.debug("RrsContextListener contextInitialized");

	try {
	    Class cl = Class.forName("org.postgresql.Driver");
	} catch (ClassNotFoundException ex) {
	    logger.error("RRS_V1 ***** contextInitialized: No org.postgresql.Driver", ex);
	    //ex.printStackTrace();
	}

	final String corpusDbName = getContextParam(sc, RrsConstants.CORPUS_SERVER_DB_JNDI_NAME_ATTRIBUTE);

	//try {
	if (corpusDbName != null) {
	    logger.debug("Connecting to database by name " + corpusDbName);
	    corpusDbConnection = new CorpusStructureDBImpl(corpusDbName);
	}

	if (corpusDbConnection == null) {
	    final String corpusJdbcURL = getContextParam(sc, RrsConstants.CORPUS_SERVER_DB_JDBC_URL_ATTRIBUTE);
	    final String corpusUser = getContextParam(sc, RrsConstants.CORPUS_SERVER_DB_USER_ATTRIBUTE);
	    final String corpusPass = getContextParam(sc, RrsConstants.CORPUS_SERVER_DB_PASS_ATTRIBUTE);

	    logger.debug("Connecting to database by url " + corpusJdbcURL + " and provided username/password");
	    boolean bootstrapMode = false;
	    corpusDbConnection = new CorpusStructureDBImpl(corpusJdbcURL, bootstrapMode, corpusUser, corpusPass);

	    if (corpusDbConnection == null) {
		logger.fatal("************ corpusDbConnection is null");
		logger.fatal("corpusJdbcURL: " + corpusJdbcURL);
		logger.fatal("corpusUser: " + corpusUser);
	    }
	}
	rrsRegistrationFileName = getContextParam(sc, RrsConstants.REGISTRATION_FILE_ATTRIBUTE);
	RegisFileIO rfio = this.getRegisFileIO();
	sc.setAttribute(RrsConstants.REGIS_FILE_IO, rfio);

	UserGenerator ug = this.getUserGenerator(null, null, null);	// ams2 : using defaults
	logger.debug("using UserGenerator " + ug.getInfo());

	sc.setAttribute(RrsConstants.AMS2_DB_CONNECTION_ATTRIBUTE, ug);
	sc.setAttribute(RrsConstants.CORPUS_DB_CONNECTION_ATTRIBUTE, corpusDbConnection);
	sc.setAttribute(RrsConstants.ARCHIVE_OBJECTS_DB_CONNECTION_ATTRIBUTE, corpusDbConnection);


	String archiveUsersIdpName = getContextParam(sc, RrsConstants.ARCHIVE_USERS_IDP_NAME_ATTRIBUTE);
	if (archiveUsersIdpName != null && archiveUsersIdpName.length() == 0) {
	    archiveUsersIdpName = null;
	}

	sc.setAttribute(RrsConstants.ARCHIVE_USERS_IDP_NAME_ATTRIBUTE, archiveUsersIdpName);
    }

    private void setAttributeFromContextParam(ServletContext servletContext, String name) {
	final String initParamString = getContextParam(servletContext, name);
	servletContext.setAttribute(name, initParamString);
	logger.debug("Stored into servlet context attribute " + name);
    }

    private void setBooleanAttributeFromContextParam(ServletContext servletContext, String name) {
	final String initParamString = getContextParam(servletContext, name);
	final boolean initParamValue = Boolean.parseBoolean(initParamString);
	logger.debug("Parsed to boolean value " + initParamValue);
	servletContext.setAttribute(name, initParamValue);
	logger.debug("Stored into servlet context attribute " + name);
    }

    private String getContextParam(ServletContext servletContext, String name) {
	final String initParamName = RrsConstants.RRS_CONTEXT_PARAM_PREFIX + name;
	logger.debug("Reading context parameter " + initParamName);
	final String initParamString = servletContext.getInitParameter(initParamName);
	logger.debug("Read value string " + initParamString);
	return initParamString;
    }

    public void contextDestroyed(ServletContextEvent sce) {
	logger.debug("contextDestroyed");
	if (corpusDbConnection != null) {
	    logger.debug("Closing Corpus DB");
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

	logger.debug("initializing new (ams2)user-generator...");

	springConfigPaths = Text.notEmpty(springConfigPaths)
		? springConfigPaths
		: "spring-ams2-auth.xml";
	logger.debug("Spring config paths: " + springConfigPaths);

	SpringContextLoader spring = new SpringContextLoader();
	spring.init(springConfigPaths);
	logger.debug("Spring context loader: " + spring.toString());

	PrincipalService principalService = (PrincipalService) spring.getBean(Text.notEmpty(principalSrv) ? principalSrv : Constants.BEAN_PRINCIPAL_SRV);
	logger.debug("Principal service: " + principalService.toString());
	AuthenticationService authenticationService = (AuthenticationService) spring.getBean(Text.notEmpty(authenticationSrv) ? authenticationSrv : Constants.BEAN_INTEGRATED_AUTHENTICATION_SRV);
	logger.debug("Authentication service: " + authenticationService.toString());

	Ams2UserGenerator userGenerator = new Ams2UserGenerator(principalService, authenticationService);
	logger.debug("User generator initialized: " + userGenerator.getInfo());

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
