/**
 * Copyright 2004 - 2014 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.types;

import java.io.Serializable;

import com.anaptecs.jeaf.xfun.api.checks.Check;

public class EncryptedString implements Serializable {
  /**
   * Default serial version UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constant for the name of the value attribute
   */
  public static final String VALUE_ATTRIBUTE = "value";

  /**
   * Attribute represents the value of the string. The attribute may be null which means there is no value.
   */
  private final String value;

  /**
   * Constant for convenience purpose that represents an unlimited period.
   */
  public static final EncryptedString EMPTY_STRING = new EncryptedString((String) null);

  /**
   * Initialize new period object.
   * 
   * @param pValue The value of the encrypted String. The parameter may be null.
   */
  public EncryptedString( String pValue ) {
    value = pValue;
  }

  /**
   * Initialize new period object.
   * 
   * @param pEncryptedString The value of the encrypted String. The parameter must not be null.
   */
  public EncryptedString( EncryptedString pEncryptedString ) {
    // Check parameter
    Check.checkInvalidParameterNull(pEncryptedString, "pEncryptedString");

    value = pEncryptedString.value;
  }

  /**
   * Method returns the value of the encrypted String.
   * 
   * @return {@link String} The value of the encrypted String. The method will return null if the value is not set.
   */
  public String getValue( ) {
    return value;
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
    if (pObject instanceof EncryptedString) {
      // Check start of period
      EncryptedString lOtherEncryptedString = (EncryptedString) pObject;

      // Start of period is set on this object
      String lOtherValue = lOtherEncryptedString.getValue();
      if (value != null) {
        lIsEqual = value.equals(lOtherValue);
      }
      // This value is null
      else {
        lIsEqual = (lOtherValue == null);
      }

    }
    // Passed object is not a period or null.
    else {
      lIsEqual = false;
    }
    return lIsEqual;
  }

  /**
   * Method returns the hashCode of this object. Therefore the value is used.
   * 
   * @return HashCode of this object.
   */
  @Override
  public int hashCode( ) {
    int lHashCode;
    if (value != null) {
      lHashCode = value.hashCode();
    }
    else {
      lHashCode = 0;
    }
    return lHashCode;
  }

  /**
   * Method returns a string representation of this object describing the start and end of the period.
   * 
   * @return String representation of this object. The method never returns null.
   */
  public String toString( ) {
    return value;
  }

}
