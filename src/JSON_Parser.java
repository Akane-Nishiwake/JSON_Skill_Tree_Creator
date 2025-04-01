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

    JSON_Parser(String inputFileName) throws FileNotFoundException {
        mInputFileName = inputFileName;
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

    /**
     * Method: readSkillTree
     * This method takes in the filename of the file to be read
     * and then reads in the file to the correct java object.
     * The method uses the Gson object from the GSON library
     * to make sure that the Java object is being made correctly
     * with the correct Deserialization method.
     */
    public SkillTree readSkillTree(String filename) throws FileNotFoundException {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Reader reader = new FileReader(filename);
        JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        SkillTree skillTree = gson.fromJson(jsonObject.getAsJsonObject("skill_tree"), SkillTree.class);
        return skillTree;

    }

    /**
     * Method: writeSkillTree
     * This method reads in the data from the related Java object
     * then writes it to a new file with the pre-determined
     * file name.
     */
    public void writeSkillTree() throws IOException {
        // Convert to JSON and write to a file
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(getOutputFileName())) {
            // Wrap mSkillTree inside SkillTreeWrapper
            gson.toJson(new SkillTreeWrapper(mSkillTree), writer);
            System.out.println("Skill tree successfully written to ../" + mOutputFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
