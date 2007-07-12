/*
 * ImdiNodes.java
 *
 * Created on February 23, 2007, 3:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mpi.rrs.model.corpusdb;

/**
 *
 * @author kees
 */

import java.util.ArrayList;
import mpi.rrs.model.*;
import mpi.rrs.model.utilities.RrsUtil;

public class ImdiNodes {
    
    /** Creates a new instance of ImdiNodes */
    public ImdiNodes() {
        imdiNodes = new ArrayList<ImdiNode>();
    }
    
    private ArrayList<ImdiNode> imdiNodes;
    private boolean valid;
    private int size;
    private String htmlTable;
    private String imdiNodesInfo;
    
    
    public void addImdiNode(ImdiNode imdiNode) {
        imdiNodes.add(imdiNode);
    }
    
    public ArrayList<ImdiNode> getImdiNodes() {
        return imdiNodes;
    }
    
    public void setImdiNodes(ArrayList<ImdiNode> imdiNode) {
        this.imdiNodes = imdiNode;
    }
    
    public boolean isValid() {
        
        return valid;
    }
    
    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
    public int getSize() {
        return imdiNodes.size();
    }
    
    public ImdiNode getImdiNode(int index) {
        if (index < imdiNodes.size()) {
            return imdiNodes.get(index);
        } else {
            return null;
        }
    }
    
    public void setImdiNode(int index, ImdiNode imdiNode) {
        this.imdiNodes.remove(index);
        this.imdiNodes.add(index, imdiNode);
    }
    
    
    public String getHtmlTable() {
        
        //htmlTable = "<table border=1>";
        htmlTable = "";
        htmlTable += "<tr>";
        htmlTable += "  <th>Id</th>";
        htmlTable += "  <th>Name </th>";
        htmlTable += "</tr>";
        for (int i=0; i< this.getSize(); i++) {
            htmlTable += "<tr>";
            htmlTable += "  <td>";
            htmlTable +=      this.getImdiNode(i).getImdiNodeIdAsInt();
            htmlTable += "  </td>";
            htmlTable += "  <td>";
            htmlTable +=      this.getImdiNode(i).getImdiNodeName();
            htmlTable += "  </td>";
            htmlTable += "</tr>";
            
        }
        //htmlTable += "</table>";
        
        return htmlTable;        
    }
    
    public String toHtmlString() {
        String newLine = "<br>";
        
        String result = "Node0: " + this.getImdiNode(0).getImdiNodeName() + newLine +
                "Node1: " + this.getImdiNode(1).getImdiNodeName() + newLine;
        return result;
        
    }
    
    public String toString() {
        return this.getImdiNodesInfo();
    }

    public String getImdiNodesInfo() {
        if (RrsUtil.isEmpty(imdiNodesInfo)) {
            this.setImdiNodesInfo();
        }
        return imdiNodesInfo;
    }

    public void setImdiNodesInfo() {
        String newLine = "\n";
        String result = "";
        
        for (int i=0; i< this.getSize(); i++) {
            String nodeId = this.getImdiNode(i).getImdiNodeIdWithPrefix();
            //String nodeName = this.getImdiNode(i).getImdiNodeName();
            String nodeUrl = this.getImdiNode(i).getImdiNodeUrl();
            String nodeUri = this.getImdiNode(i).getImdiNodeUri();
            
            String nodeIdHtml = nodeId.substring(0,nodeId.length()-1) + "%23"; // substitute # with %23
            String nodeOpenPath = this.getImdiNode(i).getOpenPathPrefix() + nodeIdHtml;
            
            result += "ID : " + nodeId  + newLine;
            result += "URL: " + nodeUrl + newLine;
            result += "URI: " + nodeUri + newLine;
            result += "OP : " + nodeOpenPath + newLine + newLine;
        }
        
        this.imdiNodesInfo = result;
    }
    
}
