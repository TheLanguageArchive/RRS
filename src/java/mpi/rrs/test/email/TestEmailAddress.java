/*
 * TestEmailAddress.java
 *
 * Created on March 30, 2007, 3:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mpi.rrs.test.email;

/**
 *
 * @author kees
 */
import javax.mail.internet.*;

public class TestEmailAddress {
    
    
    /** Creates a new instance of TestEmailAddress */
    public TestEmailAddress() {
    }
    
    public static void main(String args[]) {
        TestEmailAddress me = new TestEmailAddress();
        me.test();
    }
    
    public void test() {
        
        InternetAddress[] addresses = null;
        String userEmail = "1*dfjsjfkl fdjsjflksj";
        
        try {
            boolean strict = true;
            addresses = InternetAddress.parse(userEmail, strict);
            System.out.println("ok");
        } catch (AddressException ex) {
            System.out.println("error");
            
        }
        
        
    }
}
