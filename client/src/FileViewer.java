import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.*;

class Main extends JFrame implements ActionListener {


    DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(".root");

    private DefaultTreeModel m_model = new DefaultTreeModel(rootNode);
    private FileSystemModel fileSystemModel;
    private JTree m_tree = new JTree(m_model);
    final JPopupMenu popupmenu = new JPopupMenu("Edit");
    private JButton m_addButton;
    private JButton m_delButton;
    JPanel jPanelButton;
    int x, y;


    public Main(String directory) {

        fileSystemModel = new FileSystemModel(new File(directory));
        addingToNode("./", rootNode);

        m_tree.setEditable(true);
        m_tree.setSelectionRow(0);
        m_tree.setRowHeight(25);

        m_tree.addMouseListener(getMyMouseListener());

        JScrollPane scrollPane = new JScrollPane(m_tree);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        jPanelButton= new JPanel();
        jPanelButton.setPreferredSize(new Dimension(50, 70));
        JLabel jLabel = new JLabel("File System Server");
        Font fieldFont = new Font("Arial", Font.BOLD, 20);
        jLabel.setFont(fieldFont);
        jLabel.setBackground(Color.white);
        jLabel.setForeground(Color.black.brighter());
        jLabel.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.gray),
                new EmptyBorder(new Insets(15, 25, 15, 25))));
        jPanelButton.add(jLabel);
        jPanelButton.add(Box.createVerticalStrut(10));
        getContentPane().add(jPanelButton, BorderLayout.NORTH);

        JPanel jPanel = new JPanel();
        jPanel.setPreferredSize(new Dimension(50, 50));
        JMenuItem create = new JMenuItem("Create");
        JMenuItem delete = new JMenuItem("Delete");
        create.addActionListener(this);
        delete.addActionListener(this);

        popupmenu.add(create);
        popupmenu.add(delete);
        getContentPane().add(jPanel, BorderLayout.WEST);

        setSize(500, 600);
        setVisible(true);
    }


    public void addingToNode(String directory, DefaultMutableTreeNode rootNode)
    {
        File file = new File(directory);
        File[] listOfFiles = file.listFiles();

        for(File f: listOfFiles)
        {
            if(fileSystemModel.isLeaf(f))
            {
                DefaultMutableTreeNode forums = new DefaultMutableTreeNode(f.getName());
                rootNode.add(forums);
            }else{
                DefaultMutableTreeNode forums = new DefaultMutableTreeNode(f.getName());
                rootNode.add(forums);
                addingToNode(directory+"/"+f.getName(), forums);
        }

        }
    }

    public  MouseListener getMyMouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) m_tree
                        .getLastSelectedPathComponent();
                System.out.println(selNode);


                if (SwingUtilities.isRightMouseButton(e)) {

                    int row = m_tree.getClosestRowForLocation(e.getX(), e.getY());
                    m_tree.setSelectionRow(row);
                    popupmenu.show(e.getComponent(), e.getX(), e.getY());
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }


    public static void main(String[] arg) {
        new Main("./");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equalsIgnoreCase("Create")){
            DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) m_tree
                    .getLastSelectedPathComponent();

            if (selNode == null) {
                return;
            }

            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("New");
            m_model.insertNodeInto(newNode, selNode, selNode.getChildCount());

            TreeNode[] nodes = m_model.getPathToRoot(newNode);
            String directory = "";
            int i = 1;
            for(TreeNode node:nodes){
                if(i == 1 || i == nodes.length)
                {
                    directory += node;
                }else{
                    directory += node+"/";
                }
                i++;

            }

            TreePath path = new TreePath(nodes);
            m_tree.scrollPathToVisible(path);

            m_tree.setSelectionPath(path);

            m_tree.startEditingAtPath(path);
            File dir = new File(directory);
            dir.mkdir();
        }else{
            DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) m_tree
                    .getLastSelectedPathComponent();
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
            TreeNode[] nodes = m_model.getPathToRoot(toBeSelNode);

            String directory = "";
            int i = 1;
            for(TreeNode node:nodes){
                if(i == 1 )
                {
                    directory += node;
                }else if(i != nodes.length){
                    directory += node+"/";
                }
                i++;

            }
            File index = new File(directory+selNode);
            String[]entries = index.list();
            if(index.isDirectory()){
                for(String s: entries){
                    File currentFile = new File(index.getPath(),s);
                    currentFile.delete();
                }
            }
            index.delete();

            TreePath path = new TreePath(nodes);
            m_tree.scrollPathToVisible(path);
            m_tree.setSelectionPath(path);
            m_model.removeNodeFromParent(selNode);
        }
    }
}

class FileSystemModel implements TreeModel {
    private File root;

    private Vector listeners = new Vector();

    public FileSystemModel(File rootDirectory) {
        root = rootDirectory;
    }

    public Object getRoot() {
        return root;
    }

    public Object getChild(Object parent, int index) {
        File directory = (File) parent;
        String[] children = directory.list();
        return new TreeFile(directory, children[index]);
    }

    public int getChildCount(Object parent) {
        File file = (File) parent;
        if (file.isDirectory()) {
            String[] fileList = file.list();
            if (fileList != null)
                return file.list().length;
        }
        return 0;
    }

    public boolean isLeaf(Object node) {
        File file = (File) node;
        return file.isFile();
    }

    public int getIndexOfChild(Object parent, Object child) {
        File directory = (File) parent;
        File file = (File) child;
        String[] children = directory.list();
        for (int i = 0; i < children.length; i++) {
            if (file.getName().equals(children[i])) {
                return i;
            }
        }
        return -1;

    }

    public void valueForPathChanged(TreePath path, Object value) {
        File oldFile = (File) path.getLastPathComponent();
        String fileParentPath = oldFile.getParent();
        String newFileName = (String) value;
        File targetFile = new File(fileParentPath, newFileName);
        oldFile.renameTo(targetFile);
        File parent = new File(fileParentPath);
        int[] changedChildrenIndices = { getIndexOfChild(parent, targetFile) };
        Object[] changedChildren = { targetFile };
        fireTreeNodesChanged(path.getParentPath(), changedChildrenIndices, changedChildren);

    }

    private void fireTreeNodesChanged(TreePath parentPath, int[] indices, Object[] children) {
        TreeModelEvent event = new TreeModelEvent(this, parentPath, indices, children);
        Iterator iterator = listeners.iterator();
        TreeModelListener listener = null;
        while (iterator.hasNext()) {
            listener = (TreeModelListener) iterator.next();
            listener.treeNodesChanged(event);
        }
    }

    public void addTreeModelListener(TreeModelListener listener) {
        listeners.add(listener);
    }

    public void removeTreeModelListener(TreeModelListener listener) {
        listeners.remove(listener);
    }

    private class TreeFile extends File {
        public TreeFile(File parent, String child) {
            super(parent, child);
        }

        public String toString() {
            return getName();
        }
    }
}

