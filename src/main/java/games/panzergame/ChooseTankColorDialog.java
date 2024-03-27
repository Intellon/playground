package games.panzergame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Stark Louis
 * @version 1.0
 * @apiNote Panzer Game
 */
public class ChooseTankColorDialog extends JDialog{

    private final GamePanel panzerGame;
    private final Tank playersTank;

    private final JPanel dialogPanel;

    private final JButton setTurretColorButton = new JButton("Set Turret Color...");
    private final JButton setCannonColorButton = new JButton("Set Cannon Color...");
    private final JButton cancelButton = new JButton("Cancel");
    private final JButton applyButton = new JButton("Apply");

    private Color oldTurretColor;
    private Color oldCannonColor;
    private Color tempTurretColor;
    private Color tempCannonColor;

    public ChooseTankColorDialog(Frame frame, GamePanel game) {
        super(frame);
        setTitle("Choose Your Tank's Colors");
        setModal(true);

        this.panzerGame = game;
        playersTank = panzerGame.getPlayersTank();

        dialogPanel = new JPanel();
        dialogPanel.add(createPreviewPanel());
        dialogPanel.add(createButtonsPanel());
        add(dialogPanel);

        registerButtonListeners();

        setPreferredSize(new Dimension(350,330));
    }

    private JPanel createPreviewPanel() {
        JLabel titleLabel = new JLabel("Tank Preview");
        titleLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JPanel previewPanel = new JPanel();
        previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.Y_AXIS));
        previewPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        previewPanel.add(titleLabel);
        previewPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        previewPanel.add(new TankPreviewPanel(playersTank));

        return previewPanel;
    }

    private JPanel createButtonsPanel() {
        // Hier erzeugen wir das Panel, welches die Buttons enthÃ¤lt
        JPanel colorButtonsPanel = new JPanel();
        colorButtonsPanel.add(setTurretColorButton);
        colorButtonsPanel.add(setCannonColorButton);

        JPanel mainButtonsPanel = new JPanel();
        mainButtonsPanel.add(cancelButton);
        mainButtonsPanel.add(applyButton);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.add(colorButtonsPanel);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        buttonsPanel.add(new JSeparator());
        buttonsPanel.add(mainButtonsPanel);

        return buttonsPanel;
    }

    private void registerButtonListeners() {
        // Hier registrieren wir ActionListeners fÃ¼r die Buttons
        oldTurretColor = playersTank.getTurretColor();
        tempTurretColor = oldTurretColor;

        oldCannonColor = playersTank.getCannonColor();
        tempCannonColor = oldCannonColor;

        setTurretColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newTurretColor = JColorChooser.showDialog(null, "Choose Turret Color", tempTurretColor);
                if (newTurretColor != null) {
                    tempTurretColor = newTurretColor;
                    playersTank.setTurretColor(newTurretColor);
                    dialogPanel.repaint();
                }
            }
        });

        setCannonColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newCannonColor = JColorChooser.showDialog(null, "Choose Cannon Color", tempCannonColor);
                if (newCannonColor != null) {
                    tempCannonColor = newCannonColor;
                    playersTank.setCannonColor(newCannonColor);
                    dialogPanel.repaint();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playersTank.setTurretColor(oldTurretColor);
                playersTank.setCannonColor(oldCannonColor);

                tempTurretColor = oldTurretColor;
                tempCannonColor = oldCannonColor;

                panzerGame.continueGame();
                setVisible(false);
            }
        });

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oldTurretColor = playersTank.getTurretColor();
                oldCannonColor = playersTank.getCannonColor();

                panzerGame.repaint();
                panzerGame.continueGame();
                setVisible(false);
            }
        });
    }

}