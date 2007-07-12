/*
 * ImdiResources.java
 *
 * Created on February 16, 2007, 4:15 PM
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
import mpi.rrs.model.*;

public class ImdiResources {
    
    /** Creates a new instance of ImdiResources */
    public ImdiResources() {
        imdiResources = new ArrayList<ImdiResource>();
    }
    
    private ArrayList<ImdiResource> imdiResources;
    private int size;
    private String htmlTable;
    private boolean valid;
    
    public void addImdiResource(ImdiResource imdiResource) {
        imdiResources.add(imdiResource);
    }
    
    public int getSize() {
        return imdiResources.size();
    }
    
    public ImdiResource getImdiResource(int index) {
        if (index < imdiResources.size()) {
            return imdiResources.get(index);
        } else {
            return null;
        }
    }
    
    public ArrayList<ImdiResource> getImdiResources() {
        return imdiResources;
    }
    
    public void setImdiResources(ArrayList<ImdiResource> imdiResources) {
        this.imdiResources = imdiResources;
    }
    
    public boolean isValid() {
        
        return valid;
    }
    
    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
    
    public String getHtmlTable() {
        
        //htmlTable = "<table border=1>";
        htmlTable = "";
        htmlTable += "<tr>";
        htmlTable += "  <th>Name</th>";
        htmlTable += "  <th>Url </th>";
        htmlTable += "</tr>";
        for (int i=0; i< this.getSize(); i++) {
            htmlTable += "<tr>";
            htmlTable += "  <td>";
            htmlTable +=      this.getImdiResource(i).getImdiResourceName();
            htmlTable += "  </td>";
            htmlTable += "  <td>";
            htmlTable +=      this.getImdiResource(i).getImdiResourceUrl();
            htmlTable += "  </td>";
            htmlTable += "</tr>";
            
        }
        //htmlTable += "</table>";
        
        return htmlTable;
        
        
        
        
    }
    
    public String toHtmlString() {
        String newLine = "<br>";
        
        String result = "Resource0: " + this.getImdiResource(0).getImdiResourceName() + newLine +
                "Resource1: " + this.getImdiResource(1).getImdiResourceName() + newLine;
        return result;
        
    }
    
    public String toString() {
        String newLine = "<br>";
        
        String result = "Resource0: " + this.getImdiResource(0).getImdiResourceName() + newLine +
                "Resource1: " + this.getImdiResource(1).getImdiResourceName() + newLine;
        return result;
        
    }
    
}
