package edu.depaul.email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ListWriterTest {
  @Test
  @DisplayName("Tests constructor for ListWriter.")
  void testConstructor() {
    OutputStream stream = mock(OutputStream.class);
    ListWriter writer = new ListWriter(stream);
    assertNotNull(writer);
  }

  @Test
  @DisplayName("Tests for a list containing expected results after writing to a list")
  void testWriteList() {
    String string1 = "one";
    String string2 = "two";
    String string3 = "three";
    String expected = string1 + "\n" + string2 + "\n" + string3 + "\n";

    Collection<String> collection = new ArrayList<String>();
    collection.add(string1);
    collection.add(string2);
    collection.add(string3);

    OutputStream stream = new ByteArrayOutputStream();
    ListWriter writer = new ListWriter(stream);
    String streamContents = stream.toString();

    try {
      writer.writeList(collection);
      assertEquals(expected, streamContents);
    } catch (Exception err) {
      throw new EmailFinderException("Error ", err);
    }
  }

}