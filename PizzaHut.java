package pizzahutproject;

import java.util.*;

public class PizzaHut {
    private Menu menu = new Menu();
    private HashMap<String, Customer> customers = new HashMap<>();
    private OrderQueue orderQueue = new OrderQueue();
    private UndoStack undoStack = new UndoStack();
    private DijkstraPathFinder pathFinder;

    public PizzaHut() {
        pathFinder = new DijkstraPathFinder();
    }

    public OrderQueue getOrderQueue() { return orderQueue; }
    public Menu getMenu() { return menu; }
    public DijkstraPathFinder getPathFinder() { return pathFinder; }

    public void seedSampleData() {
        // --- CITIES (X, Y Coordinates) ---
        pathFinder.addLocation("Sukkur (Shop)", 0, 0);
        pathFinder.addLocation("Rohri", 5, 2);
        pathFinder.addLocation("Khairpur", 25, 10);
        pathFinder.addLocation("Larkana", -30, 15);
        pathFinder.addLocation("Shikarpur", -10, 35);
        pathFinder.addLocation("Pano Aqil", 35, -5);
        pathFinder.addLocation("Ghotki", 60, -10);
        pathFinder.addLocation("Jacobabad", -25, 50);
        pathFinder.addLocation("Ranipur", 30, 20);
        pathFinder.addLocation("Gambat", 35, 25);

        pathFinder.autoGenerateGraph();

        // --- MENU ---
        menu.addLast(new Pizza("Peppy Paneer", 595, 395, 215, 15));
        menu.addLast(new Pizza("Indi Tandoori Paneer", 695,400,235,14));
        menu.addLast(new Pizza("Farmhouse",700,495,315, 14));
        menu.addLast(new Pizza("Margherita",535,335,185, 10));
        menu.addLast(new Pizza("Chicken Paradise",695,400,235, 15));

        // --- CUSTOMERS ---
        acceptCustomer(new Customer("Ananya", "B3 Silver Township", "Khairpur", "8983282880"));
        acceptCustomer(new Customer("Esha", "401 KB Society", "Rohri", "7517362175"));
    }

    public void acceptCustomer(Customer c) { customers.put(c.mobile, c); }
    public void undoLastAddition() { undoStack.pop(); }
}