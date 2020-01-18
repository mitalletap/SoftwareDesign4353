import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.awt.BorderLayout.WEST;


public class CodeEditorFrame extends JFrame implements ActionListener {


    private String osName = System.getProperty("os.name");
    private String ProjectName;
    private String ProjectPath;
    private String outputPath;
    private JTextPane ExecutionPane;
    private JLabel wordLabel;
    private ProjectTree theProjectTree;
    public CodeField theCodeField;

    public CodeEditorFrame() {
        // Create Frame
        super("Code Editor");
        setSize(1500, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        createMenu();
    }

    public void createMenu() {
        try {
            // Set look and feel
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

            // Set theme to ocean
            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
        } catch (Exception e) {

        }
        // Statistic Panel
        JPanel StatPanel = new JPanel();
        this.add(StatPanel, BorderLayout.EAST);
        StatPanel.setBackground(Color.WHITE);
        StatPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.ORANGE));
        StatPanel.setPreferredSize(new Dimension(400, 800));
        wordLabel = new JLabel("Key words: 0", JLabel.LEFT);
        StatPanel.add(wordLabel);
        wordLabel.setBounds(50, 25, 100, 30);


        // Code Executing Panel
        JPanel ExecPanel = new JPanel();
        ExecPanel.setLayout(new BorderLayout());
        this.add(ExecPanel, BorderLayout.SOUTH);
        ExecPanel.setBackground(Color.WHITE);
        ExecPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.lightGray));
        ExecPanel.setPreferredSize(new Dimension(1500, 200));
        ExecutionPane = new JTextPane();
        JScrollPane ExecutionScrollPane = new JScrollPane(ExecutionPane);
        ExecPanel.add(ExecutionScrollPane);

        // Menu Bar
        JMenuBar MenuBar = new JMenuBar();
        this.setJMenuBar(MenuBar);
        MenuBar.setPreferredSize(new Dimension(1500, 40));

        // File Menu
        JMenu FileMenu = new JMenu("File");

        // Menu Item New Project
        JMenuItem NewProject = new JMenuItem("New Project");
        FileMenu.add(NewProject);
        NewProject.addActionListener(this);

        // Menu Item Open Project
        JMenuItem OpenProject = new JMenuItem("Open Project");
        FileMenu.add(OpenProject);
        OpenProject.addActionListener(this);

        // Menu Item Save Project
        JMenuItem SaveProject = new JMenuItem("Save Project");
        FileMenu.add(SaveProject);
        SaveProject.addActionListener(this);

        // Menu Item Edit Project
        JMenuItem EditProject = new JMenuItem("Edit Project");
        FileMenu.add(EditProject);

        // Menu Item Close Project
        JMenuItem CloseProject = new JMenuItem("Close Project");
        FileMenu.add(CloseProject);
        CloseProject.addActionListener(this);

        // Menu Item Count Words
        JMenuItem CountWords = new JMenuItem("Count Words");
        FileMenu.add(CountWords);
        CountWords.addActionListener(e -> getWordCount(e));

        // Run Menu
        JMenu RunMenu = new JMenu("Run");
        JMenuItem CompileProject = new JMenuItem("Compile Project");
        RunMenu.add(CompileProject);
        CompileProject.addActionListener(this);

        JMenuItem ExecuteProject = new JMenuItem("Execute Project");
        RunMenu.add(ExecuteProject);
        ExecuteProject.addActionListener(this);

        MenuBar.add(FileMenu);
        MenuBar.add(RunMenu);
    }

    public void displayNewProject(String theProjectName, String theProjectPath) {

        ProjectName = theProjectName;
        ProjectPath = theProjectPath;

        // Create Project Tree
        theProjectTree = new ProjectTree();
        theProjectTree.createProjectTree(this, ProjectName, ProjectPath);
        this.add(theProjectTree, WEST);
        theProjectTree.setVisible(false);
        theProjectTree.setVisible(true);
    }

    public void newProject() {
        // create frame using NewProjectFrame class
        NewProjectFrame theNewProjectFrame = new NewProjectFrame(this);
        theNewProjectFrame.setVisible(true);
    }

    public void openProject() {
        // create frame
        JFrame openProjectFrame = new JFrame("Open Project");

        // Create the File Chooser
        JFileChooser theFileChooser = new JFileChooser("f");

        // Opens only Directories
        theFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // Invoke the showsOpenDialog function to show the save dialog
        int option = theFileChooser.showOpenDialog(this);

        // Opens the file the user selects if the file is acceptable
        if (option == JFileChooser.APPROVE_OPTION) {
            // Gets the selected directory
            File theDirectory = theFileChooser.getSelectedFile();

            String wholeProjectPath = theDirectory.getPath();
            Path path = Paths.get(wholeProjectPath);
            ProjectName = path.getFileName().toString();
            ProjectPath = theDirectory.getParent();

            // Create Project Tree
            theProjectTree = new ProjectTree();
            theProjectTree.openProjectTree(this, ProjectPath, ProjectName);
            this.add(theProjectTree, WEST);
            theProjectTree.setVisible(false);
            theProjectTree.setVisible(true);
        }
        // If the user cancelled the operation
        else
            JOptionPane.showMessageDialog(openProjectFrame, "the user cancelled the operation");

    }

    // Save all files in the project
    public void saveProject() {

        int tabCount = theProjectTree.getTabCounts();
        for (int i = 0; i < tabCount; i++) {
            String fileName = theProjectTree.getTabTitle(i);
            System.out.println(fileName);
            try {
                String filePath = ProjectPath + "/" + ProjectName + "/src/" + fileName;
                FileWriter out = new FileWriter(filePath);
                System.out.println(filePath);
                theCodeField = (CodeField) theProjectTree.getTabbedPane().getComponent(i);
                out.write(theCodeField.getText());
                out.close();
            } catch (Exception f) {
                f.printStackTrace();
            }

        }

    }

    public void compileProject() {

        com.sun.tools.javac.Main javac = new com.sun.tools.javac.Main();

        // create "out" folder
        File outputFolder = new File(ProjectPath + '/' + ProjectName + '/' + "out");
        outputFolder.mkdir();
        outputPath = ProjectPath + '/' + ProjectName + '/' + "out";

        // get all java file names in the "src" folder
        File folder = new File(ProjectPath + '/' + ProjectName + '/' + "src");
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> listOfJavaFiles = new ArrayList<String>();

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                String fileName = listOfFile.getName();

                if (fileName.length() > 5) {
                    // Substring containing last 5 characters
                    String lastFiveDigits = fileName.substring(fileName.length() - 5);

                    if (lastFiveDigits.equals(".java")) {
                        listOfJavaFiles.add(fileName);

                    }
                }
            }
        }

        //        String fileToCompile = ProjectPath + '/' + ProjectName + "/src/Main.java";
        String[] args = new String[listOfJavaFiles.size() + 2];
        args[0] = "-d";
        args[1] = outputPath;
        for (int i = 0; i < listOfJavaFiles.size(); i++) {
            args[i + 2] = ProjectPath + '/' + ProjectName + "/src/" + listOfJavaFiles.get(i);
        }
        int status = javac.compile(args);
        System.out.println(status);
    }

    public void executeProject() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException, IOException, InterruptedException, BadLocationException {

        Runtime rt = Runtime.getRuntime();
        rt.traceMethodCalls(true);
        compileProject();

        String mainClass = "";

        File outFolder = new File(outputPath);
        File[] listOfFiles = outFolder.listFiles();
        String className;
        String classesLoaded = "";
        String methodsCalled = "";

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                String fileName = listOfFile.getName();
                if (fileName.substring(fileName.length() - 6).equals(".class")) {
                    className = fileName.substring(0, fileName.length() - 6);
                    classesLoaded = classesLoaded + "\n  " + className;
                    Process find = null;
                    if (osName.toUpperCase().contains("WIN")) {
                        find = Runtime.getRuntime().exec("javap -p -cp " + outputPath + ";. " + className);
                        find.waitFor();
                        BufferedReader reader2 = new BufferedReader(new InputStreamReader(find.getInputStream()));
                        String line;
                        while ((line = reader2.readLine()) != null) {
                            if (line.contains("(") && line.contains(")")) {
                                methodsCalled = methodsCalled + "\n" + line;
                            }
                            if (line.length() > 25) {
                                if (line.substring(0, 25).equals("  public static void main"))
                                    mainClass = className;
                            }
                        }
                    } else if (osName.toUpperCase().contains("MAC")) {
                        find = Runtime.getRuntime().exec("javap -cp " + outputPath + " " + className);
                        find.waitFor();
                        BufferedReader reader2 = new BufferedReader(new InputStreamReader(find.getInputStream()));
                        String line;
                        while ((line = reader2.readLine()) != null) {
                            if (line.contains("(") && line.contains(")")) {
                                methodsCalled = methodsCalled + "\n" + line;
                            }
                            if (line.length() > 25) {
                                if (line.substring(0, 25).equals("  public static void main"))
                                    mainClass = className;
                            }
                        }
                    }


                }
            }
        }
        if (!mainClass.equals("")) {
            ExecutionPane.setText("");


            StyledDocument doc = ExecutionPane.getStyledDocument();
            SimpleAttributeSet att = new SimpleAttributeSet();
            StyleConstants.setAlignment(att, StyleConstants.ALIGN_LEFT);
            ExecutionPane.setParagraphAttributes(att, true);

            //can use javap -c for finding names of methods called during execution
            Process p = null;
            int statusCode = -1;
            if (osName.toUpperCase().contains("WIN")) {
                p = rt.exec("java -cp " + outputPath + ";. " + mainClass);
                statusCode = p.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    doc.insertString(doc.getLength(), line + "\n", null);
                }
            } else if (osName.toUpperCase().contains("MAC")) {
                p = rt.exec("java -cp " + outputPath + " " + mainClass);
                statusCode = p.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    doc.insertString(doc.getLength(), line + "\n", null);
                }

                doc.insertString(doc.getLength(), "\nProcess finished with exit code " + statusCode + "\n", null);
                doc.insertString(doc.getLength(), "\nExecution Information:\n", null);
                doc.insertString(doc.getLength(), "\nClasses: " + classesLoaded + "\n", null);
                doc.insertString(doc.getLength(), "\nMethods: " + methodsCalled + "\n", null);

                // Find classes loaded in memory
//                doc.insertString(doc.getLength(), "\nClasses loaded in memory: \n", null);
                PrintWriter output = null;
                try {
                    output = new PrintWriter(new FileOutputStream(ProjectPath + "/" + ProjectName + "/classes_loaded_in_memory.txt"));
                } catch (FileNotFoundException e) {
                    System.out.println("Problem opening files.");
                    System.exit(0);
                }
                System.out.println(outputPath);
                Process p2 = rt.exec("java -verbose:class -classpath " + outputPath + " " + mainClass);
                statusCode = p2.waitFor();
                BufferedReader reader3 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
                String line1;
//                System.out.println("Classed loaded in mem: ");
                while ((line1 = reader3.readLine()) != null) {
//                    doc.insertString(doc.getLength(), line1 + "\n", null);
                    output.println(line1);
                }

                output.close();
            }
        }
    }

    public void closeProject() {
        // Write your code for Close Project functionality here

        // Open new frame to confirm closing of project
        JFrame CloseProjectFrame = new JFrame("Close Project");
        CloseProjectFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CloseProjectFrame.setSize(300, 150);
        CloseProjectFrame.setLocation(550, 300);

        JPanel main = new JPanel();
        CloseProjectFrame.add(main);

        main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));
        JPanel message = new JPanel(new BorderLayout());

        main.add(message);

        JLabel confirmClose = new JLabel("Are you sure you want to close your project?", SwingConstants.CENTER);
        message.add(confirmClose, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout());
        main.add(buttons);

        JButton buttonsc = new JButton("Save & Close");
        JButton buttonclose = new JButton("Close");
        JButton buttoncancel = new JButton("Cancel");

        buttons.add(buttonsc);
        buttons.add(buttonclose);
        buttons.add(buttoncancel);

        // If save & close, save the project and then close it
        buttonsc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProject();
                //close project
                theProjectTree.closeProjectTree();

                CloseProjectFrame.dispose();
            }
        });

        // If yes, close project
        buttonclose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //close project
                if (theProjectTree != null) {
                    theProjectTree.closeProjectTree();
                    removeProjectTree();
                    theProjectTree = null;
                    ProjectName = null;
                    ProjectPath = null;
                }
                CloseProjectFrame.dispose();

            }
        });

        // If cancel, end close operation
        buttoncancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CloseProjectFrame.dispose();
            }
        });

        CloseProjectFrame.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
        String buttonString = e.getActionCommand();

        if (buttonString.equals("New Project")) {
            newProject();
        } else if (buttonString.equals("Open Project")) {
            openProject();
        } else if (buttonString.equals("Save Project")) {
            saveProject();
        } else if (buttonString.equals("Close Project")) {
            closeProject();
        } else if (buttonString.equals("Compile Project")) {
            compileProject();
        } else if (buttonString.equals("Execute Project")) {
            try {
                executeProject();
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (NoSuchMethodException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void rightClickPerformed() {
        JPopupMenu rMenu = new JPopupMenu();
        JMenuItem delete = new JMenuItem("Delete");

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    rMenu.show(e.getComponent(), e.getX(), e.getY());
                    System.out.println("Right Click, Activated");
                }
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("This item has been deleted!");
            }
        });
        rMenu.add(delete);
    }

    public String getProjectName() {
        return ProjectName;
    }

    public String getProjectPath() {
        return ProjectPath;
    }

    public void removeProjectTree() {
        if (theProjectTree != null) this.remove(theProjectTree);
    }

    public void getWordCount(ActionEvent event) {

        //As long as the text area is filled, it will run correctly
        if (theCodeField != null) {

            String text = theCodeField.getText();
            int forCount = 0, whileCount = 0, ifCount = 0, elseCount = 0;
            Pattern forPattern = Pattern.compile("for");
            Pattern whilePattern = Pattern.compile("while");
            Pattern ifPattern = Pattern.compile("if");
            Pattern elsePattern = Pattern.compile("else");
            Matcher forMatcher = forPattern.matcher(text);
            Matcher whileMatcher = whilePattern.matcher(text);
            Matcher ifMatcher = ifPattern.matcher(text);
            Matcher elseMatcher = elsePattern.matcher(text);

            //Each line of the code is read as an array
            while (forMatcher.find()) {
                forCount++;
            }
            while (whileMatcher.find()) {
                whileCount++;
            }
            while (ifMatcher.find()) {
                ifCount++;
            }
            while (elseMatcher.find()) {
                elseCount++;
            }

            String message = ("Amount of for: " + forCount + "\n Amount of while: " + whileCount + "\n Amount of if: " + ifCount + "\n Amount of else: " + elseCount);
            wordLabel.setText(message);
        }
    }
}