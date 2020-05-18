package edu.depaul.email;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PageFetcherTest {
  @Test
  @DisplayName("Tests PageFetcher constructor.")
  void testConstructor() {
    PageFetcher fetcher = new PageFetcher();
    assertNotNull(fetcher);
  }

  @Test
  @DisplayName("Checks for returned online document via get().")
  void testGetHTTP() {
    PageFetcher fetcher = new PageFetcher();
    Document output = fetcher.get("http://www.google.com");
    assertNotNull(output);
  }

  @Test
  @DisplayName("Checks for returned HTML string via getString().")
  void testGetString() {
    PageFetcher fetcher = new PageFetcher();
    String output = fetcher.getString("http://www.google.com");
    assertNotNull(output);
  }

  @Test
  @DisplayName("Checks for returned local document via get().")
  void testGetLocal() {
    PageFetcher fetcher = new PageFetcher();
    Document output = fetcher.get(System.getProperty("user.dir") + "\\src\\test\\resources\\test-1.html");
    assertNotNull(output);
  }

  @Test
  @DisplayName("Checks for thrown EmailFinderException via get().")
  void testEmailFinderExceptionGet() {
    PageFetcher fetcher = new PageFetcher();
    String url = "http://www.something.com";  // Site does not exist
    assertThrows(EmailFinderException.class, () -> fetcher.get(url));
  }

  @Test
  @DisplayName("Checks for thrown EmailFinderException via getString().")
  void testEmailFinderExceptionGetString() {
    PageFetcher fetcher = new PageFetcher();
    String url = "http://www.something.com";  // Site does not exist
    assertThrows(EmailFinderException.class, () -> fetcher.getString(url));
  }
}