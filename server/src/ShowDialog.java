import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ShowDialog {
    public static void showDialog(String msg)
    {
        Main.dialog.setLayout(new BoxLayout(Main.dialog.getContentPane(), BoxLayout.Y_AXIS));
        JLabel message = new JLabel(msg);
        Font fieldFont = new Font("Arial", Font.BOLD, 20);
        message.setFont(fieldFont);
        message.setBackground(Color.white);
        message.setForeground(Color.red.brighter());
        message.setAlignmentX(Container.CENTER_ALIGNMENT);
        message.setAlignmentY(Container.CENTER_ALIGNMENT);
        message.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.BLACK),
                new EmptyBorder(new Insets(15, 25, 15, 25))));

        JButton button = new JButton("Ok");
        button.setFont(fieldFont);
        button.setPreferredSize(new Dimension(100, 40));
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setAlignmentX(Container.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.DARK_GRAY),
                new EmptyBorder(new Insets(15, 25, 15, 25))));
        button.addActionListener(new Listener());


        Main.dialog.setBounds(150, 300, 500, 200);
        Main.dialog.add(Box.createVerticalStrut(30));
        Main.dialog.add(message);
        Main.dialog.add(Box.createVerticalStrut(20));
        Main.dialog.add(button);
        Main.dialog.setVisible(true);
    }
}
