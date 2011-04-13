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
    private final Calendar cal;

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
        System.out.println(this.pulldownDayOfMonth("myDay"));
        System.out.println(this.pulldownMonthOfYear("myMonth"));
        System.out.println(this.pulldownYear("myYear", 2005, 2010));

        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);

        System.out.println(this.pulldownDayOfMonth("myDay"));
        System.out.println(this.pulldownMonthOfYear("myMonth"));
        System.out.println(this.pulldownYear("myYear", 2005, 2010));
    }

    public String pulldownDayOfMonth(String name) {
        final int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        StringBuilder dayPullDown = new StringBuilder("<select name=\"" + name + "\">" + "\n");

        for (int i = 1; i <= cal.getMaximum(Calendar.DAY_OF_MONTH); i++) {
            String option = (i == dayOfMonth) ? "<option selected=\"selected\">" : "<option>";
            dayPullDown.append(option).append(i).append("</option>" + "\n");
        }
        dayPullDown.append("</select>" + "\n");

        return dayPullDown.toString();
    }

    public String pulldownMonthOfYear(String name) {
        final int monthOfYear = cal.get(Calendar.MONTH);
        final String[] shortMonths = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        StringBuilder monthPullDown = new StringBuilder("<select name=\"" + name + "\">" + "\n");

        for (int i = 0; i <= shortMonths.length - 1; i++) {
            String option = (i == monthOfYear) ? "<option selected=\"selected\">" : "<option>";
            monthPullDown.append(option).append(shortMonths[i]).append("</option>" + "\n");
        }

        monthPullDown.append("</select>\n");

        return monthPullDown.toString();
    }

    public String pulldownYear(String name) {
        int fromYear = -1;
        int toYear = -1;
        return this.pulldownYear(name, fromYear, toYear);
    }

    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value="IL_INFINITE_LOOP",justification="Incorrectly classified as an infinite loop")
    public String pulldownYear(String name, int fromYear, int toYear) {
        int year = cal.get(Calendar.YEAR);
        if (fromYear == -1) {
            fromYear = year;
        }

        if (toYear == -1) {
            toYear = fromYear + yearRange;
        }
        StringBuilder yearPullDown = new StringBuilder();
        yearPullDown.append("<select name=\"").append(name).append("\">" + "\n");

        for (int i = fromYear; i <= toYear; i++) {
            String option = (i != year) ? "<option>" : "<option selected=\"selected\">";
            yearPullDown.append(option).append(i).append("</option>\n");
        }

        yearPullDown.append("</select>\n");
        return yearPullDown.toString();
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
