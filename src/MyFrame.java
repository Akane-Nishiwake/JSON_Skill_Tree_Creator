import javafx.scene.image.WritableImage;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import java.util.List;

/**
 * MyFrame - Main application window for the Skill Tree Creator
 */
public class MyFrame extends JFrame
{
    // UI Components
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
    private JMenu settingsMenu;
    private JSON_Parser jsonParser;
    private JButton previewButton;
    private JScrollPane jScrollPane;
    private JPanel diagramPreview;

    /**
     * Constructor
     * Creates a new MyFrame instance and initializes the UI
     */
    public MyFrame()
    {
        super();
        init();
    }

    /**
     * Initialize frame
     * Calls method to create main panel
     */
    private void init()
    {
        createMainPanel();
    }

    /**
     * Create main panel
     * Sets up the main window, components and event listeners
     */
    private void createMainPanel()
    {
        setTitle("Skill Tree Creator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setMenuBar();
        AddFile.addActionListener(_ ->
                                  {
                                      try
                                      {
                                          addFilesToList();
                                      }
                                      catch (FileNotFoundException ex)
                                      {
                                          throw new RuntimeException(ex);
                                      }
                                  });
        convertToJSON.addActionListener(_ ->
                                        {
                                            try
                                            {
                                                addConvertedJSONFileToList();
                                            }
                                            catch (FileNotFoundException ex)
                                            {
                                                throw new RuntimeException(ex);
                                            }
                                        });
        convertToPDF.addActionListener(_ ->
                                       {
                                           try
                                           {
                                               addConvertedPDFFileToList();
                                           }
                                           catch (FileNotFoundException ex)
                                           {
                                               throw new RuntimeException(ex);
                                           }
                                       });
        previewButton.addActionListener(_ -> updatePreview());
    }

    /**
     * Set up menu bar
     * Creates and configures the main menu bar
     */
    private void setMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        settingsMenu = new JMenu("Settings");

        setFileMenu();
        setSettingsMenu();

        menuBar.add(fileMenu);
        menuBar.add(settingsMenu);

        this.setJMenuBar(menuBar);
    }

    /**
     * Configure file menu
     * Sets up file menu items and their actions
     */
    private void setFileMenu()
    {
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.add(openItem);
        fileMenu.add(exitItem);
        openItem.addActionListener(_ ->
                                   {
                                       try
                                       {
                                           addFilesToList();
                                       }
                                       catch (FileNotFoundException ex)
                                       {
                                           throw new RuntimeException(ex);
                                       }
                                   });
        exitItem.addActionListener(_ -> System.exit(0));
    }

    /**
     * Configure settings menu
     * Sets up settings menu items
     */
    private void setSettingsMenu()
    {
        JMenuItem preferencesItem = new JMenuItem("Preferences");
        settingsMenu.add(preferencesItem);
    }

    /**
     * Add files to input list
     * Opens file chooser and adds selected JSON files to input list
     *
     * @throws FileNotFoundException if selected file cannot be found
     */
    private void addFilesToList() throws FileNotFoundException
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select JSON files");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(true);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            File[] selectedFiles = chooser.getSelectedFiles();

            if (inputFileList.getModel() instanceof DefaultListModel<String> model)
            {
                for (File file : selectedFiles)
                {
                    model.addElement(file.getName());
                    jsonParser = new JSON_Parser(file.getAbsolutePath());
                }
            }
            else
            {
                DefaultListModel<String> model = new DefaultListModel<>();
                for (File file : selectedFiles)
                {
                    model.addElement(file.getName());
                    jsonParser = new JSON_Parser(file.getAbsolutePath());
                }
                inputFileList.setModel(model);
            }
        }
    }

    /**
     * Utility helper
     * Validates input selection and initializes output list model
     */
    private void utilityFileHelper()
    {
        if (!(inputFileList.getModel() instanceof DefaultListModel) || inputFileList.getSelectedValuesList().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Please select one or more files from the input list!", "Error",
                                          JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!(outputFileList.getModel() instanceof DefaultListModel))
        {
            outputFileList.setModel(new DefaultListModel<>());
        }
    }

    /**
     * Convert to JSON
     * Converts selected files to JSON format and adds to output list
     *
     * @throws FileNotFoundException if input file cannot be found
     */
    private void addConvertedJSONFileToList() throws FileNotFoundException
    {
        utilityFileHelper();
        List<String> selectedFilePaths = inputFileList.getSelectedValuesList();
        DefaultListModel<String> outputModel = (DefaultListModel<String>) outputFileList.getModel();

        for (String selectedFilePath : selectedFilePaths)
        {
            jsonParser = new JSON_Parser(selectedFilePath);
            try
            {
                jsonParser.writeSkillTree();
                outputModel.addElement(jsonParser.getOutputFileName());
            }
            catch (IOException ex)
            {
                JOptionPane.showMessageDialog(null, "Error converting file: " + ex.getMessage(), "Error",
                                              JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Convert to PDF
     * Converts selected files to PDF format and adds to output list
     *
     * @throws FileNotFoundException if input file cannot be found
     */
    private void addConvertedPDFFileToList() throws FileNotFoundException
    {
        utilityFileHelper();
        List<String> selectedFilePaths = inputFileList.getSelectedValuesList();
        DefaultListModel<String> outputModel = (DefaultListModel<String>) outputFileList.getModel();

        for (String selectedFilePath : selectedFilePaths)
        {
            try
            {
                jsonParser = new JSON_Parser(selectedFilePath);
                SkillTree skillTree = jsonParser.readSkillTree(selectedFilePath);

                // Create renderer and wait for initialization
                MermaidRender renderer = new MermaidRender(skillTree);
                Thread.sleep(2000); // Give time for JavaFX initialization

                // Get diagram snapshot
                WritableImage snapshot = renderer.takeSnapshot();
                if (snapshot != null)
                {
                    BufferedImage diagram = MermaidRender.convertToBufferedImage(snapshot);
                    if (diagram == null)
                    {
                        throw new RuntimeException("Failed to convert snapshot to BufferedImage");
                    }
                    else
                    {
                        boolean saved = MermaidRender.saveAsPNG(diagram, "Output_Images/diagram.png");
                        if (saved)
                        {
                            System.out.println("Image saved successfully");
                        }
                        else
                        {
                            System.out.println("Failed to save image");
                        }
                    }
                    PDF_Parser pdfParser = new PDF_Parser(jsonParser);
                    pdfParser.writePDF(diagram);
                    outputModel.addElement(pdfParser.getOutputPDF());
                }
                else
                {
                    throw new RuntimeException("Failed to capture diagram");
                }
                //            } catch (InterruptedException e) {
                //Thread.currentThread().interrupt();
                //JOptionPane.showMessageDialog(this, "PDF conversion interrupted", "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(this, "Error converting to PDF: " + e.getMessage(), "Error",
                                              JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Update preview
     * Updates the diagram preview with the selected skill tree
     */
    private void updatePreview()
    {
        try
        {
            if (inputFileList.getSelectedValue() == null)
                return;

            String selectedFilePath = inputFileList.getSelectedValue();
            JSON_Parser parser = new JSON_Parser(selectedFilePath);
            SkillTree skillTree = parser.readSkillTree(selectedFilePath);

            MermaidRender renderer = new MermaidRender(skillTree);
            diagramPreview.removeAll();
            diagramPreview.setLayout(new BorderLayout());
            diagramPreview.add(renderer.getJfxPanel(), BorderLayout.CENTER);
            diagramPreview.revalidate();
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Error updating diagram preview!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
