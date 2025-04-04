//Swing imports
import javax.swing.*;

//Action Imports

//File IO Imports
import java.io.*;

import java.util.List;


public class MyFrame extends JFrame {

    private JPanel mainPanel;
    private JSplitPane splitPane;
    private JScrollPane scrollLeftPane;
    private JScrollPane scrollRightPane;
    private JButton convertToJSON;
    private JButton convertToPDF;
    private JPanel utilityOptionsPanel;
    private JList<String> inputFileList;
    private JList<String> outputFileList;
    private JButton AddFile;
    private JPanel actionButtonPanel;
    private JMenu fileMenu;
    private JMenu previewMenu;
    private JMenu settingsMenu;
    private JSON_Parser jsonParser;
    JFileChooser chooser;
    private JTextArea previewTextArea;
    private JButton previewButton;
    private JScrollPane jScrollPane;

    public MyFrame() { super(); init(); }

    private void init() { createMainPanel(); }

    private void createMainPanel() {
        setTitle("Skill Tree Creator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
        previewTextArea.setEditable(false);
        setMenuBar();
        AddFile.addActionListener(_ -> {
            try {
                addFilesToList();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        convertToJSON.addActionListener(_ -> {
            try {
                addConvertedJSONFileToList ();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        convertToPDF.addActionListener(_ -> {
            try {
                addConvertedPDFFileToList();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        previewButton.addActionListener(_ -> updatePreview());
    }

    private void setMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        settingsMenu = new JMenu("Settings");

        setFileMenu();
        setSettingsMenu();

        menuBar.add(fileMenu);
        menuBar.add(settingsMenu);

        this.setJMenuBar(menuBar);
    }

    private void setFileMenu() {
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.add(openItem);
        fileMenu.add(exitItem);
        openItem.addActionListener(_ -> {
            try {
                addFilesToList ();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        exitItem.addActionListener(_ -> System.exit(0));
    }

    private void setSettingsMenu() {
        JMenuItem preferencesItem = new JMenuItem("Preferences");
        settingsMenu.add(preferencesItem);
    }

    private void addFilesToList() throws FileNotFoundException {
        chooser = new JFileChooser();
        chooser.setDialogTitle("Select JSON files");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(true); // Enable multi-file selection

        int userSelection = chooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = chooser.getSelectedFiles(); // Get multiple selected files

            if (inputFileList.getModel() instanceof DefaultListModel<String> model) {
                for (File file : selectedFiles) {
                    model.addElement(file.getName()); // Add each file to the list
                    jsonParser = new JSON_Parser(file.getAbsolutePath()); // Process each file
                }
            }
            else {
                DefaultListModel<String> model = new DefaultListModel<>();
                for (File file : selectedFiles) {
                    model.addElement(file.getName());
                    jsonParser = new JSON_Parser(file.getAbsolutePath());
                }
                inputFileList.setModel(model);
            }
        }
    }
    private void utilityFileHelper()
    {
        if (!(inputFileList.getModel() instanceof DefaultListModel) || inputFileList.getSelectedValuesList().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select one or more files from the input list!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!(outputFileList.getModel() instanceof DefaultListModel)) {
            outputFileList.setModel(new DefaultListModel<>());
        }
    }

    private void addConvertedJSONFileToList() throws FileNotFoundException {
        utilityFileHelper();
        List<String> selectedFilePaths = inputFileList.getSelectedValuesList();
        DefaultListModel<String> outputModel = (DefaultListModel<String>) outputFileList.getModel();

        for (String selectedFilePath : selectedFilePaths) {
            jsonParser = new JSON_Parser(selectedFilePath);
            try {
                jsonParser.writeSkillTree();
                outputModel.addElement(jsonParser.getOutputFileName()); // Store and display converted file path
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error converting file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addConvertedPDFFileToList() throws FileNotFoundException {
        utilityFileHelper();
        List<String> selectedFilePaths = inputFileList.getSelectedValuesList();
        DefaultListModel<String> outputModel = (DefaultListModel<String>) outputFileList.getModel();

        for (String selectedFilePath : selectedFilePaths) {
            jsonParser = new JSON_Parser(selectedFilePath);
            PDF_Parser pdfParser = new PDF_Parser(jsonParser);
            pdfParser.writePDF();
            outputModel.addElement(pdfParser.getOutputPDF()); // Store and display converted file path
        }
    }

    private void updatePreview() {
        if (outputFileList.getSelectedValue() == null) {
            previewTextArea.setText("");
            return;
        }

        String selectedFilePath = outputFileList.getSelectedValue();
        File file = new File(selectedFilePath);

        if (!file.exists()) {
            previewTextArea.setText("File not found.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            previewTextArea.setText("");
            String line;
            while ((line = reader.readLine()) != null) {
                previewTextArea.append(line + "\n");
            }
        } catch (IOException e) {
            previewTextArea.setText("Error loading file.");
        }
    }

}
