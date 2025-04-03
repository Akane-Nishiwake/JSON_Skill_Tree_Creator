import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;

public class JSON_Parser {
    private final String mInputFileName;
    private final String mOutputFileName;
    private final SkillTree mSkillTree;

    JSON_Parser(String inputFileName) throws FileNotFoundException {
        mInputFileName = inputFileName;
        File outputDir = new File("Output_JSON");
        if (!outputDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
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
        return gson.fromJson(jsonObject.getAsJsonObject("skill_tree"), SkillTree.class);

    }

    public void writeSkillTree() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(getOutputFileName())) {
            gson.toJson(new SkillTreeWrapper(mSkillTree), writer);
        } catch (IOException _) {
        }
    }
}
