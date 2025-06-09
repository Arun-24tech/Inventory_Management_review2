package utils;

public class InputValidator {
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static boolean isValidQuantity(int qty) {
        return qty >= 0;
    }

    public static boolean isValidPrice(double price) {
        return price >= 0;
    }
}
