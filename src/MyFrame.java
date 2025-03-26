import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyFrame extends JFrame {

    private JPanel mainPanel;
    private JSplitPane splitPane;
    private JScrollPane scrollLeftPane;
    private JScrollPane scrollRightPane;
    private JButton convertToJSON;
    private JButton convertToPDF;
    private JPanel utilityOptionsPanel;
    private JList inputFileList;
    private JList outputFileList;
    private JButton AddFile;
    private JPanel actionButtonPanel;
    private JMenu fileMenu;
    private JMenu previewMenu;
    private JMenu settingsMenu;


    public MyFrame() {
        super();
        init(); //calling the init on construction of the class.
    }

    private void init() {
        createMainPanel();
    }

    private void createMainPanel() {
        setTitle("Skill Tree Creator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setMenuBar();
    }

    private void setMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        settingsMenu = new JMenu("Settings");
        previewMenu = new JMenu("Preview");

        setFileMenu();
        setSettingsMenu();

        menuBar.add(fileMenu);
        menuBar.add(settingsMenu);
        menuBar.add(previewMenu);

        this.setJMenuBar(menuBar);
    }

    private void setFileMenu() {
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void setSettingsMenu() {
        JMenuItem preferencesItem = new JMenuItem("Preferences");
        settingsMenu.add(preferencesItem);
    }

}
