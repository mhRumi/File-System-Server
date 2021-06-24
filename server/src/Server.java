import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class Server extends Thread implements Serializable {
    static ArrayList<MyFile> myFiles = new ArrayList<>();
    int fileId = 0;
    ServerSocket serverSocket;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;
    OutputStream outputStream;
    InputStream inputStream;
    Socket socket;
    PrintWriter out;
    File folder = new File("./Uploads/");
    File[] listOfFiles = folder.listFiles();
    String fileNames = "";
    int port;

    Server(int port){
        this.port = port;
        try {
            fileNames();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        try {

            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();

            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();

            // sending file informations to client
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectInputStream = new ObjectInputStream(inputStream);
            sendAllFiles();


            while (true && !socket.isClosed()){

                System.out.println(socket.isClosed());
                // Receiving file from client
                inputStream = socket.getInputStream();

                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                String operation = (String) objectInputStream.readObject();

                DataInputStream dataInputStream = new DataInputStream(inputStream);

                if(operation.equalsIgnoreCase("upload")){
                    System.out.println(operation);
                    String fileName = (String) objectInputStream.readObject();

                    int fileContentLength = dataInputStream.readInt();
                    if(fileContentLength > 0 )
                    {
                        byte[] fileContentBytes = new byte[fileContentLength];
                        dataInputStream.readFully(fileContentBytes, 0, fileContentBytes.length);


                        // Write file to a file
                        File file = new File("Uploads/"+fileName);
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        fileOutputStream.write(fileContentBytes);
                        fileOutputStream.close();

                        MyFile newFile = new MyFile(fileId, fileName, getFileExtension(fileName));
                        myFiles.add(newFile);
                        fileId++;
                        sendFileInformation(newFile);

                        Main.main.addingSingleNode(fileName);
                    }

                }else if(operation.equalsIgnoreCase("download")){
                    System.out.println(operation);
                    int downloadFileId = dataInputStream.readInt();
                    for(MyFile file: myFiles)
                    {
                        if(file.getId() == downloadFileId){
                            File readFile = new File(file.getName());
                            sendFileToClient(readFile.getName());
                        }
                    }


                }else if(operation.equalsIgnoreCase("delete")){
                    int deleteFileId = dataInputStream.readInt();
                    String filename = "";
                    for(MyFile file: myFiles)
                    {
                        if(file.getId() == deleteFileId){
                            File f = new File("Uploads/"+file.getName());
                            filename = f.getName();
                            if(f.delete())                      //returns Boolean value
                            {
                                String msg = filename+" deleted";
                                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                                objectOutputStream.writeObject(msg);
                                System.out.println(msg);
                            }
                            deleteFile(deleteFileId);
                            break;
                        }
                    }

                    Main.main.deleteSingleNode(filename);

                }else if(operation.equalsIgnoreCase("close")){
                    socket.close();
                    serverSocket.close();
                    Server server = new Server(Integer.parseInt(Main.jTextFieldPort.getText()));
                    server.start();
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    void fileNames() throws IOException {
        myFiles.clear();
        listOfFiles = folder.listFiles();
        for(File file: listOfFiles){

            String fileName = file.getName();
            MyFile newFile = new MyFile(fileId,fileName,getFileExtension(fileName));
            myFiles.add(newFile);
            fileId++;
        }

    }

    public static String getFileExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            // Set the extension to the extension of the filename.
            return fileName.substring(i + 1);
        } else {
            return "";
        }
    }



    public void sendFileInformation(MyFile newFile) throws IOException {
        objectOutputStream.writeInt(1);
        objectOutputStream.writeObject(newFile);
    }

    public void sendAllFiles() throws IOException {
        objectOutputStream.writeObject(myFiles);
    }

    public void deleteFile(int fileId){
        MyFile deleteFile = null ;
        for(MyFile file: myFiles)
        {
            if(file.getId() == fileId)
            {
               deleteFile = file;
               break;
            }
        }
        myFiles.remove(deleteFile);
    }


    public  void sendFileToClient(String filename) throws IOException {
        listOfFiles = folder.listFiles();
        for(File file: listOfFiles) {
            if (file.getName().equalsIgnoreCase(filename))
            {
                try {
                    FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
                    byte[] fileContentBytes = new byte[(int) file.length()];
                    if ((int) file.length() > 0) {
                        fileInputStream.read(fileContentBytes);
                    }
                    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                    String fileName = file.getName();

                    objectOutputStream.writeObject(fileName);

                    dataOutputStream.writeInt(fileContentBytes.length);
                    dataOutputStream.write(fileContentBytes);

                    dataOutputStream.writeInt(1);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        }

    }

}
