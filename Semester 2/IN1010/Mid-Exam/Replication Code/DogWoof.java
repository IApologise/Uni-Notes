class Dog {
    private String voff;
    Dog(String bjeff) {
        voff = bjeff;
    }
    public void woof() {
        System.out.println(voff);
    }
}

public class DogWoof {
    public static void main (String[] args) {
        Dog h1, h2, h3;
        h1 = new Dog("Voff-voff");
        h2 = new Dog("Bjeff-bjeff");
        h2 = h1;
        h1 = null;
        h3 = h2;
        h3.woof();
    }
}
