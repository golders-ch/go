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
    private Vehicle vehicle1 = new Vehicle("1", "VW", "Golf", 4, 5, "Test", "TG123456", 1234, 1234, FuelType.DIESEL);
    private Vehicle vehicle2 = new Vehicle("2", "Audi", "A3", 4, 5, "Test", "ZH123456", 1234, 1234, FuelType.ELECTRIC);
    private Reservation reservation1 = new Reservation("1", "1", startDateTime, startDateTime.plusHours(1),
            ReservationStatus.PROVISIONAL, vehicle1, employee);
    private Reservation reservation2 = new Reservation("2", "2", startDateTime, startDateTime.plusHours(1),
            ReservationStatus.PROVISIONAL, vehicle1, employee);
    private Reservation reservation3 = new Reservation("3", "3", startDateTime.plusHours(1),
            startDateTime.plusHours(2),
            ReservationStatus.PROVISIONAL, vehicle1, employee);
    private Reservation reservation4 = new Reservation("4", "4", startDateTime,
            startDateTime.plusHours(1),
            ReservationStatus.CANCELLED, vehicle1, employee);
    private Reservation reservation5 = new Reservation("5", "5", startDateTime,
            startDateTime.plusHours(1),
            ReservationStatus.PROVISIONAL, vehicle2, employee);
    private Reservation reservation6 = new Reservation("6", "6", startDateTime.plusMinutes(30),
            startDateTime.plusMinutes(90),
            ReservationStatus.PROVISIONAL, vehicle1, employee);
    private Reservation reservation7 = new Reservation("7", "7", startDateTime.minusMinutes(30),
            startDateTime.plusMinutes(30),
            ReservationStatus.PROVISIONAL, vehicle1, employee);
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

    @DisplayName("Test overlapping reservations for different vehicles")
    @Test
    void testOverlappingReservationsForDifferentVehiclesAreAllowed() {
        ReservationService reservationService = new ReservationService(reservationRepository);
        reservationRepository.save(reservation1);
        reservationService.reserve(reservation5);
        Reservation savedReservation = reservationRepository.findById("1");
        assertSame(reservation1, savedReservation);
        Reservation savedReservation2 = reservationRepository.findById("5");
        assertSame(reservation5, savedReservation2);
    }

    @DisplayName("Test partially overlapping reservations 1")
    @Test
    void testPartiallyOverlappingReservations1() {
        ReservationService reservationService = new ReservationService(reservationRepository);
        reservationService.reserve(reservation1);
        assertThrows(IllegalStateException.class, () -> reservationService.reserve(reservation6));
    }

    @DisplayName("Test partially overlapping reservations 2")
    @Test
    void testPartiallyOverlappingReservations() {
        ReservationService reservationService = new ReservationService(reservationRepository);
        reservationService.reserve(reservation1);
        assertThrows(IllegalStateException.class, () -> reservationService.reserve(reservation7));
    }

}
