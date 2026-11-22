package ch.golders.backend.application;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertSame;
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

    private Employee employee = new Employee("1234", Role.EMPLOYEE);
    private Vehicle vehicle = new Vehicle("1", "VW", "Golf", 4, 5, "Test", "TG123456", 1234, 1234, FuelType.DIESEL);
    private Reservation reservation = new Reservation("1", "1", LocalDateTime.now(), LocalDateTime.now().plusHours(1),
            ReservationStatus.PROVISIONAL, vehicle, employee);
    private ReservationRepository reservationRepository = new InMemoryReservationRepository();

    @Test
    void testReserve() {
        ReservationService reservationService = new ReservationService(reservationRepository);
        reservationService.reserve(reservation);
        Reservation savedReservation = reservationRepository.findById("1");
        assertSame(reservation, savedReservation);
    }
}
