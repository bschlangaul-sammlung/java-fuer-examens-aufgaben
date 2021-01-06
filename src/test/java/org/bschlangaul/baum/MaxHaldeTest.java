package org.bschlangaul.baum;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

@SuppressWarnings("rawtypes")
public class MaxHaldeTest {

  private void vergleicheHaldenFeld(Comparable[] eingefügt, Comparable[] erwartet) {
    Halde halde = new MaxHalde(eingefügt.length);
    for (int i = 0; i < eingefügt.length; i++) {
      halde.fügeEin(eingefügt[i]);
    }
    assertArrayEquals(erwartet, halde.gibHaldenFeld());
  }

  @Test
  public void methodeGibHaldenFeld() {
    vergleicheHaldenFeld(new Comparable[] { 1, 2, 3, 4, 5 }, new Comparable[] { 1, 2, 3, 4, 5 });
    vergleicheHaldenFeld(new Comparable[] { 5, 4, 3, 2, 1 }, new Comparable[] { 1, 2, 4, 5, 3 });
    vergleicheHaldenFeld(new Comparable[] { 9, 5, 7, 1, 6, 4, 8, 2, 3 },
        new Comparable[] { 1, 2, 4, 3, 6, 7, 8, 9, 5 });
  }

}
