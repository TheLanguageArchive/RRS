/*
 * TestRegexp.java
 *
 * Created on February 21, 2007, 3:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mpi.rrs.test;

/**
 *
 * @author kees
 */
import java.util.regex.*;
import java.util.*;

public class TestRegexp {
    
    /** Creates a new instance of TestRegexp */
    public TestRegexp() {
    }
    
    public static void main(String args[]) {
        TestRegexp me = new TestRegexp();
        me.testRegexp();
    }
    
    public void testRegexp() {
        
        String[] arResource = new String[10];
        
        arResource[0] = null;
        arResource[1] = "paramResourceName_1";
        arResource[2] = "paramResourceName_2";
        
        String[] arNode = new String[10];
        
        arNode[0] = null;
        arNode[1] = "paramNodeName_1";
        arNode[2] = "paramNodeName_2";
        
        String patternResource = "paramResourceName\\_([0-9]+)$";
        Pattern rResource = Pattern.compile(patternResource);
        
        String patternNode = "paramNodeName\\_([0-9]+)$";
        Pattern rNode = Pattern.compile(patternNode);
        
        for (int i=1; i<=3; i++) {
            String test = "paramResourceName_" + i;
            
            Matcher m = rResource.matcher(test);
            if (m.find()) {
                System.out.println("test: " + test + " matches " + patternResource);
                System.out.println("groups: " + m.groupCount());
                System.out.println("matched: " + m.group(1));
                
               
                
            } else {
                System.out.println("test: " + test + " matches not " + patternResource);
            }
        }
        
    }
}
