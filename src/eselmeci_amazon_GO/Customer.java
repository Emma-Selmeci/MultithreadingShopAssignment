package eselmeci_amazon_GO;

import java.util.Vector;

/**
 * A class to use as a handler when interacting with AmazonShop
 */

public class Customer implements Runnable {

    private final String name;
    private int wallet;
    private AmazonShop.ShopInterface shopInterface;
    private final Vector<Receipt> receipts = new Vector<>();

    Customer(String name, int initialMoney) {
        this.name = name;
        wallet = initialMoney;
    }

    public void receiveMoney(int amount) {
        wallet+=amount;
    }
    void takeMoney(int amount) {wallet-=amount;}
    public int getMoney() {
        return wallet;
    }
    public void enterShop() {
        System.out.println(name + " enters the shop");
        shopInterface = AmazonShop.checkIn(this);
    }
    public void leaveShop() throws GreedException {
        System.out.println(name + " goes to the register");
        receipts.add(shopInterface.checkOut());
        System.out.println(name + " leaves the shop");
    }
    public int takeProduct(Product product, int amount) {
        int taken = shopInterface.takeProduct(product,amount);
        System.out.println(name + " attempts to take " + amount + " " + product + ", and manages to get hold of " + taken);
        return taken;
    }
    public void returnProduct(Product product, int amount) {
        System.out.println(name + " returns " + amount + " " + product + "s");
        shopInterface.returnProduct(product, amount);
    }
    public int getAmountInBasket(Product product) {
        return shopInterface.getAmountInBasket(product);
    }
    public void speak(String s) {
        System.out.println(name + " says: \"" + s + "\"");
    }

    @Override
    public void run() {
    }
}
