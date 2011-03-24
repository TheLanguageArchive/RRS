/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.mpi.rrs.model.user;

import nl.mpi.rrs.model.utilities.RrsUtil;

/**
 *
 * @author kees
 */
public class RequestUser extends User {
    public RequestUser() {
        super();
    }
    
    private String userInfo;
    private String htmlUserInfo;
    
    public String toHtmlString() {
        this.setHtmlUserInfo();
        return this.getHtmlUserInfo();
    }

    public String getUserInfo() {
        if (RrsUtil.isEmpty(userInfo)) {
            this.setUserInfo();
        }

        return userInfo;
    }

    public void setUserInfo() {
        String newLine = "\n";
        this.userInfo = this.createUserInfo(newLine);
    }

    public String getHtmlUserInfo() {
        return htmlUserInfo;
    }

    public void setHtmlUserInfo() {
        String newLine = "<br>\n";
        this.htmlUserInfo = this.createUserInfo(newLine);
    }

    public String createUserInfo(String newLine) {
        String result = "User: " + this.getFullName() + newLine +
                "Username: " + this.getUserName() + newLine +
                "Organisation: " + this.getOrganization() + newLine +
                "Email: " + this.getEmail();

        return result;
    }


}
