/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.types.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.anaptecs.jeaf.xfun.types.Gender;

public class GenderTest {
  @Test
  public void testGender( ) {
    assertEquals("male", Gender.MALE.getGenderName());
    assertEquals("female", Gender.FEMALE.getGenderName());
    assertEquals("third_gender", Gender.THIRD_GENDER.getGenderName());
    assertEquals("unknown", Gender.UNKNOWN.getGenderName());

    assertEquals("Male", Gender.MALE.toString());
    assertEquals("Female", Gender.FEMALE.toString());
    assertEquals("Third Gender", Gender.THIRD_GENDER.toString());
    assertEquals("Unknown", Gender.UNKNOWN.toString());
  }
}
