import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static java.lang.System.exit;

public class Listener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equalsIgnoreCase("Download")){
            Client.downloadFile(Client.selectedFileId);
        }else if(e.getActionCommand().equalsIgnoreCase("Ok")){
            Client.dialog.dispose();
        }
        else if(e.getActionCommand().equalsIgnoreCase("Delete")){
            Client.deleteFile(Client.selectedFileId);
        }else if(e.getActionCommand().equalsIgnoreCase("connect")){
            startClient();
        }
        else if(e.getActionCommand().equalsIgnoreCase("choose")){
            Client.chooseFile();
        }
        else if(e.getActionCommand().equalsIgnoreCase("upload")){
            Client.uploadToserver();
        }
        else if(e.getActionCommand().equalsIgnoreCase("close")){
            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(Client.outputStream);
                objectOutputStream.writeObject("close");
                Client.socket.close();
                ClientInterface.connectButton.setEnabled(true);
                ClientInterface.closeButton.setEnabled(false);
                JOptionPane.showMessageDialog(ClientInterface.frame, "Connection close","Acknowledgement", JOptionPane.INFORMATION_MESSAGE);
                ClientInterface.frame.dispose();
                new ClientInterface();

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public void startClient()
    {
        new Client(ClientInterface.host.getText(), Integer.parseInt( ClientInterface.port.getText()));
        ClientInterface.connectButton.setEnabled(false);
        ClientInterface.closeButton.setEnabled(true);
        ClientInterface.chooseFileButton.setEnabled(true);
        ClientInterface.uploadButton.setEnabled(true);
    }
}
