# Architektur-Entscheidungsprotokoll

## 2026-11-12 – Vorläufige Reservierungen blockieren Zeiträume

**Entscheidung:** Vorläufige Reservierungen blockieren den Zeitraum eines
Fahrzeugs. Stornierte Reservierungen blockieren ihn nicht.

**Kontext:** Bei der Prüfung auf Überschneidungen muss der Status bestehender
Reservierungen berücksichtigt werden.

**Alternative:** Nur bestätigte Reservierungen blockieren einen Zeitraum.

**Begründung:** Ein Fahrzeug soll während einer vorläufigen Reservierung nicht
gleichzeitig anderweitig reserviert werden können. Wie abgeschlossene
Reservierungen behandelt werden, ist noch offen; derzeit werden sie bei der
Prüfung berücksichtigt.

## 2026-11-12 – Direkt anschliessende Reservierungen erlauben

**Entscheidung:** Eine Reservierung darf genau dann beginnen, wenn die vorherige
endet. Für die Überschneidungsprüfung gelten Zeiträume als halb offen: Der
Startzeitpunkt gehört dazu, der Endzeitpunkt nicht.

**Kontext:** Die Verfügbarkeitsprüfung für Fahrzeuge wird im Reservierungsservice
entwickelt. Der Umgang mit aneinandergrenzenden Zeiträumen muss eindeutig sein.

**Alternative:** Eine Pause zwischen Reservierungen vorschreiben.

**Begründung:** Fahrzeuge können ohne vorgeschriebene Pause direkt erneut
reserviert werden. Eine allfällige Vorbereitungszeit ist damit nicht
berücksichtigt.

## 2026-11-09 – Verfügbarkeitsprüfung im Service statt im Repository

**Entscheidung:** Die Prüfung, ob ein Fahrzeug in einem Zeitraum verfügbar ist
(`checkAvailability`), gehört **nicht** zur Schnittstelle
`ReservationRepository`, sondern als Geschäftslogik in den
`ReservationService` (Paket `application`).

**Kontext:** Beim Entwurf der Repository-Schnittstellen (`repositories.puml`)
war `checkAvailability` zunächst im Repository vorgesehen. Repositories sollen
jedoch nur Datenhaltungsoperationen kapseln (Laden und Speichern), nicht die
Geschäftslogik.

**Alternativen:** `checkAvailability` im Repository belassen (vermischt
Datenhaltung und Geschäftslogik); eine eigene Abfragemethode wie
`findOverlapping(...)` im Repository ergänzen (könnte bei Dataverse später
sinnvoll sein, vorerst jedoch YAGNI).

**Begründung:** Der Service lädt mit `findByVehicle(vehicle)` die Reservierungen
und prüft Überschneidungen selbst. Dadurch bleibt die Regel an einem Ort, ist
ohne Datenbank testbar und die Repository-Schnittstelle bleibt schlank. Bei
späterem Leistungsbedarf kann eine Abfragemethode ergänzt werden, ohne die
Regel zu duplizieren.

## 2026-11-09 – Mitarbeiterklasse auf entraId und role beschränken

**Entscheidung:** `Employee` enthält nur `entraId` und `role`. Personendaten
(Vorname, Nachname, Geburtsdatum, Abteilung usw.) werden im Backend nicht
gespeichert.

**Kontext:** Identitäts- und Personendaten werden bereits in Microsoft Entra ID
gepflegt. Eine Duplikation im Backend müsste synchronisiert werden und würde
personenbezogene Daten unnötig an einem weiteren Ort speichern.

**Alternativen:** Ein vollständiges Mitarbeiterprofil im Backend (mit
Synchronisierungsaufwand) oder ein Teilprofil (nur Anzeigename); beides wurde
verworfen.

**Begründung:** Entra ID bleibt die einzige Quelle der Wahrheit. Das entspricht
der Datenminimierung und verringert den Codeumfang. Das Backend benötigt die
Entra-ID zur Authentifizierung und die Rolle zur Autorisierung – mehr nicht.

## 2026-10-31 – Schlanke Paketstruktur nach Clean Architecture

**Entscheidung:** Das Backend wird in vier Pakete gegliedert:
`domain` (Entitäten, Enums und Repository-Schnittstellen), `application`
(Services und Geschäftslogik), `infrastructure` (In-Memory-Repositories,
später Dataverse) und `api` (REST-Controller).

**Kontext:** Die Datenhaltung mit Dataverse kommt erst später; die
Geschäftslogik soll jedoch bereits jetzt umgesetzt und getestet werden.

**Alternativen:** Eine flache Struktur (alles in einem Paket) oder die
klassische Controller-Service-Repository-Struktur ohne eigenes
Domain-Paket.

**Begründung:** Abhängigkeiten zeigen nach innen in Richtung `domain`. Beim
späteren Wechsel zu Dataverse wird nur `infrastructure` ersetzt;
Geschäftslogik und API bleiben unverändert. Gleichzeitig bleibt die Struktur
für ein Semesterprojekt überschaubar.

## 2026-10-31 – Reservierungsstatus ACTIVE und COMPLETED ergänzen

**Entscheidung:** Die Reservierungsstatus werden um `ACTIVE` und `COMPLETED`
ergänzt. Ablauf: `PROVISIONAL → CONFIRMED → ACTIVE → COMPLETED`; vor der
Übernahme ist jederzeit `CANCELLED` möglich.

**Kontext:** Die README sieht Geschäftslogik für Fahrzeugübernahme und
-rückgabe vor. Das ursprüngliche Modell (`PROVISIONAL`, `CONFIRMED`,
`CANCELLED`) konnte diese Vorgänge nicht abbilden.

**Alternativen:** Die Übernahme nur über den Fahrzeugstatus (`RENTED`)
abbilden oder Übernahme und Rückgabe vorerst weglassen.

**Begründung:** Ein ausdrücklicher Lebenszyklus definiert die zulässigen
Statuswechsel klar und macht sie gut testbar. Genau darauf liegt laut README
der Schwerpunkt der Backend-Tests.

## 2026-11-01 – Reservierungszeiträume mit LocalDateTime abbilden

**Entscheidung:** `startDate` und `endDate` in `Reservation` werden von
`LocalDate` auf `LocalDateTime` umgestellt und in `startDateTime` und
`endDateTime` umbenannt.

**Kontext:** Mitarbeitende benötigen Fahrzeuge teilweise nur für einige
Stunden (zum Beispiel für einen Kundenbesuch am Morgen). Zu diesem Zeitpunkt
gibt es noch keine Datenhaltung, API oder Frontend; die Änderung ist daher
jetzt nahezu kostenlos und wäre später aufwendiger.

**Alternativen:** Tagesbasierte Reservierungen mit `LocalDate` (einfacher,
aber keine halbtägigen Buchungen); `Instant` oder `ZonedDateTime`
(Zeitzonenunterstützung – zu aufwendig für ein Unternehmen mit nur einem
Standort in der Schweiz).

**Begründung:** Die Überschneidungslogik ist für beide Typen strukturell
identisch; der zusätzliche Aufwand beschränkt sich auf die Frontend-Eingabe und
Tests. Der fachliche Nutzen rechtfertigt die Änderung. `LocalDateTime` bildet
einen Zeitraum als lokale Uhrzeit ohne Komplexität durch Zeitzonen korrekt ab.
