import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.*;
import java.awt.*;
import java.io.File;

public class Main extends JFrame {


    static DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("./");

    static public   DefaultTreeModel m_model = new DefaultTreeModel(rootNode);
    static private FileSystemModel fileSystemModel;
    public static JTree m_tree = new JTree(m_model);
    public static final JPopupMenu popupmenu = new JPopupMenu("Edit");
    JPanel jPanelButton;
    Listener listener = new Listener();
    public static MyMouseListener mouseListener = new MyMouseListener();
    public static Main main;
    public static JScrollPane scrollPane;
    static JDialog dialog;
    static Server server;


    public Main(String directory) {
         dialog = new JDialog(this,"Alert", Dialog.ModalityType.APPLICATION_MODAL);
        nodeInit(directory);

        jPanelButton= new JPanel();
        jPanelButton.setLayout(new FlowLayout(10, 100, 10));
        jPanelButton.setPreferredSize(new Dimension(50, 70));
        JLabel jLabel = new JLabel("File System Server");
        Font fieldFont = new Font("Arial", Font.BOLD, 20);
        jLabel.setFont(fieldFont);
        jLabel.setBackground(Color.white);
        jLabel.setForeground(Color.black.brighter());
        jLabel.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.darkGray),
                new EmptyBorder(new Insets(15, 25, 15, 25))));
        jPanelButton.add(jLabel);
        jPanelButton.add(Box.createVerticalStrut(10));
        jPanelButton.add(jLabel);
        getContentPane().add(jPanelButton, BorderLayout.NORTH);

        JLabel waiting = new JLabel("listening");
        waiting.setBackground(Color.white);
        waiting.setForeground(Color.black);
        Font wt = new Font("Arial", Font.PLAIN, 20);
        waiting.setFont(wt);
        waiting.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.BLACK),
                new EmptyBorder(new Insets(15, 25, 15, 25))));



        JPanel jPanel = new JPanel();
        jPanel.setPreferredSize(new Dimension(150, 50));
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
        jPanel.add(waiting);
        getContentPane().add(jPanel, BorderLayout.WEST);

        setSize(500, 700);
        setVisible(true);

    }

    public static void closeScreen()
    {
        main.dispose();;
    }



    public void nodeInit(String directory)
    {
        fileSystemModel = new FileSystemModel(new File(directory));

        m_tree.setEditable(true);
        m_tree.setSelectionRow(0);
        m_tree.setRowHeight(25);
        m_tree.getCellEditor().addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                String filename = (String)  m_tree.getCellEditor().getCellEditorValue();
                String fileExtension = Server.getFileExtension(filename);

                Listener.createFile(Listener.directory+filename, fileExtension);
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
            }
        });


        m_tree.addMouseListener(mouseListener.getMyMouseListener());

         scrollPane = new JScrollPane(m_tree);

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        addingToNode("./Uploads", rootNode);
    }



    public  void addingToNode(String directory, DefaultMutableTreeNode rootNode)
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

    public void addingSingleNode(String directory)
    {
        File file = new File(directory);
        System.out.println("Adding file name "+ file.getName());
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(file.getName());
        Main.m_model.insertNodeInto(newNode, rootNode, rootNode.getChildCount());
        //nodeInit("./");

    }

    public void deleteSingleNode(String fileName){
        int childCount = rootNode.getChildCount();
        for(int i=0; i<childCount; i++)
        {
            DefaultMutableTreeNode deleteNode = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            String node = deleteNode.toString();
            if(fileName.equalsIgnoreCase(node))
            {
                Listener.deleteNode(deleteNode);
                break;
            }
        }
    }


    public static void main(String[] arg) {
         main =  new Main("./");
         server = new Server();
        server.start();
    }

}


