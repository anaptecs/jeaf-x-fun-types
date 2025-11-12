/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.types;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import com.anaptecs.jeaf.xfun.api.checks.Assert;
import com.anaptecs.jeaf.xfun.api.checks.Check;

/**
 * Class represents a period of time which is defined by a start and an end date. This class also supports either an
 * open beginning and end or both.
 * 
 * The class is immutable which means that the objects internal state can not be edited after the object was once
 * created.
 * 
 * @author JEAF Development Team
 * @version JEAF Release 1.2
 */
public class Period implements Serializable {
  /**
   * Constant defines the pattern that is used to convert dates in string format to java objects. The string
   * representation of a date has to have the following structure: "yyyy-MM-dd" (e.g. "2004-11-28")
   */
  public static final String DATE_PATTERN = "yyyy-MM-dd";

  /**
   * Constant defines the pattern that is used to convert time stamps in string format to java objects. The string
   * representation of a time stamp has to have the following structure: "yyyy-MM-dd HH:mm:ss" (e.g. "2004-11-28
   * 13:31:17")
   */
  public static final String DATE_TIME_PATTERN = DATE_PATTERN + " HH:mm";

  /**
   * Constant defines the pattern that is used to convert time stamps in string format to java objects. The string
   * representation of a time stamp has to have the following structure: "yyyy-MM-dd HH:mm:ss" (e.g. "2004-11-28
   * 13:31:17")
   */
  public static final String DATE_TIME_SECONDS_PATTERN = DATE_TIME_PATTERN + ":ss";

  /**
   * Constant defines the pattern that is used to convert time stamps in string format to java objects. The string
   * representation of a time stamp has to have the following structure: "yyyy-MM-dd HH:mm:ss.SSS" (e.g. "2004-11-28
   * 13:31:17.098")
   */
  public static final String TIMESTAMP_PATTERN = DATE_PATTERN + " HH:mm:ss.SSS";

  /**
   * Constant for convenience purpose that represents an unlimited period.
   */
  public static final Period UNLIMITED_PERIOD = new Period((Date) null, (Date) null);

  /**
   * Constant for the name of the start attribute
   */
  public static final String START_ATTRIBUTE = "start";

  /**
   * Constant for the name of the end attribute
   */
  public static final String END_ATTRIBUTE = "end";

  /**
   * Default serial version uid for this class.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Attribute represents the beginning of the represented period of time. The attribute may be null which means the
   * period is open to the beginning.
   */
  private final Date start;

  /**
   * Attribute represents the end of the represented period of time. The attribute may be null which means the period is
   * open to the end.
   */
  private final Date end;

  /**
   * Initialize new period object.
   * 
   * @param pStart Beginning of the period of time. The parameter may be null.
   * @param pEnd End of the period of time. The parameter may be null.
   */
  public Period( Date pStart, Date pEnd ) {
    // Check parameters.
    Check.checkValidPeriod(pStart, pEnd);

    // Create clone of passed start date in order to ensure that it can not be edited.
    if (pStart != null) {
      start = (Date) pStart.clone();
    }
    else {
      start = null;
    }

    // Create clone of end date in order to ensure that it can not be edited.
    if (pEnd != null) {
      end = (Date) pEnd.clone();
    }
    else {
      end = null;
    }
  }

  /**
   * Initialize new period object.
   * 
   * @param pStart Beginning of the period of time. The parameter may be null.
   * @param pEnd End of the period of time. The parameter may be null.
   */
  public Period( Calendar pStart, Calendar pEnd ) {
    Date lStartDate;
    if (pStart != null) {
      lStartDate = pStart.getTime();
    }
    else {
      lStartDate = null;
    }
    Date lEndDate;
    if (pEnd != null) {
      lEndDate = pEnd.getTime();
    }
    else {
      lEndDate = null;
    }
    Check.checkValidPeriod(lStartDate, lEndDate);

    start = lStartDate;
    end = lEndDate;
  }

  /**
   * Initialize new period object.
   * 
   * @param pPeriod Period that should be used to initialize the object. The parameter must not be null.
   */
  public Period( Period pPeriod ) {
    // Check parameter.
    Check.checkInvalidParameterNull(pPeriod, "pPeriod");

    if (pPeriod.start != null) {
      start = new Date(pPeriod.start.getTime());
    }
    else {
      start = null;
    }

    if (pPeriod.end != null) {
      end = new Date(pPeriod.end.getTime());
    }
    else {
      end = null;
    }
  }

  /**
   * Method returns the start of the period.
   * 
   * @return {@link Date} Start date of the period of time. The method will return null if the period has an open
   * beginning.
   */
  public Date getStart( ) {
    Date lStart;
    // Start date not defined.
    if (start != null) {
      // Method returns a clone to ensure immutability.
      lStart = (Date) start.clone();
    }
    else {
      lStart = null;
    }
    return lStart;
  }

  /**
   * Method returns the end of the period.
   * 
   * @return {@link Date} End date of the period of time. The method will return null if the period has an open
   * beginning.
   */
  public Date getEnd( ) {
    Date lEnd;
    // Start date not defined.
    if (end != null) {
      // Method returns a clone to ensure immutability.
      lEnd = (Date) end.clone();
    }
    else {
      lEnd = null;
    }
    return lEnd;
  }

  /**
   * Method checks if this period has an open beginning.
   * 
   * @return boolean Method returns true if the period has an open beginning and false in all other cases.
   */
  public boolean hasOpenBeginning( ) {
    return start == null;
  }

  /**
   * Method checks if this period has an open end.
   * 
   * @return boolean Method returns true if the period has an open end and false in all other cases.
   */
  public boolean hasOpenEnd( ) {
    return end == null;
  }

  /**
   * Method checks whether the passed point of time represented by the passed date object is enclosed by the represented
   * period of time.
   * 
   * @param pPointOfTime Point of time for which will be checked if it is enclosed within the period of time. The
   * parameter must not be null.
   * @return boolean Method returns true is the passed date is within the period that is defined by this object and
   * false in all other cases.
   */
  public boolean isEnclosed( Date pPointOfTime ) {
    // Check parameter.
    Check.checkInvalidParameterNull(pPointOfTime, "pPointOfTime");

    // Start of period is defined AND Parameter is before beginning of period.
    boolean lIsEnclosed = true;
    if (start != null && pPointOfTime.before(start) == true) {
      lIsEnclosed = false;
    }

    // Check of start successful. Now let's also check ends.
    if (end != null && lIsEnclosed == true && pPointOfTime.after(end) == true) {
      lIsEnclosed = false;
    }

    // Return result.
    return lIsEnclosed;
  }

  /**
   * Method checks whether the passed point of time represented by the Calendar date object is enclosed by the
   * represented period of time.
   * 
   * @param pPointOfTime Point of time for which will be checked if it is enclosed within the period of time. The
   * parameter must not be null.
   * @return boolean Method returns true is the passed date is within the period that is defined by this object and
   * false in all other cases.
   */
  public boolean isEnclosed( Calendar pPointOfTime ) {
    // Check parameter
    Check.checkInvalidParameterNull(pPointOfTime, "pPointOfTime");

    return this.isEnclosed(pPointOfTime.getTime());
  }

  /**
   * Method checks whether now is enclosed within the period that is defined by this object.
   * 
   * @return boolean The method returns true if now is enclosed within the period and false in all other cases.
   */
  public boolean isNowEnclosed( ) {
    return this.isEnclosed(new Date());
  }

  /**
   * Method checks whether this period and the passed period overlap. Two period do not overlap if one end before the
   * other one or it starts after it.
   * 
   * @param pOtherPeriod Period that should be check with this one for overlapping. The parameter must not be null.
   * @return boolean The method returns true if the passed period overlaps with this one and false in all other cases.
   */
  public boolean overlaps( Period pOtherPeriod ) {
    // Check parameter.
    Check.checkInvalidParameterNull(pOtherPeriod, "pOtherPeriod");

    // Two periods do not overlap if the end of the second period ends before the start of the first one or if the first
    // one ends before the second one starts.
    boolean lOverlaps;
    if (this.startsAfter(pOtherPeriod) || this.endsBefore(pOtherPeriod)) {
      lOverlaps = false;
    }
    else {
      lOverlaps = true;
    }
    return lOverlaps;
  }

  /**
   * Method checks whether this period and the passed periods overlap. Two period do not overlap if one end before the
   * other one or it starts after it.
   * 
   * @param pPeriods Collection of periods that should be check with this one for overlapping. The parameter must not be
   * null.
   * @return boolean The method returns true if at least one of the passed period overlaps with this one and false in
   * all other cases.
   */
  public boolean overlaps( Collection<Period> pPeriods ) {
    // Check parameter
    Check.checkInvalidParameterNull(pPeriods, "pPeriods");

    // Check if any of the passed period overlaps with this one.
    boolean lOverlaps = false;
    for (Period lNextPeriod : pPeriods) {
      if (this.overlaps(lNextPeriod) == true) {
        lOverlaps = true;
        break;
      }
    }

    // Return result
    return lOverlaps;
  }

  /**
   * Method returns the subset of the passed periods that overlap with this period.
   * 
   * @param pPeriods Collection of periods that should be checked for overlapping with this period. The parameter must
   * not be null.
   * 
   * @return {@link Collection} Collection with all periods that overlap with this period. The method never returns
   * null. In case that no periods overlap an empty collection will be returned.
   */
  public Collection<Period> getOverlappingPeriods( Collection<Period> pPeriods ) {
    // Check parameter.
    Check.checkInvalidParameterNull(pPeriods, "pPeriods");

    // Find all periods that overlap
    Collection<Period> lOverlappingPeriods = new ArrayList<>(pPeriods.size());
    for (Period lNextPeriod : pPeriods) {
      if (this.overlaps(lNextPeriod) == true) {
        lOverlappingPeriods.add(lNextPeriod);
      }
    }

    // Return collection with all overlapping periods.
    return lOverlappingPeriods;
  }

  /**
   * Method checks if this period starts after the passed period ends. This means that the end of the passed period must
   * end before or equal to the start of this period.
   * 
   * @param pOtherPeriod Period that should be compared to this one. The parameter must not be null.
   * @return boolean The method returns true if the passed period ends before this one and false in all other cases.
   */
  public boolean startsAfter( Period pOtherPeriod ) {
    // Check parameter.
    Check.checkInvalidParameterNull(pOtherPeriod, "pOtherPeriod");

    boolean lStartsAfter;
    if (start != null) {
      if (pOtherPeriod.end != null) {
        // If start is after or equal to the end of the passed period it ends before.
        lStartsAfter = start.compareTo(pOtherPeriod.end) >= 0;
      }
      // End of other period is null which means that it never ends.
      else {
        lStartsAfter = false;
      }
    }
    // Start of period is null. This means that this period starts at the beginning of time and thus no other period can
    // end before this period starts.
    else {
      lStartsAfter = false;
    }
    return lStartsAfter;
  }

  /**
   * Method checks if this period ends before the passed period starts. This means that the start of the passed period
   * must be equal or after this one ends.
   * 
   * @param pOtherPeriod Period that should be compared to this one. The parameter must not be null.
   * @return boolean The method returns true if the passed period starts after this one ends and false in all other
   * cases.
   */
  public boolean endsBefore( Period pOtherPeriod ) {
    // Check parameter.
    Check.checkInvalidParameterNull(pOtherPeriod, "pOtherPeriod");

    boolean lEndsBefore;
    if (end != null) {
      if (pOtherPeriod.start != null) {
        lEndsBefore = end.compareTo(pOtherPeriod.start) <= 0;
      }
      // Begin of other period is null which means that it starts at the beginning of time ;-)
      else {
        lEndsBefore = false;
      }
    }
    // End of period is null. This means that the period never ends and thus the other period can not start after this
    // one ends.
    else {
      lEndsBefore = false;
    }
    return lEndsBefore;
  }

  /**
   * Method returns the hashCode of this object. Therefore the start or end of the period is used.
   * 
   * @return HashCode of this object.
   */
  @Override
  public int hashCode( ) {
    int lHashCode;
    if (start != null) {
      lHashCode = start.hashCode();
    }
    else if (end != null) {
      lHashCode = end.hashCode();
    }
    else {
      lHashCode = 0;
    }
    return lHashCode;
  }

  /**
   * Method implements equals method as defined by its super class. Therefore the start or end of the period is used.
   * 
   * @param pObject Object that should be compared with this object. The parameter may be null.
   * @return The method returns true if the passed object is equal and false in all other cases.
   */
  @Override
  public boolean equals( Object pObject ) {
    boolean lIsEqual;
    if (pObject instanceof Period) {
      // Check start of period
      boolean lStartIsEqual;
      boolean lEndIsEqual;
      Period lOtherPeriod = (Period) pObject;

      // Start of period is set on this object
      Date lOtherPeriodStart = lOtherPeriod.getStart();
      if (start != null) {
        lStartIsEqual = start.equals(lOtherPeriodStart);
      }
      // Start of period is not set
      else {
        lStartIsEqual = (lOtherPeriodStart == null);
      }

      // Check end of period
      if (lStartIsEqual == true) {
        // End of period is set on this object
        Date lOtherPeriodEnd = lOtherPeriod.getEnd();
        if (end != null) {
          lEndIsEqual = end.equals(lOtherPeriodEnd);
        }
        // End of period is not set
        else {
          lEndIsEqual = (lOtherPeriodEnd == null);
        }
      }
      // Check of end of period is not required since its start is not equal.
      else {
        lEndIsEqual = false;
      }
      lIsEqual = lStartIsEqual && lEndIsEqual;
    }
    // Passed object is not a period or null.
    else {
      lIsEqual = false;
    }
    return lIsEqual;
  }

  /**
   * Method returns a string representation of this object describing the start and end of the period.
   * 
   * @return String representation of this object. The method never returns null.
   */
  public String toString( ) {
    return this.toString(DateStringRepresentation.TIMESTAMP);
  }

  /**
   * Method returns a string representation of this object describing the start and end of the period.
   * 
   * @return String representation of this object. The method never returns null.
   */
  public String toString( DateStringRepresentation pStringRepresentationType ) {
    StringBuilder lBuilder = new StringBuilder(64);

    lBuilder.append("Start: ");
    if (start != null) {
      lBuilder.append(this.toString(start, pStringRepresentationType));
    }
    else {
      lBuilder.append("null");
    }

    lBuilder.append(" End: ");
    if (end != null) {
      lBuilder.append(this.toString(end, pStringRepresentationType));
    }
    else {
      lBuilder.append("null");
    }
    return lBuilder.toString();
  }

  /**
   * Method returns a string representation of the passed date as
   * 
   * @param pDate Date that should be returned as string. The parameter must not be null.
   * @param pStringRepresentationType Way how the passed date should be represented as string.
   * @return String representation of the passed date. The method never returns null.
   */
  private String toString( Date pDate, DateStringRepresentation pStringRepresentationType ) {
    // Check parameter
    Check.checkInvalidParameterNull(pStringRepresentationType, "pStringRepresentationType");

    SimpleDateFormat lDateFormat;
    switch (pStringRepresentationType) {
      case DATE:
        lDateFormat = new SimpleDateFormat(DATE_PATTERN);
        break;

      case DATE_TIME:
        lDateFormat = new SimpleDateFormat(DATE_TIME_PATTERN);
        break;

      case DATE_TIME_SECONDS:
        lDateFormat = new SimpleDateFormat(DATE_TIME_SECONDS_PATTERN);
        break;

      case TIMESTAMP:
        lDateFormat = new SimpleDateFormat(TIMESTAMP_PATTERN);
        break;

      default:
        lDateFormat = null;
        Assert.unexpectedEnumLiteral(pStringRepresentationType);
    }
    return lDateFormat.format(pDate);
  }

  public enum DateStringRepresentation {
    DATE, DATE_TIME, DATE_TIME_SECONDS, TIMESTAMP;
  }
}
