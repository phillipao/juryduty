package com.philoertel.juryduty;

import android.support.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static final String TAG = "Parser";

    /**
     * Parses an Instructions object from the text content of the reporting instructions website.\
     */
    static Instructions parseInstructions(String content) {
        // ?s causes . to match across newlines
        // .*? is the non-greedy version of .*
        Pattern p = Pattern.compile("(?s)GROUPS REPORTING:(.*?)GROUPS");
        Matcher m = p.matcher(content);
        DateTime date = null;
        ArrayList<String> groups = new ArrayList<>();
        while (m.find()) {
            String substring = m.group();
            date = parseDate(date, substring);
            groups.addAll(parseGroups(substring));
        }
        if (date == null) {
            throw new IllegalArgumentException("Did not find instructions for a date");
        }
        Instructions instructions = new Instructions();
        instructions.setDateString(date.toString("yyyyMMdd", Locale.US));
        instructions.setReportingGroups(groups);
        return instructions;
    }

    private static DateTime parseDate(@Nullable DateTime previouslySeenDate, String input) {
        Pattern datePattern = Pattern.compile("((January|February|March|April|May|June|July|August|September|October|November|December) \\d{1,2}, \\d{4})");
        Matcher dateMatcher = datePattern.matcher(input);
        if (!dateMatcher.find()) {
            throw new IllegalArgumentException("Reporting instructions do not contain a date: " + input);
        }
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMM dd, yyyy").withLocale(Locale.US);
        DateTime newDate = formatter.parseDateTime(dateMatcher.group());
        if (previouslySeenDate == null) {
            previouslySeenDate = newDate;
        } else {
            if (!previouslySeenDate.equals(newDate)) {
                throw new IllegalArgumentException(String.format("Input contained instructions for different dates: %s and %s", previouslySeenDate, newDate));
            }
        }
        return previouslySeenDate;
    }

    private static Collection<String> parseGroups(String input) {
        ArrayList<String> groups = new ArrayList<>();
        String noGroupsStr = "There are no groups scheduled";
        if (input.contains(noGroupsStr)) {
            return new ArrayList<>();
        }
        Pattern groupsPattern = Pattern.compile("Group(.*\\d+.*)report to");
        Matcher groupsMatcher = groupsPattern.matcher(input);
        while (groupsMatcher.find()) {
            Pattern groupPattern = Pattern.compile("\\d+");
            Matcher groupMatcher = groupPattern.matcher(groupsMatcher.group());
            while (groupMatcher.find()) {
                groups.add(groupMatcher.group());
            }
        }
        if (groups.isEmpty()) {
            throw new IllegalArgumentException("Could not parse groups from input " + input);
        }
        return groups;
    }
}
