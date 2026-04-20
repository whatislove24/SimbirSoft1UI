package utils;

public class PriceUtils {

    public static double parsePrice(String priceText) {
        if (priceText == null || priceText.isEmpty()) return 0.0;

        String cleaned = priceText.replaceAll("[^0-9.]", "");
        return cleaned.isEmpty() ? 0.0 : Double.parseDouble(cleaned);
    }
}