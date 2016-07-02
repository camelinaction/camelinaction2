package camelinaction;

import java.util.List;

public class Customer {

    private int id;
    private String name;
    private List<Department> departments;

    public Customer(int id, String name, List<Department> departments) {
        this.id = id;
        this.name = name;
        this.departments = departments;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    @Override
    public String toString() {
        return "Customer " + id + ", name";
    }
}
