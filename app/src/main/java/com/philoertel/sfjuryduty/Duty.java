package com.philoertel.sfjuryduty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.format.ISODateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Duty {
    private final String week;
    private final String group;

    public Duty(@JsonProperty("week") String week, @JsonProperty("group") String group) {
        this.week = week;
        this.group = group;
    }

    /**
     * Constructs a Duty from year, month, day, and group, to match the Edit and Add activities.
     *
     * @param year the selected year
     * @param month the selected month, 1-indexed (e.g., 1 is January) to be consistent with
     *              JodaTime. When loading from a DatePicker, add 1, because DatePicker is
     *              0-indexed.
     * @param day the selected day of month (1-31)
     * @param group the user's selected group. Usually numeric, but not required.
     */
    public static Duty fromYearMonthDayGroup(int year, int month, int day, String group) {
        DateTime dateTime = new DateTime(year, month, day, 0, 0, DateTimeZone.forID("US/Pacific"));
        String weekYear = ISODateTimeFormat.weekyearWeek().print(dateTime);
        return new Duty(weekYear, group);
    }

    @JsonIgnore
    public Date getDate() {
        return getWeekInterval().getStart().toDate();
    }

    public String getWeek() { return week; }

    @JsonIgnore
    public Interval getWeekInterval() {
        DateTime start = new DateTime(week, DateTimeZone.forID("US/Pacific"));
        DateTime end = start.plusWeeks(1);
        return new Interval(start, end);
    }

    public String getGroup() {
        return group;
    }

    @Override
    public String toString() {
        return SimpleDateFormat.getDateInstance().format(getDate());
    }

    /**
     * Whether this Duty and the provided Instructions intersect.
     */
    public boolean calledBy(Instructions instructions) {
        return getWeekInterval().contains(instructions.getDateTime())
                && instructions.getReportingGroups().contains(group);
    }
}
