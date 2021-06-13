import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {
    static JTextField host;
    static JTextField port;
    static JButton button;
    static JLabel imgLabel;
    static JLabel connected;
    static JButton upload;
    static JPanel jPanel;
    static JPanel jPanelConnection;
    static JPanel deleteJpanel;
    static JPanel jPanelUpload;
    static JFrame jFrame2nd;
    static JDialog dialog;

    static ArrayList<MyFile> myFiles = new ArrayList<>();
    static Socket socket = null;
    static InputStream inputStream = null;
    static OutputStream outputStream = null;
    static DataOutputStream dataOutputStream = null;
    static ObjectOutputStream objectOutputStream;
    static ObjectInputStream objectInputStream;
    static JScrollPane jScrollPane;
    static JPanel jpFileRow;
    static JLabel jlFileName;
    static JFrame jFrame;
    static Color backgroundColor;
    public static final JPopupMenu popupmenu = new JPopupMenu("Edit");
    public static int selectedFileId;


    public static void main(String[] args) throws IOException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        jFrame = new JFrame("Client");
        dialog = new JDialog(jFrame,"Alert", Dialog.ModalityType.APPLICATION_MODAL);
        jFrame.setSize(500, 800);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
       jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jPanelConnection = new JPanel();
        jPanelConnection.setLayout(new GridLayout(1, 3));
        jPanelConnection.setPreferredSize(new Dimension(10, 50));
        jPanelConnection.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.gray),
                new EmptyBorder(new Insets(1, 2, 1, 2))));


       jPanel = new JPanel();
       jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
       jPanel.setBorder(new EmptyBorder(10,10, 150,10));



         jScrollPane = new JScrollPane(jPanel);
        // Make it so there is always a vertical scrollbar.
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);




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



        final File[] fileToSend = new File[1];
        int deleteId = 0;

        host = new JTextField();
        host.setBounds(80, 20, 100, 50);
        host.setText("localhost");
        host.setToolTipText("Enter host IP");

        Font fieldFont = new Font("Arial", Font.PLAIN, 20);
        host.setFont(fieldFont);
        host.setBackground(Color.white);
        host.setForeground(Color.black.brighter());
        host.setColumns(30);
        host.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.gray),
                new EmptyBorder(new Insets(15, 25, 15, 25))));



        port = new JTextField();
        port.setBounds(250, 20, 150, 50);
        port.setText("6000");
        port.setToolTipText("Enter port no");
        port.setFont(fieldFont);
        port.setForeground(Color.black.brighter());
        port.setColumns(30);
        port.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.gray),
                new EmptyBorder(new Insets(15, 25, 5, 25))));

        // Image

        imgLabel = new JLabel();
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imgLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        imgLabel.setIcon(new ImageIcon("/home/rumi/Downloads/image1.jpg"));


        connected = new JLabel("Connected with server");
        connected.setFont(fieldFont);
        connected.setBounds(420,10,100,30);
        connected.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.darkGray),
                new EmptyBorder(new Insets(15, 25, 15, 25))));
        connected.setVisible(false);

        button = new JButton("Connect");
        button.setBackground(Color.black);
        button.setForeground(Color.white);
        Font bFont = new Font("Arial", Font.BOLD, 20);
        button.setFont(bFont);

        button.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.gray),
                new EmptyBorder(new Insets(15, 25, 15, 25))));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                _hide();
                try {
                    String hostIp = host.getText();
                    int portN = Integer.parseInt(port.getText());
                    socket = new Socket(hostIp, portN);
                    Client.outputStream = socket.getOutputStream();
                    Client.inputStream = socket.getInputStream();
                    if(socket.isConnected()){
                        getFileInfo();
                    }else{
                        System.out.println("Can't connect with server");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });


        upload = new JButton("Upload");
        upload.setBounds(5, 5, 70, 50);
        upload.setFont(fieldFont);
        upload.setBackground(Color.black);
        upload.setForeground(Color.white);
        upload.setVisible(false);
        upload.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.gray),
                new EmptyBorder(new Insets(15, 25, 15, 25))));
        upload.addActionListener(new java.awt.event.ActionListener(){
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Choose a file to send");

                    if (fileChooser.showOpenDialog(null)  == JFileChooser.APPROVE_OPTION) {
                        // Get the selected file.
                        fileToSend[0] = fileChooser.getSelectedFile();
                        // Change the text of the java swing label to have the file name.
                        //jlFileName.setText("The file you want to send is: " + fileToSend[0].getName());

                        try {

                            FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());
                             dataOutputStream = new DataOutputStream(Client.outputStream);
                            String fileName = fileToSend[0].getName();
                            byte[] fileBytes = new byte[(int)fileToSend[0].length()];

                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(Client.outputStream);
                            String op = new String("upload");

                            fileInputStream.read(fileBytes); // read file from file system

                            objectOutputStream.writeObject(op); // sending operation
                            objectOutputStream.writeObject(fileName); // sending file name

                            dataOutputStream.writeInt(fileBytes.length);
                            dataOutputStream.write(fileBytes);
                            getSingleElement();
                            JOptionPane.showMessageDialog(jFrame, "Upload complete", "About", JOptionPane.INFORMATION_MESSAGE);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        jFrame.add(Box.createVerticalStrut(5));
        jPanelConnection.add(host);
        jPanelConnection.add(port);
        jPanelConnection.add(button);

        jFrame.add(jPanelConnection);

        jFrame.add(imgLabel);
        jFrame.add(connected);
        jFrame.add(Box.createVerticalStrut(10));
        jFrame.add(upload);
        jFrame.add(Box.createVerticalStrut(10));
        jFrame.add(jScrollPane, BorderLayout.CENTER);

        jFrame.setVisible(true);
        jFrame.setEnabled(true);


    }


    static void _hide(){
        jPanelConnection.setVisible(false);
        connected.setVisible(true);
        upload.setVisible(true);
        imgLabel.setVisible(false);
        connected.setVisible(true);
        upload.setVisible(true);


    }

    static public void downloadFile(int fileId){
        try {
            dataOutputStream = new DataOutputStream(Client.outputStream);
            String op = new String("download");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(Client.outputStream);
            objectOutputStream.writeObject(op);

            dataOutputStream.writeInt(fileId);
            DataInputStream dataInputStream = new DataInputStream(Client.inputStream);
            Object object = objectInputStream.readObject();
            String fileName = (String) object;

            System.out.println(fileName);
            int fileContentLength = dataInputStream.readInt();
            byte[] fileContentBytes = new byte[fileContentLength];
            dataInputStream.readFully(fileContentBytes, 0, fileContentBytes.length);

            // Write file to a file
            File file = new File(fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(fileContentBytes);
            fileOutputStream.close();

            JOptionPane.showMessageDialog(jFrame, "Download complete", "About", JOptionPane.INFORMATION_MESSAGE);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static public void deleteFile(int fileId)
    {
        try {
            dataOutputStream = new DataOutputStream(Client.outputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(Client.outputStream);
            String op = new String("delete");
            objectOutputStream.writeObject(op);

            dataOutputStream.writeInt(fileId); // sending file id
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            String message = (String) objectInputStream.readObject(); // receiveing message
            System.out.println(message);
            deleteJpanel.setVisible(false);
            JOptionPane.showMessageDialog(jFrame, "Successfully deleted", "Acknowledgement", JOptionPane.INFORMATION_MESSAGE);

            //deleteing from front-end

            MyFile deleteFile = null;
            for(MyFile file: myFiles)
            {
                if(file.getId() == selectedFileId)
                {
                    deleteFile = file;
                    break;
                }
            }
            myFiles.remove(deleteFile);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void getSingleElement() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(Client.inputStream);
        Object object = objectInputStream.readObject();
        MyFile newFile = (MyFile) object;
        myFiles.add(newFile);

        System.out.println(newFile.getId() + " " + newFile.getName());
        addIntoScrollPanel(newFile);

    }

    public static void getFileInfo() throws IOException, ClassNotFoundException {
        myFiles = null;
        objectInputStream = new ObjectInputStream(Client.inputStream);
        Object object = objectInputStream.readObject();
        myFiles = (ArrayList<MyFile>) object;

        for (MyFile file : myFiles) {
           addIntoScrollPanel(file);

        }
    }



    public static void addIntoScrollPanel(MyFile file){

        JPanel jpFileRow = new JPanel();
        jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.Y_AXIS));
        jpFileRow.add(Box.createVerticalStrut(10));
        jpFileRow.setBackground(Color.white);
        // Set the file name.
        JLabel jlFileName = new JLabel(file.getName());
        jlFileName.setFont(new Font("Arial", Font.PLAIN, 20));

        jlFileName.setBorder(new EmptyBorder(10,0, 10,0));
//        jlFileName.setAlignmentX(Component.CENTER_ALIGNMENT);
        jlFileName.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.gray),
                new EmptyBorder(new Insets(15, 25, 15, 25))));

        jpFileRow.setName((String.valueOf(file.getId())));
        jpFileRow.addMouseListener(MyMouseListener.getMyMouseListener());
        // Add everything.
        jpFileRow.add(jlFileName);
        jPanel.add(jpFileRow);
        jFrame.validate();
    }
}
