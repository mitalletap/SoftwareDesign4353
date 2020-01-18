import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import java.nio.file.spi.FileTypeDetector;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class NewProjectFrame extends JFrame implements ActionListener {

    private String ProjectName;
    private String ProjectPath;
    private CodeEditorFrame ParentFrame;


    public NewProjectFrame(CodeEditorFrame theParentFrame) {
        // Create Frame
        super("New Project");
        ParentFrame = theParentFrame;

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.getContentPane().setBackground(Color.WHITE);
        JPanel InnerPanel = new JPanel();
        this.add(InnerPanel, BorderLayout.WEST);
        InnerPanel.setBackground(Color.WHITE);
        InnerPanel.setPreferredSize(new Dimension(800, 400));
        BoxLayout boxLayout = new BoxLayout(InnerPanel, BoxLayout.Y_AXIS);
        InnerPanel.setLayout(boxLayout);
        InnerPanel.add(Box.createRigidArea(new Dimension(10, 40)));

        // Project Name Label
        JLabel ProjectNameLabel = new JLabel("Project Name: ");
        InnerPanel.add(ProjectNameLabel, BorderLayout.WEST);
        InnerPanel.add(Box.createRigidArea(new Dimension(10, 10)));

        // Inner Panel for Project Name Field
        JPanel InnerPanel1 = new JPanel();
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        InnerPanel1.setLayout(flowLayout);
        InnerPanel1.setBackground(Color.WHITE);
        InnerPanel1.setPreferredSize(new Dimension(300, 65));
        InnerPanel.add(InnerPanel1, BorderLayout.WEST);
        InnerPanel1.setAlignmentX(LEFT_ALIGNMENT);

        // Project Name Field
        JTextField textField1 = new JTextField(20);
        textField1.setAlignmentX(LEFT_ALIGNMENT);

        InnerPanel1.add(textField1, BorderLayout.WEST);
        InnerPanel.add(Box.createRigidArea(new Dimension(10, 40)));

        // Location Label
        JLabel LocationLabel = new JLabel("Project Location: ");
        InnerPanel.add(LocationLabel, BorderLayout.WEST);
        InnerPanel.add(Box.createRigidArea(new Dimension(10, 10)));

        // Choose Location button
        JButton LocationButton = new JButton("Choose Project Location");
        InnerPanel.add(LocationButton);
        InnerPanel.add(Box.createRigidArea(new Dimension(10, 10)));

        // Project Location Text Label
        JLabel DestinationLabel = new JLabel(" ");
        InnerPanel.add(DestinationLabel);
        InnerPanel.add(Box.createRigidArea(new Dimension(10, 40)));

        // Section for "Choose Project Location" button
        LocationButton.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose Project Location");

            // Set the selection mode to directories only
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int option = fileChooser.showOpenDialog(null);
            if(option == JFileChooser.APPROVE_OPTION){
                File file = fileChooser.getSelectedFile();
                ProjectPath = file.getPath();
                DestinationLabel.setText(ProjectPath);
            }
        });

        InnerPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        InnerPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        InnerPanel.add(Box.createRigidArea(new Dimension(10, 220)));

        JButton button1 = new JButton("Submit");
        InnerPanel.add(button1, BorderLayout.SOUTH);
        InnerPanel.add(Box.createRigidArea(new Dimension(10, 280)));
        button1.addActionListener(actionEvent -> {
            ProjectName = textField1.getText();

            File newProjectFolder = new File(ProjectPath + '/' + ProjectName);
            File srcFolder = new File(ProjectPath + '/' + ProjectName + '/' + "src");
            File main = new File(ProjectPath + '/' + ProjectName + '/' + "src" + '/' + "Main.java");
            PrintWriter pw;

            try {
                if (newProjectFolder.mkdir()) {
                    FileWriter fw = new FileWriter(main, true);
                    pw = new PrintWriter(fw);
                    pw.println("// This is the Main class.");
                    pw.close();

                    ParentFrame.displayNewProject(ProjectName, ProjectPath);

                    setVisible(false);

                    dispose();

                } else {
                    JFrame openProjectFrame;
                    openProjectFrame = new JFrame("Folder Exists");
                    JOptionPane.showMessageDialog(openProjectFrame, "Folder Name Already Exists.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }

    public void actionPerformed(ActionEvent e) {
        String buttonString = e.getActionCommand();
    }


}
