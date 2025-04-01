//Swing imports
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

//Action Imports
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//File IO Imports
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;


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
    private String inputFileName;
    private String outputFileName;
    private JSON_Parser jparser;
    private ArrayList<JSON_Parser> jsonParserList;
    private PDF_Parser pdfParser;

    JFileChooser chooser;


    public MyFrame() {
        super();
        jsonParserList = new ArrayList<>();
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
        AddFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    addFilesToList();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        convertToJSON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addConvertedJSONFileToList ();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        convertToPDF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addConvertedPDFFileToList();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
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
        //JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
       // JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");

       // fileMenu.add(newItem);
        fileMenu.add(openItem);
        //fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addFilesToList ();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
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

    public void addFilesToList() throws FileNotFoundException {
        chooser = new JFileChooser();
        chooser.setDialogTitle("Select JSON files");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(true); // Enable multi-file selection

        int userSelection = chooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = chooser.getSelectedFiles(); // Get multiple selected files

            if (inputFileList.getModel() instanceof DefaultListModel) {
                DefaultListModel<String> model = (DefaultListModel<String>) inputFileList.getModel();
                for (File file : selectedFiles) {
                    model.addElement(file.getName()); // Add each file to the list
                    jparser = new JSON_Parser(file.getAbsolutePath()); // Process each file
                    jsonParserList.add(jparser);
                }
            }
            else {
                DefaultListModel<String> model = new DefaultListModel<>();
                for (File file : selectedFiles) {
                    model.addElement(file.getName());
                    jparser = new JSON_Parser(file.getAbsolutePath());
                    jsonParserList.add(jparser);
                }
                inputFileList.setModel(model);
            }
        }
    }

    public void addConvertedJSONFileToList() throws FileNotFoundException {
        if (!(inputFileList.getModel() instanceof DefaultListModel) || inputFileList.getSelectedValuesList().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select one or more files from the input list!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> selectedFilePaths = inputFileList.getSelectedValuesList();

        if (!(outputFileList.getModel() instanceof DefaultListModel)) {
            outputFileList.setModel(new DefaultListModel<>());
        }

        DefaultListModel<String> outputModel = (DefaultListModel<String>) outputFileList.getModel();

        for (String selectedFilePath : selectedFilePaths) {
            jparser = new JSON_Parser(selectedFilePath);
            try {
                jparser.writeSkillTree();
                outputModel.addElement(jparser.getOutputFileName()); // Store and display converted file path
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error converting file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void addConvertedPDFFileToList() throws FileNotFoundException {
        if (!(inputFileList.getModel() instanceof DefaultListModel) || inputFileList.getSelectedValuesList().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select one or more files from the input list!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> selectedFilePaths = inputFileList.getSelectedValuesList();

        if (!(outputFileList.getModel() instanceof DefaultListModel)) {
            outputFileList.setModel(new DefaultListModel<>());
        }

        DefaultListModel<String> outputModel = (DefaultListModel<String>) outputFileList.getModel();

        for (String selectedFilePath : selectedFilePaths) {
            jparser = new JSON_Parser(selectedFilePath);
            pdfParser = new PDF_Parser(jparser);
            pdfParser.writePDF();
            outputModel.addElement(pdfParser.getOutputPDF()); // Store and display converted file path
        }
    }


}
