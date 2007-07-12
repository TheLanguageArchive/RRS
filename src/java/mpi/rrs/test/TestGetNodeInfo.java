/*
 * TestGetNodeInfo.java
 *
 * Created on January 4, 2007, 4:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mpi.rrs.test;

/**
 *
 * @author kees
 */
import mpi.rrs.test.CorpusNodes;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.BasicConfigurator;

import mpi.corpusstructure.NodeIdUtils;

public class TestGetNodeInfo {
    static Logger logger = Logger.getRootLogger();
    
    CorpusNodes cn;
    
    /** Creates a new instance of TestGetNodeInfo */
    public TestGetNodeInfo() {
        BasicConfigurator.configure();
        logger.setLevel(Level.DEBUG);
        
        cn = new CorpusNodes();
        int nodeIdInt = 363998;
        //String nodeId = "MPI363998#";
        String nodeId = NodeIdUtils.TONODEID(nodeIdInt);
        
        System.out.println("TestGetNodeInfo");
        System.out.println("nodeId: " + nodeId);
        System.out.println("nodeName: " + cn.getNodeName(nodeId));
    }
    
    public static void main(String args[]) {
        TestGetNodeInfo me = new TestGetNodeInfo();
        me.GetNodeInfo("MPI123456#");
        
    }
    
    public String GetNodeInfo(String nodeId) {
        BasicConfigurator.configure();
        logger.setLevel(Level.DEBUG);
        
        if (NodeIdUtils.isNodeId(nodeId)) {
            
            cn = new CorpusNodes();
            
            //int nodeIdInt = 363998;
            //String nodeId = "MPI363998#";
            //String nodeId = NodeIdUtils.TONODEID(nodeIdInt);
            String nodeName = cn.getNodeName(nodeId);
            
            
            logger.debug("TestGetNodeInfo");
            logger.debug("nodeId: " + nodeId);
            logger.debug("nodeName: " + nodeName);
            
            logger.debug("getCanonicalVPath(nodeId): " + cn.csdb.getCanonicalVPath(nodeId));
            logger.debug("getNameFromId(nodeId): " + cn.csdb.getNameFromId(nodeId));
            logger.debug("getObjectChecksum(nodeId): " + cn.csdb.getObjectChecksum(nodeId));
            logger.debug("getObjectURI(nodeId): " + cn.csdb.getObjectURI(nodeId).toString());
            
            
            logger.debug("end");
            
            return nodeName;
            
        } else {
            logger.debug("invalid nodeid: " + nodeId);
            return null;
        }
    }
    
}
