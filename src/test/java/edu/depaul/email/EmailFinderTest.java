package edu.depaul.email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class EmailFinderTest {
  private static final String EMAIL_PATH = "email.txt";
  private static final String BADLINKS_PATH = "badlinks.txt";
  private static final String GOODLINKS_PATH = "good-links.txt";
  private static final String URL = "src\\test\\resources\\test-7.html";

  /**
   * Removes all temporary storage files (not a test).
   */
  private void cleanStorage() {
    // Delete email storage file
    try {
      Files.deleteIfExists(Paths.get(EMAIL_PATH));
    } catch (NoSuchFileException e) {
      System.out.println("No such file/directory exists");
    } catch (DirectoryNotEmptyException e) {
      System.out.println("Directory is not empty.");
    } catch (IOException e) {
      System.out.println("Invalid permissions.");
    }

    // Delete badlinks storage file
    try {
      Files.deleteIfExists(Paths.get(BADLINKS_PATH));
    } catch (NoSuchFileException e) {
      System.out.println("No such file/directory exists");
    } catch (DirectoryNotEmptyException e) {
      System.out.println("Directory is not empty.");
    } catch (IOException e) {
      System.out.println("Invalid permissions.");
    }

    // Delete goodlinks storage file
    try {
      Files.deleteIfExists(Paths.get(GOODLINKS_PATH));
    } catch (NoSuchFileException e) {
      System.out.println("No such file/directory exists");
    } catch (DirectoryNotEmptyException e) {
      System.out.println("Directory is not empty.");
    } catch (IOException e) {
      System.out.println("Invalid permissions.");
    }
  }

  @Test
  @DisplayName("Tests passing a single argument to run().")
  void testRunSingleArg() {
    cleanStorage();

    String[] args = {URL};
    EmailFinder.main(args);

    File file1 = new File(BADLINKS_PATH);
    File file2 = new File(GOODLINKS_PATH);
    File file3 = new File(EMAIL_PATH);

    assertAll(
      () -> assertTrue(file1.exists(), BADLINKS_PATH + " not found"),
      () -> assertTrue(file2.exists(), GOODLINKS_PATH + " not found"),
      () -> assertTrue(file3.exists(), EMAIL_PATH + " not found")
    );
  }

  @Test
  @DisplayName("Tests passing multiple arguments to run().")
  void testRunMultipleArgs() {
    cleanStorage();

    String[] args = {URL, "1"};
    EmailFinder.main(args);

    File file1 = new File(BADLINKS_PATH);
    File file2 = new File(GOODLINKS_PATH);
    File file3 = new File(EMAIL_PATH);

    assertAll(
      () -> assertTrue(file1.exists(), BADLINKS_PATH + " not found"),
      () -> assertTrue(file2.exists(), GOODLINKS_PATH + " not found"),
      () -> assertTrue(file3.exists(), EMAIL_PATH + " not found")
    );
  }

  @Test
  @DisplayName("Tests passing no arguments to run().")
  void testRunNoArgs() {
    cleanStorage();

    String[] args = {};
    EmailFinder.main(args);

    File file1 = new File(BADLINKS_PATH);
    File file2 = new File(GOODLINKS_PATH);
    File file3 = new File(EMAIL_PATH);

    assertAll(
      () -> assertFalse(file1.exists(), BADLINKS_PATH + " not found"),
      () -> assertFalse(file2.exists(), GOODLINKS_PATH + " not found"),
      () -> assertFalse(file3.exists(), EMAIL_PATH + " not found")
    );
  }

  @Test
  @DisplayName("Tests for creation of output files.")
  void testOutputFile() {
    cleanStorage();

    EmailFinder finder = new EmailFinder();
    String[] args = {URL};
    finder.run(args);
    File file1 = new File(BADLINKS_PATH);
    File file2 = new File(GOODLINKS_PATH);
    File file3 = new File(EMAIL_PATH);

    assertAll(
      () -> assertTrue(file1.exists(), BADLINKS_PATH + " not found"),
      () -> assertTrue(file2.exists(), GOODLINKS_PATH + " not found"),
      () -> assertTrue(file3.exists(), EMAIL_PATH + " not found")
    );
  }

  @Test
  @DisplayName("Tests EmailFinder end-to-end performance.")
  void testPerformance() {
    cleanStorage();

    long start;
    long end;
    long elapsed;
    long total = 0L;
    long maxTime = 5000L;  // Target time is 5 seconds
    int passes = 10;

    EmailFinder finder = new EmailFinder();
    String[] args = {URL};

    // Perform multiple passes and check average time
    for (int i = 0; i != passes; i++) {
      start = System.currentTimeMillis();
      finder.run(args);
      end = System.currentTimeMillis();
      elapsed = end - start;
      total = total + elapsed;
    }

    long average = total / passes;

    assertTrue(average < maxTime);
  }

}