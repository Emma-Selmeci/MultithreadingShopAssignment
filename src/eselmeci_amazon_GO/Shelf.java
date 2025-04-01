package eselmeci_amazon_GO;

import java.security.InvalidParameterException;
import java.util.Random;

/**
 * A shelf with static final capacity. Initialized to random amount (numOfProducts).
 * Amount can be refilled or reduced. Amount is in [0, capacity].
 * @author emma-selmeci
 */
class Shelf {
    private static final int capacity = 20; //Shelves can only hold 20 items each
    private int numOfProducts;

    /**
     * Sets the initial value of numOfProducts to be random
     */
    Shelf() {
        Random rand = new Random();
        numOfProducts = rand.nextInt(20);
    }

    /**
     * @return numOfProducts
     */
    int getNumOfProducts() {
        return numOfProducts;
    }

    /**
     * Remove up to amount products from numOfProducts.
     * Ifamount is greater than the numOfProducts, remove all.
     * @param amount numOfProducts will be reduced by amount
     * @return the reduction (might be lesser than amount)
     * @throws InvalidParameterException if amount is less than 0
     */
    int take(int amount) {
        if(amount < 0) throw new InvalidParameterException("Value can't be negative");

        Random rand = new Random();
        try {
            Thread.sleep(rand.nextInt(2000)+2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(amount > numOfProducts) amount = numOfProducts;
        numOfProducts -= amount;
        return amount;
    }

    /**
     * Adds amount to numOfProducts, up to capacity
     * @param amount
     */
    void refill(int amount) {
        numOfProducts += amount;
        if(numOfProducts > capacity) numOfProducts = capacity;
    }

    private static int getCapacity() {
        return capacity;
    }

    public static void main(String[] args) {
        System.out.println("Testing Shelf");

        System.out.println("Testing Shelf()");
        Shelf[] sl = new Shelf[100];
        boolean successful = true;
        for(Shelf s : sl) if(s.getNumOfProducts() < 0 || s.getNumOfProducts() > getCapacity()) {
            successful = false;
            break;
        }
        if(successful) System.out.println("Test successful"); else System.out.println("Test failed");

        System.out.println("Testing take()");
        Shelf s = new Shelf();
        Exception e = null;
        try {
            s.take(-1);
        } catch(InvalidParameterException exception) {
            e = exception;
        }
        if(e != null && e instanceof  InvalidParameterException) System.out.println("Test successful"); else System.out.println("Test failed");

        s.refill(capacity);
        int result = s.take(1);
        if(result == 1 && s.getNumOfProducts() == capacity-1) System.out.println("Test successful"); else System.out.println("Test failed");

        result = s.take(capacity);
        if(result == capacity-1 && s.getNumOfProducts() == 0) System.out.println("Test successful"); else System.out.println("Test failed");

    }
}