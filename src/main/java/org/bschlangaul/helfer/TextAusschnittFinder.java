package org.bschlangaul.helfer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Findet in einer Text-Datei mit Hilfe eines regulären Ausdrucks
 * Textausschnitte, die weiterverarbeitet werden können.
 */
public class TextAusschnittFinder {

  public static String gibRegexFürTexMakro(String makroName, String inhalt) {
    return "\\\\" + makroName + "\\{" + inhalt + "\\}";
  }

  public static String gibRegexFürTexUmgebung(String umgebungsName) {
    return gibRegexFürTexMakro("begin", umgebungsName) + "(?<markup>.*?)" + gibRegexFürTexMakro("end", umgebungsName);
  }

  /**
   * Lese den Inhalt einer Text-Datei ein.
   *
   * @param datei Eine Text-Datei.
   *
   * @return Der Inhalt der Text-Datei.
   */
  private static String leseTextDatei(File datei) {
    try {
      return Files.readString(datei.toPath());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Lese den Inhalt einer Text-Datei ein.
   *
   * @param pfad Der Dateipfad zur Text-Datei.
   *
   * @return Der Inhalt der Text-Datei.
   */
  private static String leseTextDatei(String pfad) {
    File datei = new File(pfad);
    return leseTextDatei(datei);
  }

  /**
   * @param inhalt Der Textinhalt in dem mit Hilfe des regulären Ausdrucks nach
   *               Ausschnitten gesucht werden soll.
   * @param regex  Ein regulärer Ausdruck der (?<markup>...) enthält
   *
   * @return Eine Liste an gefunden Markups
   */
  private static List<String> sucheAusschnitteInText(String inhalt, String regex) {
    Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
    Matcher ergebnis = pattern.matcher(inhalt);
    List<String> markups = new ArrayList<>();
    if (ergebnis.find()) {
      markups.add(ergebnis.group("markup"));
    }
    return markups;
  }

  /**
   * @param pfad  Der Dateipfad zur Text-Datei.
   *
   * @param regex Ein regulärer Ausdruck der (?<markup>...) enthält
   *
   * @return Eine Liste an gefunden Markups
   */
  public static List<String> sucheAusschnitteInTextDatei(String pfad, String regex) {
    String inhalt = leseTextDatei(pfad);
    if (inhalt != null) {
      return sucheAusschnitteInText(inhalt, regex);
    }
    return null;
  }

  /**
   * @param datei Eine Text-Datei.
   *
   * @param regex Ein regulärer Ausdruck der (?<markup>...) enthält
   *
   * @return Eine Liste an gefunden Markups
   */
  public static List<String> sucheAusschnitteInTextDatei(File datei, String regex) {
    String inhalt = leseTextDatei(datei);
    if (inhalt != null) {
      return sucheAusschnitteInText(inhalt, regex);
    }
    return null;
  }
}
