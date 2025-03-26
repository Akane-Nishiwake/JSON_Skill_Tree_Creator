import javax.swing.JFrame;
import javax.swing.SwingUtilities;
public class Main {
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                constructGUI();
            }
        });
    }

    static void constructGUI() //this method creates a MyFram object that calls the initialization of my JFrame
    {
        JFrame.setDefaultLookAndFeelDecorated(true);
        MyFrame frame = new MyFrame();
        frame.setVisible(true);
    }
}