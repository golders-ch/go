# Projektplan GO

## Projektziel

Firmeninternes Fahrzeugvermietungssystem für Golders.ch. Mitarbeitende können
Firmenfahrzeuge finden, reservieren, übernehmen und zurückgeben.

- Semesterprojekt
- Architektur: Power Apps Code App → Spring-Boot-API → Microsoft Dataverse
- Die gesamte Geschäftslogik liegt im Spring-Boot-Backend

## Aktueller Stand (2026-11-22)

- ✅ Projektstruktur initialisiert (Frontend, Backend, Dokumentation)
- ✅ Fachmodell definiert: `Vehicle`, `Employee`, `Reservation`
  (siehe `architecture.puml`)
- ✅ Frontend-Vorlage eingerichtet (React 19, Vite, Tailwind, shadcn/ui,
  Router, React Query)
- ✅ Backend-Grundgerüst eingerichtet (Spring Boot, Java 21, Maven)
- ✅ **Domänenmodell implementiert**:
  - Klassen: `Vehicle`, `Employee`, `Reservation`
  - Aufzählungstypen: `VehicleStatus`, `FuelType`, `Role`, `ReservationStatus`
  - Paketstruktur: `ch.golders.backend.domain`
  - Unveränderliche Felder mit `final` (ausser Status)
- ✅ **Mitarbeiterklasse verschlankt** (nur `entraId` und `role`;
  Personendaten verbleiben in Entra ID)
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
- ⬜ Reservierungsservice und Repository in Spring verdrahten
- ⬜ Weitere fachliche Tests für den Reservierungsservice ergänzen
- ⬜ Verhalten bei abgeschlossenen Reservierungen festlegen und umsetzen
- ⬜ Fahrzeugstatus bei Reservierungen berücksichtigen
- ⬜ Stornierung, Übernahme, Rückgabe und erlaubte Statuswechsel umsetzen
- ⬜ REST-API
- ⬜ Anbindung an Dataverse
- ⬜ Das Frontend zeigt noch Demo-Inhalte

## Nächste Schritte

1. **Backend: Geschäftslogik vervollständigen**
   - Reservierungsservice und Repository in Spring verdrahten
   - Weitere fachliche Tests für Zeiträume, Überschneidungen und
     Reservierungsstatus schreiben
   - Festlegen, wie abgeschlossene Reservierungen bei Überschneidungen
     berücksichtigt werden
   - Fahrzeugstatus prüfen
   - Stornierung, Übernahme, Rückgabe und zulässige Statuswechsel umsetzen
2. **Backend: REST-API** und Swagger/OpenAPI
3. **Dataverse-Anbindung** (Datenhaltung, OAuth über Entra ID)
4. **Frontend: Fachliche Ansichten** (Fahrzeugliste, Reservierung usw.)
5. **Bereitstellung** auf Azure Container Apps

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
| Backend: REST-API                        | ⬜ offen                   |
| Dataverse-Anbindung                      | ⬜ offen                   |
| Frontend: Kernfunktionen                 | ⬜ offen                   |
| Bereitstellung                           | ⬜ offen                   |
