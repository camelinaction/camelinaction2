package camelinaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Customer service bean
 */
public class CustomerService {

    /**
     * Split the customer into a list of departments
     *
     * @param customer the customer
     * @return the departments
     */
    public List<Department> splitDepartments(Customer customer) {
        // this is a very simple logic, but your use cases
        // may very well require more complex logic
        return customer.getDepartments();
    }

    /**
     * Create a dummy customre for testing purprose
     */
    public static Customer createCustomer() {
        List<Department> departments = new ArrayList<Department>();
        departments.add(new Department(222, "Oceanview 66", "89210", "USA"));
        departments.add(new Department(333, "Lakeside 41", "22020", "USA"));
        departments.add(new Department(444, "Highstreet 341", "11030", "USA"));

        Customer customer = new Customer(123, "Honda", departments);
        return customer;
    }
}
