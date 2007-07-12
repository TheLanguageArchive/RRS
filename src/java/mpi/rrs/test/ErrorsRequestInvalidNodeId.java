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

public class ErrorsRequestInvalidNodeId extends ErrorsRequest {
    
    /** Creates a new instance of ErrorsRequestInvalidNodeId */
    public ErrorsRequestInvalidNodeId() {
        errorType = "INVALID_NODE_ID";
    }
    
    private String errorsHtmlTable;
    private String errorType;
    private ArrayList<ErrorRequest> errorsOfThisClass;
    private int errorsOfThisClassCount;
    
    
    public String getErrorsHtmlTable() {
        return errorsHtmlTable;
    }
    
    public void setErrorsHtmlTable() {
        errorsOfThisClass = getErrors(errorType);
        int size = errorsOfThisClass.size();
        
        if (size > 0) {
            
            String htmlTable = "<br><table border=1>";
            htmlTable += "<tr>";
            htmlTable += "  <th>Type</th>";
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
            
            htmlTable += "</table><br>";
            this.errorsHtmlTable = htmlTable;
            
        } else {
            this.errorsHtmlTable = null;
        }
    }
    
    public int getErrorsOfThisClassCount() {
        return errorsOfThisClassCount;
    }
    
    public void setErrorsOfThisClassCount() {
        this.errorsOfThisClassCount = errorsOfThisClass.size();
    }
    
    
    
}
