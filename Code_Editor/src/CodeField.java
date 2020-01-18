import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class CodeField extends JTextPane implements ActionListener, KeyListener {

    private String ProjectName;
    private String ProjectPath;
    private String FileName;

    public StyleContext context = new StyleContext();
    public Style style = context.addStyle("test", null);

    public CodeField(String theProjectName, String theProjectPath, String theFileName) {
        super();
        ProjectName = theProjectName;
        ProjectPath = theProjectPath;
        FileName = theFileName;

        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.lightGray));
        this.setPreferredSize(new Dimension(700, 1000));
        this.addKeyListener(this);
        // here add if u see the symbols then add red text

    }

    public void actionPerformed(ActionEvent e) {
        String buttonString = e.getActionCommand();
    }

    public void keyReleased(KeyEvent e) {

    }

    // Save Code by pressing Ctrl + S
    public void keyPressed(KeyEvent e) {
        if ((e.getKeyCode() == KeyEvent.VK_S) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
//        if ((e.getKeyCode() == KeyEvent.VK_F4)) {
            try {
                String filePath = ProjectPath + "/" + ProjectName + "/src/" + FileName;
                FileWriter out = new FileWriter(filePath);
                System.out.println(filePath);
                String text = this.getText();
                System.out.println(text);
                out.write(text);
                out.close();
            } catch (Exception f) {
                f.printStackTrace();
            }

        }

    }

    public void keyTyped(KeyEvent e) {

        String length = this.getText();
        int stringlength = length.length() - 1;

        try{
            // Highlights keywords blue
            if(this.getText().charAt(stringlength) == 'f' && this.getText().charAt(stringlength-1) == 'i') {
                System.out.println("if");
                StyleConstants.setForeground(style, Color.BLUE);
                this.getStyledDocument().setCharacterAttributes(stringlength - 2, stringlength, style, false);
                StyleConstants.setForeground(style, Color.BLUE);
                this.setForeground(Color.black);

            }

            if(this.getText().charAt(stringlength) == 'e' && this.getText().charAt(stringlength-1) == 's'&& this.getText().charAt(stringlength-2) == 'l'&& this.getText().charAt(stringlength-3) == 'e') {
                System.out.println("else");
                StyleConstants.setForeground(style, Color.BLUE);
                this.getStyledDocument().setCharacterAttributes(stringlength - 4, stringlength, style, false);
                StyleConstants.setForeground(style, Color.BLUE);
                this.setForeground(Color.black);

            }

            if(this.getText().charAt(stringlength) == 'r' && this.getText().charAt(stringlength-1) == 'o'&& this.getText().charAt(stringlength-2) == 'f') {
                System.out.println("for");
                StyleConstants.setForeground(style, Color.BLUE);
                this.getStyledDocument().setCharacterAttributes(stringlength - 3, stringlength, style, false);
                StyleConstants.setForeground(style, Color.BLUE);
                this.setForeground(Color.black);

            }
            if(this.getText().charAt(stringlength) == 'e' && this.getText().charAt(stringlength-1) == 'l'&& this.getText().charAt(stringlength-2) == 'i'&& this.getText().charAt(stringlength-3) == 'h'&& this.getText().charAt(stringlength-4) == 'w') {

                System.out.println("while");
                StyleConstants.setForeground(style, Color.BLUE);
                this.getStyledDocument().setCharacterAttributes(stringlength - 5, stringlength, style, false);
                StyleConstants.setForeground(style, Color.BLUE);
                this.setForeground(Color.black);

            }

            // Highlights operators red
            if (this.getText().charAt(stringlength) == '+' || this.getText().charAt(stringlength) == '-' ||
                    this.getText().charAt(stringlength) == '*' || this.getText().charAt(stringlength) == '/' ||
                    (this.getText().charAt(stringlength) == '|' && this.getText().charAt(stringlength - 1) == '|') ||
                    (this.getText().charAt(stringlength) == '&' && this.getText().charAt(stringlength - 1) == '&')) {

                if ((this.getText().charAt(stringlength) == '|' && this.getText().charAt(stringlength - 1) == '|')) {
                    StyleConstants.setForeground(style, Color.red);
                    this.getStyledDocument().setCharacterAttributes(stringlength - 1, stringlength, style, false);
                    StyleConstants.setForeground(style, Color.red);
                    this.setForeground(Color.black);
                }
                if ((this.getText().charAt(stringlength) == '&' && this.getText().charAt(stringlength - 1) == '&')) {
                    StyleConstants.setForeground(style, Color.red);
                    this.getStyledDocument().setCharacterAttributes(stringlength - 1, stringlength, style, false);
                    StyleConstants.setForeground(style, Color.red);
                    this.setForeground(Color.black);
                }
                StyleConstants.setForeground(style, Color.red);
                this.getStyledDocument().setCharacterAttributes(stringlength, stringlength, style, false);
                StyleConstants.setForeground(style, Color.red);
                this.setForeground(Color.black);
                StyleConstants.setForeground(style, Color.black);
            }

        }catch (Exception ex){

        }

    }

}