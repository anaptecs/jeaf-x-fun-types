/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.types;

/**
 * Enumeration represents the gender of person e.g. As the gender is sometimes unknown the enumeration defines besides
 * the cases male and female also the case unknown.
 */
public enum Gender {
  /**
   * Male gender.
   */
  MALE("male"),

  /**
   * Female gender.
   */
  FEMALE("female"),

  /**
   * Enum for third gender
   */
  THIRD_GENDER("third_gender"),

  /**
   * Unknown gender.
   */
  UNKNOWN("unknown");

  /**
   * Initialize object.
   */
  private Gender( String pGender ) {
    genderName = pGender;
  }

  /**
   * Name of the gender. This is currently not a localized text. This is planed for later releases.
   */
  private String genderName;

  /**
   * Method returns the attribute "genderName".
   * 
   * @return String Value to which the attribute "gender" is set.
   */
  public String getGenderName( ) {
    return genderName;
  }

  @Override
  public String toString( ) {
    String lGenderString;
    if (this == MALE) {
      lGenderString = "Male";
    }
    else if (this == FEMALE) {
      lGenderString = "Female";
    }
    else if (this == THIRD_GENDER) {
      lGenderString = "Third Gender";
    }
    else {
      lGenderString = "Unknown";
    }
    return lGenderString;
  }
}
