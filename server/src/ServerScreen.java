import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class ServerScreen extends JFrame{
    static JButton button;
    static JLabel label;
    Server server;

    ServerScreen(){
        initComponents();
    }

    private void initComponents() {
        button = new JButton("Start");
//        button.setBounds(400, 10, 80, 30);
        button.setBackground(Color.green);
        button.setBorder(new EmptyBorder(10 , 10, 10, 10));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                server = new Server();
                server.start();;
            }
        });

        label = new JLabel("Server is running...");
        label.setBounds(150, 20, 200, 30);
        label.setVisible(false);

        add(button);
        add(label);
        setSize(500, 800);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setVisible(true);

    }



    static void hideComponent(){
        button.setVisible(false);
        label.setVisible(true);

    }

    public static void main(String[] args) {
        new ServerScreen();
    }
}
