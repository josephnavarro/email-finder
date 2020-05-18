package edu.depaul.email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailFinderTest {
  @Test
  @DisplayName("Tests for output file creation")
  void testOutputFile() {
    EmailFinder finder = new EmailFinder();
    String[] args = {"file:///C:/Users/Joey/Documents/GitHub/email-finder/src/test/resources/test-1.html"};
    finder.run(args);
  }

}