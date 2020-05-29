package edu.depaul.email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PageCrawlerTest {
  private static final String EMAIL_PATH = "src/test/resources/email.txt";
  private static final String BADLINKS_PATH = "src/test/resources/badlinks.txt";
  private static final String GOODLINKS_PATH = "src/test/resources/goodlinks.txt";

  /**
   *  Removes all temporary storage files (not a test).
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

  /**
   * Instantiates an actual local StorageService.
   */
  private StorageService makeStorage() {
    StorageService storage = new StorageService();
    storage.addLocation(StorageService.StorageType.EMAIL, EMAIL_PATH);
    storage.addLocation(StorageService.StorageType.BADLINKS, BADLINKS_PATH);
    storage.addLocation(StorageService.StorageType.GOODLINKS, GOODLINKS_PATH);
    return storage;
  }

  @Test
  @DisplayName("Tests PageCrawler construction without max emails given.")
  void testConstructorNoMaxEmails() {
    StorageService storage = mock(StorageService.class);
    PageCrawler crawler = new PageCrawler(storage);
    assertNotNull(crawler);
  }

  @Test
  @DisplayName("Tests PageCrawler construction with max emails given.")
  void testConstructorWithMaxEmails() {
    StorageService storage = mock(StorageService.class);
    PageCrawler crawler = new PageCrawler(storage, 20);
    assertNotNull(crawler);
  }

  @Test
  @DisplayName("Tests output file creation for bad link detection.")
  void testCrawlBadLinks() {
    cleanStorage();
    StorageService storage = makeStorage();
    PageCrawler crawler = new PageCrawler(storage);

    String url = "http://something.com";  // URL does not exist
    crawler.crawl(url);
    crawler.report();

    List<String> expected = new ArrayList<String>();
    expected.add(url);

    try {
      List<String> content = Files.readAllLines(Paths.get(BADLINKS_PATH), StandardCharsets.US_ASCII);
      assertEquals(expected, content);
    } catch (IOException e) {
      fail("Output file was not found");
    }
  }

  @Test
  @DisplayName("Tests bad link detection via getBadLinks().")
  void testCrawlBadLinksGet() {
    cleanStorage();
    StorageService storage = makeStorage();
    PageCrawler crawler = new PageCrawler(storage);

    String url = "http://something.com";  // URL does not exist
    crawler.crawl(url);
    crawler.report();

    Set<String> expected = new HashSet<String>();
    expected.add(url);

    Set<String> actual = crawler.getBadLinks();
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Tests output file creation for email detection.")
  void testCrawlEmails() {
    cleanStorage();
    StorageService storage = makeStorage();
    PageCrawler crawler = new PageCrawler(storage);

    String url = System.getProperty("user.dir") + "\\src\\test\\resources\\test-4.html";
    crawler.crawl(url);
    crawler.report();

    String email1 = "foo@gmail.com";
    String email2 = "bar@gmail.com";
    String email3 = "baz@gmail.com";

    // Construct list of expected emails in stack order (last first)
    List<String> expected = new ArrayList<String>();
    expected.add(email3);
    expected.add(email2);
    expected.add(email1);

    try {
      List<String> content = Files.readAllLines(Paths.get(EMAIL_PATH), StandardCharsets.US_ASCII);
      assertEquals(expected, content);
    } catch (IOException e) {
      fail("Output file was not found");
    }
  }

  @Test
  @DisplayName("Tests email detection via getBadLinks().")
  void testCrawlEmailsGet() {
    cleanStorage();
    StorageService storage = makeStorage();
    PageCrawler crawler = new PageCrawler(storage);

    String url = System.getProperty("user.dir") + "\\src\\test\\resources\\test-4.html";
    crawler.crawl(url);
    crawler.report();

    String email1 = "foo@gmail.com";
    String email2 = "bar@gmail.com";
    String email3 = "baz@gmail.com";

    // Construct list of expected emails in stack order (last first)
    Set<String> expected = new HashSet<String>();
    expected.add(email3);
    expected.add(email2);
    expected.add(email1);

    Set<String> actual = crawler.getEmails();
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Tests output file creation for good link detection.")
  void testCrawlGoodLinks() {
    cleanStorage();
    StorageService storage = makeStorage();
    PageCrawler crawler = new PageCrawler(storage);

    String url = System.getProperty("user.dir") + "\\src\\test\\resources\\test-1.html";
    crawler.crawl(url);
    crawler.report();

    List<String> expected = new ArrayList<String>();
    expected.add(url);

    try {
      List<String> content = Files.readAllLines(Paths.get(GOODLINKS_PATH), StandardCharsets.US_ASCII);
      assertEquals(expected, content);
    } catch (IOException e) {
      fail("Output file was not found");
    }
  }

  @Test
  @DisplayName("Tests bad link detection via getBadLinks().")
  void testCrawlGoodLinksGet() {
    cleanStorage();
    StorageService storage = makeStorage();
    PageCrawler crawler = new PageCrawler(storage);

    String url = System.getProperty("user.dir") + "\\src\\test\\resources\\test-1.html";
    crawler.crawl(url);
    crawler.report();

    Set<String> expected = new HashSet<String>();
    expected.add(url);

    Set<String> actual = crawler.getGoodLinks();
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Tests email detection handling when exceeding maximum email count.")
  void testMaximumEmails() {
    int maxEmails = 1;

    cleanStorage();
    StorageService storage = makeStorage();
    PageCrawler crawler = new PageCrawler(storage, maxEmails);

    // test-5.html contains one email address and links to test-6.html, which contains one more email
    String url = System.getProperty("user.dir") + "\\src\\test\\resources\\test-5.html";
    crawler.crawl(url);
    crawler.report();

    Set<String> content = crawler.getEmails();
    assertEquals(maxEmails, content.size());
  }

  @Test
  @Timeout(10)
  @DisplayName("Tests for infinite loop avoidance.")
  void testNoInfiniteLoop() {
    StorageService storage = mock(StorageService.class);
    PageCrawler crawler = new PageCrawler(storage);

    // `test-2.html` links to `test-3.html`, which links back to `test-2.html`
    String url = System.getProperty("user.dir") + "\\src\\test\\resources\\test-2.html";
    crawler.crawl(url);
  }
}