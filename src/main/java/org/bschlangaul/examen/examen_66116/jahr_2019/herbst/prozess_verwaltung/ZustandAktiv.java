package org.bschlangaul.examen.examen_66116.jahr_2019.herbst.prozess_verwaltung;

/**
 * Entspricht der „KonkreterZustand“-Unterklasse in der Terminologie der „Gang of
 * Four“.
 */
public class ZustandAktiv extends ProzessZustand {

  public ZustandAktiv(Prozess prozess) {
    super("aktiv", prozess);
  }

  public void suspendieren() {
    System.out.println("Der Prozess wird suspendiert.");
    prozess.setzeZustand(new ZustandSuspendiert(prozess));
  }

  public void beenden() {
    System.out.println("Der Prozess wird beendet.");
    prozess.setzeZustand(new ZustandBeendet(prozess));
  }
}
