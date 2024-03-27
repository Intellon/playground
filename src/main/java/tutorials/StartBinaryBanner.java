package tutorials;

import java.util.Scanner;

/**
 * Java realisiert mehrdimensionale Arrays durch Arrays von Arrays.
 * Sie können etwa für die Darstellung von mathematischen Matrizen oder Rasterbildern verwendet werden.
 * ----
 * Erfrage vom Benutzer eine Zahl über die CMD welche diese Zahl in Binärdarstellung ausgibt.
 * Damit der Text in der CMD leserlich ist, sollen die Binärzeichen um 90 Grad gedreht werden.
 * Die Ausgabe der Binärzahlen soll folgendermassen aussehen:
 *
 *      #
 *   #####
 *
 *    ###
 *   #   #
 *    ###
 *
 */
public class StartBinaryBanner {
    public static void main( String[] args ) {
        int input  = new Scanner( System.in ).nextInt();
        String bin = Integer.toBinaryString( input );
        System.out.printf( "Banner für %s (binär %s):%n", input, bin );
        for ( int i = 0; i < bin.length(); i++ )
            switch ( bin.charAt( i ) ) {
                case '0': printZero(); break;
                case '1': printOne(); break;
            }
    }

    static void printZero() {
        char[][] zero = {
                { ' ', '#', ' ' },
                { '#', ' ', '#' },
                { '#', ' ', '#' },
                { '#', ' ', '#' },
                { ' ', '#', ' ' } };
        printLetter( zero );
    }

    static void printOne() {
        char[][] one = {
                { ' ', '#' },
                { '#', '#' },
                { ' ', '#' },
                { ' ', '#' },
                { ' ', '#' } };
        printLetter( one );
    }

    static void printLetter( char[][] letter ) {
        for ( int column = 0; column < letter[0].length; column++ ) {
            for ( int line = letter.length - 1; line >= 0; line-- )
                System.out.print( letter[line][column] );
            System.out.println();
        }
        System.out.println();
    }
}
