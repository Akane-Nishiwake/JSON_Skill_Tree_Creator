import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Paragraph;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class PDF_Parser {

    private String inputJson;
    private JSON_Parser jsonParser;
    PDF_Parser(JSON_Parser jsonParser) {
        this.jsonParser = jsonParser;
        String inputJson = this.jsonParser.getInputFileName(); // Input JSON file
        String outputPdf = "Output"+ this.jsonParser.getInputFileWithoutExtension()+".pdf"; // Output PDF file

        writePDF(inputJson, outputPdf);
    }

    private static void writePDF(String inputJson, String outputPdf) {
        try {
            // Read JSON file
            Gson gson = new Gson();

            FileReader reader = new FileReader(inputJson);

            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            // Deserialize JSON into SkillTree object
            SkillTree skillTree = gson.fromJson(jsonObject.getAsJsonObject("skill_tree"), SkillTree.class);

            // Create PDF writer and document (iText 5 method)
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(outputPdf));
            document.open();

            // Add Title
            document.add(new Paragraph("Skill Tree: " + skillTree.name));

            // Add Skill Nodes
            for (SkillNode node : skillTree.nodes) {
                document.add(new Paragraph("Skill: " + node.name));
                document.add(new Paragraph("Description: " + node.description));
                document.add(new Paragraph("Cost: " + node.cost));
                document.add(new Paragraph("Effect: " + node.effect));
                document.add(new Paragraph("Prerequisites: " + (node.prerequisites.isEmpty() ? "None" : String.join(", ", node.prerequisites))));
                document.add(new Paragraph("--------------------------------------------------------"));
            }

            // Close document
            document.close();
            System.out.println("PDF created successfully: " + outputPdf);

        } catch (IOException | com.itextpdf.text.DocumentException e) {
            e.printStackTrace();
        }
    }
}
