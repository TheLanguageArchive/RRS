package nl.mpi.rrs.model.user;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author kees
 */
public class RegistrationUser extends User {

    public RegistrationUser() {
	super();
    }
    private final static Log _log = LogFactory.getLog(RegistrationUser.class);
    private String userInfo;
    private List<String> nodeIds;

    public String createUserRecordAsString(String newLine, String delim, String nodeIdDelim) {
	StringBuilder result = new StringBuilder();

	result.append(this.removeDelim(this.getUserName(), delim)).append(delim);
	result.append(this.removeDelim(this.getFirstName(), delim)).append(delim);
	result.append(this.removeDelim(this.getLastName(), delim)).append(delim);
	result.append(this.removeDelim(this.getEmail(), delim)).append(delim);
	result.append(this.removeDelim(this.getOrganization(), delim)).append(delim);
	result.append(encodePassword(this.removeDelim(this.getPassword(), delim))).append(delim);
	result.append(this.isDobesCocSigned()).append(delim);
	result.append(serializeNodeIds(nodeIdDelim)).append(delim);
	result.append(this.removeDelim(this.getCreation_ts(), delim)).append(newLine);

	return result.toString();
    }

    public String removeDelim(String input, String delim) {
	if (input != null) {
	    return input.replaceAll(delim, "");
	} else {
	    return "";
	}
    }

    public String serializeNodeIds(String delim) {
	if (nodeIds == null || nodeIds.isEmpty()) {
	    return "-";
	} else {
	    StringBuilder nodesString = new StringBuilder();
	    for (int i = 0; i < nodeIds.size(); i++) {
		nodesString.append(nodeIds.get(i));
		if (i < nodeIds.size() - 1) {
		    nodesString.append(delim);
		}
	    }
	    return nodesString.toString();
	}
    }

    public String createUserInfo(String newLine) {

	StringBuilder userInfoStringBuilder = new StringBuilder();

	userInfoStringBuilder.append("User Id: ").append(this.getUserName()).append(newLine);
	userInfoStringBuilder.append("First Name: ").append(this.getFirstName()).append(newLine);
	userInfoStringBuilder.append("Last Name: ").append(this.getLastName()).append(newLine);
	userInfoStringBuilder.append("Organisation: ").append(this.getOrganization()).append(newLine);
	userInfoStringBuilder.append("Email: ").append(this.getEmail());

	return userInfoStringBuilder.toString();
    }

    public String getUserInfo() {
	if (this.userInfo == null) {
	    this.setUserInfo();
	}

	return userInfo;
    }

    public void setUserInfo() {
	String newLine = "\n";
	this.userInfo = this.createUserInfo(newLine);
    }

    public List<String> getNodeIds() {
	return nodeIds;
    }

    public void setNodeIds(List<String> nodeIds) {
	this.nodeIds = nodeIds;
    }

    public static String decodePassword(String crypticPassword) {
	String result = "";

	if (crypticPassword != null) {
	    if (crypticPassword.length() > 1) {
		String tmp = crypticPassword.substring(1);
		String arr[] = tmp.split("-");
		for (int i = 0; i < arr.length; i++) {
		    try {
			int charVal = Integer.valueOf(arr[i]) - 1000 - i;
			char ch = (char) charVal;
			result += ch;
		    } catch (NumberFormatException e) {
			_log.error("Invalid cryptic password: " + crypticPassword);
			return null;
		    }

		}
	    }
	}

	return result;
    }

    public static String encodePassword(String password) {
	StringBuilder result = new StringBuilder();

	if (password != null) {
	    result.append("#");
	    for (int i = 0; i < password.length(); i++) {
		int charVal = password.charAt(i);
		charVal += 1000 + i;
		result.append(charVal);
		if (i != password.length() - 1) {
		    result.append("-");
		}
	    }
	}

	return result.toString();
    }

    public boolean validate() {
	return !isNullOrEmpty(getUserName())
		&& !isNullOrEmpty(getFirstName())
		&& !isNullOrEmpty(getLastName())
		&& !isNullOrEmpty(getEmail())
		&& !isNullOrEmpty(getOrganization());
    }

    private static boolean isNullOrEmpty(String string) {
	if (string == null) {
	    return false;
	} else {
	    return string.isEmpty();
	}
    }
}
