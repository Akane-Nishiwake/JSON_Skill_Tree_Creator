import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;


/**
 * MermaidRender - Renders skill trees as Mermaid diagrams
 */
public class MermaidRender
{
    private final SkillTree skillTree;
    private final JFXPanel jfxPanel;
    private WebView webView;
    private boolean contentLoaded = false;

    /**
     * Constructor
     *
     * @param skillTree The SkillTree object to render
     *                  Creates a new MermaidRender instance and starts diagram rendering
     */
    public MermaidRender(SkillTree skillTree)
    {
        this.skillTree = skillTree;
        this.jfxPanel = new JFXPanel();
        Platform.runLater(this::diagramRender);
    }

    /**
     * Get the JavaFX panel containing the rendered diagram
     *
     * @return JFXPanel component for embedding in Swing
     */
    public JFXPanel getJfxPanel()
    {
        return jfxPanel;
    }

    /**
     * Render diagram
     * Creates a WebView to display the Mermaid diagram and adds it to the JFXPanel
     */
    private void diagramRender()
    {
        webView = new WebView();
        Scene scene = new Scene(webView);
        jfxPanel.setScene(scene);

        String mermaidDefinition = buildMermaidDiagram();
        String html = generateHtml(mermaidDefinition);

        webView.getEngine().loadContent(html);
        webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) ->
                                                                        {
                                                                            if (newValue == Worker.State.SUCCEEDED)
                                                                            {
                                                                                contentLoaded = true;
                                                                            }
                                                                        });

    }

    /**
     * Generate HTML for Mermaid diagram
     *
     * @param mermaidDefinition The Mermaid diagram definition
     * @return String containing the HTML content
     */
    private String generateHtml(String mermaidDefinition)
    {
        return "<html>" + "<head>" +
               "<script type=\"text/javascript\" src=\"https://cdn.jsdelivr.net/npm/mermaid/dist/mermaid.min" +
               ".js\"></script>" +
               "<script type=\"text/javascript\">" + "mermaid.initialize({startOnLoad:true});" + "</script>" +
               "</head>" + "<body>" + "<div class=\"mermaid\">" + mermaidDefinition + "</div>" + "</body>" + "</html>";
    }

    /**
     * Build Mermaid diagram
     *
     * @return String containing the Mermaid diagram definition
     * Constructs diagram by iterating through skill tree nodes and their prerequisites
     */
    private String buildMermaidDiagram()
    {
        StringBuilder sb = new StringBuilder("graph TD\n");
        for (SkillNode node : skillTree.nodes)
        {
            sb.append(node.id).append("[\"").append(node.name).append("\"]\n");
            for (String prereq : node.prerequisites)
            {
                sb.append(prereq).append(" --> ").append(node.id).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Take snapshot of diagram
     *
     * @return WritableImage containing the diagram snapshot
     */
    public WritableImage takeSnapshot()
    {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<WritableImage> image = new AtomicReference<>();

        Platform.runLater(() ->
                          {
                              // Wait for content to load
                              while (!contentLoaded)
                              {
                                  try
                                  {
                                      Thread.sleep(100);
                                  }
                                  catch (InterruptedException e)
                                  {
                                      Thread.currentThread().interrupt();
                                      return;
                                  }
                              }

                              // Additional wait for rendering
                              try
                              {
                                  Thread.sleep(500);
                              }
                              catch (InterruptedException e)
                              {
                                  Thread.currentThread().interrupt();
                                  return;
                              }

                              if (webView != null)
                              {
                                  image.set(webView.snapshot(null, null));
                              }
                              latch.countDown();
                          });

        try
        {
            if (!latch.await(5, TimeUnit.SECONDS))
            {
                throw new TimeoutException("Snapshot timed out");
            }
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
        catch (TimeoutException e)
        {
            e.printStackTrace();
        }

        return image.get();
    }


    /**
     * Convert WritableImage to BufferedImage
     *
     * @param writableImage JavaFX image to convert
     * @return BufferedImage for use with PDF generation
     */
    public static BufferedImage convertToBufferedImage(WritableImage writableImage)
    {
        int width = (int) writableImage.getWidth();
        int height = (int) writableImage.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        PixelReader pixelReader = writableImage.getPixelReader();
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                bufferedImage.setRGB(x, y, pixelReader.getArgb(x, y));
            }
        }
        return bufferedImage;
    }

    /**
     * Save BufferedImage as PNG
     *
     * @param image      BufferedImage to save
     * @param outputPath Path where the PNG file should be saved
     * @return boolean indicating if save was successful
     */
    public static boolean saveAsPNG(BufferedImage image, String outputPath)
    {
        try
        {
            File outputFile = new File(outputPath);
            // Create directories if they don't exist
            outputFile.getParentFile().mkdirs();
            return ImageIO.write(image, "PNG", outputFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
