//https://docs.oracle.com/javase/tutorial/uiswing/examples/components/TreeDemoProject/src/components/TreeDemo.java

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class ProjectTree extends JPanel implements TreeSelectionListener {

    private String osName = System.getProperty("os.name");
    private JTree tree;
    private DefaultMutableTreeNode top;
    private DefaultTreeModel treeModel;
    //Optionally set the look and feel.
    private static boolean useSystemLookAndFeel = false;

    private String ProjectName;
    private String ProjectPath;
    private CodeEditorFrame theCodeEditorFrame;
    private JTabbedPane tabbedPane = null;

    public ProjectTree() {
        super(new GridLayout(1, 0));

    }
    public void createProjectTree(CodeEditorFrame theMainFrame, String theProjectName, String theProjectPath){
        ProjectName = theProjectName;
        ProjectPath = theProjectPath;
        theCodeEditorFrame = theMainFrame;

        // Creates the nodes from the directory's path
        top = new DefaultMutableTreeNode(ProjectName);
        createNodes(top);

        treeModel = new DefaultTreeModel(top);

        // Creates a tree that allows one selection at a time.
        tree = new JTree(treeModel);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Listen for when the selection changes.
        tree.addTreeSelectionListener(this);

        //Create the scroll pane and add the tree to it.
        JScrollPane treeView = new JScrollPane(tree);

        //Add the tree view to this panel.
        add(treeView);
        //tree.setScrollsOnExpand(false);

        // Show Popup on Tree
        final TreePopup treePopup = new TreePopup(tree);
        tree.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3) {
                    treePopup.show(e.getComponent(), e.getX(), e.getY());
                    //System.out.println(e.getComponent());
                }
            }
        });
    }

    public void openProjectTree(CodeEditorFrame theMainFrame, String theProjectPath, String theProjectName){
        ProjectPath = theProjectPath;
        ProjectName = theProjectName;
        theCodeEditorFrame = theMainFrame;

        // Root of the tree of the Project's path
        String wholeProjectPath = ProjectPath + '/' + ProjectName;
        File fileRoot = new File(wholeProjectPath);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new FileNode(fileRoot));

        // Creates the children nodes, doesn't matter if its a file or a folder
        ChildNode ccn = new ChildNode(fileRoot, root);
        new Thread(ccn).start();

        treeModel = new DefaultTreeModel(root);

        // Creates a tree that allows one selection at a time.
        tree = new JTree(treeModel);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Listen for when the selection changes.
        tree.addTreeSelectionListener(this);

        //Create the scroll pane and add the tree to it.
        JScrollPane treeView = new JScrollPane(tree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JScrollBar bar = treeView.getVerticalScrollBar();
        bar.setPreferredSize(new Dimension(50, 0));

        //Add the tree view to this panel.
        add(treeView);
        tree.setScrollsOnExpand(false);

        // Show Popup on Tree
        final TreePopup treePopup = new TreePopup(tree);
        tree.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3) {
                    treePopup.show(e.getComponent(), e.getX(), e.getY());
                    //System.out.println(e.getComponent());
                }
            }
        });
    }

    public void closeProjectTree(){

        if(tabbedPane != null){
            tabbedPane.removeAll();
            theCodeEditorFrame.remove(tabbedPane);
        }
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        root.removeAllChildren();
        model.reload();
        model.setRoot(null);
    }

    // Required by TreeSelectionListener interface.
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                tree.getLastSelectedPathComponent();

        if (node == null) return;

        if (node.isLeaf()) {
            System.out.println("Node is a leaf");
        }
    }

    // Creates Nodes for Tree
    private void createNodes(DefaultMutableTreeNode top) {
        DefaultMutableTreeNode folder;
        DefaultMutableTreeNode file;

        folder = new DefaultMutableTreeNode("src");
        top.add(folder);

        //original Tutorial
        file = new DefaultMutableTreeNode("Main.java");
        folder.add(file);

    }

    // Get tab info in JTabbedPane
    public int getTabCounts(){
        return tabbedPane.getTabCount();
    }

    public String getTabTitle(int num){
        return tabbedPane.getTitleAt(num);
    }

    public JTabbedPane getTabbedPane(){
        return tabbedPane;
    }

    // Popup Specifics
    class TreePopup extends JPopupMenu {
        public TreePopup(JTree tree1) {

            JMenuItem delete = new JMenuItem("Delete File");
            JMenuItem add = new JMenuItem("Add File");
            JMenuItem open = new JMenuItem("Open File");
            JMenuItem close = new JMenuItem("Close File");


            delete.addActionListener(ae -> {
                System.out.println("Delete child");
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
                DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                model.removeNodeFromParent(selectedNode);
            });
            add(delete);
            add(new JSeparator());

            add.addActionListener(ae -> {
                System.out.println("Add child");
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
                DefaultMutableTreeNode tempRoot = selectedNode.getPreviousNode();
                DefaultMutableTreeNode root = (DefaultMutableTreeNode) selectedNode.getRoot();

                if(selectedNode.isLeaf()){

                } else {
                    String input = JOptionPane.showInputDialog("New File Name");

                    if (input != null) {
                        if (!input.contains(".java")){
                            input = input + ".java";
                        }
                        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                        DefaultMutableTreeNode folder = null;
                        selectedNode.add(new DefaultMutableTreeNode(input));

                        File newClass = new File(ProjectPath + '/' + ProjectName + '/' + "src" + '/' + input);

                        try {
                            FileWriter fw = new FileWriter(newClass, true);
                            PrintWriter pw = new PrintWriter(fw);
                            pw.println("// This is the " + input + " file.");
                            pw.close();
                        } catch (IOException e) {
                            System.err.println("Problem writing to the file.");
                        }
                        model.reload();
                    }
                }
            });
            add(add);
            add(new JSeparator());


            open.addActionListener(e -> {
                TreePath tp = tree.getSelectionPath();
                Object filePathToAdd = tp.getLastPathComponent();
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) filePathToAdd;

                if(selectedNode.isLeaf()) {
                    String path = tp.toString();
                    String diskPath = "";
                    if(osName.toUpperCase().contains("WIN"))
                        diskPath = ProjectPath + "\\" +
                            path.replace("[", "").replace("]","").replace(", ", File.separator);
                    else if(osName.toUpperCase().contains("MAC"))
                        diskPath = ProjectPath + "/" +
                                path.replaceAll("\\]| |\\[|", "").replaceAll(",", File.separator);
                    System.out.println(diskPath);
                    String fileName = selectedNode.getUserObject().toString();
                    File newFile = new File(diskPath);

                    if (tabbedPane == null) {

                        tabbedPane = new JTabbedPane();

                        theCodeEditorFrame.theCodeField = new CodeField(ProjectName, ProjectPath, fileName);

                        JScrollPane scrollableCodePane = new JScrollPane(theCodeEditorFrame.theCodeField, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                        if (newFile != null) {
                            try {
                                theCodeEditorFrame.theCodeField.setPage(newFile.toURI().toURL());
                            } catch (IOException e1) {
                                System.err.println("Attempted to read a bad file " + newFile );
                                e1.printStackTrace();
                            }
                        } else {
                            System.err.println("Couldn't find file");
                        }
                        tabbedPane.addTab(selectedNode.getUserObject().toString(), scrollableCodePane);
                        theCodeEditorFrame.add(tabbedPane,  BorderLayout.CENTER);
                        tabbedPane.setVisible(false);
                        tabbedPane.setVisible(true);
                    } else {
                        theCodeEditorFrame.theCodeField = new CodeField(ProjectName, ProjectPath, fileName);
                        JScrollPane scrollableCodeField = new JScrollPane(theCodeEditorFrame.theCodeField, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                        scrollableCodeField.setBounds(5,5,300,200);

                        if (newFile != null) {
                            try {
                                theCodeEditorFrame.theCodeField.setPage(newFile.toURI().toURL());
                            } catch (IOException e1) {
                                System.err.println("Attempted to read a bad file " + newFile );
                                e1.printStackTrace();
                            }
                        } else {
                            System.err.println("Couldn't find file");
                        }
                        tabbedPane.addTab(selectedNode.getUserObject().toString(), scrollableCodeField);
                        theCodeEditorFrame.add(tabbedPane,  BorderLayout.CENTER);
                        tabbedPane.setVisible(false);
                        tabbedPane.setVisible(true);

                    }
                }
            });
            add(open);
            add(new JSeparator());

            close.addActionListener(e -> {

                TreePath tp = tree.getSelectionPath();
                Object filePathToAdd = tp.getLastPathComponent();
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) filePathToAdd;

                JFrame CloseProjectFrame = new JFrame("Close File");
                CloseProjectFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                CloseProjectFrame.setSize(600,150);
                CloseProjectFrame.setLocation(550, 300);

                JPanel main = new JPanel();
                CloseProjectFrame.add(main);

                main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));
                JPanel message = new JPanel(new BorderLayout());

                main.add(message);

                JLabel confirmClose = new JLabel("Are you sure you want to close this file? Don't forget to save before closing using 'Ctrl + S'.", SwingConstants.CENTER);
                message.add(confirmClose, BorderLayout.CENTER);

                JPanel buttons = new JPanel(new FlowLayout());
                main.add(buttons);

                JButton buttonclose = new JButton("Close");
                JButton buttoncancel = new JButton("Cancel");

                buttons.add(buttonclose);
                buttons.add(buttoncancel);


                //if yes, close project
                buttonclose.addActionListener(e13 -> {
                    //close file
                    if(selectedNode.isLeaf()){

                        tabbedPane.remove(tabbedPane.getSelectedComponent());
                    }
                    CloseProjectFrame.dispose();
                });

                //if cancel, end close operation
                buttoncancel.addActionListener(e12 -> CloseProjectFrame.dispose());

                CloseProjectFrame.setVisible(true);

            });
            add(close);
        }
    }

}