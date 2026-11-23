package ch.golders.backend;

import ch.golders.backend.application.ReservationService;
import ch.golders.backend.domain.Employee;
import ch.golders.backend.domain.FuelType;
import ch.golders.backend.domain.ReservationRepository;
import ch.golders.backend.domain.ReservationStatus;
import ch.golders.backend.domain.Role;
import ch.golders.backend.domain.Reservation;
import ch.golders.backend.domain.Vehicle;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
class BackendApplicationTests {

	@Autowired
	ReservationService reservationService;

	@Autowired
	ReservationRepository reservationRepository;

	@Test
	void reservationIsSaved() {
		LocalDateTime startDateTime = LocalDateTime.of(2026, 1, 1, 12, 0);
		Employee employee = new Employee("1234", Role.EMPLOYEE);
		Vehicle vehicle1 = new Vehicle("1", "VW", "Golf", 4, 5, "Test", "TG123456", 1234, 1234, FuelType.DIESEL);
		Reservation reservation1 = new Reservation("1", "1", startDateTime, startDateTime.plusHours(1),
				ReservationStatus.PROVISIONAL, vehicle1, employee);

		reservationService.reserve(reservation1);
		Reservation retrievedReservation = reservationRepository.findById("1");
		assertEquals(reservation1.getId(), retrievedReservation.getId());
	}

	@Test
	void contextLoads() {
	}

}
