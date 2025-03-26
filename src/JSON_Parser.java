import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.List;

public class JSON_Parser {
    private String mInputFileName;
    private String mOutputFileName;

    JSON_Parser(String inputFileName, String outputFileName) {
        mInputFileName = inputFileName;
        mOutputFileName = outputFileName;
    }

    public void run() throws IOException
    {
        SkillTree skillTree = new SkillTree();
        skillTree = readSkillTree(mInputFileName);
        try {
            writeSkillTree(mOutputFileName, skillTree);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public SkillTree readSkillTree(String filename) throws FileNotFoundException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Read JSON file
        Reader reader = new FileReader(filename);

        // Parse JSON
        JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

        // Deserialize the "skill_tree" object
        SkillTree skillTree = gson.fromJson(jsonObject.getAsJsonObject("skill_tree"), SkillTree.class);

        // Print the skill tree
        System.out.println("Skill Tree Name: " + skillTree.name);
        for (SkillNode node : skillTree.nodes) {
            System.out.println("Skill: " + node.name + " | Description: " + node.description +
                    " | Cost: " + node.cost + " | Effect: " + node.effect +
                    " | Prerequisites: " + node.prerequisites);
        }
        return skillTree;
    }

    public void writeSkillTree(String filename, SkillTree skillTree) throws IOException {
        //Create the object needed
        skillTree.name = "Final Fantasy XVI Skill Tree";
        skillTree.nodes = List.of(
                new SkillNode("1", "Lunge", "A fast thrust attack to close the distance.", 100, "Increases attack speed when closing distance.", List.of()),
                new SkillNode("2", "Rising Flame", "A fiery uppercut that launches enemies.", 200, "Launches enemies into the air.", List.of("1"))
        );

        // Convert to JSON and write to a file
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(skillTree, writer);
            System.out.println("Skill tree successfully written to ../OutputSkillTree.json.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
