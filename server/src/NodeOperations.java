import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

public class NodeOperations {
    public static String getLastSelecteNodeName(){
        DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) Main.m_tree
                .getLastSelectedPathComponent();
         selNode = (DefaultMutableTreeNode) selNode.getParent();
        return  selNode.toString();
    }

    public static void deleteIfNodeIfCreatedWithinAFile(){
        DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) Main.m_tree
                .getLastSelectedPathComponent();
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selNode.getParent();
        String parentName = parent.toString();
        System.out.println("Parent: "+parentName);
        if(Server.getFileExtension(parentName).length() > 0){
            Listener.deleteNode(selNode);
            JOptionPane.showMessageDialog(Main.scrollPane, "Choose a directory", "Acknowledgement", JOptionPane.INFORMATION_MESSAGE);
        }else{
//            File file = new File(Listener.directory+selNode.toString());
//            String filename = Listener.directory+selNode.toString();
//            System.out.println("Hello: "+filename);
//            if(file.exists())
//            {
//                Listener.deleteNode(selNode);
//                JOptionPane.showMessageDialog(Main.scrollPane, "Already exist !", "Acknowledgement", JOptionPane.INFORMATION_MESSAGE);
//            }else{
//                file.mkdir();
//            }
        }
    }

}
