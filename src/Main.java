import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class Main extends JFrame {
    private InventoryDAO dao = new InventoryDAOImpl();
    private DefaultTableModel tableModel;
    private JTable table;

    public Main() {
        setTitle("Inventory Manager");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Qty", "Price"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons + Search Field Panel
        JPanel bottomPanel = new JPanel(new BorderLayout());

// Left side: buttons
        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Add Item");
        JButton btnEdit = new JButton("Edit Item");
        JButton btnDelete = new JButton("Delete Item");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        bottomPanel.add(buttonPanel, BorderLayout.WEST);

// Right side: search field
        JPanel searchPanel = new JPanel();
        JTextField searchField = new JTextField(10);
        JButton btnSearch = new JButton("Search");
        searchPanel.add(new JLabel("Search by ID:"));
        searchPanel.add(searchField);
        searchPanel.add(btnSearch);
        bottomPanel.add(searchPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

// Load data
        loadItems();

// Button actions
        btnAdd.addActionListener(e -> addItem());
        btnEdit.addActionListener(e -> editItem());
        btnDelete.addActionListener(e -> deleteItem());

// Search logic
        btnSearch.addActionListener(e -> {
            try {
                int searchId = Integer.parseInt(searchField.getText());
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    int rowId = (int) tableModel.getValueAt(i, 0);
                    if (rowId == searchId) {
                        table.setRowSelectionInterval(i, i);
                        table.scrollRectToVisible(table.getCellRect(i, 0, true));
                        return;
                    }
                }
                JOptionPane.showMessageDialog(this, "Item with ID " + searchId + " not found.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid numeric ID.");
            }
        });

    }

    private void loadItems() {
        tableModel.setRowCount(0); // Clear table
        List<Item> items = dao.getAllItems();
        for (Item item : items) {
            tableModel.addRow(new Object[]{item.getId(), item.getName(), item.getQuantity(), item.getPrice()});
        }
    }

    private void addItem() {
        try {
            // Auto-generate ID
            int maxId = 0;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                int existingId = (int) tableModel.getValueAt(i, 0);
                if (existingId > maxId) {
                    maxId = existingId;
                }
            }
            int newId = maxId + 1;

            // Ask for other details
            String name = JOptionPane.showInputDialog(this, "Enter Name:");
            int qty = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Quantity:"));
            double price = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter Price:"));

            Item item = new Item(newId, name, qty, price);
            if (dao.addItem(item)) {
                JOptionPane.showMessageDialog(this, "Item added successfully with ID: " + newId);
                loadItems();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add item.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input.");
        }
    }


    private void editItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to edit.");
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            Item item = dao.getItemById(id);
            if (item == null) {
                JOptionPane.showMessageDialog(this, "Item not found in database.");
                return;
            }

            String name = JOptionPane.showInputDialog(this, "Edit Name:", item.getName());
            int qty = Integer.parseInt(JOptionPane.showInputDialog(this, "Edit Quantity:", item.getQuantity()));
            double price = Double.parseDouble(JOptionPane.showInputDialog(this, "Edit Price:", item.getPrice()));

            item.setName(name);
            item.setQuantity(qty);
            item.setPrice(price);

            if (dao.updateItem(item)) {
                JOptionPane.showMessageDialog(this, "Item updated.");
                loadItems();
            } else {
                JOptionPane.showMessageDialog(this, "Update failed.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input.");
        }
    }

    private void deleteItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this item?");
        if (confirm != JOptionPane.YES_OPTION) return;

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        if (dao.deleteItem(id)) {
            JOptionPane.showMessageDialog(this, "Item deleted.");
            loadItems();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete item.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
