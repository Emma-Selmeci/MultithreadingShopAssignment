package eselmeci_amazon_GO;

public class Main {
    public static void main(String[] args) {

        Friend bende1 = Friend.Bende;

        bende1.vizsgajegy = 2;

        Friend bende2 = Friend.Bende;

        System.out.println(bende2.vizsgajegy);

    }
}

enum Friend implements Runnable {
    Dorina(5), Bende(4), Bori(4), Bogi(5);

    Friend(int i) {
        vizsgajegy = i;
    }

    public int vizsgajegy;

    @Override
    public void run() {

    }
}