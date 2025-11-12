/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.types;

import java.io.Serializable;
import java.util.Arrays;

import com.anaptecs.jeaf.xfun.api.XFunMessages;
import com.anaptecs.jeaf.xfun.api.checks.Assert;
import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.errorhandling.ErrorCode;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;

/**
 * This class represents a number that is encoded using base 36. Base 36 encoded numbers have 36 possible values for one
 * digit. Therefore the characters 'A'-'Z' and '0'-'9' are used where '0' is the smallest value '9' is smaller than 'A'
 * and 'Z' is the largest value for a digit. Since this class is intended to be used as primary key on a database it is
 * internally organized unlike the natural order of numbers. This means if you read the digits from left to right that
 * the most left digit has the smallest weight.
 * 
 * Sample: "123#" is bigger that "3210###" since the third digit has the highest weight.
 * 
 * The class does currently not support negative values.
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
public class Base36 implements Serializable, Comparable<Base36> {
  /**
   * Constant defines the offset of digits compared with characters.
   */
  private static final int DIGIT_OFFSET = 10;

  /**
   * Serial version uid of this class.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Character array contains all possible values for one base 36 encoded digit. The values are ordered to their natural
   * order.
   */
  private static final char[] VALUES = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C',
    'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

  /**
   * Constant for the value 1 encoded as base 36 char array.
   */
  private static final char[] ONE = new char[] { VALUES[1] };

  /**
   * Constant for the fixed size place holder as it is used by the Persistence Framework Avantis Unisuite. This constant
   * is only used for compatibility with it.
   */
  private static final char AVANTIS_EMPTY_CHAR = '#';

  /**
   * Attribute contains the divisor to create a base36-number.
   */
  private static final int BASE = 36;

  /**
   * Regular expression pattern that describes structure of a base 36 encoded string.
   */
  private static final String BASE36_NUMBER_PATTERN = "[0-9A-Z]+[#]*";

  /**
   * Base 36 encoded representation of a number. The reference is never null since this value has to be passed to the
   * class' constructor. The values are ordered in a way that is reversed to the natural order of numbers. This means if
   * you read the digits from left to right that the most left digit has the smallest weight.
   * 
   * Sample: "123#" is bigger that "3210###" since the third digit has the highest weight.
   */
  private final char[] value;

  /**
   * Create new Base36 object with the passed value.
   * 
   * @param pValue Value to initialize the object. All characters of the passed string must be one of the following
   * characters: [0-9;A-Z;#]. The parameter must not be null.
   * @param pDigits Number of digits of which the base 36 number consists at a maximum. The passed string must not have
   * more characters than digits but may have less.
   */
  public Base36( String pValue, int pDigits ) {
    // Check of parameters not required. All parameters are checked by the called method.
    value = this.toBase36Chars(pValue, pDigits);
  }

  /**
   * Create new Base36 object with the integer passed value.
   * 
   * @param pValue Value to initialize the object.
   * @param pDigits Number of digits of which the base 36 number consists at a maximum. The passed integer value must
   * not have more characters than digits but may have less.
   */
  public Base36( int pValue, int pDigits ) {
    char[] emptyValue = new char[pDigits];
    Arrays.fill(emptyValue, AVANTIS_EMPTY_CHAR);
    value = this.add(emptyValue, this.toBase36Chars(pValue));
    // Check maximum length of base 36 encoded value.
    Check.checkMaxStringLength(new String(value), pDigits, "pValue");
  }

  /**
   * Initialize object. Therefore a string has to be passed to this method to set the value of the base 36 encoded
   * number.
   * 
   * @param pValue The passed String must match to the pattern [0-9A-Z]+[#]* in order to be a valid base 36 encoded
   * number. The parameter must not be null.
   */
  public Base36( String pValue ) {
    // Check parameters
    Check.checkInvalidParameterNull(pValue, "pValue");

    // Convert passed String to base 36 encoded number.
    value = this.toBase36Chars(pValue, pValue.length());
  }

  /**
   * Method returns the maximum length of the base 36 encoded number.
   * 
   * @return int Maximum length of this base 36 number.
   */
  public final int getMaxLength( ) {
    return value.length;
  }

  /**
   * Method adds the passed Base36 number to this number and returns the result.
   * 
   * @param pValueToAdd Value to add to this object. The parameter may be null. In this case the method returns this
   * object.
   * @return Base36 Result of the addition. The method never returns null.
   */
  public final Base36 add( Base36 pValueToAdd ) {
    // No parameter check required.
    Base36 lResult;
    if (pValueToAdd != null) {
      final char[] lResultArray = this.add(value, pValueToAdd.value);
      lResult = new Base36(new String(lResultArray), lResultArray.length);
    }
    // Nothing to add.
    else {
      lResult = this;
    }
    // Return result.
    return lResult;
  }

  /**
   * Method adds the passed value to this base 36 encoded number.
   * 
   * @param pAddValue Integer value to add to this object. The parameter must not be negative.
   * @return {@link Base36} New base 36 encoded number with the result of the addition. The method never returns null.
   */
  public final Base36 add( int pAddValue ) {
    // Check parameter.
    Check.checkIsZeroOrGreater(pAddValue, "pAddValue");

    // Convert int value to base 36 number, add it to this object and return the result.
    final char[] lValueAsBase36Chars = this.toBase36Chars(pAddValue);
    final char[] lResultValue = this.add(value, lValueAsBase36Chars);
    return new Base36(new String(lResultValue));
  }

  /**
   * Method increments this base 36 number by 1 and returns the new number.
   * 
   * @return Base36 New base 36 number that is larger than this object by 1. The method never returns null.
   */
  public final Base36 increment( ) {
    // Increment this object by 1 and return the result.
    final char[] lResultArray = this.add(value, ONE);
    return new Base36(new String(lResultArray), lResultArray.length);
  }

  /**
   * Method adds the two base 36 encoded char[] and returns the result.
   * 
   * @param pFirstValue Character array with the first summand. The parameter must not be null.
   * @param pSecondValue Character array with the second summand. The parameter must not be null.
   * @return char[] Result of the addition. The method never returns null.
   */
  private char[] add( char[] pFirstValue, char[] pSecondValue ) {
    // Check parameters.
    Assert.assertNotNull(pFirstValue, "pFirstValue");
    Assert.assertNotNull(pSecondValue, "pSecondValue");

    // Create new array for result.
    final int lSize = Math.max(pFirstValue.length, pSecondValue.length);
    char[] lResult = new char[lSize];

    final int lFirstValueLength = pFirstValue.length;
    final int lSecondValueLength = pSecondValue.length;

    int lOverflow = 0;
    int lDigitValue = 0;
    for (int i = 0; i < lSize; i++) {
      // Get next value for first summand.
      int lFirstDigit;
      if (i < lFirstValueLength) {
        lFirstDigit = this.toIntValue(pFirstValue[i]);
      }
      else {
        lFirstDigit = 0;
      }

      // Get next value for second summand.
      int lSecondDigit;
      if (i < lSecondValueLength) {
        lSecondDigit = this.toIntValue(pSecondValue[i]);
      }
      else {
        lSecondDigit = 0;
      }

      int lSum = lFirstDigit + lSecondDigit + lOverflow;

      lOverflow = lSum / BASE;
      lDigitValue = lSum % BASE;
      lResult[i] = VALUES[lDigitValue];
    }
    // Check if there is an overflow on the last digit. If yes this means that were ran over the maximum possible
    // number.
    if (lOverflow > 0) {
      final ErrorCode lErrorCode = XFunMessages.MAX_BASE36_VALUE_EXCEEDED;
      String[] lParams = new String[] { new String(pFirstValue), new String(pSecondValue) };
      throw new JEAFSystemException(lErrorCode, lParams);
    }

    // if there are 0 at the end of the number, replace them with #
    boolean lZero = true;
    int lResLength = lResult.length - 1;
    while (lZero) {
      if (lResult[lResLength] == '0') {
        lResult[lResLength] = AVANTIS_EMPTY_CHAR;
      }
      else {
        lZero = false;
      }
      lResLength--;
      if (lResLength < 1) {
        lZero = false;
      }

    }

    // Return result of addition.
    return lResult;
  }

  /**
   * Method converts the passed base 36 encoded String object to a character array with the passed length. If the
   * resulting character array has more digits than the passed string characters then the returned char[] will be filled
   * up with <code>AVANTIS_EMPTY_CHAR</code>.
   * 
   * @param pValue Base 36 encoded String. The parameter must not be null must must only contain characters that match
   * to the defined pattern for base 36 numbers.
   * @param pDigits Number of digits that the returned char[] should have. The parameter must not be smaller than the
   * length of the passed string.
   * @return char[] Character array containing the base 36 encoded number. The method never returns null.
   * 
   * @see #AVANTIS_EMPTY_CHAR
   */
  private char[] toBase36Chars( String pValue, int pDigits ) {
    // Check parameters.
    Check.checkInvalidParameterNull(pValue, "pValue");
    Check.checkPattern(pValue, BASE36_NUMBER_PATTERN);
    Check.checkMaxStringLength(pValue, pDigits, "pValue");

    // Create character array to store the value of this object.
    char[] lValue;
    final int lLength = pValue.length();
    final char[] lValueAsChars = pValue.toCharArray();
    if (lLength < pDigits) {
      // Create array to store the value
      lValue = new char[pDigits];

      System.arraycopy(lValueAsChars, 0, lValue, 0, lLength);
      Arrays.fill(lValue, lLength, pDigits, AVANTIS_EMPTY_CHAR);
    }
    // The case that the passed string is longer than pDigits is already checked above.
    else {
      lValue = lValueAsChars;
    }
    return lValue;
  }

  /**
   * Method converts the passed integer value to a base 36 encoded representation (as char[]).
   * 
   * @param pValue Integer value that should be encoded as base 36 number. The parameter must not be negative.
   * @return char[] Base 36 encoded character array representing the passed integer value. The method never returns
   * null.
   */
  private char[] toBase36Chars( int pValue ) {
    // Check that parameter is positive
    Assert.assertIsZeroOrGreater(pValue, "pValue");

    // Calculate number of digits that are required to store the integer.
    int lRequiredDigits = 0;
    int lRemains = pValue;
    do {
      lRemains = lRemains / BASE;
      lRequiredDigits++;
    }
    while (lRemains > 0);

    // Create char array for base 36 representation.
    char[] lResult = new char[lRequiredDigits];

    // Convert integer to base 36 encoded chars.
    int lLeftValue = pValue;

    for (int i = 0; i < lRequiredDigits; i++) {
      // Calculate value of next digit.
      lResult[i] = VALUES[(lLeftValue % BASE)];
      lLeftValue = lLeftValue / BASE;
    }
    // Return result of calculation.
    return lResult;
  }

  /**
   * Method returns the array index of the highest digit of this base 36 number.
   * 
   * @return int Array index of the internal representation of a base 36 number of the highest digit of this object.
   */
  private int getIndexOfHighestDigit( ) {
    int lHighestDigit = 0;
    for (int i = value.length - 1; i >= 0; i--) {
      // Get numerical representation of current digit.
      int lCurrentIntValue = this.toIntValue(value[i]);

      // If the current digit is more than zero the highest digit is found.
      if (lCurrentIntValue > 0) {
        lHighestDigit = i;
        break;
      }
    }
    // Return index of highest digit.
    return lHighestDigit;
  }

  /**
   * Method returns the int value of the passed base36 encoded char.
   * 
   * @param pBase36Char Base 36 encoded character that should be converted to an integer value.
   * @return int Integer representation of the passed base 36 character.
   */
  private int toIntValue( char pBase36Char ) {
    // Character is between 0 and 9.
    int lIntValue;
    if ('0' <= pBase36Char && pBase36Char <= '9') {
      lIntValue = pBase36Char - '0';
    }
    // Character is between A and Z.
    else if ('A' <= pBase36Char && pBase36Char <= 'Z') {
      lIntValue = pBase36Char - 'A' + DIGIT_OFFSET;
    }
    else if (AVANTIS_EMPTY_CHAR == pBase36Char) {
      lIntValue = 0;
    }
    // Invalid character
    else {
      // This should never happen since the the passed characters are always checked for correct characters before.
      String[] lParams = new String[] { Character.toString(pBase36Char) };
      throw new JEAFSystemException(XFunMessages.INVALID_BASE_36_DIGIT, lParams);
    }
    // Return integer value of passed character.
    return lIntValue;
  }

  /**
   * Method returns a String representation of the base 36 encoded value.
   * 
   * @return {@link String} String representation of this number. The method never returns null.
   */
  public final String toString( ) {
    return new String(value);
  }

  /**
   * Method tries to convert the represented base 36 coded value to an integer. This is possible as long as INTEGER_MAX
   * is not reached.
   * 
   * @return long Long value of this objects.
   */
  public final long toLong( ) {
    long lDigitPositionValue = 1;
    long lLongValue = 0;

    // Loop over all digits to calculate the integer value of this object.
    for (int i = 0; i < value.length; i++) {
      // Multiply current digit with its base and add it to current integer value
      lLongValue = lLongValue + (this.toIntValue(value[i]) * lDigitPositionValue);
      // Increment base to value for next digit.
      lDigitPositionValue = lDigitPositionValue * BASE;
    }
    // Return integer value.
    return lLongValue;
  }

  /**
   * Method tries to convert the represented base 36 coded value to an integer. This is possible as long as INTEGER_MAX
   * is not reached.
   * 
   * @return int Integer value of this objects.
   */
  public final int toInteger( ) {
    return (int) this.toLong();
  }

  /**
   * Method checks if the passed object is equal to this one. The method only returns true if the passed object is an
   * instance of class Base36 and its value is equal to this one.
   * 
   * @param pObject Object to be compared for equality. The parameter may be null.
   * @return boolean Method returns true only if the passed object is a Base36 object with the same value. In all other
   * cases the method returns false.
   * 
   * @see Object#equals(Object)
   */
  public final boolean equals( Object pObject ) {
    boolean lIsEqual;
    // Passed object is not null.
    if (pObject != null) {
      // Check if the internal representation of the base 36 number is the same.
      if (pObject instanceof Base36) {
        int lCompareResult = this.compareTo((Base36) pObject);
        if (lCompareResult == 0) {
          lIsEqual = true;
        }
        else {
          lIsEqual = false;
        }
      }
      // Passed object is not an instance of class Base36.
      else {
        lIsEqual = false;
      }
    }
    // In the case the null is passed to the method, false has to be returned.
    else {
      lIsEqual = false;
    }
    // Return result.
    return lIsEqual;
  }

  /**
   * Method returns the hash code of this Base36 object.
   * 
   * @return int Hash code of this Base36 object.
   * 
   * @see Object#hashCode()
   */
  public final int hashCode( ) {
    return Arrays.hashCode(value);
  }

  /**
   * Method compares this Base36 object with another.
   * 
   * @param pAnotherBase36 object to be compared
   * @return 0 if pAnotherBase36 is equal to this object, value less than 0 if this object is less than pAnotherBase36
   * and value greater than 0 if this object is greater than pAnotherBase36
   * 
   * @see Comparable#compareTo(Object)
   */
  public final int compareTo( Base36 pAnotherBase36 ) {
    // Check parameter.
    Check.checkInvalidParameterNull(pAnotherBase36, "pAnotherBase36");

    final int lThisHighestDigit = this.getIndexOfHighestDigit();
    final int lParamHighestDigit = pAnotherBase36.getIndexOfHighestDigit();
    final int lHighestDigit = Math.max(lThisHighestDigit, lParamHighestDigit);
    // Compare all values with each other.
    int lResult = 0;
    for (int i = lHighestDigit; i >= 0; i--) {
      // Get current digit of this base 36 number.
      int lThisCurrentDigit;
      if (i < value.length) {
        lThisCurrentDigit = this.toIntValue(value[i]);
      }
      else {
        lThisCurrentDigit = 0;
      }

      // Get current digit of the passed base 36 number.
      int lParamCurrentDigit;
      if (i < pAnotherBase36.value.length) {
        lParamCurrentDigit = this.toIntValue(pAnotherBase36.value[i]);
      }
      else {
        lParamCurrentDigit = 0;
      }
      // Compare current digits with each other.
      lResult = lThisCurrentDigit - lParamCurrentDigit;
      if (lResult != 0) {
        break;
      }
    }

    // Return result.
    return lResult;
  }
}
