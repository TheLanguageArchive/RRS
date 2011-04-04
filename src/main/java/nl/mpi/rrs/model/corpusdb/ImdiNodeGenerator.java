/*
 * ImdiNodeGenerator.java
 *
 * Created on February 8, 2007, 4:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.mpi.rrs.model.corpusdb;

/**
 *
 * @author kees
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ImdiNodeGenerator {
    private final static Log logger = LogFactory.getLog(ImdiNodeGenerator.class); 
    
    private static Connection con;
    
    /** Creates a new instance of ImdiNodeGenerator */
    public ImdiNodeGenerator() {
    }
    
    public ImdiNodeGenerator(Connection con) {
        
        if (con != null) {
            try {
                con.setReadOnly(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
            this.con = con;
        }
        
    }
    
    public boolean isExistingNodeIdInDb(String nodeId) {
        String sql = "select count(*) from corpusnodes where nodeid = ?";
        
        PreparedStatement statement;
        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, nodeId);
            statement.execute();
            
            ResultSet result = statement.getResultSet();
            if (result.next()) {
                if (result.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            logger.error("ImdiNodeGenerator:isExistingNodeIdInDb:", ex);
            //ex.printStackTrace();
        }
        
        return false;
    }
    
    public String getNodeNameFromDb(String nodeId) {
        ImdiNode imdiNode = new ImdiNode();
        
        imdiNode.setImdiNodeIdAsInt(nodeId);
        int nodeIdAsInt = imdiNode.getImdiNodeIdAsInt();
        
        String sql = "select name from corpusnodes where nodeid = ?";
        
        PreparedStatement statement;
        try {
            statement = con.prepareStatement(sql);
            statement.setInt(1, nodeIdAsInt);
            statement.execute();
            
            ResultSet result = statement.getResultSet();
            if (result.next()) {
                imdiNode.setImdiNodeName(result.getString(1));
            }
            
        } catch (SQLException ex) {
            logger.error("ImdiNodeGenerator:getNodeNameFromDb:", ex);
            //ex.printStackTrace();
        }
        
        return imdiNode.getImdiNodeName();
        
    }
    
    public ImdiNode getNodeInfoFromDb(String nodeId) {
        ImdiNode imdiNode = new ImdiNode();
        
        imdiNode.setImdiNodeIdAsInt(nodeId);
        int nodeIdAsInt = imdiNode.getImdiNodeIdAsInt();
        
        String sql = "select nodetype, name, format, title from corpusnodes where nodeid = ?";
        
        PreparedStatement statement;
        try {
            statement = con.prepareStatement(sql);
            statement.setInt(1, nodeIdAsInt);
            statement.execute();
            
            ResultSet result = statement.getResultSet();
            if (result.next()) {
                imdiNode.setImdiNodeType(result.getInt(1));
                imdiNode.setImdiNodeName(result.getString(2));
                imdiNode.setImdiNodeFormat(result.getString(3));
                imdiNode.setImdiNodeUrl(result.getString(4));
            }
            
        } catch (SQLException ex) {
            logger.error("ImdiNodeGenerator:getNodeInfoFromDb:", ex);
            //ex.printStackTrace();
        }
        
        return imdiNode;
        
    }
}
