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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PageCrawlerTest {
  private static final String EMAIL_PATH = "src/test/resources/email.txt";
  private static final String BADLINKS_PATH = "src/test/resources/badlinks.txt";
  private static final String GOODLINKS_PATH = "src/test/resources/goodlinks.txt";

  private void cleanStorage() {
    try {
      Files.deleteIfExists(Paths.get(EMAIL_PATH));
    } catch (NoSuchFileException e) {
      System.out.println("No such file/directory exists");
    } catch (DirectoryNotEmptyException e) {
      System.out.println("Directory is not empty.");
    } catch (IOException e) {
      System.out.println("Invalid permissions.");
    }

    try {
      Files.deleteIfExists(Paths.get(BADLINKS_PATH));
    } catch (NoSuchFileException e) {
      System.out.println("No such file/directory exists");
    } catch (DirectoryNotEmptyException e) {
      System.out.println("Directory is not empty.");
    } catch (IOException e) {
      System.out.println("Invalid permissions.");
    }

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
  @DisplayName("Tests bad link detection.")
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
      System.out.println("No such file/directory exists");
    }
  }

  @Test
  @DisplayName("Tests good link detection.")
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
      System.out.println("No such file/directory exists");
    }
  }

  @Test
  @Timeout(10)
  @DisplayName("Tests for infinite loop avoidance.")
  void testNoInfiniteLoop() {
    StorageService storage = mock(StorageService.class);
    PageCrawler crawler = new PageCrawler(storage);

    // `test-2.html` links to `test-3.html`, which links back to `test-2.html`
    String url = "C:\\Users\\Joey\\Documents\\GitHub\\email-finder\\src\\test\\resources\\test-2.html";
    crawler.crawl(url);
  }
}