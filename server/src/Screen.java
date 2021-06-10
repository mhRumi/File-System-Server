import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Screen {
    static JButton button;
    static JLabel label;
    static Server server;
    Screen(){

    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Server");
        // Set the size of the frame.
        jFrame.setSize(500, 700);
        // Give the frame a box layout that stacks its children on top of each other.
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        // When closing the frame also close the program.
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel that will hold the title label and the other jpanels.
        JPanel jPanel = new JPanel();
        // Make the panel that contains everything to stack its child elements on top of eachother.
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        button = new JButton("Start");
        button.setBounds(400, 10, 80, 30);
        button.setAlignmentX(Component.RIGHT_ALIGNMENT);
        button.setBackground(Color.green);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server = new Server();
                server.start();;
            }
        });

        jFrame.add(button);

        // Make it scrollable when the data gets in jpanel.
        JScrollPane jScrollPane = new JScrollPane(jPanel);
        Border blackline = BorderFactory.createLineBorder(Color.black);
        jScrollPane.setBorder(blackline);
        // Make it so there is always a vertical scrollbar.
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        jFrame.add(jScrollPane);
        // Make the GUI show up.
        jFrame.setVisible(true);
    }
}
