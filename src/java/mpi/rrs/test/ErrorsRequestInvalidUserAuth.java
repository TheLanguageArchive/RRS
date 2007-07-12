/*
 * ErrorsRequestInvalidNodeId.java
 *
 * Created on March 1, 2007, 3:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mpi.rrs.test;

/**
 *
 * @author kees
 */
import java.util.ArrayList;
import mpi.rrs.model.errors.*;

public class ErrorsRequestInvalidUserAuth extends ErrorsRequest {
    
    /** Creates a new instance of ErrorsRequestInvalidNodeId */
    public ErrorsRequestInvalidUserAuth() {
        errorType = "INVALID_USER_AUTH";
    }
    
    private String errorsHtmlTable;
    private String errorType;
    private ArrayList<ErrorRequest> errorsOfThisClass;
    
    
    
    public String getErrorsHtmlTable() {
        return errorsHtmlTable;
    }
    
    public void setErrorsHtmlTable() {
        errorsOfThisClass = getErrors(errorType);
        int size = errorsOfThisClass.size();
        if (size > 0) {
            
            String htmlTable = "<table border=1>";
            htmlTable += "<tr>";
            htmlTable += "  <th>Id</th>";
            htmlTable += "  <th>Error</th>";
            htmlTable += "</tr>";
            for (int i=0; i< size; i++) {
                htmlTable += "<tr>";
                htmlTable += "  <td>";
                htmlTable +=      this.getError(i).getErrorType();
                htmlTable += "  </td>";
                htmlTable += "  <td>";
                htmlTable +=      this.getError(i).getErrorMessage();
                htmlTable += "  </td>";
                htmlTable += "</tr>";
            }
            
            htmlTable += "</table>";
            this.errorsHtmlTable = htmlTable;
            
        } else {
            this.errorsHtmlTable = null;
        }
    }
    
    
    
}
