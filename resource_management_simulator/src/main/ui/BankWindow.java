package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

// Creates new JFrame GUI to show bank information
public class BankWindow extends JFrame {
    private JPanel contentPane;
    private JLabel lblText;
    private Font customFont;

    // EFFECTS: creates bankAccount window and sets attributes
    public BankWindow() {
        setTitle("Bank Account");
        setBounds(0, 0, 300, 60);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setLocationRelativeTo(null);
        setContentPane(contentPane);
        contentPane.setVisible(true);
        contentPane.setBackground(new java.awt.Color(240, 231, 216));
        setResizable(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setVisible(true);

        createFont();

        createLblText();
    }

    // MODIFIES: lblText, contentPane
    // EFFECTS: sets label and adds it to frame
    private void createLblText() {
        lblText = new JLabel("Bank Account Balance: $" + "0.0");
        lblText.setBounds(10, 10, 500, 20);
        lblText.setHorizontalAlignment(JTextField.LEFT);
        lblText.setFont(customFont.deriveFont(18f));
        contentPane.add(lblText);
    }

    // MODIFIES: lblText, contentPane
    // EFFECTS: updates label text and update frame
    public void updateLblText(double newBalance) {
        lblText.setText("Bank Account Balance: $" + String.format("%.2f", newBalance));
        contentPane.revalidate();
        contentPane.repaint();
    }

    // MODIFIES: customFont
    // EFFECTS: creates font and registers it
    private void createFont() {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("data/MinecraftFont.otf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            System.out.println("font error");
        }
    }
}
