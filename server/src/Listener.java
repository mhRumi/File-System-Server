import javax.swing.filechooser.FileView;
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
        if(e.getActionCommand().equalsIgnoreCase("Create")){
            DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) Main.m_tree
                    .getLastSelectedPathComponent();

            if (selNode == null) {
                return;
            }

            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("New");
            Main.m_model.insertNodeInto(newNode, selNode, selNode.getChildCount());

            TreeNode[] nodes = Main.m_model.getPathToRoot(newNode);
            String directory = "";
            int i = 1;
            for(TreeNode node:nodes){
                if(i == 1 || i == nodes.length)
                {
                    if(i == 1)
                    {
                        directory += node+"Uploads/";
                    }else
                        directory += node;
                }else{
                    directory += node+"/";
                }
                i++;

            }

            TreePath path = new TreePath(nodes);
            Main.m_tree.scrollPathToVisible(path);

            Main.m_tree.setSelectionPath(path);

            Main.m_tree.startEditingAtPath(path);
            File dir = new File(directory);
            dir.mkdir();
        }else{
            System.out.println("Delete");
            DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) Main.m_tree
                    .getLastSelectedPathComponent();
            deleteNode(selNode);

        }
    }

    public static void deleteNode(DefaultMutableTreeNode selNode){

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
        for(TreeNode node:nodes){
            if(i == 1 )
            {
                directory += node+"Uploads/";
            }else if(i != nodes.length){
                directory += node+"/";
            }
            i++;

        }
        File index = new File(directory+selNode);
        System.out.println(directory+selNode);
        String[]entries = index.list();
        if(index.isDirectory()){
            for(String s: entries){
                File currentFile = new File(index.getPath(),s);
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
