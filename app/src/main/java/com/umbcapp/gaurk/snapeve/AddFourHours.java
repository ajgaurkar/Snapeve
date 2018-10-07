package com.umbcapp.gaurk.snapeve;

import java.util.Calendar;
import java.util.Date;


public class AddFourHours {

    Date current_date;

    public AddFourHours addHours(Date json_date) {
        AddFourHours addFourHours = new AddFourHours();


        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(json_date); // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, -4); // adds 4 hour
        addFourHours.setCurrent_date(cal.getTime());

        return addFourHours;
    }

    public Date getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(Date current_date) {
        this.current_date = current_date;
    }
}
