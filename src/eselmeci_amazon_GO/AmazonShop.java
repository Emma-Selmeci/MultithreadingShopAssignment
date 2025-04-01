package eselmeci_amazon_GO;

//AmazonShop is fully static

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.HashSet;

public class AmazonShop {
    private static HashSet<Customer> customers = new HashSet<>();
    private static Shelf[] shelves = new Shelf[Product.NUMOFPRODUCTS];
    static {
        for(int i = 0; i < shelves.length; ++i) shelves[i] = new Shelf();
    }

    static ShopInterface checkIn(Customer customer) {
        synchronized (customers) {
            if(customers.contains(customer)) throw new InvalidParameterException("Customer already in shop");
            customers.add(customer);
        }
        return new ShopInterface(customer);
    }

    static class ShopInterface {
        private boolean valid = true;
        private HashMap<Product, Integer> basket = new HashMap<>();
        private Customer customer;

        ShopInterface(Customer customer) {
            this.customer = customer;
        }

        int getCurrentCost() {
            if(!valid) throw new RuntimeException("Customer not in shop");
            int sum = 0;
            for(var v : basket.entrySet()) {
                sum += v.getKey().price * v.getValue();
            }
            return sum;
        }

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

        Receipt checkOut() throws GreedException {
            if(!valid) throw new RuntimeException("Customer not in shop");
            int cost = getCurrentCost();
            if(customer.getMoney() < cost) throw new GreedException("Insufficient money to buy products in basket");
            synchronized (customers) {
                customers.remove(customer);
            }
            return new Receipt(basket);
        }

        public static void main(String[] args) {
            System.out.println("Testing Shopinterface");


        }

    }

    private AmazonShop() {} //Please don't make an instance of AmazonShop
}
