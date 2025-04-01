import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;

public class JSON_Parser {
    private String mInputFileName;
    private String mOutputFileName;
    private SkillTree mSkillTree;

    JSON_Parser(String inputFileName) throws FileNotFoundException {
        mInputFileName = inputFileName;
        File outputDir = new File("Output_JSON");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        mOutputFileName = "Output"+getInputFileName();
        mSkillTree = readSkillTree(mInputFileName);
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

    public SkillTree readSkillTree(String filename) throws FileNotFoundException {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Reader reader = new FileReader(filename);
        JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        SkillTree skillTree = gson.fromJson(jsonObject.getAsJsonObject("skill_tree"), SkillTree.class);
        return skillTree;

    }

    public void writeSkillTree() throws IOException {
        // Convert to JSON and write to a file
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(getOutputFileName())) {
            // Wrap mSkillTree inside SkillTreeWrapper
            gson.toJson(new SkillTreeWrapper(mSkillTree), writer);
           // System.out.println("Skill tree successfully written to ../" + mOutputFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
