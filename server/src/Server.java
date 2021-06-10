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
    OutputStream outputStream;
    InputStream inputStream;
    Socket socket;
    PrintWriter out;
    File folder = new File("./Uploads/");
    File[] listOfFiles = folder.listFiles();
    String fileNames = "";
    Server(){

        try {
            fileNames();
            serverSocket = new ServerSocket(6000);
            socket = serverSocket.accept();

            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();

            // sending file informations to client
            objectOutputStream = new ObjectOutputStream(outputStream);
            sendAllFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void run() {

        while (true){

        try {

            // Receiving file from client
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            int opLength = dataInputStream.readInt();

            byte[] opBytes = new byte[opLength];
            dataInputStream.readFully(opBytes, 0, opBytes.length);
            String operation = new String(opBytes);


            if(operation.equalsIgnoreCase("upload")){
                System.out.println(operation);
                int fileNameLength = dataInputStream.readInt();
                if(fileNameLength > 0)
                {
                    byte[] fileNameBytes = new byte[fileNameLength];
                    dataInputStream.readFully(fileNameBytes, 0, fileNameBytes.length);
                    String fileName = new String(fileNameBytes);

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

                        MyFile newFile = new MyFile(fileId, fileName, fileContentBytes, getFileExtension(fileName));
                        myFiles.add(newFile);
                        fileId++;
                        printFileNames();
                        sendFileInformation(newFile);
                    }
                }
            }else if(operation.equalsIgnoreCase("download")){
                System.out.println(operation);
                int downloadFileId = dataInputStream.readInt();
                for(MyFile file: myFiles)
                {
                    if(file.getId() == downloadFileId){
                        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                        String fileName = file.getName();
                        byte[] fileNameBytes = fileName.getBytes();
                        dataOutputStream.writeInt(fileNameBytes.length);
                        dataOutputStream.write(fileNameBytes);
                        dataOutputStream.writeInt(file.getData().length);
                        dataOutputStream.write(file.getData());
                    }
                }
            }else if(operation.equalsIgnoreCase("delete")){
                int deleteFileId = dataInputStream.readInt();

                for(MyFile file: myFiles)
                {
                    if(file.getId() == deleteFileId){
                        File f = new File("Uploads/"+file.getName());
                        if(f.delete())                      //returns Boolean value
                        {
                           String msg = "File "+ file.getName()+" is successfully deleted";
                           byte[] msgBytes = msg.getBytes();
                           DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                           dataOutputStream.writeInt(msgBytes.length);
                           dataOutputStream.write(msgBytes);
                            System.out.println(msg);
                        }
                        myFiles.remove(deleteFileId);
                        break;
                    }
                }

                printFileNames();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }

    void shutdown() throws IOException {
        socket.close();
        serverSocket.close();
    }

    void fileNames() throws IOException {
        for(File file: listOfFiles){
            try {
                FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
                String fileName = file.getName();
                byte[] fileContentBytes = new byte[(int)file.length()];
                fileInputStream.read(fileContentBytes);

                MyFile newFile = new MyFile(fileId,fileName, fileContentBytes,getFileExtension(fileName));
                newFile.setData(fileContentBytes);
                myFiles.add(newFile);
                fileId++;

                System.out.println(newFile.getId()+" "+newFile.getName()+" "+newFile.getData().length+" "+newFile.getFileExtension());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static String getFileExtension(String fileName) {
        // Get the file type by using the last occurence of . (for example aboutMe.txt returns txt).
        // Will have issues with files like myFile.tar.gz.
        int i = fileName.lastIndexOf('.');
        // If there is an extension.
        if (i > 0) {
            // Set the extension to the extension of the filename.
            return fileName.substring(i + 1);
        } else {
            return "No extension found.";
        }
    }

    public void printFileNames() throws IOException {

        for(MyFile f: myFiles)
        {
            System.out.println(f.getId()+" "+f.getName());
        }
    }

    public void sendFileInformation(MyFile newFile) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(newFile);
    }

    public void sendAllFiles() throws IOException {
        objectOutputStream.writeObject(myFiles);
    }


}
