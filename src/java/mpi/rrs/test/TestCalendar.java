/*
 * TestCalendar.java
 *
 * Created on March 12, 2007, 2:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mpi.rrs.test;

/**
 *
 * @author kees
 */

import java.util.Calendar;

public class TestCalendar {
    
    /** Creates a new instance of TestCalendar */
    public TestCalendar() {
    }
    
    public static void main(String args[]) {
        TestCalendar me = new TestCalendar();
        me.test(9,9,2007);
        me.test(10,9,2007);
    }
    
    public void test(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        
        cal.setLenient(false);
        try {
            
            
            cal.set(year,month-1,day);
            System.out.println(year + ", " + month + " " + day + " : " + cal.getTime());
            //cal.add(cal.DATE,3);
            //System.out.println("add 3 days:");
            System.out.println(year + ", " + month + " " + day + " : " + cal.getTime());
            System.out.println(year + ", " + month + " " + day + " : " + cal.getTimeInMillis());
            
            //System.out.println("Now: " + cal.getTime());
            
            int year2 = cal.get(cal.YEAR);
            int month2 = cal.get(cal.MONTH) + 1;
            int day2 = cal.get(cal.DAY_OF_MONTH);
            
            System.out.println("date: " + day2 + " " + month2 + " " + year2);
            
            
            System.out.println("cal.get(1)" + cal.get(cal.DATE));
            System.out.println("cal.get(2)" + (cal.get(cal.MONTH) +1)) ;
            System.out.println("cal.get(3)" + cal.get(cal.YEAR));

            System.out.println("cal.APRIL: " + cal.APRIL);
            
            System.out.println("cal.DATE: " + cal.get(cal.DATE));
            
            System.out.println("cal.DAY_OF_MONTH: " + cal.get(cal.DAY_OF_MONTH));
            System.out.println("cal.DAY_OF_WEEK_IN_MONT: " + cal.get(cal.DAY_OF_WEEK_IN_MONTH));
            System.out.println("cal.DAY_OF_YEAR:" + cal.get(cal.DAY_OF_YEAR));
            
            System.out.println("cal.SUNDAY: " + cal.SUNDAY);
            System.out.println("cal.FRIDAY: " + cal.FRIDAY);
            System.out.println("cal.SATURDAY: " + cal.SATURDAY);
            
        } catch (IllegalArgumentException ex) {
            System.out.println("illegal date: " + year + ", " + month + " " + day);
            //ex.printStackTrace();
        } finally {
        }
        
        
    }
}
