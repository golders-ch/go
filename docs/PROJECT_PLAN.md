# Projektplan GO

## Projektziel

Firmeninternes Fahrzeugvermietungssystem für Golders.ch. Mitarbeitende können
Firmenfahrzeuge finden, reservieren, übernehmen und zurückgeben.

- Semesterprojekt
- Zielarchitektur: lokal ausgeführte Power Apps Code App → Spring-Boot-API →
  H2-Datenbank
- Die gesamte Geschäftslogik liegt im Spring-Boot-Backend
- Ursprünglich geplant: Power Apps Code App, Spring-Boot-API, Dataverse,
  Azure Container Apps und Microsoft Entra ID
- Wegen des zu grossen Zeitaufwands auf lokale Ausführung und H2 reduziert;
  Azure-Bereitstellung, Dataverse und Entra-ID-Integration sind nicht Teil des
  aktuellen Projektumfangs

## Aktueller Stand (2026-11-23)

- ✅ Projektstruktur initialisiert (Frontend, Backend, Dokumentation)
- ✅ Fachmodell definiert: `Vehicle`, `Employee`, `Reservation`
  (siehe `architecture.puml`)
- ✅ Power-Apps-Code-App-Vorlage eingerichtet (React 19, Vite, Tailwind,
  shadcn/ui, Router, React Query); lokale Ausführung ist vorgesehen
- ✅ Backend-Grundgerüst eingerichtet (Spring Boot, Java 21, Maven)
- ✅ **Domänenmodell implementiert**:
  - Klassen: `Vehicle`, `Employee`, `Reservation`
  - Aufzählungstypen: `VehicleStatus`, `FuelType`, `Role`, `ReservationStatus`
  - Paketstruktur: `ch.golders.backend.domain`
  - Unveränderliche Felder mit `final` (ausser Status)
- ✅ **Mitarbeiterklasse verschlankt** (nur `entraId` und `role`;
  vorbereitet für eine mögliche spätere Entra-ID-Anbindung; aktuell gibt es
  keine Entra-Authentifizierung oder -Integration)
- ✅ Repository-Schnittstellen entworfen (siehe `repositories.puml`)
- ✅ Repository-Schnittstellen in Java umgesetzt (`domain`)
- ✅ In-Memory-Implementierungen der Repositories hinzugefügt (`infrastructure`)
- ✅ Starttest zum Laden des Spring-Kontexts vorhanden
- ✅ Reservierungszeiträume werden geprüft: Der Startzeitpunkt darf nicht nach
  dem Endzeitpunkt liegen
- ✅ `ReservationService.reserve(...)` prüft auf zeitliche Überschneidungen und
  speichert die Reservierung über das Repository
- ✅ Erster fachlicher Test für `ReservationService.reserve(...)`:
  Eine konfliktfreie Reservierung wird im In-Memory-Repository gespeichert
  und über ihre ID wiedergefunden
- ✅ Weitere Tests prüfen, dass Überschneidungen abgelehnt werden, direkt
  anschliessende Reservierungen erlaubt sind und stornierte Reservierungen
  einen Zeitraum nicht blockieren
- ✅ Tests decken auch teilweise Überschneidungen und gleichzeitige
  Reservierungen für verschiedene Fahrzeuge ab
- ✅ Reservierungsservice und In-Memory-Repository in Spring verdrahtet;
  ein Spring-Test speichert eine Reservierung über den Service und findet
  sie über das Repository wieder
- ✅ In-Memory-Mitarbeiterrepository als Spring-Bean registriert
- ⬜ Weitere fachliche Tests für den Reservierungsservice ergänzen
- ⬜ Verhalten bei abgeschlossenen Reservierungen festlegen und umsetzen
- ⬜ Fahrzeugstatus bei Reservierungen berücksichtigen
- ⬜ Stornierung, Übernahme, Rückgabe und erlaubte Statuswechsel umsetzen
- ⬜ REST-API
- ⬜ H2-Datenbank lokal einrichten und an die Repositories anbinden
- ⬜ Das Frontend zeigt noch Demo-Inhalte

## Nächste Schritte

1. **Backend: Geschäftslogik vervollständigen**
   - Weitere fachliche Tests für Zeiträume, Überschneidungen und
     Reservierungsstatus schreiben
   - Festlegen, wie abgeschlossene Reservierungen bei Überschneidungen
     berücksichtigt werden
   - Fahrzeugstatus prüfen
   - Stornierung, Übernahme, Rückgabe und zulässige Statuswechsel umsetzen
2. **Backend: H2-Datenbank** lokal einrichten und Repositories daran anbinden
3. **Backend: REST-API** und Swagger/OpenAPI
4. **Frontend: Fachliche Ansichten** (Fahrzeugliste, Reservierung usw.)

## Mögliche Erweiterungen (Aufgabenliste)

- Vermietbarkeit pro Abteilung einschränken (Idee aus der Regelsammlung,
  vorerst bewusst nicht umgesetzt – YAGNI)

## Meilensteine

| Meilenstein                              | Status                    |
| ---------------------------------------- | ------------------------- |
| Projektstruktur und Fachmodell           | ✅ abgeschlossen           |
| Backend: Domänenmodell                   | ✅ abgeschlossen           |
| Backend: Reservierungsservice            | 🔄 teilweise umgesetzt     |
| Backend: Geschäftslogik mit Tests        | ⬜ offen                   |
| Backend: lokale Datenhaltung mit H2      | ⬜ offen                   |
| Backend: REST-API                        | ⬜ offen                   |
| Frontend: Kernfunktionen                 | ⬜ offen                   |
