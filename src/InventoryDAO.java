import java.util.List;

public interface InventoryDAO {
    boolean addItem(Item item);
    boolean updateItem(Item item);
    boolean deleteItem(int id);
    Item getItemById(int id);
    List<Item> getAllItems();
}
