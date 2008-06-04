/*
 * RrsDate.java
 *
 * Created on February 7, 2007, 2:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mpi.rrs.model.date;

/**
 *
 * @author kees
 */
import java.util.ArrayList;
import java.util.Calendar;
//import java.util.Date;

import mpi.rrs.model.utilities.RrsUtil;

import org.apache.log4j.Logger;

public class RrsDate {
    static Logger logger = Logger.getLogger(RrsDate.class);
    
    public final static ArrayList<String>  MONTHS;
    static {
        MONTHS = new ArrayList();
        MONTHS.add(null);
        MONTHS.add("January");
        MONTHS.add("February");
        MONTHS.add("March");
        MONTHS.add("April");
        MONTHS.add("May");
        MONTHS.add("June");
        MONTHS.add("July");
        MONTHS.add("August");
        MONTHS.add("September");
        MONTHS.add("October");
        MONTHS.add("November");
        MONTHS.add("December");
    }
    
    
    public static ArrayList<String> MONTHS_SHORT;
    static {
        MONTHS_SHORT = new ArrayList();
        MONTHS_SHORT.add(null);
        MONTHS_SHORT.add("Jan");
        MONTHS_SHORT.add("Feb");
        MONTHS_SHORT.add("Mar");
        MONTHS_SHORT.add("Apr");
        MONTHS_SHORT.add("May");
        MONTHS_SHORT.add("Jun");
        MONTHS_SHORT.add("Jul");
        MONTHS_SHORT.add("Aug");
        MONTHS_SHORT.add("Sep");
        MONTHS_SHORT.add("Oct");
        MONTHS_SHORT.add("Nov");
        MONTHS_SHORT.add("Dec");
    }
    
    
    public ArrayList<String> getMONTHS() {
        return MONTHS;
    }
    
    /*
    public static HashMap months2;
    static {
        months2 = new HashMap();
        months2.put("Jan","January");
        months2.put("Feb","February");
        months2.put("Mar","March");
        months2.put("Apr","April");
        months2.put("May","May");
        months2.put("Jun","June");
        months2.put("Jul","July");
        months2.put("Aug","August");
        months2.put("Sep","September");
        months2.put("Oct","October");
        months2.put("Nov","November");
        months2.put("Dec","December");
    }
     */
    
    
    
    
    
    /** Creates a new instance of RrsDate */
    public RrsDate() {
        aValidDate = true;
    }
    
    private String day;
    private int dayInt;
    
    private String month;
    private int monthInt;
    
    private String year;
    private int yearInt;
    
    private String value;
    private boolean aValidDate;
    
    public String getDay() {
        return day;
    }
    
    public void setDay(String day) {
        try {
            this.dayInt = Integer.parseInt(day);
        } catch (NumberFormatException ex) {
            this.aValidDate = false;
            //ex.printStackTrace();
        }
        this.day = day;
    }
    
    public String getMonth() {
        return month;
    }
    
    public void setMonth(String month) {
        
        if (MONTHS_SHORT.contains(month)) {
            monthInt = MONTHS_SHORT.indexOf(month);
        } else {
            this.aValidDate = false;
        }
        
        this.month = month;
    }
    
    public String getYear() {
        return year;
    }
    
    public void setYear(String year) {
        try {
            this.yearInt = Integer.parseInt(year);
        } catch (NumberFormatException ex) {
            this.aValidDate = false;
            //ex.printStackTrace();
        }
        
        this.year = year;
    }
    
    public String getValue() {
        if (RrsUtil.isEmpty(this.value)) {
            this.setValue();
        }
        
        return value;
    }
    
    public void setValue() {
        this.value = day + "-" + month + "-" + year;
        
    }
    
    public boolean isAValidDate() {
        if (this.aValidDate) {
            
            Calendar cal = Calendar.getInstance();
            
            cal.setLenient(false);
            
            try {
                cal.set(yearInt,monthInt-1,dayInt);
                //Date test = cal.getTime();
                
            }  catch (IllegalArgumentException ex) {
                logger.debug("illegal date: " + year + ", " + month + " " + day);
                aValidDate = false;
                return aValidDate;
                //ex.printStackTrace();
            }
        }
        logger.debug("legal date: " + year + ", " + month + " " + day);
        return aValidDate;
    }
    
    public void setAValidDate(boolean aValidDate) {
        this.aValidDate = aValidDate;
    }
    
    public int getDayInt() {
        return dayInt;
    }
    
    public void setDayInt(int dayInt) {
        this.day = new Integer(dayInt).toString();
        this.dayInt = dayInt;
    }
    
    public int getMonthInt() {
        
        return monthInt;
    }
    
    public void setMonthInt(int monthInt) {
        if (monthInt >= 1  && monthInt <= 12) {
            this.month = MONTHS_SHORT.get(monthInt);
        } else {
            this.aValidDate = false;
        }
        
        this.monthInt = monthInt;
    }
    
    public int getYearInt() {
        return yearInt;
    }
    
    public void setYearInt(int yearInt) {
        this.year = new Integer(year).toString();
        this.yearInt = yearInt;
    }
    
    public Calendar toCalendar() {
        
        if (this.isAValidDate()) {
            Calendar cal = Calendar.getInstance();
            cal.set(yearInt,monthInt-1,dayInt);
            
            return cal;
        }
        
        return null;
        
    }
    
    public boolean isLaterThan(Calendar calOtherDate) {
        Calendar calThisDate = this.toCalendar();
        
        if (calThisDate != null && calOtherDate != null) {
            
            if (calThisDate.getTimeInMillis() > calOtherDate.getTimeInMillis()) {
                return true;
            }
        }
        
        return false;
    }
    
    
}
