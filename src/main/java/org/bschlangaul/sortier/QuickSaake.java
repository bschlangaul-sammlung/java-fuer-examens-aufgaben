package org.bschlangaul.sortier;


/**
 * Sortiere ein Zahlen-Feld mit Hilfe des Quicksort-Algorithmus. (Nach Saake
 * Seite 138)
 *
 * Der Algorithmus wählt das mittlere Element aus. In der „zerlegen“ Methode
 * wird das Pivot-Element temporär an die obere Grenze verschoben. Die
 * „zerlege“-Methode arbeitet sich von links nach rechts hoch um die endgültige
 * Position des Pivot-Elements zu bestimmen.
 */
public class QuickSaake extends Quick {

  /**
   * Hilfsmethode zum Zerlegen der Folge. Diese Methode heißt im Englischen auch
   * oft „partition“.
   *
   * @param links  Die Index-Nummer der unteren Grenze.
   * @param rechts Die Index-Nummer der oberen Grenze.
   *
   * @return Die endgültige Index-Nummer des Pivot-Elements.
   */
  private int zerlege(int links, int rechts) {
    berichte.feldAusschnitt(links, rechts, "zerlege");
    int pivotIndex = bestimmePivot(links, rechts);

    int pivotWert = zahlen[pivotIndex];
    int pivotIndexEndgültig = links;
    // Pivot-Element an das Ende verschieben
    if (pivotIndex != rechts) {
      vertausche(links, rechts, pivotIndex, rechts);
    }
    for (int i = links; i < rechts; i++) {
      if (zahlen[i] <= pivotWert) {
        vertausche(links, rechts, pivotIndexEndgültig, i);
        pivotIndexEndgültig++;
      }
    }
    // Pivot-Element an die richtige Position kopieren
    vertausche(links, rechts, rechts, pivotIndexEndgültig);
    // neue Pivot-Position zurückgeben
    return pivotIndexEndgültig;
  }

  /**
   * Hilfsmethode zum rekursiven Sortieren
   *
   * @param links  Die Index-Nummer der unteren Grenze.
   * @param rechts Die Index-Nummer der oberen Grenze.
   */
  private void sortiereRekursiv(int links, int rechts) {
    if (rechts > links) {
      // Feld zerlegen
      int pivotIndexEndgültig = zerlege(links, rechts);
      // und zerlegeen sortieren
      sortiereRekursiv(links, pivotIndexEndgültig - 1);
      sortiereRekursiv(pivotIndexEndgültig + 1, rechts);
    }
  }

  /**
   * Sortiere ein Zahlen-Feld mit Hilfe des Quicksort-Algorithmus.
   *
   * @return Das sortierte Zahlenfeld.
   */
  public int[] sortiere() {
    sortiereRekursiv(0, zahlen.length - 1);
    return zahlen;
  }

  public static void main(String[] args) {
    new QuickSaake().teste();
  }
}
