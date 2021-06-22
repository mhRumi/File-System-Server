import com.sun.tools.javac.Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowDialog {
    public static JFrame jFrame;
    public static void showDialog(String msg)
    {
        // Frame to hold everything.
        jFrame = new JFrame("Alert");
        // Set the size of the frame.
        jFrame.setSize(500, 200);

        // Panel to hold everything.
        JPanel jPanel = new JPanel();
        // Make the layout a box layout with child elements stacked on top of each other.
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));


        // Label to prompt the user if they are sure they want to download the file.
        JLabel jlPrompt = new JLabel("Permanently delete this file?");
        jlPrompt.setFont(new Font("Arial", Font.BOLD, 20));
        jlPrompt.setBorder(new EmptyBorder(20,0,10,0));
        jlPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);
        jlPrompt.setBackground(Color.white);

        jlPrompt.setForeground(Color.red.brighter());
        jlPrompt.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.BLACK),
                new EmptyBorder(new Insets(15, 25, 15, 25))));

        // Create the yes for accepting the download.
        JButton jbYes = new JButton("Yes");
        jbYes.setPreferredSize(new Dimension(100, 50));
        jbYes.setFont(new Font("Arial", Font.BOLD, 20));

        jbYes.setBackground(Color.BLACK);
        jbYes.setForeground(Color.red);
        jbYes.setAlignmentX(Container.CENTER_ALIGNMENT);
        jbYes.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.DARK_GRAY),
                new EmptyBorder(new Insets(15, 25, 15, 25))));
        jbYes.addActionListener(new Listener());

        // No button for rejecting the download.
        JButton jbNo = new JButton("No");
        jbNo.setPreferredSize(new Dimension(100, 50));
        jbNo.setFont(new Font("Arial", Font.BOLD, 20));
        jbNo.setBackground(Color.BLACK);
        jbNo.setForeground(Color.green);
        jbNo.setAlignmentX(Container.CENTER_ALIGNMENT);
        jbNo.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.DARK_GRAY),
                new EmptyBorder(new Insets(15, 25, 15, 25))));
        jbNo.addActionListener(new Listener());

        // Panel to hold the yes and no buttons and make the next to each other left and right.
        JPanel jpButtons = new JPanel();
        // Add spacing around the panel.
        jpButtons.setBorder(new EmptyBorder(20, 0, 10, 0));
        // Add the yes and no buttons.
        jpButtons.add(jbYes);
        jpButtons.add(jbNo);

        jPanel.add(Box.createRigidArea(new Dimension(500, 30)));
        jPanel.add(jlPrompt);
        jPanel.add(jpButtons);
        jFrame.add(jPanel);

        jFrame.setVisible(true);
    }

    public static void closeFrame(){
        jFrame.dispose();
    }
}
