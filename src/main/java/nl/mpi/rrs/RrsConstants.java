package nl.mpi.rrs;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class RrsConstants {

    private final static String MPI_PREFIX = "nl.mpi.";
    private final static String RRS_PREFIX = MPI_PREFIX + "rrs.";
    // nl.mpi parameters
    public final static String EMAIL_ADDRESS_CORPMAN_ATTRIBUTE = MPI_PREFIX + "emailAddressCorpman";
    public final static String SMTP_HOST_ATTRIBUTE = MPI_PREFIX + "smtpHost";
    // nl.mpi.rrs parameters
    public final static String AUTHENTICATION_PROVIDER_ATTRIBUTE = RRS_PREFIX + "authenticationProvider";
    public final static String AMS2_DB_CONNECTION_ATTRIBUTE = RRS_PREFIX + "ams2DbConnection";
    public final static String ARCHIVE_OBJECTS_DB_CONNECTION_ATTRIBUTE = RRS_PREFIX + "archiveObjectsDbConnection";
    public final static String ALLOW_NEW_INTERNAL_USERS_ATTRIBUTE = RRS_PREFIX + "allowNewInternalUsers";
    public final static String REGISTRATION_FILE_ATTRIBUTE = RRS_PREFIX + "registrationFile";
    public final static String ARCHIVE_USERS_IDP_NAME_ATTRIBUTE = RRS_PREFIX + "archiveUsersIdpName";
    public final static String DOBES_COC_LINK_ATTRIBUTE = RRS_PREFIX + "dobesCocLink";
    public final static String DOBES_COC_LICENSE_NAME_ATTRIBUTE = RRS_PREFIX + "dobesCocLicenseName";
    public final static String CORPUS_DB_CONNECTION_ATTRIBUTE = RRS_PREFIX + "corpusDbConnection";
    public final static String CORPUS_SERVER_DB_JNDI_NAME_ATTRIBUTE = RRS_PREFIX + "corpusServerDb.jndiName";
    public final static String OPEN_PATH_PREFIX_ATTRIBUTE = RRS_PREFIX + "openPathPrefix";
    public final static String AMS_INTERFACE_LINK_ATTRIBUTE = RRS_PREFIX + "amsInterfaceLink";
    public final static String REGISTRATION_STATIC_PAGE_ATTRIBUTE = RRS_PREFIX + "registrationStaticPage";
    public final static String REGIS_FILE_IO = RRS_PREFIX + "regisFileIO";
    public final static String SESSION_NODE_IDS = RRS_PREFIX + "nodeIds";
}
