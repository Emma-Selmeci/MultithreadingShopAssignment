package eselmeci_amazon_GO;

import java.util.HashMap;

/**
 * Represents a receipt. Hold a Product->Integer map representing the amount bough of each product.
 * Also contains the time of payment and the sum of payment
 */

public class Receipt {
    private final long time = System.nanoTime();
    private final HashMap<Product,Integer> productList;
    private final int sum;
    
    Receipt(HashMap<Product, Integer> basket, int sum) {
        productList = (HashMap<Product, Integer>) basket.clone();
        this.sum = sum;
    }

    public long getTimeOfPayment() {
        return time;
    }
}
