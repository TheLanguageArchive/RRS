/*
 * Error.java
 *
 * Created on February 28, 2007, 4:42 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mpi.rrs.model.errors;

/**
 *
 * @author kees
 */

import java.lang.Exception;


public class ErrorRequest {
    
    /** Creates a new instance of Error */
    public ErrorRequest() {
    }
    
    private String errorValue;
    private String errorException;
    private String errorMessage;
    private String errorClass;
    private String errorType;
    private String errorFormFieldLabel;
    private boolean errorRecoverable;

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorValue() {
        return errorValue;
    }

    public void setErrorValue(String errorValue) {
        this.errorValue = errorValue;
    }

    public String getErrorException() {
        return errorException;
    }

    public void setErrorException(String errorException) {
        this.errorException = errorException;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorClass() {
        return errorClass;
    }

    public void setErrorClass(String errorClass) {
        this.errorClass = errorClass;
    }

    public String getErrorFormFieldLabel() {
        return errorFormFieldLabel;
    }

    public void setErrorFormFieldLabel(String errorFormFieldLabel) {
        this.errorFormFieldLabel = errorFormFieldLabel;
    }

    public boolean isErrorRecoverable() {
        return errorRecoverable;
    }

    public void setErrorRecoverable(boolean errorRecoverable) {
        this.errorRecoverable = errorRecoverable;
    }

    
    
    
}
