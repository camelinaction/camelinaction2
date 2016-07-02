package camelinaction;

public class Department {

    private int id;
    private String address;
    private String zip;
    private String country;

    public Department(int id, String address, String zip, String country) {
        this.id = id;
        this.address = address;
        this.zip = zip;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getZip() {
        return zip;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "Department " + id + ", " + address + ", " + zip + ", " + country;
    }
}
