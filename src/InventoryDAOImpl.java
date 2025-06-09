import java.sql.*;
import java.util.*;

public class InventoryDAOImpl implements InventoryDAO {
    private Connection conn = DBConnection.getConnection();

    @Override
    public boolean addItem(Item item) {
        try {
            String query = "INSERT INTO items (id, name, quantity, price) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, item.getId());
            stmt.setString(2, item.getName());
            stmt.setInt(3, item.getQuantity());
            stmt.setDouble(4, item.getPrice());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Add Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateItem(Item item) {
        try {
            String query = "UPDATE items SET name=?, quantity=?, price=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, item.getName());
            stmt.setInt(2, item.getQuantity());
            stmt.setDouble(3, item.getPrice());
            stmt.setInt(4, item.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteItem(int id) {
        try {
            String query = "DELETE FROM items WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Delete Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Item getItemById(int id) {
        try {
            String query = "SELECT * FROM items WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Item(rs.getInt("id"), rs.getString("name"),
                        rs.getInt("quantity"), rs.getDouble("price"));
            }
        } catch (SQLException e) {
            System.out.println("Fetch Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        try {
            String query = "SELECT * FROM items";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                items.add(new Item(rs.getInt("id"), rs.getString("name"),
                        rs.getInt("quantity"), rs.getDouble("price")));
            }
        } catch (SQLException e) {
            System.out.println("Fetch All Error: " + e.getMessage());
        }
        return items;
    }
}
