package com.philoertel.juryduty;

import android.support.annotation.Nullable;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Parser {

    /**
     * Parses an Instructions object from the text content of the reporting instructions website.
     */
    static Instructions parseInstructions(String content) throws ParseException {
        content = content.replaceAll("\\s+|&nbsp;", " ");
        // ?s causes . to match across newlines
        // .*? is the non-greedy version of .*
        Pattern p = Pattern.compile("(?s)GROUPS REPORTING:(.*?)(GROUPS ON STANDBY|GROUPS STANDBY|GROUPS EXCUSED)");
        Matcher m = p.matcher(content);
        DateTime date = null;
        ArrayList<String> groups = new ArrayList<>();
        while (m.find()) {
            String substring = m.group();
            date = parseDate(date, substring);
            groups.addAll(parseGroups(substring));
        }
        if (date == null) {
            throw new ParseException("Did not find instructions for a date");
        }
        return new Instructions(date, groups);
    }

    static class ParseException extends Exception {
        ParseException(String message) {
            super(message);
        }
    }

    private static DateTime parseDate(@Nullable DateTime previouslySeenDate, String input)
            throws ParseException {
        Pattern datePattern = Pattern.compile("((January|February|March|April|May|June|July|August|September|October|November|December) \\d{1,2}, \\d{4})");
        Matcher dateMatcher = datePattern.matcher(input);
        if (!dateMatcher.find()) {
            throw new ParseException("Reporting instructions do not contain a date: " + input);
        }
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMM dd, yyyy").withLocale(Locale.US);
        DateTime newDate = formatter.parseDateTime(dateMatcher.group());
        if (previouslySeenDate == null) {
            previouslySeenDate = newDate;
        } else {
            if (!previouslySeenDate.equals(newDate)) {
                throw new ParseException(String.format("Input contained instructions for different dates: %s and %s", previouslySeenDate, newDate));
            }
        }
        return previouslySeenDate;
    }

    private static Collection<String> parseGroups(String input) throws ParseException {
        ArrayList<String> groups = new ArrayList<>();
        String noGroupsStr = "There are no groups scheduled";
        if (input.contains(noGroupsStr)) {
            return new ArrayList<>();
        }
        Pattern groupsPattern = Pattern.compile("Group(.*?)report to");
        Matcher groupsMatcher = groupsPattern.matcher(input);
        while (groupsMatcher.find()) {
            Pattern groupPattern = Pattern.compile("\\d+");
            Matcher groupMatcher = groupPattern.matcher(groupsMatcher.group());
            while (groupMatcher.find()) {
                groups.add(groupMatcher.group());
            }
        }
        if (groups.isEmpty()) {
            throw new ParseException("Could not parse groups from input " + input);
        }
        return groups;
    }
}
