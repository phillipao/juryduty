package com.philoertel.juryduty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

/**
 * Jury reporting instructions for a given day.
 *
 * <p>This is a representation of the instructions given by the court, whose main purpose is to
 * instruct a set of reporting groups to appear in court.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Instructions {

    // Reporting instructions implicitly refer to US/Pacific time.
    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern(
            "yyyyMMdd").withZone(
            DateTimeZone.forID("America/Los_Angeles"));

    // The date to which these instructions apply.
    private String dateString;

    // The list of groups who are instructed to report.
    private List<String> reportingGroups;

    public Instructions() {}

    @JsonIgnore
    public DateTime getDateTime() {
        return formatter.parseDateTime(dateString);
    }

    /**
     * The date in yyyymmdd (ISO 8601 basic) format. All times are Pacific.
     */
    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public List<String> getReportingGroups() {
        return reportingGroups;
    }

    public void setReportingGroups(List<String> reportingGroups) {
        this.reportingGroups = reportingGroups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Instructions that = (Instructions) o;

        return new EqualsBuilder()
                .append(dateString, that.dateString)
                .append(reportingGroups, that.reportingGroups)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(dateString)
                .append(reportingGroups)
                .toHashCode();
    }
}
