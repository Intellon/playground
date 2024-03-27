package tutorials.greeter;

/**
 * Erste Beispiel Klasse als main Methode und einer CMD Ausgabe
 */
public class StartGreeter {
    public static void main( String[] args ) {
        Greeter.instance().greet( "Livio" );
    }

}
