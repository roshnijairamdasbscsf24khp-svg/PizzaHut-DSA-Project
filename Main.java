//package pizzahutproject;
//
//import java.util.Scanner;
//
//public class Main {
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        PizzaHut shop = new PizzaHut();
//
//        // seed some menu, customers and graph (sample data)
//        shop.seedSampleData();
//
//        boolean running = true;
//        while (running) {
//            shop.clearScreen();
//            System.out.println("\n\t\t=== PIZZA HUT (DSA Project) ===\n");
//            System.out.println("1. View Menu");
//            System.out.println("2. Place Order (Normal)");
//            System.out.println("3. Place VIP Order (Priority)");
//            System.out.println("4. Process Next Order (serve)");
//            System.out.println("5. Undo Last Add (stack)");
//            System.out.println("6. View All Pending Orders");
//            System.out.println("7. Add/Remove Menu Item (LinkedList)");
//            System.out.println("8. Search Customer by Mobile");
//            System.out.println("9. Show Shortest Distance to Location (Dijkstra)");
//            System.out.println("0. Exit");
//            System.out.print("\nChoose: ");
//
//            int ch = -1;
//            try { ch = Integer.parseInt(sc.nextLine().trim()); }
//            catch (Exception e) { System.out.println("Invalid input. Press Enter."); sc.nextLine(); continue; }
//
//            switch (ch) {
//                case 1 -> { shop.getMenu().displayMenu(); promptEnter(sc); }
//                case 2 -> { shop.placeOrderInteractive(false, sc); promptEnter(sc); }
//                case 3 -> { shop.placeOrderInteractive(true, sc); promptEnter(sc); }
//                case 4 -> { shop.processNextOrder(); promptEnter(sc); }
//                case 5 -> { shop.undoLastAddition(); promptEnter(sc); }
//                case 6 -> { shop.showAllPendingOrders(); promptEnter(sc); }
//                case 7 -> { shop.manageMenuInteractive(sc); promptEnter(sc); }
//                case 8 -> { shop.searchCustomerInteractive(sc); promptEnter(sc); }
//                case 9 -> { shop.showShortestPathInteractive(sc); promptEnter(sc); }
//                case 0 -> { running = false; System.out.println("Exiting..."); }
//                default -> { System.out.println("Invalid choice."); promptEnter(sc); }
//            }
//        }
//
//        sc.close();
//    }
//
//    private static void promptEnter(Scanner sc) {
//        System.out.println("\n(press Enter to continue)");
//        sc.nextLine();
//    }
//}
