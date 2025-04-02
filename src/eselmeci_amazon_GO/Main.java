package eselmeci_amazon_GO;

import java.util.Random;

public class Main {
    public static void main(String[] args) {

        AmazonShop.Clerk lazyClerk = new AmazonShop.Clerk() {
            @Override
            public void run() {
                while(true) {
                    System.out.println("\"Lazy Clerk : ZzZz\"");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    for(int i = 0; i < Product.NUMOFPRODUCTS / 3; ++i) {
                        Random rand = new Random();
                        Product p = Product.values()[rand.nextInt(Product.NUMOFPRODUCTS)];
                        fillProduct(p,2);
                        System.out.println("The Lazy Clerk places 2 " + p +"s onto the shelf, to a total of " + getShelfContent(p));
                    }
                }
            }
        };
        new Thread(lazyClerk).start();

        AmazonShop.Clerk busyClerk = new AmazonShop.Clerk() {
            @Override
            public void run() {
                while(true) {
                    System.out.println("Busy Clerk : \"It's time to refill some shelves!\"");

                    for(int i = 0; i < Product.NUMOFPRODUCTS; ++i) {
                        Random rand = new Random();
                        int amount = rand.nextInt(10) + 2;
                        Product p = Product.values()[i];
                        fillProduct(Product.values()[rand.nextInt(Product.NUMOFPRODUCTS)],2);
                        System.out.println("The Busy Clerk places " + amount + " " + p +"s onto the shelf, to a total of " + getShelfContent(p));
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {}
                    }

                    System.out.println("Busy Clerk : \"I'm getting tired of all this work\"");

                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        };

        new Thread(busyClerk).start();

        //Toni sometimes has the urge to buy peanut butter
        Customer toni = new Customer("Toni",10000) {
            @Override
            public void run() {
                while(getMoney() > 1000) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    speak("Oh, it's time for some peanut butter! :P");
                    enterShop();
                    int amount = takeProduct(Product.PEANUT_BUTTER,6);
                    if(amount > 0) {
                        speak("Maybe I should get some jam as well");
                        takeProduct(Product.JAM,8);
                    } else {
                        speak("No peanut butter. I shall quench my thirst for consumption");
                        Random rand = new Random();
                        takeProduct(Product.values()[rand.nextInt(Product.NUMOFPRODUCTS)],20);
                    }
                    while(true) {
                        try {
                            leaveShop();
                            break;
                        } catch (GreedException e) {
                            speak("I don't have enough money for this");
                            receiveMoney(10000);
                        }
                    }


                }
            }
        };
        new Thread(toni).start();

        //Ezra just likes observing things
        Customer ezra = new Customer("Ezra",3000) {
            @Override
            public void run() {
                while(true) {
                    Random rand = new Random();

                    try {
                        Thread.sleep(rand.nextInt(13)*1000+1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    enterShop();
                    for(int i = 0; i < rand.nextInt(6); ++i) {
                        Product p = Product.values()[rand.nextInt(Product.NUMOFPRODUCTS)];
                        takeProduct(p,1);
                        speak("Oh, what an interesting " + p + "!");
                        try {
                            Thread.sleep(rand.nextInt(rand.nextInt(4)*1000+1));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        returnProduct(p,1);
                    }
                    try {
                        leaveShop();
                    } catch (GreedException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        };
        new Thread(ezra).start();

        //Dorina loves the ghost merch, but she's a bit mindless
        Customer dorina = new Customer("Dorina",0) {
            @Override
            public void run() {
                Random rand = new Random();
                while(true) {
                    speak("Oh, a friend of mine told me to buy myself something nice");
                    receiveMoney(1000);

                    try {
                        Thread.sleep(rand.nextInt(10)*100+1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    enterShop();

                    //Dorina selects a random shelf close to the Ghost merch one
                    Product p = Product.values()[rand.nextInt(2)-1 + Product.GHOST_MERCH.ordinal()];
                    while(p != Product.GHOST_MERCH) {
                        takeProduct(p,rand.nextInt(10)+8);
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        speak("Wait, this is not the Ghost Merch I've wanted");
                        returnProduct(p,getAmountInBasket(p));
                    }
                    takeProduct(p,20);
                    speak("Finally!");
                    try {
                        leaveShop();
                    } catch (GreedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        new Thread(dorina).start();

        //Bogi would love to buy some shiny new stuff, but she's out of money
        Customer bogi = new Customer("Bogi",0) {
            @Override
            public void run() {
                Random rand = new Random();
                while(true) {
                    try {
                        Thread.sleep(rand.nextInt(3)*1000+1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    speak("Although I have no money, I should go to the shop!");
                    enterShop();
                    //Bogi stays in the shop until she can buy nothing
                    while(true) {
                        Product p = Product.values()[rand.nextInt(Product.NUMOFPRODUCTS)];
                        takeProduct(p,rand.nextInt(13));
                        try {
                            Thread.sleep(rand.nextInt(5)*300+1);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            leaveShop();
                            break;
                        } catch (GreedException e) {
                            returnProduct(p,getAmountInBasket(p));
                        }
                    }
                }
            }
        };
        new Thread(bogi).start();

        //Száva has lots of money
        Customer szava = new Customer("Száva", Integer.MAX_VALUE) {
            @Override
            public void run() {
                Random rand = new Random();
                while(true) {
                    try {
                        Thread.sleep(rand.nextInt(5)*1000+1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    enterShop();

                    for(int i = 0; i < rand.nextInt(20); ++i) {
                        takeProduct(Product.values()[rand.nextInt(Product.NUMOFPRODUCTS)],rand.nextInt(10)+5);
                    }

                    try {
                        leaveShop();
                    } catch (GreedException e) {
                        receiveMoney(100000);
                    }
                }
            }
        };

    }
}