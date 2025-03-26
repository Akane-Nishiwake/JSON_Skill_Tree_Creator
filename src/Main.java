import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
       SwingUtilities.invokeLater(new Runnable()
        {
           public void run()
            {
               constructGUI();
            }
        });
//        try {
//            JSON_Parser jParser = new JSON_Parser();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        PDF_Parser pParser = new PDF_Parser();
    }

    static void constructGUI() //this method creates a MyFrame object that calls the initialization of my JFrame
    {
        JFrame.setDefaultLookAndFeelDecorated(true);
        MyFrame frame = new MyFrame();
        frame.setVisible(true);
    }
}