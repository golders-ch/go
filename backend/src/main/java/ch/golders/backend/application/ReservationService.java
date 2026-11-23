package ch.golders.backend.application;

import ch.golders.backend.domain.ReservationRepository;
import ch.golders.backend.domain.Reservation;
import ch.golders.backend.domain.Vehicle;
import ch.golders.backend.domain.ReservationStatus;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    List<Reservation> getReservationsByVehicle(Vehicle vehicle) {
        return reservationRepository.findByVehicle(vehicle);
    }

    private boolean reservationsOverlap(Reservation first, Reservation second) {
        return first.getEndDateTime().isAfter(second.getStartDateTime())
                && second.getEndDateTime().isAfter(first.getStartDateTime());
    }

    private boolean overlapsExistingReservations(Reservation newReservation) {
        List<Reservation> existingReservations = getReservationsByVehicle(newReservation.getVehicle());
        for (Reservation existingReservation : existingReservations) {
            if (reservationsOverlap(newReservation, existingReservation)
                    && existingReservation.getStatus() != ReservationStatus.CANCELLED) {
                return true;
            }
        }
        return false;
    }

    public void reserve(Reservation newReservation) {
        if (overlapsExistingReservations(newReservation)) {
            throw new IllegalStateException("The reservation overlaps an existing reservation.");
        }
        reservationRepository.save(newReservation);
    }

}
