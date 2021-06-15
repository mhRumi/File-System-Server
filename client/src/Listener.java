import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

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
            new Client(ClientInterface.host.getText(), Integer.parseInt( ClientInterface.port.getText()));
            ClientInterface.connectButton.setEnabled(false);
            ClientInterface.closeButton.setEnabled(true);
            ClientInterface.chooseFileButton.setEnabled(true);
            ClientInterface.uploadButton.setEnabled(true);


        }
        else if(e.getActionCommand().equalsIgnoreCase("choose")){
            Client.chooseFile();
        }
        else if(e.getActionCommand().equalsIgnoreCase("upload")){
            Client.uploadToserver();
        }
        else if(e.getActionCommand().equalsIgnoreCase("close")){
            try {

                Client.socket.close();
                ClientInterface.frame.dispose();
                ClientInterface.shutDown();
                JOptionPane.showMessageDialog(ClientInterface.frame, "Connection close","Acknowledgement", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
