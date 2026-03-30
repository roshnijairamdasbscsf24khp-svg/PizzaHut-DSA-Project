package pizzahutproject;

import java.util.*;

// OrderQueue maintains two structures:
// - a FIFO queue for normal orders
// - a PriorityQueue for VIP orders (priority + timestamp tie-breaker)
public class OrderQueue {
    private Queue<Order> fifo = new LinkedList<>();
    private PriorityQueue<Order> vip = new PriorityQueue<>(Comparator
            .comparingInt((Order o) -> o.priority)
            .thenComparingLong(o -> o.timestamp));

    public void addOrder(Order o) {
        if (o.priority <= 2) vip.add(o); else fifo.add(o);
    }

    // poll: VIP first, then FIFO
    public Order pollOrder() {
        if (!vip.isEmpty()) return vip.poll();
        return fifo.poll();
    }

    public void displayAll() {
        System.out.println("\n--- VIP Orders (priority) ---");
        if (vip.isEmpty()) System.out.println("None");
        else {
            List<Order> tmp = new ArrayList<>(vip);
            tmp.sort(Comparator.comparingInt((Order o) -> o.priority).thenComparingLong(o -> o.timestamp));
            for (Order o : tmp) System.out.println(o.detailedString());
        }
        System.out.println("\n--- Normal Orders (FIFO) ---");
        if (fifo.isEmpty()) System.out.println("None");
        else {
            for (Order o : fifo) System.out.println(o.detailedString());
        }
    }
}
