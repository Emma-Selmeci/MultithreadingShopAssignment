package eselmeci_amazon_GO;

//AmazonShop is fully static

import java.util.HashMap;

public class AmazonShop {
    static Shelf[] shelves = new Shelf[Product.NUMOFPRODUCTS];
    static {
        for(int i = 0; i < shelves.length; ++i) shelves[i] = new Shelf();
    }

    static ShopInterface checkIn(Customer customer) {
        //Return a ShopInterface object and register the customer
        return new ShopInterface();
    }

    static class ShopInterface {

        private HashMap<Product, Integer> basket = new HashMap<>();

        private ShopInterface() {

        }

        Receipt checkOut() throws GreedException {
            return new Receipt();
        }

    }

    private AmazonShop() {} //Please don't make an instance of AmazonShop
}
