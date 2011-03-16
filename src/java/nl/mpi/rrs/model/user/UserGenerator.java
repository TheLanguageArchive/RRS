/*
 * UserGenerator.java
 *
 * Created on February 8, 2007, 11:16 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package nl.mpi.rrs.model.user;

/**
 *
 * @author kees
 */
public interface UserGenerator {

    /**
     * @see mpi.rrs.model.user.UserGeneratorIF#isValidPasswordForUsername(java.lang.String, java.lang.String)
     */
    public boolean isValidPasswordForUsername(String userName, String passWord);

    /**
     * @see mpi.rrs.model.user.UserGeneratorIF#isValidUserName(java.lang.String)
     */
//   public boolean isValidUserName(String userName);
    /**
     * @see mpi.rrs.model.user.UserGeneratorIF#getUserInfoByUserName(java.lang.String)
     */
    public User getUserInfoByUserName(String userName);

    /**
     * provides a simple string describing this object
     * @return
     */
    public String getInfo();

    boolean isExistingUserName(String userName);
    boolean addNewUser(User userinfo);
}
