/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.types.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.anaptecs.jeaf.xfun.types.Period;
import com.anaptecs.jeaf.xfun.types.Period.DateStringRepresentation;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Class tests the generation of a unique object id on base36 Creates three ids (next id, session id and class id) and
 * generates a String
 *
 * @author JEAF Development Team
 * @version 1.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PeriodTest {

  /**
   * Method tests the implementation of JEAF's period class.
   */
  @Test
  public void testPeriodWithDates( ) {
    Calendar lDate = Calendar.getInstance();
    lDate.set(2011, 0, 0, 0, 0, 0);
    lDate.set(Calendar.MILLISECOND, 0);
    Date l_2011_01_01 = lDate.getTime();
    lDate.set(2011, 11, 31);
    Date l_2011_12_31 = lDate.getTime();
    lDate.set(2010, 11, 31);
    Date l_2010_12_31 = lDate.getTime();
    lDate.set(2012, 0, 1);
    Date l_2012_01_01 = lDate.getTime();

    Period lPeriod = new Period(l_2011_01_01, l_2011_12_31);
    assertEquals(l_2011_01_01, lPeriod.getStart());
    assertEquals(l_2011_12_31, lPeriod.getEnd());
    assertEquals(false, lPeriod.hasOpenBeginning());
    assertEquals(false, lPeriod.hasOpenEnd());

    Period lPeriod_1 = new Period(null, l_2011_01_01);
    assertEquals(null, lPeriod_1.getStart());
    assertEquals(true, lPeriod_1.hasOpenBeginning());

    Period lPeriod_2 = new Period(l_2011_12_31, null);
    assertEquals(null, lPeriod_2.getEnd());
    assertEquals(true, lPeriod_2.hasOpenEnd());

    Period lPeriod_3 = new Period(null, l_2010_12_31);
    Period lPeriod_4 = new Period(l_2012_01_01, null);

    Period lOverlappingPeriod = new Period(l_2010_12_31, l_2011_12_31);

    assertTrue(lPeriod.startsAfter(lPeriod_1));
    assertFalse(lPeriod.startsAfter(lPeriod_2));

    assertTrue(lPeriod.endsBefore(lPeriod_2));
    assertTrue(lPeriod.endsBefore(lPeriod_4));
    assertFalse(lPeriod.endsBefore(lPeriod_1));
    assertFalse(lPeriod_2.endsBefore(lPeriod));

    assertFalse(lPeriod.overlaps(lPeriod_1));
    assertFalse(lPeriod.overlaps(lPeriod_3));
    assertFalse(lPeriod.overlaps(lPeriod_2));
    assertFalse(lPeriod_1.overlaps(lPeriod));
    assertFalse(lPeriod_2.overlaps(lPeriod));
    assertFalse(lPeriod.overlaps(lPeriod_4));
    assertTrue(lPeriod.overlaps(lPeriod));
    assertTrue(lPeriod.overlaps(Period.UNLIMITED_PERIOD));
    assertTrue(lPeriod.overlaps(lOverlappingPeriod));

    List<Period> lPeriods = new ArrayList<>();
    lPeriods.add(lOverlappingPeriod);
    assertTrue(lPeriod.overlaps(lPeriods));

    lPeriods.clear();
    lPeriods.add(lPeriod_1);
    lPeriods.add(lPeriod_2);
    lPeriods.add(lPeriod_3);
    lPeriods.add(lPeriod_4);
    assertFalse(lPeriod.overlaps(lPeriods));

    lPeriods.clear();
    lPeriods.add(Period.UNLIMITED_PERIOD);
    lPeriods.add(lOverlappingPeriod);
    assertTrue(lPeriod.overlaps(lPeriods));

    lPeriods.clear();
    lPeriods.add(lPeriod_1);
    lPeriods.add(lPeriod_2);
    lPeriods.add(lPeriod_3);
    lPeriods.add(lPeriod_4);
    lPeriods.add(Period.UNLIMITED_PERIOD);
    lPeriods.add(lOverlappingPeriod);
    Collection<Period> lOverlappingPeriods = lPeriod.getOverlappingPeriods(lPeriods);
    assertTrue(lOverlappingPeriods.contains(lOverlappingPeriod));
    assertTrue(lOverlappingPeriods.contains(Period.UNLIMITED_PERIOD));

    assertEquals("Start: 2010-12-31 00:00:00.000 End: 2011-12-31 00:00:00.000", lPeriod.toString());
    assertEquals("Start: null End: 2010-12-31 00:00:00.000", lPeriod_1.toString());
    assertEquals("Start: 2011-12-31 00:00:00.000 End: null", lPeriod_2.toString());

    DateStringRepresentation lFormat = DateStringRepresentation.TIMESTAMP;
    assertEquals("Start: 2010-12-31 00:00:00.000 End: 2011-12-31 00:00:00.000", lPeriod.toString(lFormat));
    assertEquals("Start: null End: 2010-12-31 00:00:00.000", lPeriod_1.toString(lFormat));
    assertEquals("Start: 2011-12-31 00:00:00.000 End: null", lPeriod_2.toString(lFormat));

    lFormat = DateStringRepresentation.DATE_TIME_SECONDS;
    assertEquals("Start: 2010-12-31 00:00:00 End: 2011-12-31 00:00:00", lPeriod.toString(lFormat));
    assertEquals("Start: null End: 2010-12-31 00:00:00", lPeriod_1.toString(lFormat));
    assertEquals("Start: 2011-12-31 00:00:00 End: null", lPeriod_2.toString(lFormat));

    lFormat = DateStringRepresentation.DATE_TIME;
    assertEquals("Start: 2010-12-31 00:00 End: 2011-12-31 00:00", lPeriod.toString(lFormat));
    assertEquals("Start: null End: 2010-12-31 00:00", lPeriod_1.toString(lFormat));
    assertEquals("Start: 2011-12-31 00:00 End: null", lPeriod_2.toString(lFormat));

    lFormat = DateStringRepresentation.DATE;
    assertEquals("Start: 2010-12-31 End: 2011-12-31", lPeriod.toString(lFormat));
    assertEquals("Start: null End: 2010-12-31", lPeriod_1.toString(lFormat));
    assertEquals("Start: 2011-12-31 End: null", lPeriod_2.toString(lFormat));

    try {
      lPeriod.toString(null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      // Nothing to do.
    }
  }

  /**
   * Method tests the implementation of JEAF's period class.
   */
  @Test
  public void testPeriodWithCalendar( ) {
    Calendar l_01_01_11 = Calendar.getInstance();
    l_01_01_11.clear();
    l_01_01_11.set(2011, 0, 1);

    Calendar l_31_12_11 = Calendar.getInstance();
    l_31_12_11.clear();
    l_31_12_11.set(2011, 11, 31);

    Calendar l_31_12_10 = Calendar.getInstance();
    l_31_12_10.clear();
    l_31_12_10.set(2010, 11, 31);

    Calendar l_01_01_12 = Calendar.getInstance();
    l_01_01_12.clear();
    l_01_01_12.set(2012, 0, 1);

    Period lPeriod = new Period(l_01_01_11, l_31_12_11);

    Period lPeriod_1 = new Period(null, l_01_01_11);
    Period lPeriod_2 = new Period(l_31_12_11, null);
    Period lPeriod_3 = new Period(null, l_31_12_10);
    Period lPeriod_4 = new Period(l_01_01_12, null);

    assertTrue(lPeriod.startsAfter(lPeriod_1));
    assertFalse(lPeriod.startsAfter(lPeriod_2));

    assertTrue(lPeriod.endsBefore(lPeriod_2));
    assertTrue(lPeriod.endsBefore(lPeriod_4));

    assertFalse(lPeriod.overlaps(lPeriod_1));
    assertFalse(lPeriod.overlaps(lPeriod_3));
    assertFalse(lPeriod.overlaps(lPeriod_2));
    assertFalse(lPeriod_1.overlaps(lPeriod));
    assertFalse(lPeriod_2.overlaps(lPeriod));
    assertFalse(lPeriod.overlaps(lPeriod_4));
    assertTrue(lPeriod.overlaps(lPeriod));
  }

  @Test
  public void testPeriodEnclosing( ) {
    Calendar lNow = Calendar.getInstance();
    Calendar lBefore = Calendar.getInstance();
    lBefore.set(Calendar.HOUR_OF_DAY, lBefore.get(Calendar.HOUR_OF_DAY) - 1);

    Calendar lAfter = Calendar.getInstance();
    lAfter.set(Calendar.MINUTE, lAfter.get(Calendar.MINUTE) + 1);

    Period lPeriod = new Period(lBefore, lAfter);
    assertTrue(lPeriod.isEnclosed(lNow.getTime()));
    assertTrue(lPeriod.isEnclosed(lNow));
    assertTrue(Period.UNLIMITED_PERIOD.isEnclosed(lNow));
    assertTrue(lPeriod.isNowEnclosed());

    lPeriod = new Period(lNow, lAfter);
    assertFalse(lPeriod.isEnclosed(lBefore));

    lPeriod = new Period(lBefore, lNow);
    assertFalse(lPeriod.isEnclosed(lAfter));

    // Test exception handling
    try {
      lPeriod.isEnclosed((Calendar) null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      // Nothing to do.
    }
    try {
      lPeriod.isEnclosed((Date) null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      // Nothing to do.
    }
  }

  @Test
  public void testEquals( ) {
    Calendar lBefore = Calendar.getInstance();
    lBefore.set(Calendar.HOUR_OF_DAY, lBefore.get(Calendar.HOUR_OF_DAY) - 1);

    Calendar lAfter = Calendar.getInstance();
    lAfter.set(Calendar.MINUTE, lAfter.get(Calendar.MINUTE) + 1);

    Period lPeriod = new Period(lBefore, lAfter);
    Period lEqualPeriod = new Period(lBefore.getTime(), lAfter.getTime());

    Period lClone = new Period(lPeriod);
    assertEquals(true, lPeriod.equals(lClone));

    lClone = new Period(Period.UNLIMITED_PERIOD);
    assertEquals(true, Period.UNLIMITED_PERIOD.equals(lClone));

    assertEquals(lPeriod.hashCode(), lEqualPeriod.hashCode());
    assertEquals(true, lPeriod.equals(lEqualPeriod));
    assertEquals(true, lEqualPeriod.equals(lEqualPeriod));
    assertEquals(true, lEqualPeriod.equals(lPeriod));
    assertEquals(false, lPeriod.equals(Period.UNLIMITED_PERIOD));
    assertEquals(false, lPeriod.equals(new Period(lBefore, null)));
    assertEquals(false, lPeriod.equals(new Period(null, lAfter)));
    assertEquals(false, lPeriod.equals(null));

    lPeriod = new Period(null, lAfter);
    assertEquals(false, lPeriod.equals(new Period(null, lBefore)));
    assertEquals(false, lPeriod.equals(new Period(lBefore, lAfter)));

    lPeriod = new Period(lBefore, null);
    assertEquals(false, lPeriod.equals(new Period(lAfter, null)));
    assertEquals(false, lPeriod.equals(new Period(lBefore, lAfter)));

    assertEquals(new Period(lBefore, null).hashCode(), new Period(lBefore, null).hashCode());
    assertEquals(new Period((Date) null, (Date) null).hashCode(), new Period((Date) null, (Date) null).hashCode());
    assertEquals(new Period(null, lAfter).hashCode(), new Period(null, lAfter).hashCode());
  }
}
