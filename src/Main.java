import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/**
 * Main - Entry point for the Skill Tree Creator application
 */
public class Main
{
    /**
     * Main method
     *
     * @param args Command line arguments (not used)
     *             Launches the application on the Event Dispatch Thread
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(Main::constructGUI);
    }

    /**
     * Construct GUI
     * Creates and displays the main application window
     * Sets system look and feel decorations
     */
    static void constructGUI()
    {
        JFrame.setDefaultLookAndFeelDecorated(true);
        MyFrame frame = new MyFrame();
        frame.setVisible(true);
    }
}