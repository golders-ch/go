# Architektur-Entscheidungsprotokoll

## 2026-11-23 – Lokal entwickeln und H2 statt Azure und Dataverse verwenden

**Entscheidung:** Das Projekt wird lokal entwickelt und soll eine H2-Datenbank
verwenden. Eine Azure-Bereitstellung und die Anbindung an Dataverse sind nicht
Bestandteil des aktuellen Projektumfangs.

**Kontext:** Der Aufwand für die ursprünglich geplante Azure- und Dataverse-
Anbindung sowie die Entra-ID-Integration und Bereitstellung auf Azure
Container Apps ist für das Projekt zu gross.

**Ursprünglicher Plan:** Die Power Apps Code App sollte mit einer
Spring-Boot-API verbunden werden. Das Backend sollte auf Azure Container Apps
laufen, Daten sollten in Dataverse gespeichert und Identitäten über Microsoft
Entra ID verwaltet werden.

**Reduzierter Umfang:** Die Power Apps Code App und das Spring-Boot-Backend
werden lokal ausgeführt. H2 ist als lokale Datenbank vorgesehen; Azure,
Dataverse und Entra ID werden vorerst nicht integriert.

**Begründung:** Der lokale Entwicklungsbetrieb mit H2 reduziert den
Integrations- und Bereitstellungsaufwand und hält den Projektumfang
beherrschbar.

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

**Kontext:** In der damaligen Planung sollten Identitäts- und Personendaten in
Microsoft Entra ID gepflegt werden. Eine Duplikation im Backend müsste
synchronisiert werden und würde personenbezogene Daten unnötig an einem
weiteren Ort speichern.

**Alternativen:** Ein vollständiges Mitarbeiterprofil im Backend (mit
Synchronisierungsaufwand) oder ein Teilprofil (nur Anzeigename); beides wurde
verworfen.

**Begründung:** In der damaligen Planung sollte Entra ID die Quelle der
Identitäts- und Personendaten sein. Das entspricht der Datenminimierung und
vermeidet eine Duplikation personenbezogener Daten im Backend.

**Aktueller Stand (2026-11-23):** Eine Entra-ID-Anbindung und Authentifizierung
werden vorerst nicht umgesetzt. Das schlanke Mitarbeiter-Modell mit `entraId`
und `role` bleibt als Vorbereitung für eine mögliche spätere Integration
bestehen; Personendaten werden lokal nicht dupliziert.

## 2026-11-23 – Entra ID vorerst nicht integrieren

**Entscheidung:** Die lokale Anwendung wird zunächst ohne Microsoft Entra ID
entwickelt. Eine andere Form der Anmeldung ist nicht Teil dieser Entscheidung.

**Kontext:** Der aktuelle Projektumfang soll lokale Entwicklung und eine
überschaubare Datenhaltung mit H2 ermöglichen, ohne zusätzliche Entra-ID-
Integration.

**Alternative:** Entra ID bereits jetzt für Anmeldung und Autorisierung
einbinden.

**Begründung:** Die Integration würde den Umfang erweitern. Das bestehende
Mitarbeiter-Modell mit `entraId` und `role` bleibt erhalten, damit eine
Integration später ergänzt werden kann.

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

**Aktueller Stand (2026-11-23):** Die damalige Planung einer späteren
Dataverse-Anbindung ist durch die Entscheidung für lokale Entwicklung mit H2
überholt. Die Paketstruktur bleibt davon unberührt.

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
