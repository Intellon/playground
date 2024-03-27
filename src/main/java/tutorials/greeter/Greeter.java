package tutorials.greeter;

public class Greeter {
    private Greeter() { }

    public static Greeter instance() {
        return new Greeter();
    }

    public void greet( String name ) {
        System.out.println( "Hey "+ name );
    }
}
