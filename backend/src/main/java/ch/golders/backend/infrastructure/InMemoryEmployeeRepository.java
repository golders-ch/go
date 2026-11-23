package ch.golders.backend.infrastructure;

import ch.golders.backend.domain.Employee;
import ch.golders.backend.domain.EmployeeRepository;
import java.util.HashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryEmployeeRepository implements EmployeeRepository {
    private HashMap<String, Employee> employeesByEntraId = new HashMap<>();

    @Override
    public Employee findByEntraId(String entraId) {
        return employeesByEntraId.get(entraId);
    }

    @Override
    public void save(Employee employee) {
        employeesByEntraId.put(employee.getEntraId(), employee);
    }

}
