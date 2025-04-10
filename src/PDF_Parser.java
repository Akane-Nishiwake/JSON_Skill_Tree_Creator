import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Paragraph;

import com.itextpdf.text.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/** PDF_Parser - Converts skill tree data to PDF format */
public class PDF_Parser {
    private final JSON_Parser jsonParser;
    private final String outputPDF;

    /** Constructor
     * @param jsonParser JSON_Parser instance containing skill tree data
     * Creates output directory and sets up PDF output path
     */
    PDF_Parser(JSON_Parser jsonParser) {
        this.jsonParser = jsonParser;
        File outputDir = new File("Output_PDF");
        if (!outputDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            outputDir.mkdirs();
        }
        outputPDF = "Output_PDF/" + "Output" + jsonParser.getInputFileWithoutExtension() + ".pdf";
    }

    /** Get output PDF path
     * @return String containing the output PDF file path
     */
    public String getOutputPDF() {
        return outputPDF;
    }

    /** Write skill tree to PDF
     * Reads JSON data and creates a formatted PDF document with skill tree information
     * Handles JSON parsing, PDF creation and writing skill node details
     */
//    public void writePDF() {
//        try {
//            // Read JSON file
//            Gson gson = new Gson();
//            FileReader reader = new FileReader(jsonParser.getInputFileName());
//            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
//
//            // Deserialize JSON into SkillTree object
//            SkillTree skillTree = gson.fromJson(jsonObject.getAsJsonObject("skill_tree"), SkillTree.class);
//
//            // Create PDF writer and document
//            Document document = new Document();
//            PdfWriter.getInstance(document, new FileOutputStream(outputPDF));
//            document.open();
//
//            // Add Title
//            document.add(new Paragraph("Skill Tree: " + skillTree.name));
//
//            // Add Skill Nodes
//            for (SkillNode node : skillTree.nodes) {
//                document.add(new Paragraph("Skill: " + node.name));
//                document.add(new Paragraph("ID: " + node.id));
//                document.add(new Paragraph("Description: " + node.description));
//                document.add(new Paragraph("Cost: " + node.cost));
//                document.add(new Paragraph("Effect: " + node.effect));
//                document.add(new Paragraph("Prerequisites: " + (node.prerequisites.isEmpty() ? "None" : String.join(", ", node.prerequisites))));
//                document.add(new Paragraph("--------------------------------------------------------"));
//            }
//
//            document.close();
//
//        } catch (IOException | com.itextpdf.text.DocumentException _) {
//        }
//    }
    /** Write skill tree to PDF with diagram
     * @param diagram BufferedImage of the Mermaid diagram to include
     * Reads JSON data and creates a formatted PDF document with skill tree information and diagram
     */
    public void writePDF(BufferedImage diagram) {
        try {
            // Read JSON file
            Gson gson = new Gson();
            FileReader reader = new FileReader(jsonParser.getInputFileName());
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            SkillTree skillTree = gson.fromJson(jsonObject.getAsJsonObject("skill_tree"), SkillTree.class);

            // Create PDF
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(outputPDF));
            document.open();

            // Add Title
            document.add(new Paragraph("Skill Tree: " + skillTree.name));

            // Add Diagram
            if (diagram != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(diagram, "png", baos);
                Image image = Image.getInstance(baos.toByteArray());
                image.scaleToFit(500, 500); // Adjust size as needed
                document.add(image);
                document.add(new Paragraph("\n")); // Add spacing
            }

            // Add Skill Nodes
            for (SkillNode node : skillTree.nodes) {
                document.add(new Paragraph("Skill: " + node.name));
                document.add(new Paragraph("ID: " + node.id));
//                document.add(new Paragraph("Description: " + node.description));
//                document.add(new Paragraph("Cost: " + node.cost));
//                document.add(new Paragraph("Effect: " + node.effect));
                document.add(new Paragraph("Prerequisites: " + (node.prerequisites.isEmpty() ? "None" : String.join(", ", node.prerequisites))));
                document.add(new Paragraph("--------------------------------------------------------"));
            }

            document.close();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }
}

