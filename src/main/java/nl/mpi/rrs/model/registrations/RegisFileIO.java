package nl.mpi.rrs.model.registrations;

import edu.emory.mathcs.backport.java.util.Collections;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import nl.mpi.rrs.model.user.RegistrationUser;
import nl.mpi.rrs.model.utilities.RrsUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author kees
 */
public class RegisFileIO implements Serializable {

    public RegisFileIO(String registrationFilename) {
	this.setRegistrationFilename(registrationFilename);
    }
    // sequence of fields in delimited file
    private final int USER_ID = 0;
    private final int USER_FIRST_NAME = 1;
    private final int USER_LAST_NAME = 2;
    private final int USER_EMAIL = 3;
    private final int USER_ORGANIZATION = 4;
    private final int USER_PASSWORD = 5;
    private final int USER_DOBES_COC_SIGNED = 6;
    private final int USER_NODE_IDS = 7;
    private final int USER_CREATION = 8;
    private final int FIELDS_COUNT = 9;
    private String registrationFilename;
    private final String delim = "\t";
    private final String nodeIdDelim = ",";
    public static final String EMPTY_NODE_IDS_LIST = "-";
    private static Log _log = LogFactory.getLog(RegisFileIO.class);

    /**
     * set field CocSigned to true in user record
     *
     * @param userInfo User object
     * @return true if sucessful
     */
    public synchronized boolean updateCocSigned(RegistrationUser userInfo) {
	boolean success = false;

	String newLine = "\n";

	synchronized (this) {

	    String backupFilename = registrationFilename + ".tmp";
	    String cmd = "mv " + backupFilename + " " + registrationFilename;

	    FileWriter writer;
	    try {

		writer = new FileWriter(backupFilename);

		BufferedReader buffReader = new BufferedReader(new FileReader(getRegistrationFilename()));

		String line;
		while (((line = buffReader.readLine()) != null)) {
		    String fields[] = line.split(delim);

		    if (fields.length == FIELDS_COUNT) {
			String userId = fields[USER_ID];

			if (userInfo.getUserName().equalsIgnoreCase(userId)) {
			    userInfo.setDobesCocSigned(true);
			    String userRecord = userInfo.createUserRecordAsString(newLine, delim, nodeIdDelim);
			    writer.write(userRecord);
			    success = true;
			} else {
			    writer.write(line + newLine);
			}


		    }

		}

		writer.close();
		buffReader.close();

		if (success) {
		    success = RrsUtil.execCommand(cmd);
		    if (success) {
			_log.debug("Successful execute of cmd: " + cmd);
		    } else {
			_log.error("Can't execute cmd: " + cmd);
		    }
		}


	    } catch (FileNotFoundException e) {
		success = false;
		_log.error("Error file not found: " + e.getMessage());
	    } catch (IOException e) {
		success = false;
		_log.error("Error file processing: " + e.getMessage());
	    }


	}

	return success;
    }

    public synchronized boolean removeRegistrationFromFile(RegistrationUser userInfo) {
	boolean success = false;

	String newLine = "\n";

	synchronized (this) {

	    String backupFilename = registrationFilename + ".tmp";
	    String cmd = "mv " + backupFilename + " " + registrationFilename;

	    FileWriter writer;
	    try {

		writer = new FileWriter(backupFilename);

		BufferedReader buffReader = new BufferedReader(new FileReader(getRegistrationFilename()));

		String line;
		while (((line = buffReader.readLine()) != null)) {
		    String fields[] = line.split(delim);

		    if (fields.length == FIELDS_COUNT) {
			String userId = fields[USER_ID];

			if (userInfo.getUserName().contentEquals(userId)) {
			    success = true;
			} else {
			    writer.write(line + newLine);
			}


		    }

		}

		writer.close();
		buffReader.close();

		if (success) {
		    success = RrsUtil.execCommand(cmd);
		    if (success) {
			_log.debug("Successful execute of cmd: " + cmd);
		    } else {
			_log.error("Can't execute cmd: " + cmd);
		    }
		}


	    } catch (FileNotFoundException e) {
		success = false;
		_log.error("Error file not found: " + e.getMessage());
	    } catch (IOException e) {
		success = false;
		_log.error("Error file processing: " + e.getMessage());
	    }


	}

	return success;
    }

    public synchronized int removeOldRegistrationsFromFile(int ageInDays) {
	int recordsRemoved = 0;
	boolean success = false;

	String newLine = "\n";

	synchronized (this) {

	    String backupFilename = registrationFilename + ".tmp";
	    String cmd = "mv " + backupFilename + " " + registrationFilename;

	    FileWriter writer;
	    try {

		writer = new FileWriter(backupFilename);

		BufferedReader buffReader = new BufferedReader(new FileReader(getRegistrationFilename()));

		String line;
		while (((line = buffReader.readLine()) != null)) {
		    String fields[] = line.split(delim);

		    if (fields.length == FIELDS_COUNT) {
			String userCreation = fields[USER_CREATION];

			Date userCreationDate = new Date();

			try {
			    userCreationDate = new SimpleDateFormat("MMM dd, yyyy").parse(userCreation);
			} catch (ParseException e) {
			    _log.error("Invalid date: " + userCreation + " : " + e.getMessage());
			}

			if (RrsUtil.dateDiffFromToday(userCreationDate) > ageInDays) {
			    recordsRemoved++;
			    success = true;
			} else {
			    writer.write(line + newLine);
			}


		    }

		}

		writer.close();
		buffReader.close();

		if (success) {
		    success = RrsUtil.execCommand(cmd);
		    if (success) {
			_log.debug("Successful execute of cmd: " + cmd);
		    } else {
			_log.error("Can't execute cmd: " + cmd);
		    }
		}


	    } catch (FileNotFoundException e) {
		success = false;
		_log.error("Error file not found: " + e.getMessage());
	    } catch (IOException e) {
		success = false;
		_log.error("Error file processing: " + e.getMessage());
	    }


	}

	_log.debug(recordsRemoved + " records removed from registration file.");

	return recordsRemoved;
    }

    public synchronized boolean writeRegistrationToFile(RegistrationUser userInfo) {
	boolean success = true;
	boolean append = true;

	synchronized (this) {
	    FileWriter writer;

	    try {
		writer = new FileWriter(getRegistrationFilename(), append);
		String newLine = "\n";

		String userRecord = userInfo.createUserRecordAsString(newLine, delim, nodeIdDelim);
		writer.write(userRecord);

		writer.close();


	    } catch (FileNotFoundException e) {
		success = false;
		_log.error("message: " + e.getMessage());
		_log.error("Can't write to file: " + getRegistrationFilename());
	    } catch (IOException e) {
		success = false;
		_log.error("Error file processing: " + getRegistrationFilename());
	    }

	}

	return success;

    }

    public boolean isRegistrationInFile(RegistrationUser userInfo) {

	boolean success = false;

	synchronized (this) {
	    try {
		BufferedReader buffReader = new BufferedReader(new FileReader(getRegistrationFilename()));

		String line;
		while (((line = buffReader.readLine()) != null) && (!success)) {
		    String fields[] = line.split(delim);

		    if (fields.length == FIELDS_COUNT) {
			String userId = fields[USER_ID];
			String userFirstName = fields[USER_FIRST_NAME];
			String userLastName = fields[USER_LAST_NAME];
			String userEmail = fields[USER_EMAIL];
			String userOrganization = fields[USER_ORGANIZATION];
			String userPassword = fields[USER_PASSWORD];
			String userDobesCocSigned = fields[USER_DOBES_COC_SIGNED];
			String userNodeIds = fields[USER_NODE_IDS];
			String userCreation = fields[USER_CREATION];


			if (userInfo.getUserName().equalsIgnoreCase(userId)) {
			    _log.debug("Found registrated user:");
			    _log.debug("userId: " + userId);
			    _log.debug("userFirstName: " + userFirstName);
			    _log.debug("userLastName: " + userLastName);
			    _log.debug("userEmail: " + userEmail);
			    _log.debug("userOrganization: " + userOrganization);
			    _log.debug("userPassword: " + userPassword);
			    _log.debug("userDobesCocSigned: " + userDobesCocSigned);
			    _log.debug("userNodeIds: " + userNodeIds);
			    _log.debug("userCreation: " + userCreation);
			    success = true;
			}
		    }
		}


		buffReader.close();

	    } catch (FileNotFoundException e) {
		success = false;
		_log.error("message: " + e.getMessage());
		_log.error("Can't find file: " + getRegistrationFilename());
	    } catch (IOException e) {
		success = false;
		_log.error("Error file processing: " + getRegistrationFilename());
	    }
	}

	return success;

    }

    public RegistrationUser getRegistrationFromFile(String userName) {
	RegistrationUser userInfo = new RegistrationUser();

	synchronized (this) {
	    try {
		BufferedReader buffReader = new BufferedReader(new FileReader(getRegistrationFilename()));

		String line;
		boolean found = false;
		while (((line = buffReader.readLine()) != null) && (!found)) {
		    String fields[] = line.split(delim);

		    if (fields.length == FIELDS_COUNT) {
			String userId = fields[USER_ID];
			String userFirstName = fields[USER_FIRST_NAME];
			String userLastName = fields[USER_LAST_NAME];
			String userEmail = fields[USER_EMAIL];
			String userOrganization = fields[USER_ORGANIZATION];
			String userPassword = RegistrationUser.decodePassword(fields[USER_PASSWORD]);
			String userDobesCocSigned = fields[USER_DOBES_COC_SIGNED];
			String userNodeIds = fields[USER_NODE_IDS];
			String userCreation = fields[USER_CREATION];

			if (userName.equalsIgnoreCase(userId)) {
			    userInfo.setUserName(userId);
			    userInfo.setFirstName(userFirstName);
			    userInfo.setLastName(userLastName);
			    userInfo.setEmail(userEmail);
			    userInfo.setOrganization(userOrganization);
			    userInfo.setPassword(userPassword);
			    userInfo.setDobesCocSigned(Boolean.valueOf(userDobesCocSigned));
			    userInfo.setNodeIds(nodeIdsStringToList(userNodeIds));
			    userInfo.setCreation_ts(userCreation);

			    _log.debug("Found registrated user:");
			    _log.debug("userId: " + userId);
			    _log.debug("userFirstName: " + userFirstName);
			    _log.debug("userLastName: " + userLastName);
			    _log.debug("userEmail: " + userEmail);
			    _log.debug("userOrganization: " + userOrganization);
			    //_log.info("userPassword: " + userPassword);
			    _log.debug("userDobesCocSigned: " + userDobesCocSigned);
			    _log.debug("userNodeIds: " + userNodeIds);
			    _log.debug("userCreation: " + userCreation);
			    found = true;
			}
		    }
		}


		buffReader.close();

	    } catch (FileNotFoundException e) {
		_log.error("message: " + e.getMessage());
		_log.error("Can't find file: " + getRegistrationFilename());
	    } catch (IOException e) {
		_log.error("Error file processing: " + getRegistrationFilename());
	    }
	}

	return userInfo;

    }

    private List<String> nodeIdsStringToList(String userNodeIds) {
	if (userNodeIds.trim().equalsIgnoreCase(EMPTY_NODE_IDS_LIST)) {
	    return Collections.emptyList();
	} else {
	    return Arrays.asList(userNodeIds.split(nodeIdDelim));
	}
    }

    public final synchronized String getRegistrationFilename() {
	return registrationFilename;
    }

    public final synchronized void setRegistrationFilename(String registrationFilename) {
	this.registrationFilename = registrationFilename;
    }
}
