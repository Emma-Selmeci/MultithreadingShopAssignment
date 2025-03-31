package eselmeci_amazon_GO;

import java.security.InvalidParameterException;
import java.util.Random;

class Shelf {
    private static final int capacity = 20; //Shelves can only hold 20 items each
    private int numOfProducts;

    Shelf() {
        Random rand = new Random();
        numOfProducts = rand.nextInt(20);
    }

    int getNumOfProducts() {
        return numOfProducts;
    }

    /**
     * Remove up to amount product from shelf
     * @param amount
     * @return
     * @throws InvalidParameterException
     */
    int take(int amount) {
        if(amount < 0) throw new InvalidParameterException();

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

    void refill(int amount) {
        numOfProducts += amount;
        if(numOfProducts > capacity) numOfProducts = capacity;
    }

    private static int getCapacity() {
        return capacity;
    }
}