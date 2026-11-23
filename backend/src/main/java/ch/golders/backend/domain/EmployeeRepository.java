package ch.golders.backend.domain;

public interface EmployeeRepository {
    Employee findByEntraId(String entraId);

    void save(Employee employee);
}
