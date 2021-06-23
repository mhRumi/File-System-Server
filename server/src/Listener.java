import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Listener implements ActionListener {
    static String directory;
    static TreeNode[] nodes;

    @Override
    public void actionPerformed(ActionEvent e) {
        directory = "";
        if (e.getActionCommand().equalsIgnoreCase("Create")) {
            DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) Main.m_tree
                    .getLastSelectedPathComponent();


            if (selNode == null) {
                return;
            }

            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("New");
            Main.m_model.insertNodeInto(newNode, selNode, selNode.getChildCount());

            nodes = Main.m_model.getPathToRoot(newNode);

            int i = 1;
            for (TreeNode node : nodes) {
                if (i == 1 || i == nodes.length) {
                    if (i == 1) {
                        directory += node + "Uploads/";
                    }
                } else {
                    directory += node + "/";
                }
                i++;
            }


            TreePath path = new TreePath(nodes);
            Main.m_tree.scrollPathToVisible(path);
            Main.m_tree.setSelectionPath(path);
            Main.m_tree.startEditingAtPath(path);

            NodeOperations.deleteIfNodeIfCreatedWithinAFile();

        } else if (e.getActionCommand().equalsIgnoreCase("Delete")) {
            System.out.println("Delete");
            DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) Main.m_tree
                    .getLastSelectedPathComponent();
            deleteNode(selNode);

        } else if (e.getActionCommand().equalsIgnoreCase("Ok")) {
            Main.dialog.dispose();
        }
        else if (e.getActionCommand().equalsIgnoreCase("start")) {
            if(Main.jTextFieldPort.getText().equalsIgnoreCase("")){
                JOptionPane.showMessageDialog(Main.scrollPane, "Please enter port no", "Acknowledgement", JOptionPane.INFORMATION_MESSAGE);
            }else{
                Main.startServer();

            }

        }
    }


    static public void createFile(String directory, String fileExtension) {
        File dir = new File(directory);
        String directoryExtension = NodeOperations.getLastSelecteNodeName();
        System.out.println("Selected node: " + directoryExtension);
        directoryExtension = Server.getFileExtension(directoryExtension);
        if (directoryExtension.length() > 0) {
            DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) Main.m_tree
                    .getLastSelectedPathComponent();
            deleteNode(selNode);
            JOptionPane.showMessageDialog(Main.scrollPane, "Choose a directory", "Acknowledgement", JOptionPane.INFORMATION_MESSAGE);
        } else if (dir.exists()) {
            DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) Main.m_tree
                    .getLastSelectedPathComponent();
            deleteNode(selNode);
            JOptionPane.showMessageDialog(Main.scrollPane, "Already exist !!!", "Acknowledgement", JOptionPane.INFORMATION_MESSAGE);
        } else {
            if(fileExtension.length() > 0)
            {
                try {
                    dir.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                dir.mkdir();
            }

        }


    }

    public static void deleteNode(DefaultMutableTreeNode selNode) {

        if (selNode == null) {
            return;
        }
        MutableTreeNode parent = (MutableTreeNode) (selNode.getParent());
        if (parent == null) {
            return;
        }
        MutableTreeNode toBeSelNode = (MutableTreeNode) selNode.getPreviousSibling();
        if (toBeSelNode == null) {
            toBeSelNode = (MutableTreeNode) selNode.getNextSibling();
        }
        if (toBeSelNode == null) {
            toBeSelNode = parent;
        }
        TreeNode[] nodes = Main.m_model.getPathToRoot(toBeSelNode);

        String directory = "";
        int i = 1;
        for (TreeNode node : nodes) {
            if (i == 1) {
                directory += node + "Uploads/";
            } else if (i != nodes.length) {
                directory += node + "/";
            }
            i++;

        }
        File index = new File(directory + selNode);
        System.out.println(directory + selNode);
        String[] entries = index.list();
        if (index.isDirectory()) {
            for (String s : entries) {
                File currentFile = new File(index.getPath(), s);
                currentFile.delete();
            }
        }
        index.delete();

        TreePath path = new TreePath(nodes);
        Main.m_tree.scrollPathToVisible(path);
        Main.m_tree.setSelectionPath(path);
        Main.m_model.removeNodeFromParent(selNode);
    }
}
