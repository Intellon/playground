package games.brickbreaker;

import javax.swing.*;

/**
 * The player controls a tiny ball placed on a small platform at the bottom of the screen,
 * which can be moved around from left to right using the arrow keys.
 * The goal is to break the bricks without missing the ball with your platform.
 *
 *  * @author Stark Louis
 *  * @version 1.0
 *  * @apiNote Brick Breaker Game
 *  */
public class BrickBreakerGame {
    public static void main(String[] args) {
        JFrame obj=new JFrame();
        Gameplay gamePlay = new Gameplay();

        obj.setBounds(10, 10, 700, 600);
        obj.setTitle("Breakout Ball");
        obj.setResizable(false);
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(gamePlay);
        obj.setVisible(true);

    }
}
