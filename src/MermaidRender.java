import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


/** MermaidRender - Renders skill trees as Mermaid diagrams */
public class MermaidRender {
    private final SkillTree skillTree;
    private final JFXPanel jfxPanel;

    /** Constructor
     * @param skillTree The SkillTree object to render
     * Creates a new MermaidRender instance and starts diagram rendering
     */
    public MermaidRender(SkillTree skillTree) {
        this.skillTree = skillTree;
        jfxPanel = new JFXPanel();
        diagramRender();
    }

    /** Get the JavaFX panel containing the rendered diagram
     * @return JFXPanel component for embedding in Swing
     */
    public JFXPanel getJfxPanel() {
        return jfxPanel;
    }

    /** Render diagram
     * Creates a WebView to display the Mermaid diagram and adds it to the JFXPanel
     */
    private void diagramRender() {
        Platform.runLater(() -> {
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            String mermaidChart = buildMermaidDiagram();
            String html = """
                    <html>
                    <head>
                        <script type="module">
                            import mermaid from 'https://cdn.jsdelivr.net/npm/mermaid@10/dist/mermaid.esm.min.mjs';
                            mermaid.initialize({ startOnLoad: true });
                        </script>
                        <style>
                            body { background-color: #1e1e1e; color: #fff; margin: 0; padding: 10px; }
                            .mermaid { font-family: sans-serif; }
                        </style>
                    </head>
                    <body>
                        <div class="mermaid">
                            %s
                        </div>
                    </body>
                    </html>
                    """.formatted(mermaidChart);

            webEngine.loadContent(html);
            jfxPanel.setScene(new Scene(webView));
        });
    }

    /** Build Mermaid diagram
     * @return String containing the Mermaid diagram definition
     * Constructs diagram by iterating through skill tree nodes and their prerequisites
     */
    private String buildMermaidDiagram() {
        StringBuilder sb = new StringBuilder("graph TD\n");
        for (SkillNode node : skillTree.nodes) {
            sb.append(node.id).append("[\"").append(node.name).append("\"]\n");
            for (String prereq : node.prerequisites) {
                sb.append(prereq).append(" --> ").append(node.id).append("\n");
            }
        }
        return sb.toString();
    }
}
