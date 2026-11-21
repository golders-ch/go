package ch.golders.backend.domain;

import java.util.List;

public interface ReservationRepository {
    void save(Reservation reservation);
    Reservation findById(String id);
    List<Reservation> findByEmployee(Employee employee);
    List<Reservation> findByVehicle(Vehicle vehicle);
}
