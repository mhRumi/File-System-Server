import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import static java.lang.System.exit;

public class ClientInterface {
    public static final JPopupMenu popupmenu = new JPopupMenu("Edit");
    public static JTextField host;
    public static JTextField port;
    static JPanel center;
    static JFrame frame;
    static JButton connectButton;
    static JTextField showFileName;
    static JButton closeButton;
    static JButton chooseFileButton;
    static JButton uploadButton;
    static JScrollPane jScrollPane;
    static JDialog dialog;
    JFrame f;
    static Client client;
    ClientInterface(){
        frame=new JFrame();
        dialog = new JDialog(frame,"Alert", Dialog.ModalityType.APPLICATION_MODAL);
        JPanel topPanel=new JPanel();
        topPanel.setPreferredSize(new Dimension(1, 100));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JPanel leftPanel=new JPanel();
        leftPanel.setBackground(Color.white);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(140, 400));
        center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));


        connectButton = new JButton("connect");
        connectButton.setBackground(Color.black);
        connectButton.setForeground(Color.white);
        Font bFont = new Font("Arial", Font.PLAIN, 20);
        connectButton.setFont(bFont);

        connectButton.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.white),
                new EmptyBorder(new Insets(15, 25, 15, 25))));

        closeButton = new JButton("close");
        closeButton.setBackground(Color.black);
        closeButton.setForeground(Color.white);
        closeButton.setFont(bFont);

        closeButton.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.white),
                new EmptyBorder(new Insets(15, 40, 15, 40))));
        closeButton.setEnabled(false);


        chooseFileButton = new JButton("choose");
        chooseFileButton.setBackground(Color.black);
        chooseFileButton.setForeground(Color.white);
        chooseFileButton.setFont(bFont);

        chooseFileButton.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.WHITE),
                new EmptyBorder(new Insets(15, 30, 15, 30))));
        chooseFileButton.setEnabled(false);

        uploadButton = new JButton("upload");
        uploadButton.setBackground(Color.black);
        uploadButton.setForeground(Color.white);
        uploadButton.setFont(bFont);
        uploadButton.setEnabled(false);

        uploadButton.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.WHITE),
                new EmptyBorder(new Insets(15, 30, 15, 30))));


        connectButton.addActionListener(new Listener());
        chooseFileButton.addActionListener(new Listener());
        uploadButton.addActionListener(new Listener());
        closeButton.addActionListener(new Listener());

        // pop up menu

        JMenuItem create = new JMenuItem("Download");
        create.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.black),
                new EmptyBorder(new Insets(15, 25, 15, 25))));
        JMenuItem delete = new JMenuItem("Delete");
        delete.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.black),
                new EmptyBorder(new Insets(15, 25, 15, 25))));
        create.addActionListener(new Listener());
        delete.addActionListener(new Listener());

        popupmenu.add(create);
        popupmenu.add(delete);

        // top panel design

        host = new JTextField();
        host.setPreferredSize(new Dimension(200, 50));
        host.setText("localhost");
        host.setToolTipText("Enter host IP");

        Font fieldFont = new Font("Serif", Font.PLAIN, 20);
        host.setFont(fieldFont);
        host.setBackground(Color.white);
        host.setForeground(Color.black.brighter());
        host.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.gray),
                new EmptyBorder(new Insets(0, 15, 5, 5))));



        port = new JTextField();
        port.setPreferredSize(new Dimension(120, 50));
        port.setText("6000");
        port.setToolTipText("Enter port no");
        port.setFont(fieldFont);
        port.setForeground(Color.black.brighter());
        port.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.gray),
                new EmptyBorder(new Insets(0, 15, 5, 5))));

        showFileName = new JTextField();
        showFileName.setPreferredSize(new Dimension(200, 50));
        showFileName.setText("Choose file");
        showFileName.setToolTipText("Enter port no");
        showFileName.setFont(fieldFont);
        showFileName.setForeground(Color.black.brighter());
        showFileName.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.gray),
                new EmptyBorder(new Insets(0, 15, 5, 5))));
        showFileName.setVisible(false);

        // adding to top panel
        topPanel.add(host, FlowLayout.LEFT);
        topPanel.add(port, FlowLayout.CENTER);
        topPanel.add(showFileName, FlowLayout.RIGHT);
        //topPanel.add(connectButton, FlowLayout.RIGHT);

        jScrollPane = new JScrollPane(center);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        //addding to left panel
        leftPanel.add(connectButton);
        leftPanel.add(chooseFileButton);
        leftPanel.add(uploadButton);
        leftPanel.add(closeButton);


        frame.add(topPanel,BorderLayout.NORTH);
        frame.add(leftPanel,BorderLayout.WEST);
        frame.add(jScrollPane,BorderLayout.CENTER);

        frame.setSize(500,700);
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        new ClientInterface();
    }


    public static void addIntoScrollPanel(JPanel jPanel, JFrame f, ArrayList<String> list){

        for (String l: list)
        {
            System.out.println(l);

            JPanel jpFileRow = new JPanel();
            jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.Y_AXIS));
            jpFileRow.add(Box.createVerticalStrut(10));
            jpFileRow.setBackground(Color.white);
            // Set the file name.
            JLabel jlFileName = new JLabel(l);
            jlFileName.setFont(new Font("Serif", Font.PLAIN, 20));

            jlFileName.setBorder(new EmptyBorder(0,10, 5,0));
//        jlFileName.setAlignmentX(Component.CENTER_ALIGNMENT);

            jpFileRow.setName((String.valueOf(l)));
            jpFileRow.addMouseListener(MyMouseListener.getMyMouseListener());
            // Add everything.
            jpFileRow.add(jlFileName);
            jPanel.add(jpFileRow);

            f.validate();
        }

    }
}