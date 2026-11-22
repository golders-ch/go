package ch.golders.backend.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.time.LocalDateTime;
import ch.golders.backend.domain.Reservation;
import ch.golders.backend.domain.ReservationRepository;
import ch.golders.backend.domain.ReservationStatus;
import ch.golders.backend.domain.Vehicle;
import ch.golders.backend.domain.FuelType;
import ch.golders.backend.domain.Employee;
import ch.golders.backend.domain.Role;
import ch.golders.backend.infrastructure.InMemoryReservationRepository;

public class ReservationServiceTest {

    private LocalDateTime startDateTime = LocalDateTime.of(2026, 1, 1, 12, 0);
    private Employee employee = new Employee("1234", Role.EMPLOYEE);
    private Vehicle vehicle = new Vehicle("1", "VW", "Golf", 4, 5, "Test", "TG123456", 1234, 1234, FuelType.DIESEL);
    private Reservation reservation1 = new Reservation("1", "1", startDateTime, startDateTime.plusHours(1),
            ReservationStatus.PROVISIONAL, vehicle, employee);
    private Reservation reservation2 = new Reservation("2", "2", startDateTime, startDateTime.plusHours(1),
            ReservationStatus.PROVISIONAL, vehicle, employee);
    private Reservation reservation3 = new Reservation("3", "3", startDateTime.plusHours(1),
            startDateTime.plusHours(2),
            ReservationStatus.PROVISIONAL, vehicle, employee);
    private Reservation reservation4 = new Reservation("4", "4", startDateTime,
            startDateTime.plusHours(1),
            ReservationStatus.CANCELLED, vehicle, employee);
    private ReservationRepository reservationRepository = new InMemoryReservationRepository();

    // Test cases for ReservationService

    @Test
    @DisplayName("Test reserving a single reservation")
    void testReserve() {
        ReservationService reservationService = new ReservationService(reservationRepository);
        reservationService.reserve(reservation1);
        Reservation savedReservation = reservationRepository.findById("1");
        assertSame(reservation1, savedReservation);
    }

    @DisplayName("Test reserving overlapping reservations")
    @Test
    void testReserveOverlapping() {
        ReservationService reservationService = new ReservationService(reservationRepository);
        reservationService.reserve(reservation1);
        assertThrows(IllegalStateException.class, () -> reservationService.reserve(reservation2));
    }

    @DisplayName("Test reserving adjacent reservations")
    @Test
    void testReserveAdjacentReservations() {
        ReservationService reservationService = new ReservationService(reservationRepository);
        reservationService.reserve(reservation1);
        reservationService.reserve(reservation3);
        Reservation savedReservation = reservationRepository.findById("1");
        Reservation savedReservation2 = reservationRepository.findById("3");
        assertSame(reservation1, savedReservation);
        assertSame(reservation3, savedReservation2);
    }

    @DisplayName("Test cancelled reservation not blocking new reservations")
    @Test
    void testCancelledReservationDoesNotBlock() {
        ReservationService reservationService = new ReservationService(reservationRepository);
        reservationRepository.save(reservation4);
        reservationService.reserve(reservation1);
        Reservation savedReservation = reservationRepository.findById("1");
        assertSame(reservation1, savedReservation);
    }

}
