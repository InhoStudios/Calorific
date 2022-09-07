package model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Models an object that occurs once daily
 *
 * Stores a date which is set to the current date by default
 * Date can be modified with setDate and can be retrieved with getDate
 * checkDate returns whether or not two dates are equal
 *
 */

public abstract class Daily {

    protected Date date;
    public static SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy");
    public static SimpleDateFormat legibleDateFormat = new SimpleDateFormat("EEE MMM dd yyyy");

    // EFFECTS: Constructs a daily object
    public Daily() {
        this.date = new Date();
    }

    // REQUIRES: date parameter is not null, date field is not null
    // MODIFIES: nothing
    // EFFECTS: Checks if the date specified is the date stored,
    //          returns true if they are equal, false if they are not
    public boolean checkDate(Date date) {
        return sdf.format(this.date).equals(sdf.format(date));
    }

    // REQUIRES: date is not a null date
    // MODIFIES: nothing
    // EFFECTS: returns the date of this daily meal
    public Date getDate() {
        return this.date;
    }

    // REQUIRES: date object is not null
    // MODIFIES: this
    // EFFECTS: sets the date of the object to specified date
    public void setDate(Date date) {
        this.date = date;
    }

}
