/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.mpi.rrs.model;

import nl.mpi.rrs.model.user.RegistrationUser;

/**
 *
 * @author kees
 */
public class RrsRegistration {
    /** Creates a new instance of RrsRequest */
    public RrsRegistration() {
	this.setValid(true);
	this.regisId = -1;
    }
    private int regisId; // internal id for registrations only
    private boolean valid;
    private String baseUrl;
    private String checkEmailLink;
    private String emailAddressCheckContent;
    private String emailAccountDetailsContent;
    private RegistrationUser user;
    private String amsInterfaceLink;
    private String archiveUsersIdpName;
    private boolean federatedUser;

    public boolean isFederatedUser() {
	return federatedUser;
    }

    public void setFederatedUser(boolean federated) {
	this.federatedUser = federated;
    }

    public RegistrationUser getUser() {
	return user;
    }

    public void setUser(RegistrationUser user) {
	this.user = user;
    }

    public boolean isValid() {
	return valid;
    }

    public void setValid(boolean valid) {
	this.valid = valid;
    }

    public void setEmailAddressCheckContent() {
	String newLine = "\n";

	String result = "Dear " + user.getFullName() + "," + newLine + newLine;
	result += "you have registered for a LAT user account." + newLine;
	result += "Please click the following link to verify your email address: " + newLine;
	result += this.getCheckEmailLink() + newLine;
	result += newLine;
	result += "(it can take some seconds before the verification is completed)";
	this.emailAddressCheckContent = result;
    }

    public String getEmailAddressCheckContent() {
	if (this.emailAddressCheckContent == null) {
	    this.setEmailAddressCheckContent();
	}

	return this.emailAddressCheckContent;
    }

    public String getEmailAccountDetailsContent() {
	if (this.emailAccountDetailsContent == null) {
	    this.setEmailAccountDetailsContent();
	}
	return emailAccountDetailsContent;
    }

    public void setEmailAccountDetailsContent() {
	String newLine = "\n";

	String result = "Dear " + user.getFullName() + "," + newLine + newLine;
	result += "you have registered for a LAT user account." + newLine + newLine;
	result += "Your account details are: " + newLine;
	result += this.user.getUserInfo() + newLine + newLine;

	result += "To modify your account details, please go to the page below, log in and select " + newLine;
	result += "My Account -> My User Account" + newLine;
	result += "from the menu: " + newLine + newLine;
	result += this.getAmsInterfaceLink() + newLine;

	if (!isFederatedUser() && archiveUsersIdpName != null) {
	    result += newLine;
	    result += "NOTICE: If you do not have an account with any of the federated organizations, you" + newLine;
	    result += "should select the following \"identity provider\" when asked to do so after clicking" + newLine;
	    result += "'login' to use the above credentials:" + newLine + newLine;
	    result += archiveUsersIdpName + newLine + newLine;
	    result += "Otherwise, select your home organization" + newLine;
	}

	this.emailAccountDetailsContent = result;
    }

    public String getCheckEmailLink() {
	if (this.checkEmailLink == null) {
	    this.setCheckEmailLink();
	}

	return checkEmailLink;
    }

    public void setCheckEmailLink() {
	this.checkEmailLink = this.getBaseUrl() + "RrsRegisEmailCheck.do?RrsRegisUserName=" + this.getUser().getUserName() + "&RrsRegisId=" + this.getRegisId();
    }

    public int getRegisId() {
	if (this.regisId == -1) {
	    this.setRegisId();
	}
	return regisId;
    }

    /**
     * set registration id
     * just a number that can't be guessed easily
     */
    public void setRegisId() {
	String userName = this.getUser().getUserName();
	if (userName != null) {
	    this.regisId = 0;
	    for (int i = 0; i < userName.length(); i++) {
		if (i == 0) {
		    regisId += userName.codePointAt(i) * 64;
		} else {
		    regisId += userName.codePointAt(i);
		}
	    }
	}

    }

    public String getBaseUrl() {
	return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
	//this.baseUrl = baseUrl;
	this.baseUrl = baseUrl.replace("localhost", "172.16.24.160");
    }

    public String getAmsInterfaceLink() {
	return amsInterfaceLink;
    }

    public void setAmsInterfaceLink(String amsInterfaceLink) {
	this.amsInterfaceLink = amsInterfaceLink;
    }

    /**
     * Get the value of archiveUsersIdpName
     *
     * @return the value of archiveUsersIdpName
     */
    public String getArchiveUsersIdpName() {
	return archiveUsersIdpName;
    }

    /**
     * Set the value of archiveUsersIdpName
     *
     * @param archiveUsersIdpName new value of archiveUsersIdpName
     */
    public void setArchiveUsersIdpName(String archiveUsersIdpName) {
	this.archiveUsersIdpName = archiveUsersIdpName;
    }
}
