import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
        }
    }
}
