import java.util.ArrayList;

class A {
    String name;

    A (String n) {
        this.name = n;
    }

    @Override
    public String toString() {
        return (String)this.name + "a";
    }
}

class B extends A {
    B (String n) {
        super(n);
    }

    @Override
    public String toString() {
        return (String)this.name + "b";
    }
}
interface C {

}

class D implements C {
    String name;
    D (String n) {
        this.name = n;
    }
    @Override
    public String toString() {
        return (String)this.name + "d";
    }
}

class E extends A implements C {
    E (String n) {
        super(n);
    }

    @Override
    public String toString() {
        return (String)this.name + "e";
    }
}

public class TypeConversion {
    public static void main(String[] args) {

        // Creating test subjects
        A aa = new A("a");
        A ab = new B("a");
        // A ac = new C("a");
        // A ad = new D("a");
        A ae = new E("a");

        // B ba = new A("b");
        B bb = new B("b");
        // B bc = new C("b");
        // B bd = new D("b");
        // B be = new E("b");

        // C ca = new A("c");
        // C cb = new B("c");
        // C cc = new C("c");
        C cd = new D("c");
        C ce = new E("c");

        // D da = new A("d");
        // D db = new B("d");
        // D dc = new C("d");
        D dd = new D("d");
        // D de = new E("d");

        // E ea = new A("e");
        // E eb = new B("e");
        // E ec = new C("e");
        // E ed = new D("e");
        E ee = new E("e");

        // Creating list
        ArrayList<Object> myList = new ArrayList<>();

        // Adding to list
        myList.add(aa);
        myList.add(ab);
        myList.add(ae);
        myList.add(bb);
        myList.add(cd);
        myList.add(ce);
        myList.add(dd);
        myList.add(ee);

        // Iterating
        for (Object item : myList) {

            // Printing
            System.out.println(item);

            // Testing A
            try {
                A test = (A)item;
            } catch (Exception e) {
                System.out.println("Error converting " + item + " to A.");
            }

            // Testing B
            try {
                B test = (B)item;
            } catch (Exception e) {
                System.out.println("Error converting " + item + " to B.");
            }

            // Testing C
            try {
                C test = (C)item;
            } catch (Exception e) {
                System.out.println("Error converting " + item + " to C.");
            }

            // Testing D
            try {
                D test = (D)item;
            } catch (Exception e) {
                System.out.println("Error converting " + item + " to D.");
            }

            // Testing E
            try {
                E test = (E)item;
            } catch (Exception e) {
                System.out.println("Error converting " + item + " to E.");
            }
            System.out.println();
        }
    }
}