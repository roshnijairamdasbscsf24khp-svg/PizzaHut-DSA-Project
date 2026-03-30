package pizzahutproject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;

public class ModernPizzaGUI extends JFrame {

    private final PizzaHut backend;

    // --- GUI Components ---
    private JTable menuTable;
    private DefaultTableModel menuModel;

    // POS Inputs
    private JTextField txtMobile, txtName, txtAddress;
    private JComboBox<String> cmbLocation; // String for City Names
    private JComboBox<String> cmbPizzaSelect;
    private JComboBox<String> cmbSize;
    private JSpinner spinQty;

    // Admin Inputs
    private JTextField txtItemName, txtPriceS, txtPriceM, txtPriceL, txtBakeTime, txtRemoveID;

    // Queue Lists
    private DefaultListModel<String> vipListModel;
    private DefaultListModel<String> normalListModel;

    // Styling
    private final Color BRAND_RED = new Color(200, 16, 46);
    private final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);

    public ModernPizzaGUI() {
        // 1. Initialize Backend & Seed Data (Cities & Menu)
        backend = new PizzaHut();
        backend.seedSampleData();

        // 2. Window Setup
        setTitle("Pizza Hut Management System (Sukkur IBA Project)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 760);
        setLocationRelativeTo(null);

        // Apply Theme
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) { /* Ignore */ }

        setLayout(new BorderLayout());

        // 3. Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BRAND_RED);
        JLabel titleLabel = new JLabel("  PIZZA HUT POS SYSTEM", JLabel.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(15, 15, 15, 15));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // 4. Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabs.addTab("Point of Sale", createPOSPanel());
        tabs.addTab("Kitchen Display (KDS)", createKitchenPanel());
        tabs.addTab("Menu Management", createAdminPanel());

        add(tabs, BorderLayout.CENTER);

        // 5. Initial Data Load
        refreshMenuData();
        refreshQueueData();
    }

    // ================= POS PANEL =================
    private JPanel createPOSPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // -- Left: Menu Table --
        String[] columns = {"ID", "Item Name", "Small (Rs)", "Medium (Rs)", "Large (Rs)", "Time(m)"};
        menuModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        menuTable = new JTable(menuModel);
        menuTable.setRowHeight(30);
        menuTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane tableScroll = new JScrollPane(menuTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder(null, "Current Menu", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, HEADER_FONT));

        // -- Right: Order Form --
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(null, "New Order", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, HEADER_FONT));
        formPanel.setPreferredSize(new Dimension(420, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Customer Section
        addSectionHeader(formPanel, "Customer Details", gbc, 0);

        addLabel(formPanel, "Mobile:", gbc, 1);
        txtMobile = new JTextField();
        JPanel mobilePanel = new JPanel(new BorderLayout(5, 0));
        mobilePanel.add(txtMobile, BorderLayout.CENTER);
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> searchCustomer());
        mobilePanel.add(btnSearch, BorderLayout.EAST);
        gbc.gridx = 1; formPanel.add(mobilePanel, gbc);

        addLabel(formPanel, "Name:", gbc, 2);
        txtName = new JTextField();
        gbc.gridx = 1; formPanel.add(txtName, gbc);

        addLabel(formPanel, "City:", gbc, 3);
        // Load cities dynamically from backend
        java.util.List<String> cities = backend.getPathFinder().getCityNames();
        cmbLocation = new JComboBox<>(cities.toArray(new String[0]));
        gbc.gridx = 1; formPanel.add(cmbLocation, gbc);

        addLabel(formPanel, "Address:", gbc, 4);
        txtAddress = new JTextField();
        gbc.gridx = 1; formPanel.add(txtAddress, gbc);

        // Order Section
        addSectionHeader(formPanel, "Item Selection", gbc, 5);

        addLabel(formPanel, "Select Pizza:", gbc, 6);
        cmbPizzaSelect = new JComboBox<>();
        gbc.gridx = 1; formPanel.add(cmbPizzaSelect, gbc);

        addLabel(formPanel, "Size:", gbc, 7);
        cmbSize = new JComboBox<>(new String[]{"S", "M", "L"});
        gbc.gridx = 1; formPanel.add(cmbSize, gbc);

        addLabel(formPanel, "Quantity:", gbc, 8);
        spinQty = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        gbc.gridx = 1; formPanel.add(spinQty, gbc);

        // Buttons
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        JButton btnNormal = createStyledButton("Place Normal", new Color(46, 204, 113));
        JButton btnVIP = createStyledButton("Place VIP", new Color(241, 196, 15));

        btnNormal.addActionListener(e -> placeOrder(false));
        btnVIP.addActionListener(e -> placeOrder(true));

        btnPanel.add(btnNormal);
        btnPanel.add(btnVIP);

        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        formPanel.add(btnPanel, gbc);

        JButton btnUndo = new JButton("Undo Last Order");
        btnUndo.addActionListener(e -> performUndo());
        gbc.gridy = 10; formPanel.add(btnUndo, gbc);

        panel.add(tableScroll, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.EAST);

        return panel;
    }

    // ================= ADMIN PANEL (FIXED) =================
    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // -- Add Item Section --
        JLabel lblAdd = new JLabel("Add New Menu Item");
        lblAdd.setFont(HEADER_FONT);
        lblAdd.setForeground(new Color(0, 102, 204));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblAdd, gbc);

        txtItemName = new JTextField(20);
        txtPriceS = new JTextField(10);
        txtPriceM = new JTextField(10);
        txtPriceL = new JTextField(10);
        txtBakeTime = new JTextField(10);

        gbc.gridwidth = 1;
        gbc.gridy = 1; addLabel(panel, "Name:", gbc, 1); gbc.gridx=1; panel.add(txtItemName, gbc);
        gbc.gridy = 2; addLabel(panel, "Price Small:", gbc, 2); gbc.gridx=1; panel.add(txtPriceS, gbc);
        gbc.gridy = 3; addLabel(panel, "Price Medium:", gbc, 3); gbc.gridx=1; panel.add(txtPriceM, gbc);
        gbc.gridy = 4; addLabel(panel, "Price Large:", gbc, 4); gbc.gridx=1; panel.add(txtPriceL, gbc);
        gbc.gridy = 5; addLabel(panel, "Bake Time (min):", gbc, 5); gbc.gridx=1; panel.add(txtBakeTime, gbc);

        JButton btnAdd = createStyledButton("Add to Menu", new Color(0, 102, 204));
        btnAdd.addActionListener(e -> addMenuItem());
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        panel.add(btnAdd, gbc);

        // -- Remove Item Section --
        gbc.gridy = 7;
        panel.add(new JSeparator(), gbc);

        JLabel lblRem = new JLabel("Remove Menu Item");
        lblRem.setFont(HEADER_FONT);
        lblRem.setForeground(BRAND_RED);
        gbc.gridy = 8;
        panel.add(lblRem, gbc);

        JPanel removeContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        removeContainer.add(new JLabel("Enter ID (from table): "));
        txtRemoveID = new JTextField(5);
        removeContainer.add(txtRemoveID);
        JButton btnRemove = createStyledButton("Remove", BRAND_RED);
        btnRemove.addActionListener(e -> removeMenuItem());
        removeContainer.add(btnRemove);

        gbc.gridy = 9;
        panel.add(removeContainer, gbc);

        // Spacer to push everything up
        gbc.gridy = 10; gbc.weighty = 1.0;
        panel.add(new JLabel(), gbc);

        return panel;
    }

    // ================= KITCHEN PANEL =================
    private JPanel createKitchenPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 15, 0));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel vipPanel = new JPanel(new BorderLayout());
        vipPanel.setBorder(BorderFactory.createTitledBorder(null, "VIP Queue (Priority)", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, HEADER_FONT, Color.ORANGE));
        vipListModel = new DefaultListModel<>();
        JList<String> listVip = new JList<>(vipListModel);
        listVip.setFont(new Font("Monospaced", Font.BOLD, 14));
        vipPanel.add(new JScrollPane(listVip), BorderLayout.CENTER);

        JPanel normalPanel = new JPanel(new BorderLayout());
        normalPanel.setBorder(BorderFactory.createTitledBorder(null, "Standard Queue (FIFO)", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, HEADER_FONT, Color.GREEN));
        normalListModel = new DefaultListModel<>();
        JList<String> listNormal = new JList<>(normalListModel);
        listNormal.setFont(new Font("Monospaced", Font.PLAIN, 14));
        normalPanel.add(new JScrollPane(listNormal), BorderLayout.CENTER);

        JButton btnServe = createStyledButton("SERVE NEXT ORDER", BRAND_RED);
        btnServe.setPreferredSize(new Dimension(0, 50));
        btnServe.addActionListener(e -> serveNextOrder());

        JPanel container = new JPanel(new BorderLayout());
        container.add(panel, BorderLayout.CENTER);
        container.add(btnServe, BorderLayout.SOUTH);

        panel.add(vipPanel);
        panel.add(normalPanel);
        return container;
    }

    // ================= LOGIC =================

    private void searchCustomer() {
        String mobile = txtMobile.getText().trim();
        if (mobile.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter Mobile Number."); return; }
        try {
            Field mapField = PizzaHut.class.getDeclaredField("customers");
            mapField.setAccessible(true);
            HashMap<String, Customer> customers = (HashMap<String, Customer>) mapField.get(backend);
            Customer c = customers.get(mobile);

            if (c != null) {
                txtName.setText(c.name);
                txtAddress.setText(c.address);
                cmbLocation.setSelectedItem(c.location); // Auto-select City
                JOptionPane.showMessageDialog(this, "Customer Found!");
            } else {
                JOptionPane.showMessageDialog(this, "Customer not found. Please fill details.");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void placeOrder(boolean isVIP) {
        try {
            if (txtMobile.getText().isEmpty() || txtName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Customer details missing!");
                return;
            }

            // Create Customer
            Customer c = new Customer(txtName.getText(), txtAddress.getText(), (String) cmbLocation.getSelectedItem(), txtMobile.getText());
            backend.acceptCustomer(c);

            // Create Order
            int pizzaIdx = cmbPizzaSelect.getSelectedIndex();
            Pizza p = backend.getMenu().getAt(pizzaIdx);
            char size = ((String) cmbSize.getSelectedItem()).charAt(0);
            int qty = (Integer) spinQty.getValue();
            int priority = isVIP ? 1 : 5;

            Order o = new Order(c, p, size, qty, priority);
            backend.getOrderQueue().addOrder(o);

            // Add to Undo Stack
            try {
                Field stackField = PizzaHut.class.getDeclaredField("undoStack");
                stackField.setAccessible(true);
                UndoStack stack = (UndoStack) stackField.get(backend);
                stack.push("ADD_ORDER:#" + o.id + " " + o.pizza.name);
            } catch (Exception ex) {}

            // SHOW REALISTIC RECEIPT
            String receipt = String.format("""
                    ********** PIZZA HUT RECEIPT **********
                    Order ID: #%d
                    Date: %s
                    ---------------------------------------
                    Customer: %s
                    Mobile: %s
                    Location: %s
                    ---------------------------------------
                    Item: %s
                    Size: %s
                    Qty: %d
                    ---------------------------------------
                    TOTAL AMOUNT: Rs. %.2f
                    ***************************************
                    """, o.id, new Date(), c.name, c.mobile, c.location, p.name, size, qty, o.getTotalPrice());

            JTextArea area = new JTextArea(receipt);
            area.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Order Placed Successfully", JOptionPane.INFORMATION_MESSAGE);

            refreshQueueData();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error placing order. Check inputs.");
        }
    }

    private void serveNextOrder() {
        OrderQueue q = backend.getOrderQueue();
        Order next = q.pollOrder();
        if (next == null) { JOptionPane.showMessageDialog(this, "No orders to serve."); return; }

        double dist = backend.getPathFinder().getDistanceToCity(next.customer.location);

        String msg = String.format("""
                SERVING ORDER #%d
                ---------------------
                Customer: %s
                Destination: %s
                Calculated Distance: %.2f km
                ---------------------
                Driver assigned.
                """, next.id, next.customer.name, next.customer.location, dist);

        JOptionPane.showMessageDialog(this, msg, "Kitchen Update", JOptionPane.INFORMATION_MESSAGE);
        refreshQueueData();
    }

    private void addMenuItem() {
        try {
            String name = txtItemName.getText();
            int s = Integer.parseInt(txtPriceS.getText());
            int m = Integer.parseInt(txtPriceM.getText());
            int l = Integer.parseInt(txtPriceL.getText());
            int t = Integer.parseInt(txtBakeTime.getText());

            Pizza p = new Pizza(name, l, m, s, t);
            backend.getMenu().addLast(p);
            refreshMenuData();
            JOptionPane.showMessageDialog(this, "Menu Item Added!");

            // Clear fields
            txtItemName.setText(""); txtPriceS.setText(""); txtPriceM.setText(""); txtPriceL.setText(""); txtBakeTime.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid numbers entered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeMenuItem() {
        try {
            int id = Integer.parseInt(txtRemoveID.getText().trim());
            Pizza removed = backend.getMenu().removeAt(id - 1); // ID starts at 1, Index at 0
            if (removed != null) {
                refreshMenuData();
                JOptionPane.showMessageDialog(this, "Removed: " + removed.name);
                txtRemoveID.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "ID not found.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid ID.");
        }
    }

    private void performUndo() {
        backend.undoLastAddition();
        refreshQueueData();
        JOptionPane.showMessageDialog(this, "Last action was undone.");
    }

    // ================= HELPERS =================

    private void refreshMenuData() {
        menuModel.setRowCount(0);
        cmbPizzaSelect.removeAllItems();
        Menu menu = backend.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            Pizza p = menu.getAt(i);
            menuModel.addRow(new Object[]{i + 1, p.name, p.cost_S, p.cost_M, p.cost_L, p.time_reqd});
            cmbPizzaSelect.addItem(p.name);
        }
    }

    private void refreshQueueData() {
        vipListModel.clear();
        normalListModel.clear();
        OrderQueue q = backend.getOrderQueue();

        // Access private queues via Reflection (or you can add getters in OrderQueue)
        try {
            Field vipField = OrderQueue.class.getDeclaredField("vip");
            vipField.setAccessible(true);
            PriorityQueue<Order> vipQ = (PriorityQueue<Order>) vipField.get(q);

            Field fifoField = OrderQueue.class.getDeclaredField("fifo");
            fifoField.setAccessible(true);
            Queue<Order> fifoQ = (Queue<Order>) fifoField.get(q);

            for (Order o : vipQ) vipListModel.addElement(o.detailedString());
            for (Order o : fifoQ) normalListModel.addElement(o.detailedString());
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void addSectionHeader(JPanel p, String t, GridBagConstraints gbc, int r) {
        JLabel l = new JLabel(t); l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setForeground(BRAND_RED);
        gbc.gridx = 0; gbc.gridy = r; gbc.gridwidth = 2;
        p.add(l, gbc);
        gbc.gridwidth = 1;
    }

    private void addLabel(JPanel p, String t, GridBagConstraints gbc, int r) {
        gbc.gridx = 0; gbc.gridy = r;
        p.add(new JLabel(t), gbc);
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ModernPizzaGUI().setVisible(true));
    }
}