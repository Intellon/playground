package tutorials.base64converter.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Kleine Standalone-Applikation, um Strings zu encoden / decoden
 *
 * @author Intellon
 * @date 22.03.2024
 */
public class Base64Converter extends JFrame {
	private static final long serialVersionUID = -2535666366332529383L;
	private JButton buttonEncode = null;
    private JLabel originalstringLabel = null;
    private JTextField originalstringTextField = null;
    private JTextField encodedstringTextField = null;
    private JLabel encodedstringLabel = null;
    private JButton buttonDecode = null;
    private JPanel contentpanel = null;


    /**
     * This is the default constructor
     */
    public Base64Converter() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        this.setSize(500, 200);
        this.setContentPane(getJContentPanel());
        this.setTitle("Base64 Encoder/Decoder");
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("windowClosing()"); // TODO Auto-generated Event stub windowClosing()
            }
        });
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    /**
     * This method initializes jContentPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPanel() {
        if (contentpanel == null) {
            encodedstringLabel = new JLabel();
            encodedstringLabel.setBounds(new Rectangle(15, 59, 292, 16));
            encodedstringLabel.setText("Encoded String");
            originalstringLabel = new JLabel();
            originalstringLabel.setBounds(new Rectangle(15, 9, 292, 16));
            originalstringLabel.setText("Decoded String");
            contentpanel = new JPanel();
            contentpanel.setLayout(null);
            contentpanel.add(getTextOriginal(), null);
            contentpanel.add(getTextEncoded(), null);
            contentpanel.add(getButtonEncoded(), null);
            contentpanel.add(getButtonDecoded(), null);
            contentpanel.add(encodedstringLabel, null);
            contentpanel.add(originalstringLabel, null);
        }
        return contentpanel;
    }

    /**
     * This method initializes textOriginal
     *
     * @return javax.swing.JTextField
     */
    private JTextField getTextOriginal() {
        if (originalstringTextField == null) {
            originalstringTextField = new JTextField();
            originalstringTextField.setBounds(new Rectangle(15, 30, 450, 20));
        }
        return originalstringTextField;
    }

    /**
     * This method initializes textVerschluesselt
     *
     * @return javax.swing.JTextField
     */
    private JTextField getTextEncoded() {
        if (encodedstringTextField == null) {
            encodedstringTextField = new JTextField();
            encodedstringTextField.setBounds(new Rectangle(15, 80, 450, 20));
        }
        return encodedstringTextField;
    }

    /**
     * This method initializes buttonEncode
     *
     * @return javax.swing.JButton
     */
    private JButton getButtonEncoded() {
        if (buttonEncode == null) {
            buttonEncode = new JButton();
            buttonEncode.setBounds(new Rectangle(16, 116, 200, 24));
            buttonEncode.setText("Encode");
            buttonEncode.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Encoder_Decoder ed = new Encoder_Decoder();
                    try {
                        encodedstringTextField.setText(ed.encodeString(originalstringTextField.getText()));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
        return buttonEncode;
    }

    /**
     * This method initializes ButtonDecode
     *
     * @return javax.swing.JButton
     */
    private JButton getButtonDecoded() {
        if (buttonDecode == null) {
            buttonDecode = new JButton();
            buttonDecode.setBounds(new Rectangle(264, 116, 200, 24));
            buttonDecode.setText("Decode");
            buttonDecode.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Encoder_Decoder ed = new Encoder_Decoder();
                    try {
                        originalstringTextField.setText(ed.decodeString(encodedstringTextField.getText()));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
        return buttonDecode;
    }

    public static void main(String args[]) {
        Base64Converter b64c = new Base64Converter();
        b64c.setVisible(true);
    }
}