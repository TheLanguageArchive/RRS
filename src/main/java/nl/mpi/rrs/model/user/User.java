/*
 * User.java
 *
 * Created on February 6, 2007, 11:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package nl.mpi.rrs.model.user;

/**
 *
 * @author kees
 */
import nl.mpi.rrs.model.utilities.RrsUtil;

public class User {

    private String userName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String address;
    private String phone;
    private String fax;
    private String affiliation;
    private String organization;
    private String status;
    private String email;
    private String password;
    private String creator;
    private String creation_ts;
    private String hostingInstitute;
    private boolean dobesCocSigned;
    private boolean valid;

    /** Creates a new instance of User */
    public User() {
        this.setValid(true);
        this.setDobesCocSigned(false);
        this.setCreation_ts();

    }

    public String getUserName() {
        if (RrsUtil.isNotEmpty(userName)) {
            return userName;
        } else {
            return "";
        }
    }

    public void setUserName(String userName) {
        if (RrsUtil.isNotEmpty(userName)) {
            this.userName = userName.toLowerCase();
        } else {
            this.userName = null;
        }
    }

    public String getFirstName() {
        if (RrsUtil.isNotEmpty(firstName)) {
            return firstName;
        } else {
            return "";
        }
    }

    public void setFirstName(String firstName) {
        if (RrsUtil.isNotEmpty(firstName)) {
            String fields[] = firstName.split("\\s+");
            
            StringBuilder firstNameBuilder = new StringBuilder();
            for (int i = 0; i < fields.length; i++) {
                firstNameBuilder.append(toInitCase(fields[i]));
                if (i != fields.length - 1) {
                    firstNameBuilder.append(" ");
                }
            }

            this.firstName = firstNameBuilder.toString();

        } else {
            this.firstName = null;
        }
    }

    public String getMiddleName() {
        if (RrsUtil.isNotEmpty(middleName)) {
            return middleName;
        } else {
            return "";
        }
    }

    public void setMiddleName(String middleName) {
        if (RrsUtil.isNotEmpty(middleName)) {
            this.middleName = middleName.toLowerCase();

        } else {
            this.middleName = null;
        }
    }

    public String getLastName() {
        if (RrsUtil.isNotEmpty(lastName)) {
            return lastName;
        } else {
            return "";
        }
    }

    public String getFullName() {
        if (RrsUtil.isNotEmpty(this.getMiddleName())) {
            return this.getFirstName() + " " + this.getMiddleName() + " " + this.getLastName();
        } else {
            return this.getFirstName() + " " + this.getLastName();
        }

    }

    public void setLastName(String lastName) {
        /*
        if (RrsUtil.isNotEmpty(lastName)) {
        String fields[] = lastName.split("\\s+");
        lastName = "";
        for (int i = 0; i < fields.length; i++) {
        if (i == 0) {
        lastName += toInitCase(fields[i]);
        } else {
        lastName += fields[i];
        }
        if (i != fields.length - 1) {
        lastName += " ";
        }
        }
        this.lastName = lastName;
        } else {
        this.lastName = null;
        }
         */

        this.lastName = lastName;
    }

    public String getAddress() {
        if (RrsUtil.isNotEmpty(address)) {
            return address;
        } else {
            return "";
        }
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        if (RrsUtil.isNotEmpty(phone)) {
            return phone;
        } else {
            return "";
        }
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        if (RrsUtil.isNotEmpty(fax)) {
            return fax;
        } else {
            return "";
        }
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAffiliation() {
        if (RrsUtil.isNotEmpty(affiliation)) {
            return affiliation;
        } else {
            return "";
        }
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getStatus() {
        if (RrsUtil.isNotEmpty(status)) {
            return status;
        } else {
            return "";
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        if (RrsUtil.isNotEmpty(email)) {
            return email;
        } else {
            return "";
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreation_ts() {
        return this.creation_ts;
    }

    public void setCreation_ts(String creation_ts) {
        this.creation_ts = creation_ts;
    }

    public final void setCreation_ts() {
        this.creation_ts = RrsUtil.todayDateAsString();
    }

    public boolean isValid() {
        if (this.valid && RrsUtil.isNotEmpty(getUserName())) {
            return valid;
        }

        return false;
    }

    public final void setValid(boolean valid) {
        this.valid = valid;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public String toInitCase(String name) {
        if (RrsUtil.isNotEmpty(name)) {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }

        return name;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public boolean isDobesCocSigned() {
        return dobesCocSigned;
    }

    public final void setDobesCocSigned(boolean dobesCocSigned) {
        this.dobesCocSigned = dobesCocSigned;
    }

    public String getHostingInstitute() {
	return hostingInstitute;
    }

    public void setHostingInstitute(String hostingInstitution) {
	this.hostingInstitute = hostingInstitution;
    }
}
