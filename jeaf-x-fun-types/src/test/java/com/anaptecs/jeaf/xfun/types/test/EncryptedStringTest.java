/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.types.test;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.anaptecs.jeaf.xfun.types.EncryptedString;

/**
 * Class tests the generation of a unique object id on base36 Creates three ids (next id, session id and class id) and
 * generates a String
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EncryptedStringTest {

  /**
   * Method tests the implementation of JEAF's encrypted string class.
   */
  @Test
  @Order(9)
  public void testEncryptedStringClass( ) {

    EncryptedString lEmptyString = new EncryptedString((String) null);
    EncryptedString lEncryptedString = new EncryptedString("Hello Encrypted World!");

    assertNull(lEmptyString.getValue(), "The value must be null.");
    assertNotNull(lEncryptedString.getValue(), "The value must be not null.");
    assertFalse(lEmptyString.equals(lEncryptedString), "The Encrypted strings should not be equal");
    assertTrue(lEncryptedString.equals(new EncryptedString("Hello Encrypted World!")),
        "The Encrypted strings should be equal");
    assertTrue(lEmptyString.equals(new EncryptedString((String) null)));
    assertFalse(lEncryptedString.equals(null));
    assertFalse(lEncryptedString.equals(new EncryptedString((String) null)));

    EncryptedString lClone = new EncryptedString(lEncryptedString);
    assertEquals(lEncryptedString, lClone);

    try {
      new EncryptedString((EncryptedString) null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      // Nothing to do.
    }

    assertEquals(lEncryptedString.getValue().hashCode(), lEncryptedString.hashCode());
    assertEquals(0, lEmptyString.hashCode());

    assertEquals("Hello Encrypted World!", lEncryptedString.toString());
    assertEquals(null, lEmptyString.toString());

  }
}
