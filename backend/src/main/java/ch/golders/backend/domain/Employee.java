package ch.golders.backend.domain;
public class Employee {

    private final String entraId;
    private final Role role;

    public Employee(String entraId, Role role) {
        this.entraId = entraId;
        this.role = role;
    }

    public String getEntraId() {
        return entraId;
    }

    public Role getRole() {
        return role;
    }

}
