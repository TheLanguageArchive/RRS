/*
 * TestImdiNode.java
 *
 * Created on February 28, 2007, 11:47 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mpi.rrs.test;

/**
 *
 * @author kees
 */
import mpi.rrs.model.corpusdb.*;
import mpi.corpusstructure.*;

public class TestImdiNode {
    
    /** Creates a new instance of TestImdiNode */
    public TestImdiNode() {
        CorpusNodes.setDBCredentials();
    }
    
    public static void main(String args[]) {
        TestImdiNode me = new TestImdiNode();
        me.test("281591");
    }
    
    public void test(String nodeId) {
        ImdiNodes imdiNodes = new ImdiNodes();
        ImdiNode imdiNode = new ImdiNode();
        imdiNode.setImdiNodeIdWithPrefix(nodeId);
        imdiNode.setImdiNodeName(CorpusNodes.csdb.getNode(imdiNode.getImdiNodeIdWithPrefix()).getName());
        
        imdiNodes.addImdiNode(imdiNode);
        
        System.out.println("imdiNode added: " + nodeId);
        System.out.println("imdiNode added prefix: " + imdiNode.getImdiNodeIdWithPrefix());
        System.out.println("imdiNodeName: " + imdiNode.getImdiNodeName());
        
        
    }
    
}
