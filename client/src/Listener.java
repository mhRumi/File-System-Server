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
        }else if(e.getActionCommand().equalsIgnoreCase("yes")){
            ShowDialog.closeFrame();
            Client.deleteFile(Client.selectedFileId);
        }
        else if(e.getActionCommand().equalsIgnoreCase("no")){
            ShowDialog.closeFrame();
        }
        else if(e.getActionCommand().equalsIgnoreCase("Delete")){
            ShowDialog.showDialog("Permanently delete this file?");
        }else if(e.getActionCommand().equalsIgnoreCase("connect")){
            startClient();
        }
        else if(e.getActionCommand().equalsIgnoreCase("choose")){
            Client.chooseFile();
        }
        else if(e.getActionCommand().equalsIgnoreCase("upload")){
            Client.uploadToserver();
//            ProgressBar frame = new ProgressBar((int) Client.fileToSend[0].length());
//            frame.pack();
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//            frame.iterate();

        }
        else if(e.getActionCommand().equalsIgnoreCase("close")){
            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(Client.outputStream);
                objectOutputStream.writeObject("close");
                Client.socket.close();
                ClientInterface.connectButton.setEnabled(true);
                ClientInterface.closeButton.setEnabled(false);
                JOptionPane.showMessageDialog(ClientInterface.frame, "Connection close","Acknowledgement", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public void startClient()
    {
        ClientInterface.client = new Client(ClientInterface.host.getText(), Integer.parseInt( ClientInterface.port.getText()));
        ClientInterface.connectButton.setEnabled(false);
        ClientInterface.closeButton.setEnabled(true);
        ClientInterface.chooseFileButton.setEnabled(true);
        ClientInterface.uploadButton.setEnabled(true);
    }
}
