
import javax.swing.*;

public class MyFrame extends JFrame{

    private JPanel panel1;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JSplitPane splitPane;
    private JScrollPane scrollLeftPane;
    private JScrollPane scrollRightPane;
    private JButton cancelButton;
    private JButton saveButton;
    private JPanel utilityOptionsPanel;


    public MyFrame()
    {
        super();
        init(); //calling the init on construction of the class.
    }
    private void init()
    {
        setTitle("Skill Tree Creator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel1);
        pack();
        setLocationRelativeTo(null);

    }

}
