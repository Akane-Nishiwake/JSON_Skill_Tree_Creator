import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.List;

public class JSON_Parser {
    private String mInputFileName;
    private String mOutputFileName;
    private SkillTree mSkillTree;

    JSON_Parser(String inputFileName) {
        mInputFileName = inputFileName;
        mOutputFileName = "Output"+getInputFileName();
        mSkillTree = new SkillTree();
    }

    public String getInputFileName() {
        return mInputFileName;
    }
    public String getInputFileWithoutExtension() {
        String result = "";
        if (mInputFileName.endsWith(".json")) {
            result = mInputFileName.substring(0, mInputFileName.length() - 5);
        }
        return result;
    }
    public String getOutputFileName() {
        return mOutputFileName;
    }

    public void run() throws IOException
    {
        mSkillTree = readSkillTree(mInputFileName);
        try {
            writeSkillTree();
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

    public void writeSkillTree() throws IOException {
        //Create the object needed
        mOutputFileName = "Output"+ filename;
        // Convert to JSON and write to a file
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(getOutputFileName())) {

            //////TODO THIS THING IS BROKEN LOL
            gson.toJson(mSkillTree, writer);
            System.out.println("Skill tree successfully written to ../"+mOutputFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mOutputFileName;
    }
}
