package com.philoertel.juryduty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Locale;

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

    public Instructions(@JsonProperty("dateString") String dateString,
                        @JsonProperty("reportingGroups") List<String> groups) {
        this.dateString = dateString;
        this.reportingGroups = groups;
    }

    public Instructions(DateTime date, List<String> groups) {
        this.dateString = date.toString("yyyyMMdd", Locale.US);
        this.reportingGroups = groups;
    }

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

    public List<String> getReportingGroups() {
        return reportingGroups;
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
