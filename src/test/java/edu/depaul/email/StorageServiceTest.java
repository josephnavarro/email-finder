package edu.depaul.email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StorageServiceTest {
  @Test
  @DisplayName("Tests StorageService constructor.")
  void testConstructor() {
    StorageService storage = new StorageService();
    assertNotNull(storage);
  }

  @Test
  @DisplayName("Tests for thrown exception with no `EMAIL` location added.")
  void testNoEmailLocation() {
    StorageService storage = new StorageService();
    Collection<String> collection = new ArrayList<String>();

    assertThrows(EmailFinderException.class, () -> storage.storeList(StorageService.StorageType.EMAIL, collection));
  }

  @Test
  @DisplayName("Tests for thrown exception with no `BADLINKS` location added.")
  void testNoBadLinksLocation() {
    StorageService storage = new StorageService();
    Collection<String> collection = new ArrayList<String>();

    assertThrows(EmailFinderException.class, () -> storage.storeList(StorageService.StorageType.BADLINKS, collection));
  }

  @Test
  @DisplayName("Tests for thrown exception with no `GOODLINKS` location added.")
  void testNoGoodLinksLocation() {
    StorageService storage = new StorageService();
    Collection<String> collection = new ArrayList<String>();

    assertThrows(EmailFinderException.class, () -> storage.storeList(StorageService.StorageType.GOODLINKS, collection));
  }

  @Test
  @DisplayName("Tests writing to a given `EMAIL` location.")
  void testWriteEmail() {
    String path = "src/test/resources/email.txt";

    try {
      Files.deleteIfExists(Paths.get(path));
    } catch (NoSuchFileException e) {
      System.out.println("No such file/directory exists");
    } catch (DirectoryNotEmptyException e) {
      System.out.println("Directory is not empty.");
    } catch (IOException e) {
      System.out.println("Invalid permissions.");
    }

    String email1 = "email1@gmail.com";
    String email2 = "email2@gmail.com";
    String email3 = "email3@gmail.com";

    Collection<String> collection = new ArrayList<String>();
    collection.add(email1);
    collection.add(email2);
    collection.add(email3);

    StorageService storage = new StorageService();
    storage.addLocation(StorageService.StorageType.EMAIL, path);
    storage.storeList(StorageService.StorageType.EMAIL, collection);

    try {
      List<String> content = Files.readAllLines(Paths.get(path), StandardCharsets.US_ASCII);
      assertEquals(collection, content);
    } catch (IOException e) {
      System.out.println("No such file/directory exists");
    }

  }

  @Test
  @DisplayName("Tests writing to a given `BADLINKS` location.")
  void testWriteBadLinks() {
    String path = "src/test/resources/badlinks.txt";

    try {
      Files.deleteIfExists(Paths.get(path));
    } catch (NoSuchFileException e) {
      System.out.println("No such file/directory exists");
    } catch (DirectoryNotEmptyException e) {
      System.out.println("Directory is not empty.");
    } catch (IOException e) {
      System.out.println("Invalid permissions.");
    }

    String link1 = "www.link1.com";
    String link2 = "www.link2.com";
    String link3 = "www.link3.com";

    Collection<String> collection = new ArrayList<String>();
    collection.add(link1);
    collection.add(link2);
    collection.add(link3);

    StorageService storage = new StorageService();
    storage.addLocation(StorageService.StorageType.BADLINKS, path);
    storage.storeList(StorageService.StorageType.BADLINKS, collection);

    try {
      List<String> content = Files.readAllLines(Paths.get(path), StandardCharsets.US_ASCII);
      assertEquals(collection, content);
    } catch (IOException e) {
      System.out.println("No such file/directory exists");
    }

  }

  @Test
  @DisplayName("Tests writing to a given `GOODLINKS` location.")
  void testWriteGoodLinks() {
    String path = "src/test/resources/goodlinks.txt";

    try {
      Files.deleteIfExists(Paths.get(path));
    } catch (NoSuchFileException e) {
      System.out.println("No such file/directory exists");
    } catch (DirectoryNotEmptyException e) {
      System.out.println("Directory is not empty.");
    } catch (IOException e) {
      System.out.println("Invalid permissions.");
    }

    String link1 = "www.link1.com";
    String link2 = "www.link2.com";
    String link3 = "www.link3.com";

    Collection<String> collection = new ArrayList<String>();
    collection.add(link1);
    collection.add(link2);
    collection.add(link3);

    StorageService storage = new StorageService();
    storage.addLocation(StorageService.StorageType.GOODLINKS, path);
    storage.storeList(StorageService.StorageType.GOODLINKS, collection);

    try {
      List<String> content = Files.readAllLines(Paths.get(path), StandardCharsets.US_ASCII);
      assertEquals(collection, content);
    } catch (IOException e) {
      System.out.println("No such file/directory exists");
    }

  }

  @Test
  @DisplayName("Checks for thrown exception on writing to nonexistent path.")
  void testExceptionNonexistentPath() {
    String path = "src/test/resources/DOES_NOT_EXIST/email.txt";

    try {
      Files.deleteIfExists(Paths.get(path));
    } catch (NoSuchFileException e) {
      System.out.println("No such file/directory exists");
    } catch (DirectoryNotEmptyException e) {
      System.out.println("Directory is not empty.");
    } catch (IOException e) {
      System.out.println("Invalid permissions.");
    }

    String email1 = "email1@gmail.com";
    String email2 = "email2@gmail.com";
    String email3 = "email3@gmail.com";

    Collection<String> collection = new ArrayList<String>();
    collection.add(email1);
    collection.add(email2);
    collection.add(email3);

    StorageService storage = new StorageService();
    storage.addLocation(StorageService.StorageType.EMAIL, path);

    assertThrows(EmailFinderException.class, () -> storage.storeList(StorageService.StorageType.EMAIL, collection));
  }
}