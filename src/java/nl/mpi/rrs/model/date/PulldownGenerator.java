/*
 * PulldownGenerator.java
 *
 * Created on March 26, 2007, 11:44 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.mpi.rrs.model.date;

/**
 *
 * @author kees
 */
import java.util.Calendar;

public class PulldownGenerator {
    private int yearRange = 10;
    Calendar cal;
    
    //private int monthFormat = SHORT;

    /** Creates a new instance of PulldownGenerator */
    public PulldownGenerator() {
        cal = Calendar.getInstance();
    }

    public PulldownGenerator(Calendar cal) {
        this.cal = cal;
    }


    public static void main(String[] args) {
        PulldownGenerator me = new PulldownGenerator();
        me.test();

    }

    public void test() {
        //this.setMonthFormat(cal.LONG);

        System.out.println(this.pulldownDayOfMonth("myDay"));
        System.out.println(this.pulldownMonthOfYear("myMonth"));
        System.out.println(this.pulldownYear("myYear",2005,2010));

        cal.set(cal.YEAR,cal.get(cal.YEAR) + 1);

        System.out.println(this.pulldownDayOfMonth("myDay"));
        System.out.println(this.pulldownMonthOfYear("myMonth"));
        System.out.println(this.pulldownYear("myYear",2005,2010));

    }

    public String pulldownDayOfMonth(String name) {
        int dayOfMonth = cal.get(cal.DAY_OF_MONTH);

        String dayPullDown = "<select name=\"" + name + "\">" + "\n";

        for (int i=1; i<=31; i++) {
            String option = "<option>";
            if (i == dayOfMonth) {
                option = "<option selected=\"selected\">";
            }

            dayPullDown += option + i + "</option>" + "\n";

        }

        dayPullDown += "</select>" + "\n";

        return dayPullDown;
    }

    public String pulldownMonthOfYear(String name) {
        int monthOfYear = cal.get(cal.MONTH);
        
        String[] shortMonths = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        
        /*
        String monthOfYearShortString = cal.getDisplayName(cal.MONTH,monthFormat,Locale.ENGLISH);

        DateFormatSymbols d = DateFormatSymbols.getInstance(Locale.ENGLISH);
        if (this.monthFormat == cal.SHORT) {
            shortMonths = d.getShortMonths();
        } else {
            shortMonths = d.getMonths();
        }
         */

        String monthPullDown = "<select name=\"" + name + "\">" + "\n";

        for (int i=0; i<=11; i++) {
            String option = "<option>";
            if (i == monthOfYear) {
                option = "<option selected=\"selected\">";
            }

            monthPullDown += option + shortMonths[i] + "</option>" + "\n";
        }

        monthPullDown += "</select>" + "\n";

        return monthPullDown;
    }

    public String pulldownYear(String name) {
        int fromYear = -1;
        int toYear = -1;
        return this.pulldownYear(name, fromYear, toYear);
    }

    public String pulldownYear(String name, int fromYear, int toYear) {
        int year = cal.get(cal.YEAR);
        if (fromYear == -1) {
            fromYear = year;
        }

        if (toYear == -1) {
            toYear = fromYear + yearRange;
        }


        String yearPullDown = "<select name=\"" + name + "\">" + "\n";

        for (int i=fromYear; i<=toYear; i++) {
            String option = "<option>";
            if (i == year) {
                option = "<option selected=\"selected\">";
            }

            yearPullDown += option + i + "</option>" + "\n";

        }

        yearPullDown += "</select>" + "\n";

        return yearPullDown;
    }

    /*
    public int getMonthFormat() {
        return monthFormat;
    }

    public void setMonthFormat(int monthFormat) {
        if (monthFormat == cal.SHORT || monthFormat == cal.LONG) {        
            this.monthFormat = monthFormat;
        }
    }
    */

    public int getYearRange() {
        return yearRange;
    }

    public void setYearRange(int yearRange) {
        this.yearRange = yearRange;
    }
}
