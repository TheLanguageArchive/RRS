/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.mpi.rrs.model.user;

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
    private static Log _log = LogFactory.getLog(RegistrationUser.class);
    private String userRecordAsString;
    private String userInfo;

    public String createUserRecordAsString(String newLine, String delim) {

        String result =
                this.removeDelim(this.getUserName(), delim) + delim
                + this.removeDelim(this.getFirstName(), delim) + delim
                + this.removeDelim(this.getLastName(), delim) + delim
                + this.removeDelim(this.getEmail(), delim) + delim
                + this.removeDelim(this.getOrganization(), delim) + delim
                + this.encodePassword(this.removeDelim(this.getPassword(), delim)) + delim
                + this.isDobesCocSigned() + delim
                + this.removeDelim(this.getCreation_ts(), delim) + newLine;

        return result;
    }

    public String removeDelim(String input, String delim) {
        if (input != null) {
            return input.replaceAll(delim, "");
        } else {
            return "";
        }
    }

    public String createUserInfo(String newLine) {
        String result = "User Id: " + this.getUserName() + newLine
                + "Password: " + this.getPassword() + newLine
                + "First Name: " + this.getFirstName() + newLine
                + "Last Name: " + this.getLastName() + newLine
                + "Organisation: " + this.getOrganization() + newLine
                + "Email: " + this.getEmail();

        return result;
    }

    public String getUserRecordAsString() {
        if (this.userRecordAsString == null) {
            this.setUserRecordAsString();
        }

        return userRecordAsString;
    }

    public void setUserRecordAsString() {
        String delim = "\t";
        String newLine = "\n";
        this.userRecordAsString = this.createUserRecordAsString(newLine, delim);
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

    public String decodePassword(String crypticPassword) {
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
                        _log.info("Invalid cryptic password: " + crypticPassword);
                        return null;
                    }

                }
            }
        }

        return result;
    }

    public String encodePassword(String password) {
        String result = "";

        if (password != null) {
            result = "#";
            for (int i = 0; i < password.length(); i++) {
                int charVal = password.charAt(i);
                charVal += 1000 + i;
                result += charVal;
                if (i != password.length() - 1) {
                    result += "-";
                }
            }
        }

        return result;
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
