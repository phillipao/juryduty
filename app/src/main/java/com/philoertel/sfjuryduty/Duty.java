package com.philoertel.sfjuryduty;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Duty {
    private final Date date;
    private final int group;

    public Duty(Date date, int group) {
        this.date = date;
        this.group = group;
    }

    public Date getDate() {
        return date;
    }

    public int getGroup() {
        return group;
    }

    @Override
    public String toString() {
        return SimpleDateFormat.getDateInstance().format(date);
    }
}
