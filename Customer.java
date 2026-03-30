package pizzahutproject;

public class Customer {
    public String name;
    public String address;
    public String location; // Changed from int to String (City Name)
    public String mobile;

    public Customer(String name, String address, String location, String mobile) {
        this.name = name;
        this.address = address;
        this.location = location;
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return name + " | " + address + " | " + location + " | " + mobile;
    }
}