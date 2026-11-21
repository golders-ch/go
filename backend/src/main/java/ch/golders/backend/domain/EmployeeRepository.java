package ch.golders.backend.domain;

import java.util.List;

public interface EmployeeRepository {
    Employee findByEntraId(String entraId);
    void save(Employee employee);
}
