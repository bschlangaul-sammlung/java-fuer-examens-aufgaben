package org.bschlangaul.examen.examen_66116.jahr_2014.herbst.regal;

@SuppressWarnings("unused")
public class Disk {
  private Typ typ;
  private Genre genre;
  private int bewertung;
  private String titel;

  /**
   * Erzeuge eine neue Disk. Die Bewertung wird automatisch mit Hilfe des Genres
   * berechnet.
   *
   * @param typ   Typ der Disk
   * @param genre Genre der Disk
   * @param titel Titel der Disk
   */
  public Disk(Typ typ, Genre genre, String titel) {
    this.typ = typ;
    this.genre = genre;
    this.titel = titel;
    this.bewertung = (int) erstelleStdBewertung(this.genre);
  }

  /**
   * Gib die Bewertung der Disk zurück.
   *
   * @return Bewertung
   */
  public int gibBewertung() {
    return this.bewertung;
  }

  /**
   * Berechne rekursiv die Bewertung einer Disk.
   *
   * @param genre Genre der Disk
   *
   * @return Bewertung
   */
  public double erstelleStdBewertung(Genre genre) {
    if (genre.equals(Genre.MUSIK)) {
      return 3;
    } else if (genre.equals(Genre.KOMOEDIE)) {
      return 2;
    } else if (genre.equals(Genre.THRILLER)) {
      return 2 * erstelleStdBewertung(Genre.KOMOEDIE);
    } else if (genre.equals(Genre.FANTASY)) {
      return 1.5 * erstelleStdBewertung(Genre.THRILLER);
    } else if (genre.equals(Genre.ACTION)) {
      return erstelleStdBewertung(Genre.THRILLER);
    } else {
      return 0;
    }
  }

}
