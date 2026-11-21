package ch.golders.backend.infrastructure;
import ch.golders.backend.domain.Reservation;
import ch.golders.backend.domain.ReservationRepository;
import ch.golders.backend.domain.Employee;
import ch.golders.backend.domain.Vehicle;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryReservationRepository implements ReservationRepository {

    private HashMap<String, Reservation> reservations = new HashMap<>();

    @Override
    public void save(Reservation reservation) {
        reservations.put(reservation.getId(), reservation);
    }

    @Override
    public Reservation findById(String id) {
        return reservations.get(id);
    }

    @Override
    public List<Reservation> findByVehicle(Vehicle vehicle) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation reservation : reservations.values()) {
            if (reservation.getVehicle().getId().equals(vehicle.getId())) {
                result.add(reservation);
            }
        }
        return result;
    }

    @Override
    public List<Reservation> findByEmployee(Employee employee) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation reservation : reservations.values()) {
            if (reservation.getEmployee().getEntraId().equals(employee.getEntraId())) {
                result.add(reservation);
            }
        }
        return result;
    }

}
