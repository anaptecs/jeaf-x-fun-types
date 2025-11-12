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

import com.anaptecs.jeaf.xfun.api.XFunMessages;
import com.anaptecs.jeaf.xfun.api.errorhandling.SystemException;
import com.anaptecs.jeaf.xfun.types.Base36;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
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
public class Base36Test {

  /**
   * Test of constructor. Creates Base36-objects
   *
   * @throws Exception if an error occurs during the execution of the test case.
   */
  @Test
  @Order(1)
  public void testCreateBase36Id( ) throws Exception {
    Base36 lNumber_1 = new Base36("1", 3);
    assertEquals(3, lNumber_1.getMaxLength());
    Base36 lNumber_2 = new Base36("123", 5);
    assertEquals(5, lNumber_2.getMaxLength());
    assertTrue(lNumber_1.compareTo(lNumber_2) < 0);
    assertTrue(lNumber_2.compareTo(lNumber_1) > 0);
    assertTrue(lNumber_2.compareTo(lNumber_2) == 0);

    // Test exception handling when trying to create a new base 36 number with less digits than required.
    try {
      new Base36("ABC", 2);
      fail("Creation of a base 36 number with a string of lenght 3 and max 2 digits must fail.");
    }
    catch (IllegalArgumentException e) {
      // Nothing to do.
    }

    // Test creation of Base36 object with integer value.
    int lIntegerValue = 1106;
    Base36 lBase36 = new Base36(lIntegerValue, 3);
    assertEquals("QU#", lBase36.toString());
    assertEquals(lIntegerValue, lBase36.toInteger(), "Base36.toInteger()");

    lBase36 = new Base36(2589212, 6);
    assertEquals("KUHJ1#", lBase36.toString());
    assertEquals(2589212, lBase36.toInteger());
  }

  /**
   * Test of constructor. Tries to create a base36-object with an error parameter.
   *
   * @throws Exception because "-ab�" is not allowed.
   */
  @Test
  @Order(2)
  public void testCreateBase36IdException( ) throws Exception {

    try {
      new Base36("-ab�");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      // Nothing to do.
    }
  }

  /**
   * Test of method equals(). b36_0 and b36_3 have the same values!
   *
   */
  @SuppressWarnings("unlikely-arg-type")
  @Test
  @Order(3)
  public void testBase36Equals( ) throws Exception {
    Base36 lNumber_1 = new Base36("123", 3);
    Base36 lNumber_2 = new Base36("123", 5);
    Base36 lNumber_3 = new Base36("1A", 5);

    assertTrue(lNumber_1.equals(lNumber_2));
    assertTrue(lNumber_2.equals(lNumber_1));
    assertTrue(lNumber_1.equals(lNumber_1));
    String lObject = "";
    assertFalse(lNumber_1.equals(lObject));
    assertFalse(lNumber_1.equals(null));
    assertFalse(lNumber_1.equals(lNumber_3));
    assertFalse(lNumber_3.equals(lNumber_1));
  }

  /**
   * test of method hashCode(). Gets the hashcode of the objects.
   */
  @Test
  @Order(4)
  public void testBase36HashCode( ) throws Exception {
    Base36 lNumber_1 = new Base36("7A96D", 5);
    assertEquals(81415740, lNumber_1.hashCode());
  }

  /**
   * test of compare(Base36, Base36). compares 2 base36-objects
   *
   */
  @Test
  @Order(5)
  public void testBase36Compare( ) throws Exception {
    Base36 lNumber_1 = new Base36("7A96D", 5);
    Base36 lNumber_2 = new Base36("7A96D", 5);
    Base36 lNumber_3 = new Base36("7A96D", 7);
    Base36 lNumber_Smaller = new Base36("7A96C", 5);
    Base36 lNumber_SmallerSmaller = new Base36("7A9", 3);
    assertEquals(0, lNumber_1.compareTo(lNumber_2));
    assertEquals(1, lNumber_1.compareTo(lNumber_Smaller));
    assertEquals(-1, lNumber_Smaller.compareTo(lNumber_1));
    assertTrue(lNumber_1.compareTo(lNumber_SmallerSmaller) > 0);
    assertTrue(lNumber_SmallerSmaller.compareTo(lNumber_1) < 0);

    assertEquals(0, lNumber_1.compareTo(lNumber_3));
    assertEquals(0, lNumber_1.compareTo(lNumber_2));
    assertEquals(0, lNumber_3.compareTo(lNumber_1));

  }

  /**
   * test of method add(Base36). Adds a Base36-value to another.
   *
   * @throws Exception when sum is out of range.
   */
  @Test
  @Order(6)
  public void testBase36Add( ) throws Exception {
    Base36 lNumber_1 = new Base36("7A96D", 5);
    Base36 lNumber_2 = new Base36("3", 5);
    Base36 lNumber_3 = lNumber_1.add(lNumber_2);
    assertEquals("AA96D", lNumber_3.toString());
    assertEquals("AA96D", lNumber_1.add(new Base36("3", 1)).toString());
    assertEquals("AA96D", new Base36("3", 1).add(lNumber_1).toString());

    int lInteger = lNumber_1.toInteger();
    Base36 lNumber_4 = lNumber_1.add(100);
    assertEquals(lInteger + 100, lNumber_4.toInteger(), "Base36.toInteger()");
    assertEquals(new Base36(lInteger + 100, 5), lNumber_4);

    Base36 lNumberToIncrement = lNumber_1;
    for (int i = 0; i < 100; i++) {
      lNumberToIncrement = lNumberToIncrement.increment();
    }
    assertEquals(lNumberToIncrement, lNumber_4);

    new Base36(35, 2);
    Base36 l36 = new Base36(36, 2);
    assertEquals("01", l36.toString());
    assertEquals(1, new Base36(1, 1).toInteger());
    assertEquals("1", new Base36(1, 1).toString());
    assertEquals("1#", new Base36(1, 2).toString());

    assertEquals(lNumber_1, lNumber_1.add(null));
  }

  /**
   * test of method increment(int). Increments a Base36 object by an integer.
   *
   * @throws Exception when sum is out of range.
   */
  @Test
  @Order(7)
  public void testBase36Increment( ) throws Exception {

    // Test overflow.
    final Base36 lNumber_3 = new Base36("Z", 2).increment();
    assertEquals("01", lNumber_3.toString());

    // Try to create real overflow and test exception handling
    try {
      new Base36("Z", 1).increment();
      fail("Clase should throw exception in case of an overflow when adding a value.");
    }
    catch (SystemException e) {
      assertEquals(XFunMessages.MAX_BASE36_VALUE_EXCEEDED, e.getErrorCode(), "Wrong error code.");
    }

  }
}
