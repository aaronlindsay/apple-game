import java.awt.Color;
import java.util.Random;

import tester.*;
import javalib.funworld.*;
import javalib.worldimages.*;

// to represent an apple
class Apple {
    int x;
    int y;
    WorldImage img;

    Apple(int x, int y, WorldImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
    }

    // Draw this apple
    WorldImage appleImg() {
        return this.img;
    }

    // Move this apple down
    Apple moveDown() {
        return new Apple(this.x, this.y + AppleGame.HEIGHT / 40, this.img);
    }

    // Has this apple reached the ground?
    boolean onTheGround() {
        return this.y >= AppleGame.HEIGHT;
    }

    // Has this apple been caught?
    boolean caughtApple(int basketX) {
        return this.x >= basketX - AppleGame.WIDTH / 10
                && this.x <= basketX + AppleGame.WIDTH / 10
                && this.onTheGround();
    }
}

// to represent a list of apples
interface ILoApple {

    // has an apple been caught?Bas
    boolean caughtApple(int basketX);

    // place the images of the apples in this list
    WorldScene placeApplesXY(AppleGame w);

    // placeApplesXY helper
    WorldScene placeApplesXYHelp(AppleGame w, Apple a);

    // make these apples fall
    ILoApple fall();

    // filter all apples on the ground
    ILoApple offTheGround();

    // grow a new apple
    ILoApple growApple();

    // helper method to generate a random number in the range -n to n
    int randomInRange(int n);
}

// to represent an empty list of apples
class MtLoApple implements ILoApple {

    // has an apple been caught?
    public boolean caughtApple(int basketX) {
        return false;
    }

    // place the images of the apples in this list
    public WorldScene placeApplesXY(AppleGame w) {
        return w.getEmptyScene().placeImageXY(
                w.backGround(AppleGame.APPLE_TREE), AppleGame.WIDTH / 2,
                AppleGame.HEIGHT / 2);
    }

    // placeApplesXY helper
    public WorldScene placeApplesXYHelp(AppleGame w, Apple a) {
        return w.getEmptyScene()
                .placeImageXY(
                w.backGround(AppleGame.APPLE_TREE), AppleGame.WIDTH / 2,
                AppleGame.HEIGHT / 2)
                .placeImageXY(a.appleImg(), a.x, a.y);
    }

    // make these apples fall
    public ILoApple fall() {
        return this;
    }

    // filter all apples on the ground
    public ILoApple offTheGround() {
        return this;
    }

    // helper method to generate a random number in the range -n to n
    public int randomInRange(int n) {
        return -n + (new Random().nextInt(2 * n + 1));
    }

    public ILoApple growApple() {
        if (Math.random() < 0.1) {
            if (Math.random() <= 0.333) {
                return new ConsLoApple(new Apple(AppleGame.WIDTH / 2
                        + randomInRange(AppleGame.WIDTH / 3), AppleGame.HEIGHT
                        / 6 + randomInRange(AppleGame.HEIGHT / 6),
                        new FromFileImage("red-apple.png")), this);
            } else if (Math.random() >= 0.333 && Math.random() <= 0.666) {
                return new ConsLoApple(new Apple(AppleGame.WIDTH / 2
                        + randomInRange(AppleGame.WIDTH / 3), AppleGame.HEIGHT
                        / 6 + randomInRange(AppleGame.HEIGHT / 6),
                        new FromFileImage("small-red-apple.png")), this);
            } else {
                return new ConsLoApple(new Apple(AppleGame.WIDTH / 2
                        + randomInRange(AppleGame.WIDTH / 3), AppleGame.HEIGHT
                        / 6 + randomInRange(AppleGame.HEIGHT / 6),
                        new FromFileImage("yellow-apple.png")), this);

            }
        }
        else {
            return this;
        }
    }
}

// to represent a nonempty list of apples
class ConsLoApple implements ILoApple {
    Apple first;
    ILoApple rest;

    ConsLoApple(Apple first, ILoApple rest) {
        this.first = first;
        this.rest = rest;
    }

    // has an apple been caught?
    public boolean caughtApple(int basketX) {
        return this.first.caughtApple(basketX)
                || this.rest.caughtApple(basketX);
    }

    // place the images of the apples in this list
    public WorldScene placeApplesXY(AppleGame w) {
        return this.rest.placeApplesXYHelp(w, this.first);
    }

    // placeApplesXY helper
    public WorldScene placeApplesXYHelp(AppleGame w, Apple a) {
        return this.rest.placeApplesXYHelp(w, this.first)
                        .placeImageXY(a.appleImg(), a.x, a.y);
    }

    // make these apples fall
    public ILoApple fall() {
        return new ConsLoApple(this.first.moveDown(), this.rest.fall());
    }

    // helper method to generate a random number in the range -n to n
    public int randomInRange(int n) {
        return -n + (new Random().nextInt(2 * n + 1));
    }

    // filter all apples on the ground
    public ILoApple offTheGround() {
        if (this.first.onTheGround()) {
            return this.rest.offTheGround();
        } else {
            return new ConsLoApple(this.first, this.rest.offTheGround());
        }
    }
    
    public ILoApple growApple() {
        if (Math.random() < 0.05) {
            if (Math.random() <= 0.333) {
                return new ConsLoApple(new Apple(AppleGame.WIDTH / 2
                        + randomInRange(AppleGame.WIDTH / 3), AppleGame.HEIGHT
                        / 6 + randomInRange(AppleGame.HEIGHT / 6),
                        new FromFileImage("red-apple.png")), this);
            } else if (Math.random() >= 0.333 && Math.random() <= 0.666) {
                return new ConsLoApple(new Apple(AppleGame.WIDTH / 2
                        + randomInRange(AppleGame.WIDTH / 3), AppleGame.HEIGHT
                        / 6 + randomInRange(AppleGame.HEIGHT / 6),
                        new FromFileImage("small-red-apple.png")), this);
            } else {
                return new ConsLoApple(new Apple(AppleGame.WIDTH / 2
                        + randomInRange(AppleGame.WIDTH / 3), AppleGame.HEIGHT
                        / 6 + randomInRange(AppleGame.HEIGHT / 6),
                        new FromFileImage("yellow-apple.png")), this);

            }
        }
        else {
            return this;
        }
    }
}

// to represent a basket
class Basket {
    int x;
    WorldImage img;

    Basket(int x, WorldImage img) {
        this.x = x;
        this.img = img;
    }

    // Draw this basket
    WorldImage basketImg() {
        return this.img;
    }

    // Shift this basket left and right
    Basket moveOnKey(String ke) {
        if (ke.equals("left")) {
            return new Basket(this.x - AppleGame.WIDTH / 40, this.img);
        } else if (ke.equals("right")) {
            return new Basket(this.x + AppleGame.WIDTH / 40, this.img);
        } else
            return this;
    }
}

// Represents the Apple Orchard Game
class AppleGame extends World {

    public static WorldImage APPLE_TREE = new FromFileImage("apple-tree.png");
    public static int WIDTH = 400;
    public static int HEIGHT = 400;

    ILoApple apples;
    Basket basket;
    int applesCaught;

    public AppleGame(ILoApple apples, Basket basket, int applesCaught) {
        this.apples = apples;
        this.basket = basket;
        this.applesCaught = applesCaught;
    }

    // Background image for this world
    public WorldImage backGround(WorldImage img) {
        return new OverlayImage(img, new RectangleImage(AppleGame.WIDTH,
                AppleGame.HEIGHT, OutlineMode.SOLID, Color.WHITE));
    }

    // Draw the world with the apple and basket on top
    public WorldScene makeScene() {
        return this.apples.placeApplesXY(this).placeImageXY(
                this.basket.basketImg(), this.basket.x,
                AppleGame.HEIGHT - AppleGame.HEIGHT / 20)
                .placeImageXY(new TextImage(Integer.toString(this.applesCaught),
                                            Color.red), 20, 20);
                        
    }

    // Produce the last image of this world with text
    public WorldScene lastScene(String s) {
        return this.makeScene().placeImageXY(new TextImage(s, Color.red),
                AppleGame.WIDTH / 2, AppleGame.HEIGHT / 2);
    }

    // Has an apple been caught?
    public boolean caughtApple() {
        return this.apples.caughtApple(this.basket.x);
    }

    // Create a new world on tick, adjusting apples & applesCaught fields
    public World onTick() {
        if (this.caughtApple()) {
            return new AppleGame(this.apples.fall().growApple().offTheGround(),
                    this.basket, this.applesCaught + 1);
        } else {
            return new AppleGame(this.apples.fall().growApple(),
                    this.basket, this.applesCaught);
        }
    }

    // Create a new world on key event, adjusting basket field
    public World onKeyEvent(String ke) {
        return new AppleGame(this.apples, this.basket.moveOnKey(ke),
                this.applesCaught);
    }

    public WorldEnd worldEnds() {
        if (this.applesCaught == 50) {
            return new WorldEnd(true, this.lastScene("u won"));
        } else
            return new WorldEnd(false, this.makeScene());
    }
}

class ExamplesAppleGame {

    // examples of data for the Apple class:
    Apple a = new Apple(AppleGame.WIDTH / 2, AppleGame.HEIGHT / 4,
            new FromFileImage("red-apple.png"));

    Apple a1 = new Apple(200, 50, new FromFileImage("red-apple.png"));
    Apple a2 = new Apple(200, 60, new FromFileImage("red-apple.png"));
    Apple a3 = new Apple(200, 395, new FromFileImage("red-apple.png"));
    Apple a4 = new Apple(200, 405, new FromFileImage("red-apple.png"));
    Apple a5 = new Apple(100, 405, new FromFileImage("red-apple.png"));

    // examples of data for the Basket class:
    Basket b = new Basket(AppleGame.WIDTH / 2, new RectangleImage(
            AppleGame.WIDTH / 5, AppleGame.HEIGHT / 10, OutlineMode.SOLID,
            Color.BLACK));

    Basket b1 = new Basket(200, new RectangleImage(AppleGame.WIDTH / 5,
            AppleGame.HEIGHT / 10, OutlineMode.SOLID, Color.BLACK));
    Basket b2 = new Basket(190, new RectangleImage(AppleGame.WIDTH / 5,
            AppleGame.HEIGHT / 10, OutlineMode.SOLID, Color.BLACK));
    Basket b3 = new Basket(210, new RectangleImage(AppleGame.WIDTH / 5,
            AppleGame.HEIGHT / 10, OutlineMode.SOLID, Color.BLACK));

    // examples of data for lists of apples
    ILoApple mt = new MtLoApple();
    ILoApple loa = new ConsLoApple(this.a, this.mt);

    ILoApple loa1 = new ConsLoApple(this.a1, new ConsLoApple(this.a3, this.mt));
    ILoApple loa2 = new ConsLoApple(this.a2, new ConsLoApple(this.a4, this.mt));

    // examples of data for the AppleGame class
    AppleGame w = new AppleGame(this.loa, this.b, 0);

    AppleGame w1 = new AppleGame(this.loa1, this.b1, 0);
    AppleGame w2 = new AppleGame(this.loa2, this.b1, 0);

    // test moveDown()
    boolean testMoveDown(Tester t) {
        return t.checkExpect(this.a1.moveDown(), this.a2);
    }

    // test onTheGround()
    boolean testOnTheGround(Tester t) {
        return t.checkExpect(this.a1.onTheGround(), false)
                && t.checkExpect(a4.onTheGround(), true);
    }

    /*
    // test fall()
    boolean testFall(Tester t) {
        return t.checkExpect(this.a1.fall(), this.a2)
                && t.checkExpect(this.a3.fall(), this.a4)
                && t.checkRange(this.a4.fall().x, AppleGame.WIDTH / 6,
                        5 * AppleGame.HEIGHT / 6)
                && t.checkRange(this.a4.fall().y, 0, AppleGame.HEIGHT / 3)
                && t.checkExpect(this.loa1.fall(), this.loa2);
    }
    
    */

    // test moveOnKey()
    boolean testMoveOnKey(Tester t) {
        return t.checkExpect(this.b1.moveOnKey(""), this.b1)
                && t.checkExpect(this.b1.moveOnKey("left"), this.b2)
                && t.checkExpect(this.b1.moveOnKey("right"), this.b3);
    }

    // test caughtApple()
    boolean testCaughtApple(Tester t) {
        return t.checkExpect(this.w1.caughtApple(), false)
                && t.checkExpect(this.w2.caughtApple(), true);
    }

    // run the game
    boolean testBigBang(Tester t) {
        return t.checkExpect(
                this.w.bigBang(AppleGame.WIDTH, AppleGame.HEIGHT, 0.05), true);
    }
}
