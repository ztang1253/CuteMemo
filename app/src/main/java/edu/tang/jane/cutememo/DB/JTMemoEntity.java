package edu.tang.jane.cutememo.DB;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by admin on 2017/10/16.
 */

public class JTMemoEntity implements Serializable {
    public long date;
    public int sleep = 8;
    public int weight = 100;
    public int dinner = 0;
    public int sports = 1;
    public String notes = "";
    public int period = 6; // N/A
    public int grade = 4; // N/A
    public int peeColor = 4; // N/A
    public int year;
    public int month;
    public int day;

    public JTMemoEntity() {

    }

    public void reset() {
        sleep = 8;
        weight = 100;
        dinner = 0;
        sports = 1;
        notes = "";
        period = 6;
        grade = 4;
        peeColor = 4;
        Calendar now = Calendar.getInstance();
        year = now.get(Calendar.YEAR);
        month = now.get(Calendar.MONTH);
        day = now.get(Calendar.DATE);
        now.set(year, month, day, 0, 0, 0);
        date = now.getTimeInMillis();
    }

    public void updateDate() {
        Calendar now = Calendar.getInstance();
        now.set(year, month, day, 0, 0, 0);
        date = now.getTimeInMillis();
    }

    public void updateYMD() {
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(date);
        year = now.get(Calendar.YEAR);
        month = now.get(Calendar.MONTH);
        day = now.get(Calendar.DATE);
    }

    public String toString() {
        return "Year: " + year + ", Month:" + month + ", day:" + day;
    }
}
