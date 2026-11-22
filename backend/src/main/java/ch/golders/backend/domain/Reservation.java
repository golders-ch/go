package ch.golders.backend.domain;

import java.time.LocalDateTime;

public class Reservation {
    private final String id;
    private final String reservationNumber;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private ReservationStatus status;
    private final Vehicle vehicle;
    private final Employee employee;

    public Reservation(String id, String reservationNumber, LocalDateTime startDateTime, LocalDateTime endDateTime,
            ReservationStatus status, Vehicle vehicle, Employee employee) {

        if (startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("The start date and time must be before the end date and time.");
        }

        this.id = id;
        this.reservationNumber = reservationNumber;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.status = status;
        this.vehicle = vehicle;
        this.employee = employee;
    }

    public String getId() {
        return id;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Employee getEmployee() {
        return employee;
    }
}
