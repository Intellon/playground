package games.panzergame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Stark Louis
 * @version 1.0
 * @apiNote Panzer Game
 */
public class GameWindow extends JFrame{

    private final GamePanel panzerGamePanel;
    private final ChooseTankColorDialog chooseTankColorDialog;

    public GameWindow() {

        this.panzerGamePanel = new GamePanel();
        chooseTankColorDialog  = new ChooseTankColorDialog(this, panzerGamePanel);

        registerWindowListener();
        createMenu();

        add(panzerGamePanel);
        pack();

        setTitle("Panzer Game");
        setLocation(10, 10);
        setResizable(false);

        setVisible(true);
    }

    private void registerWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
            @Override
            public void windowDeactivated(WindowEvent e) {
                // hier wird das Spiel pausiert
                panzerGamePanel.pauseGame();
            }
            @Override
            public void windowActivated(WindowEvent e) {
                // hier wird das Spiel wieder fortgesetzt
                panzerGamePanel.continueGame();
            }
        });
    }

    private void createMenu() {

        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        JMenu gameMenu = new JMenu("Game");
        JMenu prefMenu = new JMenu("Preferences");

        menuBar.add(fileMenu);
        menuBar.add(gameMenu);
        menuBar.add(prefMenu);

        addFileMenuItems(fileMenu);
        addGameMenuItems(gameMenu);
        addPrefMenuItems(prefMenu);
    }

    private void addFileMenuItems(JMenu fileMenu) {

        JMenuItem quitItem = new JMenuItem("Quit");
        fileMenu.add(quitItem);
        quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void addGameMenuItems(JMenu gameMenu) {
        JMenuItem pauseItem = new JMenuItem("Pause");
        gameMenu.add(pauseItem);
        pauseItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panzerGamePanel.pauseGame();
            }
        });

        JMenuItem continuetItem = new JMenuItem("Continue");
        gameMenu.add(continuetItem);
        continuetItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panzerGamePanel.continueGame();
            }
        });

        gameMenu.addSeparator();
        JMenuItem restartItem = new JMenuItem("Restart");
        gameMenu.add(restartItem);
        restartItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panzerGamePanel.restartGame();
            }
        });
    }

    private void addPrefMenuItems(JMenu prefMenu) {

        JMenuItem changeColorItem = new JMenuItem("Change Tank's Colors...");
        prefMenu.add(changeColorItem);

        changeColorItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //panzerGamePanel.getPlayersTank().setTurretColor(java.awt.Color.YELLOW);

                panzerGamePanel.pauseGame();
                chooseTankColorDialog.pack();
                chooseTankColorDialog.setLocation(300, 200);
                chooseTankColorDialog.setVisible(true);

            }
        });

        JMenu submenu = new JMenu("Choose Background");
        prefMenu.add(submenu);

        JMenuItem menuItem = new JMenuItem("Mud Area");
        submenu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panzerGamePanel.setBackgroundImage(0);
                repaint();
            }
        });

        menuItem = new JMenuItem("Snow Area");
        submenu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panzerGamePanel.setBackgroundImage(1);
                repaint();
            }
        });

        menuItem = new JMenuItem("Desert Area");
        submenu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panzerGamePanel.setBackgroundImage(2);
                repaint();
            }
        });
    }

}