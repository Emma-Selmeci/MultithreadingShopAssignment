package eselmeci_amazon_GO;

public class Customer implements Runnable {

    private final String name;
    private int wallet;
    private AmazonShop.ShopInterface shopInterface;

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

    }

    @Override
    public void run() {
        while(true) {

        }
    }
}
