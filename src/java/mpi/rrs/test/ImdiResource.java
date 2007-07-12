/*
 * ImdiResource.java
 *
 * Created on February 7, 2007, 12:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mpi.rrs.test;

/**
 *
 * @author kees
 */
public class ImdiResource {
    
    /** Creates a new instance of ImdiResource */
    public ImdiResource() {
    }
    
    private String ImdiResourceName;
    private String ImdiResourceUrl;
    private boolean valid;
    
    public String getImdiResourceName() {
        return ImdiResourceName;
    }
    
    public void setImdiResourceName(String ImdiResourceName) {
        this.ImdiResourceName = ImdiResourceName;
    }
    
    public String getImdiResourceUrl() {
        return ImdiResourceUrl;
    }
    
    public void setImdiResourceUrl(String ImdiResourceUrl) {
        this.ImdiResourceUrl = ImdiResourceUrl;
    }
    
    private boolean check(String value){
        
        if(value == null || value.equals(""))
            return false;
        
        return true;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
}
