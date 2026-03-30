package pizzahutproject;

public class Order {
    public static int NEXT_ID = 1;
    public final int id;
    public final Customer customer;
    public final Pizza pizza;
    public final char size; // 'S','M','L'
    public final int qty;
    public final int priority; // lower = higher priority
    public final long timestamp;

    public Order(Customer c, Pizza p, char size, int qty, int priority) {
        this.id = NEXT_ID++;
        this.customer = c;
        this.pizza = p;
        this.size = size;
        this.qty = qty;
        this.priority = priority;
        this.timestamp = System.nanoTime();
    }

    public double getTotalPrice() {
        int unit = switch (size) {
            case 'S' -> pizza.cost_S;
            case 'M' -> pizza.cost_M;
            default -> pizza.cost_L;
        };
        return unit * qty;
    }

    public String simpleString() {
        return "#" + id + " " + pizza.name + " x" + qty + " for " + customer.name;
    }

    public String detailedString() {
        // Updated to print string location
        return "#" + id + " " + pizza.name + " (" + size + ") x" + qty + " | " + customer.name + " (" + customer.location + ")";
    }
}