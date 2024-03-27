package tutorials;

/**
 * Das Programm soll einen Algorithmus ausführen, der die Quadrate der Zahlen von 1 bis 4 ausgibt.
 * Die erste Methode, quadrat(int), bekommt als Übergabeparameter eine ganze Zahl und berechnet daraus die Quadratzahl, die sie anschließend zurückgibt.
 * Eine weitere Methode ausgabe(int) übernimmt die Ausgabe der Quadratzahlen bis zu einer vorgegebenen Grenze.
 * Die ausgabe-Methode bedient sich dabei der Methode quadrat(int).
 * Zum Schluss muss es noch ein besonderes Unterprogramm main(String[]) geben, das für den Java-Interpreter den Einstiegspunkt bietet.
 * Die Methode main(String[]) ruft dann die Methode ausgabe(int) auf.
 */
public class StartSquares {

    static int quadrat( int n ) {
        return n * n;
    }

    static void ausgabe( int n ) {
        for ( int i = 1; i <= n; i = i + 1 ) {
            String s = "Quadrat(" + i + ") = " + quadrat(i);
            System.out.println( s );
        }
    }

    public static void main( String[] args ) {
        ausgabe( 5 );
    }
}
