package ch.golders.backend.infrastructure;

import ch.golders.backend.domain.Employee;
import ch.golders.backend.domain.EmployeeRepository;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

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
