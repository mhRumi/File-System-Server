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

        try {

        while (true){

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

                        MyFile newFile = new MyFile(fileId, fileName, fileContentBytes, getFileExtension(fileName), "/home/rumi/IdeaProjects/File-System-Server/server/./Uploads/"+fileName);
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
                Server server = new Server();
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
    }


    void fileNames() throws IOException {
        listOfFiles = folder.listFiles();
        for(File file: listOfFiles){
            try {
                FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
                String fileName = file.getName();
                byte[] fileContentBytes = new byte[(int)file.length()];
                if((int) file.length() > 0)
                {
                    fileInputStream.read(fileContentBytes);
                }

                MyFile newFile = new MyFile(fileId,fileName, fileContentBytes,getFileExtension(fileName), file.getAbsolutePath());
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
        if (i > 0) {
            // Set the extension to the extension of the filename.
            return fileName.substring(i + 1);
        } else {
            return "";
        }
    }



    public void sendFileInformation(MyFile newFile) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
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

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        }

    }

}
