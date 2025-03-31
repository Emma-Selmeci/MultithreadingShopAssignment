package eselmeci_amazon_GO;

public enum Product {
    RAM(15), //"Aki szeret RAM-ot venni, az használhatja ezt a megoldást" (Kami)
    GHOST_MERCH(20);

    public final int price;
    public static final int NUMOFPRODUCTS = Product.values().length;

    Product(int price) {
        this.price = price;
    }
}
