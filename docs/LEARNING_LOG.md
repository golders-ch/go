# Lernjournal

## 2026-10-31 – Java-Domänenmodell

### Das habe ich gelernt

**Paketstruktur in Java:**

- Der Verzeichnispfad muss genau dem Paketnamen entsprechen.
- `package ch.golders.backend.domain;` → Die Datei liegt in
  `ch/golders/backend/domain/`.
- Ohne Paketdeklaration befindet sich eine Klasse im Standardpaket und kann
  nicht importiert werden.

**Aufzählungstypen (Enums):**

- Enums eignen sich für kleine, bekannte Wertemengen, die das System steuern
  (Status, Rollen, Typen).
- Veränderliche oder von Benutzern eingegebene Daten sollten keine Enums sein
  (zum Beispiel Marken und Modelle).
- Beispiel: `FuelType` als Enum ✅, `make` als Zeichenkette ✅

**Unveränderlichkeit mit `final`:**

- Felder, die sich nie ändern, als `final` deklarieren.
- Das verhindert versehentliche Änderungen.
- Der Code wird dadurch verständlicher und threadsicherer.
- Beispiel: `private final String id;` – der Wert kann nur im Konstruktor
  gesetzt werden.

**Kapselung:**

- Felder immer als `private` deklarieren.
- Zugriff nur über Getter erlauben.
- Keine Setter für Felder bereitstellen, die sich nicht ändern sollen.
- Später sprechende Methoden statt Setter verwenden (zum Beispiel `rent()`
  statt `setStatus()`).

**Bidirektionale Beziehungen vermeiden:**

- `Employee` enthält keine `List<Reservation>`.
- Beziehungen werden über Repositories abgebildet.
- Das ist einfacher, flexibler und weniger fehleranfällig.

**Moderne Datums-API:**

- `java.time.LocalDate` statt der veralteten Klasse `java.util.Date` verwenden.
- Ein Datum ohne Uhrzeit und Zeitzone eignet sich zum Beispiel für Geburtstage
  und Reservierungszeiträume.

**Maven:**

- `mvnw.cmd compile` kompiliert den Code.
- Der Maven Wrapper lädt automatisch die passende Maven-Version herunter.
- Die Kompilierung erfolgt dateiweise; neu kompiliert werden nur geänderte
  Dateien.
- Bytecode-Dateien (`.class`) laufen überall dort, wo eine JVM verfügbar ist.

**Git-Commit-Nachrichten:**

- Conventional Commits verwenden: `feat:`, `fix:`, `docs:`, `chore:` usw.
- Die erste Zeile kurz halten (höchstens 50–72 Zeichen).
- Einzelheiten in den Nachrichtentext schreiben.

## 2026-11-09 – Repository-Schnittstellen in Java

### Das habe ich gelernt

**Schnittstellen als Verträge:**

- Eine Schnittstelle legt Methodensignaturen fest, aber keine Implementierung.
- Methoden sind implizit `public`; der Sichtbarkeitsmodifizierer kann
  weggelassen werden.
- Die Geschäftslogik hängt nur von der Schnittstelle ab, nicht von der
  konkreten Umsetzung (Dependency Inversion). Dadurch kann später eine
  Dataverse-Implementierung eingesetzt werden.

**Fallstrick beim Standardpaket:**

- Ohne `package`-Deklaration befindet sich eine Klasse im unbenannten
  Standardpaket.
- Klassen in benannten Paketen können Klassen aus dem Standardpaket nicht
  importieren.
- Typische Fehlermeldung: „cannot find symbol“ für eine eigentlich vorhandene
  Klasse.

## 2026-11-09 – Repository-Muster: Schnittstelle und Implementierung

### Das habe ich gelernt

**Trennung von Schnittstelle (`domain`) und Klasse (`infrastructure`):**

- Die Schnittstelle in `domain` ist der **Vertrag** (Steckdose): Sie enthält
  Signaturen, aber keine Logik.
- Die Klasse in `infrastructure` ist die **Umsetzung** (Stecker): Sie enthält
  die tatsächliche Logik, zum Beispiel mit einer `HashMap`.
- Mit `implements` wird dem Compiler zugesichert, dass alle Methoden der
  Schnittstelle bereitgestellt werden.
- `@Override` kennzeichnet Methoden aus der Schnittstelle; der Compiler kann
  so vor Tippfehlern warnen.

**Vorteile:**

- Später kann eine zweite Umsetzung ergänzt werden, zum Beispiel
  `DataverseVehicleRepository implements VehicleRepository`.
- Der Service kennt nur die Schnittstelle. Beim Wechsel der Umsetzung sind
  daher **keine Änderungen** am Service nötig.
- Abhängigkeiten zeigen nach innen in Richtung `domain` (Dependency Inversion).

**Auch intern über Schnittstellen programmieren:**

- `Map<String, Vehicle> vehicles = new HashMap<>()` statt
  `HashMap<...> vehicles`.
- Dasselbe Prinzip gilt also auch eine Ebene tiefer.

## 2026-11-09 – In-Memory-Repositories umgesetzt

### Das habe ich gelernt

**`HashMap` als „Datenbank“:**

- Bei `HashMap<String, Vehicle>` ist der Schlüssel die ID und der Wert das
  Objekt.
- `put(key, value)` entspricht INSERT/UPDATE, `get(key)` entspricht SELECT und
  `values()` liefert alle Einträge.
- `findAll()` gibt `new ArrayList<>(map.values())` zurück (defensive Kopie –
  Aufrufer können die interne Map nicht verändern).

**Filtern mit Schleifen:**

- Über `map.values()` iterieren, mit `if` filtern und passende Einträge in
  einer Ergebnisliste sammeln.
- Beziehungen anhand von **ID-Vergleichen** statt anhand von Objektverweisen
  prüfen:
  `reservation.getVehicle().getId().equals(vehicle.getId())`

**Methodenverkettung:**

- Bei `reservation.getVehicle().getId()` liefert jeder Getter ein Objekt
  zurück, auf dem anschliessend der nächste Getter aufgerufen wird.
- `Reservation` speichert Objekte (`Vehicle`, `Employee`), nicht deren IDs.

**Häufige Fehlerquellen:**

- Die Methodensignatur bei `implements` muss **exakt** mit der Schnittstelle
  übereinstimmen (Name, Parametertypen und Rückgabetyp). Andernfalls meldet der
  Compiler, dass eine abstrakte Methode nicht umgesetzt wurde.
- Java unterscheidet Gross- und Kleinschreibung: `entraId` ≠ `entraID`.
  Bei „cannot find symbol“ sollte oft die Schreibweise geprüft werden.
- Methoden, die nicht in der Schnittstelle deklariert sind, dürfen kein
  `@Override` tragen.

## 2026-11-12 – Service und Überschneidungsprüfung

- `domain` beschreibt das Fachmodell und die Repository-Verträge;
  `application` koordiniert Anwendungsfälle; `infrastructure` setzt technische
  Zugriffe um. Je nach Zuständigkeit können Fachregeln auch im Modell liegen.
- Ein Service implementiert das Repository nicht selbst. Er erhält ein
  Repository-Objekt über seinen Konstruktor und verwendet es über den
  Schnittstellentyp. Die Verdrahtung muss noch erfolgen; eine Schnittstelle
  erzeugt kein Objekt.
- Jede In-Memory-Repository-Instanz besitzt eine eigene Map. Beim Neustart der
  Anwendung gehen die darin gespeicherten Daten verloren.
- Zwei gültige Zeiträume überschneiden sich, wenn jeder vor dem Ende des
  anderen beginnt. Direkt aneinandergrenzende Zeiträume überschneiden sich
  nicht.
- Enum-Werte werden als Enums verglichen, nicht als Text.

## 2026-11-21 – Reservierungsservice

- Der Konstruktor von `Reservation` prüft, dass der Startzeitpunkt nicht nach
  dem Endzeitpunkt liegt.
- `ReservationService.reserve(...)` prüft auf Überschneidungen mit bestehenden
  Reservierungen und speichert die neue Reservierung über das Repository.
- Der Spring-Kontext-Test prüft, ob die Anwendung startet. Fachliche Tests für
  den Reservierungsservice fehlen noch.

### Nächste Lernziele

- Den Reservierungsservice und das Repository in Spring verdrahten.
- Das Verhalten bei abgeschlossenen Reservierungen festlegen.
- Gültige Zeiträume und relevante Reservierungsstatus fachlich testen.
- Weitere Unit-Tests mit JUnit schreiben.
- Statuswechsel mit Validierung umsetzen.

## 2026-11-22 – Erster Test für den Reservierungsservice

- Eine JUnit-Testmethode führt eine Aktion aus und prüft das Ergebnis mit
  einer Assertion. Ein grüner Test ohne Assertion beweist nicht, dass die
  Reservierung gespeichert wurde.
- Für den Test erhält der Service ein konkretes In-Memory-Repository über
  seinen Konstruktor. Ein nicht initialisiertes Repository-Feld ist `null`
  und führt beim Methodenaufruf zu einer `NullPointerException`.
- Der erste Test speichert eine Reservierung, ruft sie über ihre ID ab und
  prüft mit `assertSame`, ob es dasselbe Objekt ist.
- `findById` ruft das gespeicherte Objekt ab; erst die Assertion prüft das
  Ergebnis. Ein erfolgreicher Methodenaufruf allein beweist nicht, dass
  gespeichert wurde. `assertSame` prüft Objektidentität, nicht gleiche Werte;
  bei einer späteren Datenbankanbindung kann ein Wertevergleich sinnvoll sein.
- Ein weiterer Test prüft, dass eine überschneidende Reservierung abgelehnt
  wird: `assertThrows` ist erfolgreich, wenn die erwartete Ausnahme auftritt.
  Ohne Ausnahme oder bei einem anderen Ausnahmetyp schlägt der Test fehl.
- Ein einfaches `try`/`catch` reicht dafür nicht aus, wenn der Test bei einer
  ausbleibenden Ausnahme nicht ausdrücklich fehlschlägt.
- Direkt aneinandergrenzende Reservierungen sind erlaubt. Feste Zeitpunkte
  stellen sicher, dass der Test tatsächlich denselben Grenzzeitpunkt prüft.
- Eine bereits stornierte Reservierung blockiert denselben Zeitraum nicht.
  Sie wird zur Testvorbereitung direkt im Repository gespeichert; danach
  prüft der Test die neue Reservierung über den Service.
- Eine teilweise Überschneidung desselben Fahrzeugs wird in beiden
  zeitlichen Richtungen abgelehnt. Verschiedene Fahrzeuge können dagegen
  zur gleichen Zeit reserviert werden.
- Weitere Fälle für Zeiträume und Reservierungsstatus sind noch offen.

Beispiele: [ReservationServiceTest](../backend/src/test/java/ch/golders/backend/application/ReservationServiceTest.java).
