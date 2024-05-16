package tutorials.calculator;

import java.util.Scanner;

/**
 * @author Intellon
 * @date 22.03.2024
 *
 * Erstellung eines einfachen Taschenrechnerprogramms in Java.
 * Es soll ein voll funktionsfähiges Java-Taschenrechnerprogramm haben, das Addition, Subtraktion, Multiplikation und Division ausführen kann.
 * Hierbei soll ein Verständnis für Konzepte wie Variablen, Benutzereingaben, bedingte Anweisungen und Schleifen geschaffen werden, die für die Java-Programmierung und viele andere Programmiersprachen grundlegend sind.
 *
 */
public class CalculatorMain {
    public static void main(String[] args)
    {
        System.out.println("Enter first and second number:");

        Calculator calc = new Calculator();
        Scanner inp = new Scanner(System.in);
        int num1, num2;
        num1 = inp.nextInt();
        num2 = inp.nextInt();

        System.out.println("Enter your selection: 1 for addition, 2 for substraction 3 for multiplication and 4 for division:");

        int choose;
        choose = inp.nextInt();

        switch (choose){
            case 1:
                System.out.println(calc.add(num1,num2));
                break;
            case 2:
                System.out.println(calc.sub(num1,num2));
                break;
            case 3:
                System.out.println(calc.mult(num1,num2));
                break;
            case 4:
                System.out.println(calc.div(num1,num2));
                break;
            default:
                System.out.println("Illegal Operation");

        }
    }
}