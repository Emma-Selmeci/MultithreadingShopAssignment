package eselmeci_amazon_GO;

//AmazonShop is fully static

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * AmazonShop represents a shop with different products on each shelf.
 * AmazonShop can't be used by itself but an interface object can be accessed through checkIn, which generates a unique interface to track each shopping session
 * The transaction can be finalized, invalidating the interface, with checkOut()
 * A new interface can be
 * AmazonShop is synchronized
 *
 * @author emma-selmeci
 */

public class AmazonShop {
    private static final HashSet<Customer> customers = new HashSet<>();
    private static final Shelf[] shelves = new Shelf[Product.NUMOFPRODUCTS];
    static {
        for(int i = 0; i < shelves.length; ++i) shelves[i] = new Shelf();
    }

    /**
     * Returns a ShopInterface object if customer is not already registered as having one
     * @param customer The customer to register in this shop
     * @return A newly initialized ShopInterface object that can be used to access the shop
     */
    static ShopInterface checkIn(Customer customer) {
        synchronized (customers) {
            if(customers.contains(customer)) throw new InvalidParameterException("Customer already in shop");
            customers.add(customer);
        }
        return new ShopInterface(customer);
    }

    static class ShopInterface {
        private boolean valid = true;
        private final HashMap<Product, Integer> basket = new HashMap<>();
        private final Customer customer;

        ShopInterface(Customer customer) {
            this.customer = customer;
        }

        /**
         * How many of product is there in the customer's basket?
         * @param product The Product type
         * @return The amount mapped to product
         */
        int getAmountInBasket(Product product) {
            Integer i = basket.get(product);
            if(i == null) return 0;
            return i;
        }

        /**
         * @return the current sum of all products in the basket
         */
        int getCurrentCost() {
            if(!valid) throw new RuntimeException("Customer not in shop");
            int sum = 0;
            for(var v : basket.entrySet()) {
                sum += v.getKey().price * v.getValue();
            }
            return sum;
        }

        /**
         * Attempt to take amount of product from the corresponding shelf. Not guaranteed to take amount, as shelf might be partially empty
         * @param product The type of product to be taken
         * @param amount The amount to be taken from the shelf
         * @return The amount actually taken from the shelf
         */
        int takeProduct(Product product, int amount) {
            if(!valid) throw new RuntimeException("Customer not in shop");
            int result;
            synchronized (shelves[product.ordinal()]) {
                result = shelves[product.ordinal()].take(amount);
            }
            Integer i = basket.get(product);
            if(i == null)
                basket.put(product, result);
            else
                basket.put(product,i+result);
            return result;
        }

        /**
         * Returns amount of type product into the corresponding shelf.
         * Safe, returns only as many as possible and products beyond shelf capacity are "lost"
         * @param product The type of the product to be returned
         * @param amount The amount of the product to be returned
         */
        void returnProduct(Product product, int amount) {
            if(!valid) throw new RuntimeException("Customer not in shop");
            Integer i = basket.get(product);
            if(i == null) return;
            if(amount > i) amount = i;
            synchronized (shelves[product.ordinal()]) {
                shelves[product.ordinal()].refill(amount);
            }
            basket.put(product,i-amount);
        }

        /**
         * Finalises the transaction, taking the sum from the customer's wallet and returning a receipt corresponding to the transaction
         * @return A receipt documenting the finalized transaction
         * @throws GreedException If the customer doesn't have enough money to foot the bill
         */
        Receipt checkOut() throws GreedException {
            if(!valid) throw new RuntimeException("Customer not in shop");
            int cost = getCurrentCost();
            if(customer.getMoney() < cost) throw new GreedException("Insufficient money to buy products in basket");
            customer.takeMoney(cost);
            valid = false;
            synchronized (customers) {
                customers.remove(customer);
            }
            return new Receipt(basket, getCurrentCost());
        }

        public static void main(String[] args) {
            System.out.println("Testing ShopInterface");

            System.out.println("Testing empty basket");
            Customer richCustomer = new Customer("Rich Roby",10000);
            ShopInterface shopInterface = new ShopInterface(richCustomer);
            if(shopInterface.getCurrentCost() == 0) System.out.println("Test passed"); else System.out.println("Test failed");

            System.out.println("Testing basket with some items");
            shopInterface.basket.put(Product.GHOST_MERCH,50);
            shopInterface.basket.put(Product.RAM,10);
            if(shopInterface.getCurrentCost() == Product.GHOST_MERCH.price*50+Product.RAM.price*10) System.out.println("Test passed"); else System.out.println("Test failed");

            System.out.println("Returning some items");
            Shelf s = shelves[Product.RAM.ordinal()];
            int shelfValue = s.getNumOfProducts();
            if(shelfValue + 5 > Shelf.getCapacity()) {
                s.take(5);
                shelfValue-=5;
            }
            shopInterface.returnProduct(Product.RAM,5);
            if(s.getNumOfProducts() == shelfValue+5 && shopInterface.getCurrentCost() == Product.GHOST_MERCH.price*50+Product.RAM.price*5) System.out.println("Test passed"); else System.out.println("Test failed");

            System.out.println("Taking some items");
            shopInterface.takeProduct(Product.RAM,5);
            if(s.getNumOfProducts() == shelfValue && shopInterface.getCurrentCost() == Product.GHOST_MERCH.price*50+Product.RAM.price*10) System.out.println("Test passed"); else System.out.println("Test failed");

        }

    }

    private AmazonShop() {} //Please don't make an instance of AmazonShop
}
