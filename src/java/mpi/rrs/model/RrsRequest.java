/*
 * RrsRequest.java
 *
 * Created on February 7, 2007, 2:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mpi.rrs.model;

/**
 *
 * @author kees
 */

import mpi.rrs.model.corpusdb.ImdiNodes;
import mpi.rrs.model.date.RrsDate;
import mpi.rrs.model.user.RequestUser;
import mpi.rrs.model.user.User;

public class RrsRequest {
    
    /** Creates a new instance of RrsRequest */
    public RrsRequest() {
        this.setValid(true);
    }
    
    private boolean valid;
    
    private RequestUser user;
    private String userStatus;
    
    private ImdiNodes imdiNodes;
    private boolean nodesEnteredInForm;
    
    private RrsDate fromDate;
    private RrsDate toDate;
    
    private String fromDateString;
    private String toDateString;
    
    private String researchProject;
    private String publicationAim;
    private String remarksOther;
    private String emailContent;
    
    public User getUser() {
        return user;
    }
    
    public void setUser(RequestUser user) {
        this.user = user;
    }
    
    public ImdiNodes getImdiNodes() {
        return imdiNodes;
    }
    
    public void setImdiNodes(ImdiNodes imdiNodes) {
        this.imdiNodes = imdiNodes;
    }
    
    public RrsDate getFromDate() {
        return fromDate;
    }
    
    public void setFromDate(RrsDate fromDate) {
        this.fromDate = fromDate;
    }
    
    public RrsDate getToDate() {
        return toDate;
    }
    
    public void setToDate(RrsDate toDate) {
        this.toDate = toDate;
    }
    
    public String getResearchProject() {
        return researchProject;
    }
    
    public void setResearchProject(String researchProject) {
        this.researchProject = researchProject;
    }
    
    public String getPublicationAim() {
        return publicationAim;
    }
    
    public void setPublicationAim(String publicationAim) {
        this.publicationAim = publicationAim;
    }
    
    public String getRemarksOther() {
        return remarksOther;
    }
    
    public void setRemarksOther(String remarksOther) {
        this.remarksOther = remarksOther;
    }
    
    public boolean isValid() {
        if (this.getUser().isValid() && this.isValid() ) {
            return true;
        } else
            return false;
    }
    
    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
    public String toString() {
        String newLine = "\n";
        
        String result = "User: " + this.getUser() + newLine +
                "Date From: " + this.getFromDate().getValue() + newLine +
                "Date To: " + this.getToDate().getValue() + newLine
                ;
        return result;
        
    }
    
    
    
    public boolean isNodesEnteredInForm() {
        return nodesEnteredInForm;
    }
    
    public void setNodesEnteredInForm(boolean nodesEnteredInForm) {
        this.nodesEnteredInForm = nodesEnteredInForm;
    }
    
    public String getUserStatus() {
        return userStatus;
    }
    
    public void setUserStatus(String userStatus) {
        
        this.userStatus = userStatus;
    }
    
    public void setEmailContent() {
        String newLine = "\n";
        
        String userInfo = this.user.getUserInfo();
        
        this.imdiNodes.setImdiNodesInfo();
        String imdiNodesInfo = this.imdiNodes.getImdiNodesInfo();
        
        String result = "";
        result += "User info:" + newLine;
        result += "==========" + newLine;
        result += userInfo + newLine;
        result += newLine;
        result += "Nodes requested:" + newLine;
        result += "================" + newLine;
        result += imdiNodesInfo;
        result += newLine;
        result += "Usage aim:" + newLine;
        result += "==========" + newLine;
        result += "ResearchProject: " + this.getResearchProject() + newLine;
        result += "PublicationAim: " + this.getPublicationAim() + newLine;
        result += "RemarksOther: " + this.getRemarksOther() + newLine;
        result += newLine;
        result += "Period for use:" + newLine;
        result += "===============" + newLine;
        result += "From: " + this.getFromDate().getValue() + newLine;
        result += "To  : " + this.getToDate().getValue() + newLine;
        result += newLine;
        
        this.emailContent = result;
    }
    
    public String getEmailContent() {
        
        return emailContent;
    }

    public String getFromDateString() {
        return fromDateString;
    }

    public void setFromDateString(RrsDate fromDate) {
        this.fromDateString = fromDate.getValue();
    }

    public String getToDateString() {
        return toDateString;
    }

    public void setToDateString(RrsDate toDate) {
        this.toDateString = toDate.getValue();
    }
}
