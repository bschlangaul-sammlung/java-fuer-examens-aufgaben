package org.bschlangaul.graph;

/**
 * https://www.geeksforgeeks.org/printing-paths-dijkstras-shortest-path-algorithm/
 */
class DijkstraAdjazenzMatrix extends GraphAdjazenzMatrix {

  /**
   * In diesem Feld werden die kürzesten Entfernungen zu den einzelnen Knoten
   * gespeichert.
   */
  int[] kürzesteEntfernungen;

  /**
   * Feld, mit dem die Vorgänger-Knoten des kürzesten Pfads gespeichert werden.
   * Ein Vorgänger-Knoten des Pfads gibt ab, über welchen Knoten man auf kürzesten
   * Weg zum Knoten kommt.
   */
  int[] vorgänger;

  /**
   * Mit diesem Konstruktur wird die Adjazenzmatrix durch das einfache
   * Graphenformat erzeugt.
   *
   * @param graphenFormat Ein String im einfachen Graphenformat.
   */
  public DijkstraAdjazenzMatrix(String graphenFormat) {
    super(graphenFormat);
    kürzesteEntfernungen = new int[gibKnotenAnzahl()];
  }

  private static final int KEINE_VORGÄNGER = -1;

  /**
   * Diese Methode implementiert den Dijkstra-Algorithmus zum Finden des kürzesten
   * Pfads unter Angabe des Anfangsknoten.
   *
   * @param anfangsKnoten Der Name des Anfangsknoten.
   */
  public int[] sucheKürzestenPfad(String anfangsKnoten) {
    int knotenAnzahl = gibKnotenAnzahl();
    kürzesteEntfernungen = new int[knotenAnzahl];

    // besucht[i] wird auf true gesetzt, wenn sich der Knoten i im
    // Kürzesten-Pfad-Baum befindet oder der kürzeste Pfad vom Anfangskonten zum
    // Knoten i fertig berechnet ist.
    boolean[] besucht = new boolean[knotenAnzahl];

    // Initialisierung der beiden Felder kürzesteEntfernungen und
    // besucht.
    for (int i = 0; i < knotenAnzahl; i++) {
      kürzesteEntfernungen[i] = Integer.MAX_VALUE;
      besucht[i] = false;
    }

    // Die Entfernung vom Anfangsknoten zu sich selbst ist immer 0.
    kürzesteEntfernungen[gibKnotenNummer(anfangsKnoten)] = 0;

    // Feld mit dem die Vorgänger-Knoten des kürzesten Pfads gespeichert werden.
    // Ein Vorgänger-Knoten des Pfads gibt ab, über welchen Knoten man auf
    // kürzesten Weg zum Knoten kommt.
    vorgänger = new int[knotenAnzahl];

    // Der Anfangsknoten hat keinen Vorgänger.
    vorgänger[gibKnotenNummer(anfangsKnoten)] = KEINE_VORGÄNGER;

    // Hier startet der eigentliche Algorithmus.
    for (int i = 1; i < knotenAnzahl; i++) {
      // Pick the minimum distance vertex from the set of vertices not
      // yet processed. nearestVertex is always equal to startNode in
      // first iteration.
      int nähesterKnoten = -1;
      int entfernung = Integer.MAX_VALUE;
      for (int j = 0; j < knotenAnzahl; j++) {
        if (!besucht[j] && kürzesteEntfernungen[j] < entfernung) {
          nähesterKnoten = j;
          entfernung = kürzesteEntfernungen[j];
        }
      }

      // Markiere den ausgewählten Knoten als besucht.
      besucht[nähesterKnoten] = true;

      // Update dist value of the adjacent vertices of the picked
      // vertex.
      for (int j = 0; j < knotenAnzahl; j++) {
        int kantenEntfernung = matrix[nähesterKnoten][j];

        if (kantenEntfernung > 0 && ((entfernung + kantenEntfernung) < kürzesteEntfernungen[j])) {
          vorgänger[j] = nähesterKnoten;
          kürzesteEntfernungen[j] = entfernung + kantenEntfernung;
        }
      }
    }

    printSolution(anfangsKnoten, kürzesteEntfernungen, vorgänger);
    return kürzesteEntfernungen;
  }

  public String gibVorgänger(String knotenName) {
    int knotenNummer = vorgänger[gibKnotenNummer(knotenName)];
    if (knotenNummer == -1)
      return knotenName;
    return gibKnotenName(knotenNummer);
  }

  public int gibEntfernung(String knotenName) {
    return kürzesteEntfernungen[gibKnotenNummer(knotenName)];
  }

  public static int[] sucheKürzestenPfad(String einfachesGraphenFormat, String anfangsKnoten) {
    return new DijkstraAdjazenzMatrix(einfachesGraphenFormat).sucheKürzestenPfad(anfangsKnoten);
  }

  // A utility function to print the constructed distances array and
  // shortest paths
  private void printSolution(String startVertex, int[] distances, int[] parents) {
    int nVertices = distances.length;
    System.out.print("Vertex\t Distance\tPath");

    for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++) {
      if (vertexIndex != gibKnotenNummer(startVertex)) {
        System.out.print("\n" + startVertex + " -> ");
        System.out.print(gibKnotenName(vertexIndex) + " \t\t ");
        System.out.print(distances[vertexIndex] + "\t\t");
        printPath(vertexIndex, parents);
      }
    }
  }

  // Function to print shortest path from source to currentVertex using
  // parents array
  private void printPath(int currentVertex, int[] parents) {

    // Base case : Source node has
    // been processed
    if (currentVertex == KEINE_VORGÄNGER) {
      return;
    }
    printPath(parents[currentVertex], parents);
    System.out.print(gibKnotenName(currentVertex) + " ");
  }

  // Driver Code
  public static void main(String[] args) {
    // DijkstraAdjazenzMatrix dijkstra = new DijkstraAdjazenzMatrix("a -- b; b -- c: 7; a -- d: 2; b -> d: 19");
    // dijkstra.sucheKürzestenPfad("c");

    DijkstraAdjazenzMatrix d = new DijkstraAdjazenzMatrix(
      "a->b: 1; a->e: 7; b->c: 3; c->d: 8; c->e: 3; e->f: 1; c->f: 6; f->c: 1; f->d: 3");

  d.sucheKürzestenPfad("a");
  }
}
