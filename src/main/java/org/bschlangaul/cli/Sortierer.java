package org.bschlangaul.cli;

import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.List;
import java.util.concurrent.Callable;

import org.bschlangaul.sortier.BubbleIterativ;
import org.bschlangaul.sortier.Heap;
import org.bschlangaul.sortier.InsertionIterativ;
import org.bschlangaul.sortier.Merge;
import org.bschlangaul.sortier.QuickSaake;
import org.bschlangaul.sortier.SelectionRechtsIterativ;

import org.bschlangaul.sortier.Sortieralgorithmus;

@Command(name = "sortiere", aliases = {
    "so" }, mixinStandardHelpOptions = true, description = "Sortiere mit verschiedenen Algorithmen.")
class Sortierer implements Callable<Integer> {

  @ArgGroup(exclusive = true, multiplicity = "0..1")
  Algorithmus algorithmus;

  static class Algorithmus {
    @Option(names = { "-b", "--bubble" }, description = "Bubblesort.")
    boolean bubble;

    @Option(names = { "-H", "--heap" }, description = "Heapsort.")
    boolean heap;

    @Option(names = { "-i", "--insertion" }, description = "Insertionsort.")
    boolean insertion;

    @Option(names = { "-m", "--merge" }, description = "Mergsort.")
    boolean merge;

    @Option(names = { "-q", "--quick" }, description = "Quicksort.")
    boolean quick;

    @Option(names = { "-s", "--selection" }, description = "Selectionsort.")
    boolean selection;
  }

  @Parameters(arity = "1..*", description = "Eine Zahlenfolge, die sortiert werden soll.")
  List<Integer> werte;

  @Override
  public Integer call() {
    Sortieralgorithmus sortierer = null;

    if (algorithmus == null) {
      sortierer = new BubbleIterativ();
    } else if (algorithmus.heap) {
      sortierer = new Heap();
    } else if (algorithmus.insertion) {
      sortierer = new InsertionIterativ();
    } else if (algorithmus.merge) {
      sortierer = new Merge();
    } else if (algorithmus.quick) {
      sortierer = new QuickSaake();
    } else if (algorithmus.selection) {
      sortierer = new SelectionRechtsIterativ();
    } else {
      sortierer = new BubbleIterativ();
    }

    int[] zahlen = werte.stream().mapToInt(i -> i).toArray();

    sortierer.setzeZahlen(zahlen);
    sortierer.aktiviereKonsolenAusgabe();
    System.out.println("Sortieralgorithmus: " + sortierer.getClass().getName());
    sortierer.berichte.feld("Eingabe");
    sortierer.sortiere();
    sortierer.berichte.feld("Ausgabe");
    return 0;
  }
}
