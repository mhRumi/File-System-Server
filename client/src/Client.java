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
    static JLabel connected;
    static JButton upload;
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
    static ObjectInputStream objectInputStream;
    static JFrame jFrame;
    static JFileChooser fileChooser;

    public static final JPopupMenu popupmenu = new JPopupMenu("Edit");
    public static int selectedFileId;
    final static File[] fileToSend = new File[1];


    public Client(String host, int port){
        try {
             socket = new Socket(host, port);
            Client.outputStream = socket.getOutputStream();
            Client.inputStream = socket.getInputStream();
            if(socket.isConnected()){
                getFileInfo();
            }else{
                JOptionPane.showMessageDialog(ClientInterface.frame, "Cann't connect !", "Acknowledgement", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void chooseFile(){
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose a file to send");
        if (fileChooser.showOpenDialog(null)  == JFileChooser.APPROVE_OPTION) {
            // Change the text of the java swing label to have the file name.
            //jlFileName.setText("The file you want to send is: " + fileToSend[0].getName());
            fileToSend[0] = fileChooser.getSelectedFile();

            JOptionPane.showMessageDialog(ClientInterface.frame, fileToSend[0].getName()+" is selected press upload to send", "Acknowledgement", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void uploadToserver(){
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

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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
            JOptionPane.showMessageDialog(ClientInterface.frame, "Successfully deleted", "Acknowledgement", JOptionPane.INFORMATION_MESSAGE);

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
        objectInputStream = new ObjectInputStream(Client.inputStream);
        Object object = objectInputStream.readObject();
        myFiles = (ArrayList<MyFile>) object;

        for (MyFile file : myFiles) {
           addIntoScrollPanel(file);
            System.out.println(file.getName());

        }
    }



    public static void addIntoScrollPanel(MyFile file){


            JPanel jpFileRow = new JPanel();
            jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.Y_AXIS));
            jpFileRow.add(Box.createVerticalStrut(10));
            jpFileRow.setBackground(Color.white);
            // Set the file name.
            JLabel jlFileName = new JLabel(file.getName());
            jlFileName.setFont(new Font("Serif", Font.PLAIN, 20));

            jlFileName.setBorder(new EmptyBorder(0,10, 5,0));
//        jlFileName.setAlignmentX(Component.CENTER_ALIGNMENT);

            jpFileRow.setName((String.valueOf(file.getId())));
            jpFileRow.addMouseListener(MyMouseListener.getMyMouseListener());
            // Add everything.
            jpFileRow.add(jlFileName);
            ClientInterface.center.add(jpFileRow);

            ClientInterface.frame.validate();


    }
}
