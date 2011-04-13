/*
 * RrsRequest.java
 *
 * Created on February 7, 2007, 2:31 PM
 */

package nl.mpi.rrs.model;

/**
 *
 * @author kees
 */

import nl.mpi.rrs.model.corpusdb.ImdiNodes;
import nl.mpi.rrs.model.date.RrsDate;
import nl.mpi.rrs.model.user.RequestUser;
import nl.mpi.rrs.model.user.User;

public class RrsRequest {
    
    /** Creates a new instance of RrsRequest */
    public RrsRequest() {
        this.setValid(true);
    }
    
    private boolean valid;
    
    private RequestUser user;
    private String userStatus;

    private String identityProviderId;
    
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
        if (this.getUser().isValid() && this.valid ) {
            return true;
        } else
            return false;
    }
    
    public final void setValid(boolean valid) {
        this.valid = valid;
    }
    
    @Override
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

        StringBuilder result = new StringBuilder();

        //String result = "";
        result.append("User info:").append(newLine);
        result.append("==========").append(newLine);
        result.append(userInfo).append(newLine);
        result.append("Identified by: ").append(this.getIdentityProviderId()).append(newLine);
        result.append(newLine);
        result.append("Nodes requested:").append(newLine);
        result.append("================").append(newLine);
        result.append(imdiNodesInfo);
        result.append(newLine);
        result.append("Usage aim:").append(newLine);
        result.append("==========").append(newLine);
        result.append("ResearchProject: ").append(this.getResearchProject()).append(newLine);
        result.append("PublicationAim: ").append(this.getPublicationAim()).append(newLine);
        result.append("RemarksOther: ").append(this.getRemarksOther()).append(newLine);
        result.append(newLine);
        result.append("Period for use:").append(newLine);
        result.append("===============").append(newLine);
        result.append("From: ").append(this.getFromDate().getValue()).append(newLine);
        result.append("To  : ").append(this.getToDate().getValue()).append(newLine);
        result.append(newLine);
        
        this.emailContent = result.toString();
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

    /**
     * @return the identityProviderId
     */
    public String getIdentityProviderId() {
        return identityProviderId;
    }

    /**
     * @param identityProviderId the identityProviderId to set
     */
    public void setIdentityProviderId(String identityProviderId) {
        this.identityProviderId = identityProviderId;
    }
}
