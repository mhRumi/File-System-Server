import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.*;
import java.awt.*;
import java.io.File;

public class Main extends JFrame {


    static DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("./");

    static public   DefaultTreeModel m_model = new DefaultTreeModel(rootNode);
    private FileSystemModel fileSystemModel;
    public static JTree m_tree = new JTree(m_model);
    public static final JPopupMenu popupmenu = new JPopupMenu("Edit");
    JPanel jPanelButton;
    Listener listener = new Listener();
    MyMouseListener mouseListener = new MyMouseListener();


    public Main(String directory) {

        fileSystemModel = new FileSystemModel(new File(directory));
        addingToNode("./Uploads", rootNode);

        m_tree.setEditable(true);
        m_tree.setSelectionRow(0);
        m_tree.setRowHeight(25);

        m_tree.addMouseListener(mouseListener.getMyMouseListener());

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
        create.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.gray),
                new EmptyBorder(new Insets(15, 25, 15, 25))));
        delete.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.gray),
                new EmptyBorder(new Insets(15, 25, 15, 25))));
        create.addActionListener(new Listener());
        delete.addActionListener(new Listener());

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


    public static void main(String[] arg) {
        new Main("./");
        Server server = new Server();
        server.start();;
    }

}


