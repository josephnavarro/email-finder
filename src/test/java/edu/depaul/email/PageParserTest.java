package edu.depaul.email;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PageParserTest {
  @Test
  @DisplayName("Tests PageParser constructor.")
  void testConstructor() {
    PageParser parser = new PageParser();
    assertNotNull(parser);
  }

  @Test
  @DisplayName("Tests finding zero emails in a document.")
  void testFindZeroEmails() {
    String html = "<html><body></body></html>";
    Document doc = Jsoup.parse(html);
    PageParser parser = new PageParser();
    Set<String> emails = parser.findEmails(doc);
    assertEquals(0, emails.size());
  }

  @Test
  @DisplayName("Tests finding one email in a document.")
  void testFindOneEmail() {
    String email = "email1@gmail.com";
    String html = "<html><body>" + email + "</body></html>";
    Document doc = Jsoup.parse(html);
    PageParser parser = new PageParser();
    Set<String> emails = parser.findEmails(doc);
    assertEquals(1, emails.size());
  }

  @ParameterizedTest
  @MethodSource("provideMultipleEmails")
  @DisplayName("Tests finding multiple emails in a document.")
  void testFindMultipleEmails(int expected, String html) {
    Document doc = Jsoup.parse(html);
    PageParser parser = new PageParser();
    Set<String> emails = parser.findEmails(doc);
    assertEquals(expected, emails.size());
  }

  private static Stream<Arguments> provideMultipleEmails() {
    // Dummy email addresses
    String e1 = "e1@gmail.com";
    String e2 = "e2@gmail.com";
    String e3 = "e3@gmail.com";
    String e4 = "e4@gmail.com";
    String e5 = "e5@gmail.com";
    String e6 = "e6@gmail.com";

    // Basic documents with two or more email addresses
    String d2 = "<html><body>" + e1 + ", " + e2 + "</body></html>";
    String d3 = "<html><body>" + e1 + ", " + e2 + ", " + e3 + "</body></html>";
    String d4 = "<html><body>" + e1 + ", " + e2 + ", " + e3 + ", " + e4 + "</body></html>";
    String d5 = "<html><body>" + e1 + ", " + e2 + ", " + e3 + ", " + e4 + ", " + e5 + "</body></html>";
    String d6 = "<html><body>" + e1 + ", " + e2 + ", " + e3 + ", " + e4 + ", " + e5 + ", " + e6 + "</body></html>";

    return Stream.of(
      Arguments.of(2, d2),
      Arguments.of(3, d3),
      Arguments.of(4, d4),
      Arguments.of(5, d5),
      Arguments.of(6, d6)
    );
  }

  @Test
  @DisplayName("Tests finding zero links in a document.")
  void testFindZeroLinks() {
    String html = "<html><body></body></html>";
    Document doc = Jsoup.parse(html);
    PageParser parser = new PageParser();
    Set<String> links = parser.findLinks(doc);
    assertEquals(0, links.size());
  }

  @Test
  @DisplayName("Tests finding one link in a document.")
  void testFindOneLink() {
    String link = "<a href='file.html'>link</a>";
    String html = "<html><body>" + link + "</body></html>";
    Document doc = Jsoup.parse(html);
    PageParser parser = new PageParser();
    Set<String> links = parser.findLinks(doc);
    assertEquals(1, links.size());
  }

  @ParameterizedTest
  @MethodSource("provideMultipleLinks")
  @DisplayName("Tests finding multiple links in a document.")
  void testFindMultipleLinks(int expected, String html) {
    Document doc = Jsoup.parse(html);
    PageParser parser = new PageParser();
    Set<String> links = parser.findLinks(doc);
    assertEquals(expected, links.size());
  }

  private static Stream<Arguments> provideMultipleLinks() {
    // Dummy links
    String a1 = "<a href='1.html'>1</a>";
    String a2 = "<a href='2.html'>2</a>";
    String a3 = "<a href='3.html'>3</a>";
    String a4 = "<a href='4.html'>4</a>";
    String a5 = "<a href='5.html'>5</a>";
    String a6 = "<a href='6.html'>6</a>";

    // Basic documents with two or more links
    String d2 = "<html><body>" + a1 + a2 + "</body></html>";
    String d3 = "<html><body>" + a1 + a2 + a3 + "</body></html>";
    String d4 = "<html><body>" + a1 + a2 + a3 + a4 + "</body></html>";
    String d5 = "<html><body>" + a1 + a2 + a3 + a4 + a5 + "</body></html>";
    String d6 = "<html><body>" + a1 + a2 + a3 + a4 + a5 + a6 + "</body></html>";

    return Stream.of(
      Arguments.of(2, d2),
      Arguments.of(3, d3),
      Arguments.of(4, d4),
      Arguments.of(5, d5),
      Arguments.of(6, d6)
    );
  }

  @Test
  @DisplayName("Ensures erroneous <a> tags (without href) are not recognized.")
  void testFindNoHref() {
    String link = "<a>link</a>";
    String html = "<html><body>" + link + "</body></html>";
    Document doc = Jsoup.parse(html);
    PageParser parser = new PageParser();
    Set<String> links = parser.findLinks(doc);
    assertEquals(0, links.size());
  }
}