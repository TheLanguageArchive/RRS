/*
 * Errors.java
 *
 * Created on February 28, 2007, 4:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package nl.mpi.rrs.model.errors;

/**
 *
 * @author kees
 */
import java.util.ArrayList;

public class ErrorsRequest {

    /** Creates a new instance of Errors */
    public ErrorsRequest() {
        errors = new ArrayList<ErrorRequest>();
        this.setErrorRecoverable(true);
        this.setErrorFromBrowser(false);
    }
    private ArrayList<ErrorRequest> errors;
    private String errorsHtmlTable;
    private boolean errorRecoverable;
    private boolean errorFromBrowser;

    public void addError(ErrorRequest error) {
        errors.add(error);
    }

    public ErrorRequest getError(int index) {
        return errors.get(index);
    }

    public int getSize() {
        return errors.size();
    }

    public ArrayList<ErrorRequest> getErrors(String errorType) {
        ArrayList<ErrorRequest> resultErrors = new ArrayList<ErrorRequest>();

        for (int i = 0; i < this.getSize(); i++) {
            if (this.getError(i).getErrorType().equalsIgnoreCase(errorType)) {
                resultErrors.add(this.getError(i));
            }
        }

        return resultErrors;
    }

    public String getErrorsHtmlTable() {
        return errorsHtmlTable;
    }

    public void setErrorsHtmlTable() {
        StringBuilder htmlTable = new StringBuilder();

        for (int i = 0; i < this.getSize(); i++) {
            htmlTable.append("<tr>\n");
            htmlTable.append("  <td>");
            htmlTable.append(this.getError(i).getErrorFormFieldLabel());
            htmlTable.append("  </td>\n");
            htmlTable.append("  <td>");
            htmlTable.append(this.getError(i).getErrorMessage());
            htmlTable.append("  </td>\n");
            htmlTable.append("  <td>");
            htmlTable.append(this.getError(i).getErrorValue());
            htmlTable.append("  </td>\n");
            htmlTable.append("</tr>\n");
        }

        this.errorsHtmlTable = htmlTable.toString();
    }

    public boolean isErrorRecoverable() {
        return errorRecoverable;
    }

    public void setErrorRecoverable(boolean errorRecoverable) {
        this.errorRecoverable = errorRecoverable;
    }

    public boolean isErrorFromBrowser() {
        return errorFromBrowser;
    }

    public void setErrorFromBrowser(boolean errorFromBrowser) {
        this.errorFromBrowser = errorFromBrowser;
    }
}
