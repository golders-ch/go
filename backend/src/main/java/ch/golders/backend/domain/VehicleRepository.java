package ch.golders.backend.domain;

import java.util.List;

public interface VehicleRepository {
    void save(Vehicle vehicle);
    Vehicle findById(String id);
    List<Vehicle> findAll();
    void delete(Vehicle vehicle);
}
