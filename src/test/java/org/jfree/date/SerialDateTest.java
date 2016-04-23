/* ========================================================================
 * JCommon : a free general purpose class library for the Java(tm) platform
 * ========================================================================
 *
 * (C) Copyright 2000-2014, by Object Refinery Limited and Contributors.
 * 
 * Project Info:  http://www.jfree.org/jcommon/index.html
 *
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation; either version 2.1 of the License, or 
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public 
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, 
 * USA.  
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 *
 * -------------------
 * SerialDateTest.java
 * -------------------
 * (C) Copyright 2001-2014, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: SerialDateTest.java,v 1.7 2007/11/02 17:50:35 taqua Exp $
 *
 * Changes
 * -------
 * 15-Nov-2001 : Version 1 (DG);
 * 25-Jun-2002 : Removed unnecessary import (DG);
 * 24-Oct-2002 : Fixed errors reported by Checkstyle (DG);
 * 13-Mar-2003 : Added serialization test (DG);
 * 05-Jan-2005 : Added test for bug report 1096282 (DG);
 *
 */

package org.jfree.date;

import static org.jfree.date.SerialDate.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.GregorianCalendar;

import com.sun.deploy.security.MozillaMyKeyStore;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Some JUnit tests for the {@link SerialDate} class.
 */
public class SerialDateTest extends TestCase {

    /**
     * Creates a new test case.
     *
     * @param name  the name.
     */
    public SerialDateTest(final String name) {
        super(name);
    }

    /**
     * Returns a test suite for the JUnit test runner.
     *
     * @return The test suite.
     */
    public static Test suite() {
        return new TestSuite(SerialDateTest.class);
    }

    private static SerialDate d(int day, int month, int year) {
        return SerialDate.createInstance(day, month, year);
    }

    public void testIsValidWeekdayCode() {
        for (int day = 1; day <= 7; day++) {
            assertTrue(isValidWeekdayCode(day));
        }
        assertFalse(isValidWeekdayCode(0));
        assertFalse(isValidWeekdayCode(8));
    }

    /**
     * Problem that the conversion of days to strings returns the right result.  Actually, this 
     * result depends on the Locale so this test needs to be modified.
     */
    public void testWeekdayCodeToString() {
        assertEquals("Sunday", weekdayCodeToString(SUNDAY));
        assertEquals("Monday", weekdayCodeToString(MONDAY));
        assertEquals("Tuesday", weekdayCodeToString(TUESDAY));
        assertEquals("Wednesday", weekdayCodeToString(WEDNESDAY));
        assertEquals("Thursday", weekdayCodeToString(THURSDAY));
        assertEquals("Friday", weekdayCodeToString(FRIDAY));
        assertEquals("Saturday", weekdayCodeToString(SATURDAY));
    }

    /**
     * Test the conversion of a string to a weekday.  Note that this test will fail if the
     * default locale doesn't use English weekday names...devise a better test!
     */
    public void testStringToWeekdayCode() {
        String[] mondays = {"Monday", "monday", "MONDAY", " Monday ",
            "Mon", "mon", "MON", " MOn "};
        String[] tuesdays = {"Tuesday", "tuesday", "TUESDAY", " Tuesday ",
            "Tue", "tue", "TUE", " TUe "};
        String[] wednesdays = {"Wednesday", "wednesday", "WEDNESDAY",
            " Wednesday ", "Wed", "wed", "WED", " WEd "};
        String[] thursdays = {"Thursday", "thursday", "THURSDAY", " Thursday ",
            "Thu", "thu", "THU", " THu "};
        String[] fridays = {"Friday", "friday", "FRIDAY", " Friday ",
            "Fri", "fri", "FRI", " FRi "};
        String[] saturdays = {"Saturday", "saturday", "SATURDAY", " Saturday ",
            "Sat", "sat", "SAT", " SAt "};
        String[] sundays = {"Sunday", "sunday", "SUNDAY", " Sunday ",
            "Sun", "sun", "SUN", " SUn "};

        String[][] weekdays = {mondays, tuesdays, wednesdays, thursdays,
            fridays, saturdays, sundays};
        int[] weekdayCodes = {MONDAY, TUESDAY, WEDNESDAY, THURSDAY,
            FRIDAY, SATURDAY, SUNDAY};

        for (int i = 0; i < weekdayCodes.length; i++) {
            for (String weekday : weekdays[i]) {
                assertEquals(weekdayCodes[i], stringToWeekdayCode(weekday));
            }
        }

        assertEquals(-1, stringToWeekdayCode("Hello"));
    }

    /**
     * Test the conversion of a string to a month.  Note that this test will fail if the default
     * locale doesn't use English month names...devise a better test!
     */
    public void testStringToMonthCode() {
        String[] januaries = {"January", "january", "JANUARY", " January ",
            "Jan", "jan", "JAN", " JAn "};
        String[] februaries = {"February", "february", "FEBRUARY", " February ",
            "Feb", "feb", "FEB", " FEb "};
        String[] marches = {"March", "march", "MARCH", " March ",
            "Mar", "mar", "MAR", " MAr "};
        String[] aprils = {"April", "april", "APRIL", " April ",
            "Apr", "apr", "APR", " APr "};
        String[] mays = {"May", "may", "MAY", " May ",
            "May", "may", "MAY", " MAy "};
        String[] junes = {"June", "June", "JUNE", " June ",
            "Jun", "jun", "JUN", " JUn "};
        String[] julies = {"July", "july", "JULY", " July ",
            "Jul", "jul", "JUL", " JUl "};
        String[] augusts = {"August", "august", "AUGUST", " August ",
            "Aug", "aug", "AUG", " AUg "};
        String[] septembers = {"September", "september", "SEPTEMBER",
            " September ", "Sep", "sep", "SEP", " SEp "};
        String[] octobers = {"October", "october", "OCTOBER", " October ",
            "Oct", "oct", "OCT", " OCt "};
        String[] novembers = {"November", "november", "NOVEMBER", " November ",
            "Nov", "nov", "NOV", " NOv "};
        String[] decembers = {"December", "december", "DECEMBER", " December ",
            "Dec", "dec", "DEC", " DEc "};

        String[][] months = {januaries, februaries, marches, aprils, mays,
            junes, julies, augusts, septembers, octobers, novembers, decembers};
        int[] monthCodes = {JANUARY, FEBRUARY, MARCH, APRIL, MAY,
            JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER};

        for (int i = 0; i < monthCodes.length; i++) {
            for (String month : months[i]) {
                assertEquals(monthCodes[i], stringToMonthCode(month));
            }
        }

        assertEquals(-1, stringToMonthCode("0"));
        assertEquals(-1, stringToMonthCode("13"));
        assertEquals(-1,stringToMonthCode("Hello"));

        for (int m = 1; m <= 12; m++) {
            assertEquals(m, stringToMonthCode(monthCodeToString(m)));
            assertEquals(m, stringToMonthCode(monthCodeToString(m, false)));
            assertEquals(m, stringToMonthCode(monthCodeToString(m, true)));
        }

    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    public void testSerialization() {

        SerialDate d1 = SerialDate.createInstance(15, 4, 2000);
        SerialDate d2 = null;

        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(d1);
            out.close();

            ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
            d2 = (SerialDate) in.readObject();
            in.close();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        assertEquals(d1, d2);

    }

    /**
     * Miscellaneous tests for the addMonths() method.
     */
    public void testAddMonths() {
        assertEquals(d(1, FEBRUARY, 1900), addMonths(1, d(1, JANUARY, 1900)));
        assertEquals(d(28, FEBRUARY, 1900), addMonths(1, d(31, JANUARY, 1900)));
        assertEquals(d(28, FEBRUARY, 1900), addMonths(1, d(30, JANUARY, 1900)));
        assertEquals(d(28, FEBRUARY, 1900), addMonths(1, d(29, JANUARY, 1900)));
        assertEquals(d(28, FEBRUARY, 1900), addMonths(1, d(28, JANUARY, 1900)));
        assertEquals(d(27, FEBRUARY, 1900), addMonths(1, d(27, JANUARY, 1900)));

        assertEquals(d(9, JANUARY, 2002), addMonths(2, d(9, NOVEMBER, 2001)));

        // A test case for a reported bug, now fixed.
        assertEquals(d(5, DECEMBER, 2003), addMonths(2, d(5, OCTOBER, 2003)));

        // A test case for a reported bug, now fixed.
        assertEquals(d(1, JANUARY, 2003), addMonths(0, d(1, JANUARY, 2003)));

        assertEquals(d(30, JUNE, 1900), addMonths(5, d(31, JANUARY, 1900)));
        assertEquals(d(30, JUNE, 1901), addMonths(17, d(31, JANUARY, 1900)));
        assertEquals(d(29, FEBRUARY, 1904), addMonths(49, d(31, JANUARY, 1900)));

        assertEquals(d(30, JULY, 2004), addMonths(1, addMonths(1, d(31, MAY, 2004))));
    }

    public void testIsValidMonthCode() {
        for (int i = 1; i <= 12; i++) {
            assertTrue(isValidMonthCode(i));
        }
        assertFalse(isValidMonthCode(0));
        assertFalse(isValidMonthCode(13));
    }

    public void testMonthCodeToQuarter() {
        assertEquals(1, monthCodeToQuarter(JANUARY));
        assertEquals(1, monthCodeToQuarter(FEBRUARY));
        assertEquals(1, monthCodeToQuarter(MARCH));
        assertEquals(2, monthCodeToQuarter(APRIL));
        assertEquals(2, monthCodeToQuarter(MAY));
        assertEquals(2, monthCodeToQuarter(JUNE));
        assertEquals(3, monthCodeToQuarter(JULY));
        assertEquals(3, monthCodeToQuarter(AUGUST));
        assertEquals(3, monthCodeToQuarter(SEPTEMBER));
        assertEquals(4, monthCodeToQuarter(OCTOBER));
        assertEquals(4, monthCodeToQuarter(NOVEMBER));
        assertEquals(4, monthCodeToQuarter(DECEMBER));

        try {
            monthCodeToQuarter(-1);
            fail("Invalid Month Code should throw exception");
        }
        catch (IllegalArgumentException e) {
        }

        try {
            monthCodeToQuarter(0);
            fail("Invalid Month Code should throw exception");
        }
        catch (IllegalArgumentException e) {
        }

        try {
            monthCodeToQuarter(13);
            fail("Invalid Month Code should throw exception");
        }
        catch (IllegalArgumentException e) {
        }
    }

    public void testMonthCodeToString() {
        assertEquals("January", monthCodeToString(JANUARY));
        assertEquals("February", monthCodeToString(FEBRUARY));
        assertEquals("March", monthCodeToString(MARCH));
        assertEquals("April", monthCodeToString(APRIL));
        assertEquals("May", monthCodeToString(MAY));
        assertEquals("June", monthCodeToString(JUNE));
        assertEquals("July", monthCodeToString(JULY));
        assertEquals("August", monthCodeToString(AUGUST));
        assertEquals("September", monthCodeToString(SEPTEMBER));
        assertEquals("October", monthCodeToString(OCTOBER));
        assertEquals("November", monthCodeToString(NOVEMBER));
        assertEquals("December", monthCodeToString(DECEMBER));

        assertEquals("January", monthCodeToString(JANUARY, false));
        assertEquals("February", monthCodeToString(FEBRUARY, false));
        assertEquals("March", monthCodeToString(MARCH, false));
        assertEquals("April", monthCodeToString(APRIL, false));
        assertEquals("May", monthCodeToString(MAY, false));
        assertEquals("June", monthCodeToString(JUNE, false));
        assertEquals("July", monthCodeToString(JULY, false));
        assertEquals("August", monthCodeToString(AUGUST, false));
        assertEquals("September", monthCodeToString(SEPTEMBER, false));
        assertEquals("October", monthCodeToString(OCTOBER, false));
        assertEquals("November", monthCodeToString(NOVEMBER, false));
        assertEquals("December", monthCodeToString(DECEMBER, false));

        assertEquals("Jan", monthCodeToString(JANUARY, true));
        assertEquals("Feb", monthCodeToString(FEBRUARY, true));
        assertEquals("Mar", monthCodeToString(MARCH, true));
        assertEquals("Apr", monthCodeToString(APRIL, true));
        assertEquals("May", monthCodeToString(MAY, true));
        assertEquals("Jun", monthCodeToString(JUNE, true));
        assertEquals("Jul", monthCodeToString(JULY, true));
        assertEquals("Aug", monthCodeToString(AUGUST, true));
        assertEquals("Sep", monthCodeToString(SEPTEMBER, true));
        assertEquals("Oct", monthCodeToString(OCTOBER, true));
        assertEquals("Nov", monthCodeToString(NOVEMBER, true));
        assertEquals("Dec", monthCodeToString(DECEMBER, true));

        try {
            monthCodeToString(-1);
            fail("Invalid month code should throw exception");
        }
        catch (IllegalArgumentException e) {
        }

        try {
            monthCodeToString(0);
            fail("Invalid month code should throw exception");
        }
        catch (IllegalArgumentException e) {
        }

        try {
            monthCodeToString(13);
            fail("Invalid month code should throw exception");
        }
        catch (IllegalArgumentException e) {
        }
    }

    public void testIsValidWeekInMonthCode() {
        for (int w = 0; w <= 4; w++) {
            assertTrue(isValidWeekInMonthCode(w));
        }
        assertFalse(isValidWeekInMonthCode(5));
    }

    public void testIsLeapYear() {
        assertFalse(isLeapYear(1900));
        assertFalse(isLeapYear(1901));
        assertFalse(isLeapYear(1902));
        assertFalse(isLeapYear(1903));
        assertTrue(isLeapYear(1904));
        assertTrue(isLeapYear(1908));
        assertFalse(isLeapYear(1955));
        assertTrue(isLeapYear(1964));
        assertTrue(isLeapYear(1980));
        assertTrue(isLeapYear(2000));
        assertFalse(isLeapYear(2001));
        assertFalse(isLeapYear(2100));
    }

    public void testLeapYearCount() {
        assertEquals(0, leapYearCount(1899));
        assertEquals(0, leapYearCount(1900));
        assertEquals(0, leapYearCount(1901));
        assertEquals(0, leapYearCount(1902));
        assertEquals(0, leapYearCount(1903));
        assertEquals(1, leapYearCount(1904));
        assertEquals(1, leapYearCount(1905));
        assertEquals(1, leapYearCount(1906));
        assertEquals(1, leapYearCount(1907));
        assertEquals(2, leapYearCount(1908));
        assertEquals(24, leapYearCount(1999));
        assertEquals(25, leapYearCount(2000));
        assertEquals(25, leapYearCount(2001));
        assertEquals(49, leapYearCount(2101));
        assertEquals(73, leapYearCount(2201));
        assertEquals(97, leapYearCount(2301));
        assertEquals(122, leapYearCount(2401));
    }

    public void testLastDayOfMonth() {
        assertEquals(31, lastDayOfMonth(JANUARY, 1901));
        assertEquals(28, lastDayOfMonth(FEBRUARY, 1901));
        assertEquals(31, lastDayOfMonth(MARCH, 1901));
        assertEquals(30, lastDayOfMonth(APRIL, 1901));
        assertEquals(31, lastDayOfMonth(MAY, 1901));
        assertEquals(30, lastDayOfMonth(JUNE, 1901));
        assertEquals(31, lastDayOfMonth(JULY, 1901));
        assertEquals(31, lastDayOfMonth(AUGUST, 1901));
        assertEquals(30, lastDayOfMonth(SEPTEMBER, 1901));
        assertEquals(31, lastDayOfMonth(OCTOBER, 1901));
        assertEquals(30, lastDayOfMonth(NOVEMBER, 1901));
        assertEquals(31, lastDayOfMonth(DECEMBER, 1901));
        assertEquals(29, lastDayOfMonth(FEBRUARY, 1904));
    }

    public void testAddDays() {
        SerialDate newYears = d(1, JANUARY, 1900);
        assertEquals(d(2, JANUARY, 1900), addDays(1, newYears));
        assertEquals(d(1, FEBRUARY, 1900), addDays(31, newYears));
        assertEquals(d(1, JANUARY, 1901), addDays(365, newYears));
        assertEquals(d(31, DECEMBER, 1904), addDays(5 * 365, newYears));
    }

    public void testAddYears() {
        assertEquals(d(1, JANUARY, 1901), addYears(1, d(1, JANUARY, 1900)));
        assertEquals(d(28, FEBRUARY, 1905), addYears(1, d(29, FEBRUARY, 1904)));
        assertEquals(d(28, FEBRUARY, 1905), addYears(1, d(28, FEBRUARY, 1904)));
        assertEquals(d(28, FEBRUARY, 1904), addYears(1, d(28, FEBRUARY, 1903)));

        // A test for bug report 1096282 (now fixed).
        //TODO Verify this isOn method, if it is really necessary or not.
        assertTrue(addYears(1, d(29, FEBRUARY, 2004)).isOn(d(28, FEBRUARY, 2005)));
    }

    public void testGetPreviousDayOfWeek() {
        assertEquals(d(5, NOVEMBER, 2001), getPreviousDayOfWeek(MONDAY, d(9, NOVEMBER, 2001)));

        assertEquals(d(24, FEBRUARY, 2006), getPreviousDayOfWeek(FRIDAY, d(1, MARCH, 2006)));
        assertEquals(d(22, FEBRUARY, 2006), getPreviousDayOfWeek(WEDNESDAY, d(1, MARCH, 2006)));
        assertEquals(d(29, FEBRUARY, 2004), getPreviousDayOfWeek(SUNDAY, d(3, MARCH, 2004)));
        assertEquals(d(29, DECEMBER, 2004), getPreviousDayOfWeek(WEDNESDAY, d(5, JANUARY, 2005)));

        try {
            getPreviousDayOfWeek(-1, d(1, JANUARY, 2006));
            fail("Invalid day of week code should throw exception");
        }
        catch (IllegalArgumentException e) {
        }
    }

    public void testGetFollowingDayOfWeek() {
        assertEquals(d(12, NOVEMBER, 2001), getFollowingDayOfWeek(MONDAY, d(9, NOVEMBER, 2001)));

        assertEquals(d(1, JANUARY, 2005),getFollowingDayOfWeek(SATURDAY, d(25, DECEMBER, 2004)));
        assertEquals(d(1, JANUARY, 2005), getFollowingDayOfWeek(SATURDAY, d(26, DECEMBER, 2004)));
        assertEquals(d(3, MARCH, 2004), getFollowingDayOfWeek(WEDNESDAY, d(28, FEBRUARY, 2004)));

        try {
            getFollowingDayOfWeek(-1, d(1, JANUARY, 2006));
            fail("Invalid day of week code should throw exception");
        }
        catch (IllegalArgumentException e) {
        }
    }

    public void testGetNearestDayOfWeek() {
        assertEquals(d(12, NOVEMBER, 2001), getNearestDayOfWeek(MONDAY, d(9, NOVEMBER, 2001)));
        assertEquals(d(19, JANUARY, 1970), getNearestDayOfWeek(MONDAY, d(22, JANUARY, 1970)));

        assertEquals(d(16, APRIL, 2006), getNearestDayOfWeek(SUNDAY, d(16, APRIL, 2006)));
        assertEquals(d(16, APRIL, 2006), getNearestDayOfWeek(SUNDAY, d(17, APRIL, 2006)));
        assertEquals(d(16, APRIL, 2006), getNearestDayOfWeek(SUNDAY, d(18, APRIL, 2006)));
        assertEquals(d(16, APRIL, 2006), getNearestDayOfWeek(SUNDAY, d(19, APRIL, 2006)));
        assertEquals(d(23, APRIL, 2006), getNearestDayOfWeek(SUNDAY, d(20, APRIL, 2006)));
        assertEquals(d(23, APRIL, 2006), getNearestDayOfWeek(SUNDAY, d(21, APRIL, 2006)));
        assertEquals(d(23, APRIL, 2006), getNearestDayOfWeek(SUNDAY, d(22, APRIL, 2006)));

        assertEquals(d(17, APRIL, 2006), getNearestDayOfWeek(MONDAY, d(16, APRIL, 2006)));
        assertEquals(d(17, APRIL, 2006), getNearestDayOfWeek(MONDAY, d(17, APRIL, 2006)));
        assertEquals(d(17, APRIL, 2006), getNearestDayOfWeek(MONDAY, d(18, APRIL, 2006)));
        assertEquals(d(17, APRIL, 2006), getNearestDayOfWeek(MONDAY, d(19, APRIL, 2006)));
        assertEquals(d(17, APRIL, 2006), getNearestDayOfWeek(MONDAY, d(20, APRIL, 2006)));
        assertEquals(d(24, APRIL, 2006), getNearestDayOfWeek(MONDAY, d(21, APRIL, 2006)));
        assertEquals(d(24, APRIL, 2006), getNearestDayOfWeek(MONDAY, d(22, APRIL, 2006)));

        assertEquals(d(18, APRIL, 2006), getNearestDayOfWeek(TUESDAY, d(16, APRIL, 2006)));
        assertEquals(d(18, APRIL, 2006), getNearestDayOfWeek(TUESDAY, d(17, APRIL, 2006)));
        assertEquals(d(18, APRIL, 2006), getNearestDayOfWeek(TUESDAY, d(18, APRIL, 2006)));
        assertEquals(d(18, APRIL, 2006), getNearestDayOfWeek(TUESDAY, d(19, APRIL, 2006)));
        assertEquals(d(18, APRIL, 2006), getNearestDayOfWeek(TUESDAY, d(20, APRIL, 2006)));
        assertEquals(d(18, APRIL, 2006), getNearestDayOfWeek(TUESDAY, d(21, APRIL, 2006)));
        assertEquals(d(25, APRIL, 2006), getNearestDayOfWeek(TUESDAY, d(22, APRIL, 2006)));

        assertEquals(d(19, APRIL, 2006), getNearestDayOfWeek(WEDNESDAY, d(16, APRIL, 2006)));
        assertEquals(d(19, APRIL, 2006), getNearestDayOfWeek(WEDNESDAY, d(17, APRIL, 2006)));
        assertEquals(d(19, APRIL, 2006), getNearestDayOfWeek(WEDNESDAY, d(18, APRIL, 2006)));
        assertEquals(d(19, APRIL, 2006), getNearestDayOfWeek(WEDNESDAY, d(19, APRIL, 2006)));
        assertEquals(d(19, APRIL, 2006), getNearestDayOfWeek(WEDNESDAY, d(20, APRIL, 2006)));
        assertEquals(d(19, APRIL, 2006), getNearestDayOfWeek(WEDNESDAY, d(21, APRIL, 2006)));
        assertEquals(d(19, APRIL, 2006), getNearestDayOfWeek(WEDNESDAY, d(22, APRIL, 2006)));

        assertEquals(d(13, APRIL, 2006), getNearestDayOfWeek(THURSDAY, d(16, APRIL, 2006)));
        assertEquals(d(20, APRIL, 2006), getNearestDayOfWeek(THURSDAY, d(17, APRIL, 2006)));
        assertEquals(d(20, APRIL, 2006), getNearestDayOfWeek(THURSDAY, d(18, APRIL, 2006)));
        assertEquals(d(20, APRIL, 2006), getNearestDayOfWeek(THURSDAY, d(19, APRIL, 2006)));
        assertEquals(d(20, APRIL, 2006), getNearestDayOfWeek(THURSDAY, d(20, APRIL, 2006)));
        assertEquals(d(20, APRIL, 2006), getNearestDayOfWeek(THURSDAY, d(21, APRIL, 2006)));
        assertEquals(d(20, APRIL, 2006), getNearestDayOfWeek(THURSDAY, d(22, APRIL, 2006)));

        assertEquals(d(14, APRIL, 2006), getNearestDayOfWeek(FRIDAY, d(16, APRIL, 2006)));
        assertEquals(d(14, APRIL, 2006), getNearestDayOfWeek(FRIDAY, d(17, APRIL, 2006)));
        assertEquals(d(21, APRIL, 2006), getNearestDayOfWeek(FRIDAY, d(18, APRIL, 2006)));
        assertEquals(d(21, APRIL, 2006), getNearestDayOfWeek(FRIDAY, d(19, APRIL, 2006)));
        assertEquals(d(21, APRIL, 2006), getNearestDayOfWeek(FRIDAY, d(20, APRIL, 2006)));
        assertEquals(d(21, APRIL, 2006), getNearestDayOfWeek(FRIDAY, d(21, APRIL, 2006)));
        assertEquals(d(21, APRIL, 2006), getNearestDayOfWeek(FRIDAY, d(22, APRIL, 2006)));

        assertEquals(d(15, APRIL, 2006), getNearestDayOfWeek(SATURDAY, d(16, APRIL, 2006)));
        assertEquals(d(15, APRIL, 2006), getNearestDayOfWeek(SATURDAY, d(17, APRIL, 2006)));
        assertEquals(d(15, APRIL, 2006), getNearestDayOfWeek(SATURDAY, d(18, APRIL, 2006)));
        assertEquals(d(22, APRIL, 2006), getNearestDayOfWeek(SATURDAY, d(19, APRIL, 2006)));
        assertEquals(d(22, APRIL, 2006), getNearestDayOfWeek(SATURDAY, d(20, APRIL, 2006)));
        assertEquals(d(22, APRIL, 2006), getNearestDayOfWeek(SATURDAY, d(21, APRIL, 2006)));
        assertEquals(d(22, APRIL, 2006), getNearestDayOfWeek(SATURDAY, d(22, APRIL, 2006)));

        try {
            getNearestDayOfWeek(-1, d(1, JANUARY, 2006));
            fail("Invalid day of week code should throw exception");
        }
        catch (IllegalArgumentException e) {
        }
    }

    public void testGetEndOfCurrentMonth() {
        SerialDate d = SerialDate.createInstance(2);
        assertEquals(d(31, JANUARY, 2006), d.getEndOfCurrentMonth(d(1, JANUARY, 2006)));
        assertEquals(d(28, FEBRUARY, 2006), d.getEndOfCurrentMonth(d(1, FEBRUARY, 2006)));
        assertEquals(d(31, MARCH, 2006), d.getEndOfCurrentMonth(d(1, MARCH, 2006)));
        assertEquals(d(30, APRIL, 2006), d.getEndOfCurrentMonth(d(1, APRIL, 2006)));
        assertEquals(d(31, MAY, 2006), d.getEndOfCurrentMonth(d(1, MAY, 2006)));
        assertEquals(d(30, JUNE, 2006), d.getEndOfCurrentMonth(d(1, JUNE, 2006)));
        assertEquals(d(31, JULY, 2006), d.getEndOfCurrentMonth(d(1, JULY, 2006)));
        assertEquals(d(31, AUGUST, 2006), d.getEndOfCurrentMonth(d(1, AUGUST, 2006)));
        assertEquals(d(30, SEPTEMBER, 2006), d.getEndOfCurrentMonth(d(1, SEPTEMBER, 2006)));
        assertEquals(d(31, OCTOBER, 2006), d.getEndOfCurrentMonth(d(1, OCTOBER, 2006)));
        assertEquals(d(30, NOVEMBER, 2006), d.getEndOfCurrentMonth(d(1, NOVEMBER, 2006)));
        assertEquals(d(31, DECEMBER, 2006), d.getEndOfCurrentMonth(d(1, DECEMBER, 2006)));
        assertEquals(d(29, FEBRUARY, 2008), d.getEndOfCurrentMonth(d(1, FEBRUARY, 2008)));
    }

    public void testWeekInMonthToString() {
        assertEquals("First",weekInMonthToString(FIRST_WEEK_IN_MONTH));
        assertEquals("Second",weekInMonthToString(SECOND_WEEK_IN_MONTH));
        assertEquals("Third",weekInMonthToString(THIRD_WEEK_IN_MONTH));
        assertEquals("Fourth",weekInMonthToString(FOURTH_WEEK_IN_MONTH));
        assertEquals("Last",weekInMonthToString(LAST_WEEK_IN_MONTH));

        try {
            weekInMonthToString(-1);
            fail("Invalid week code should throw exception");
        }
        catch (IllegalArgumentException e) {
        }
    }

    public void testRelativeToString() {
        assertEquals("Preceding",relativeToString(PRECEDING));
        assertEquals("Nearest",relativeToString(NEAREST));
        assertEquals("Following",relativeToString(FOLLOWING));

        try {
            relativeToString(-1000);
           	fail("Invalid relative code should throw exception");
         }
        catch (IllegalArgumentException e) {
         }
    }

    public void testCreateInstanceFromDDMMYYYY() {
        SerialDate date = createInstance(1, JANUARY, 1900);
        assertEquals(1,date.getDayOfMonth());
        assertEquals(JANUARY,date.getMonth());
        assertEquals(1900,date.getYYYY());
        assertEquals(2,date.toSerial());
    }

    public void testCreateInstanceFromSerial() {
        assertEquals(d(1, JANUARY, 1900),createInstance(2));
        assertEquals(d(1, JANUARY, 1901), createInstance(367));
    }

    public void testCreateInstanceFromJavaDate() {
        assertEquals(d(1, JANUARY, 1900),
            createInstance(new GregorianCalendar(1900,0,1).getTime()));
        assertEquals(d(1, JANUARY, 2006),
            createInstance(new GregorianCalendar(2006,0,1).getTime()));
    }
}
