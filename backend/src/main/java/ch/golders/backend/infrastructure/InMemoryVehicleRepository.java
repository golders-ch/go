package ch.golders.backend.infrastructure;
import ch.golders.backend.domain.Vehicle;
import ch.golders.backend.domain.VehicleRepository;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryVehicleRepository implements VehicleRepository {
    private HashMap<String, Vehicle> vehicles = new HashMap<>();

    @Override
    public void save(Vehicle vehicle) {
        vehicles.put(vehicle.getId(), vehicle);
    }

    @Override
    public Vehicle findById(String id) {
        return vehicles.get(id);
    }

    @Override
    public List<Vehicle> findAll() {
        return new ArrayList<>(vehicles.values());
    }

    @Override
    public void delete(Vehicle vehicle) {
        vehicles.remove(vehicle.getId());
    }
}
