import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
/** JSON_Parser - Handles reading and writing of skill tree JSON files */
public class JSON_Parser {
    private final String mInputFileName;
    private final String mOutputFileName;
    private final SkillTree mSkillTree;

    /** Constructor
     * @param inputFileName Path to input JSON file
     * @throws FileNotFoundException if input file cannot be found
     * Creates output directory and initializes skill tree from input file
     */
    JSON_Parser(String inputFileName) throws FileNotFoundException {
        mInputFileName = inputFileName;
        File outputDir = new File("Output_JSON");
        if (!outputDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            outputDir.mkdirs();
        }
        mOutputFileName = "Output_JSON/Output" + getInputFileName();
        mSkillTree = readSkillTree(mInputFileName);
    }

    /** Get input file name
     * @return String containing the input file path
     */
    public String getInputFileName() {
        return mInputFileName;
    }

    /** Get input file name without extension
     * @return String containing file name without .json extension
     */
    public String getInputFileWithoutExtension() {
        String result = "";
        if (mInputFileName.endsWith(".json")) {
            result = mInputFileName.substring(0, mInputFileName.length() - 5);
        }
        return result;
    }

    /** Get output file name
     * @return String containing the output file path
     */
    public String getOutputFileName() {
        return mOutputFileName;
    }

    /** Read skill tree from JSON
     * @param filename Path to JSON file to read
     * @return SkillTree object parsed from JSON
     * @throws FileNotFoundException if input file cannot be found
     */
    public SkillTree readSkillTree(String filename) throws FileNotFoundException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Reader reader = new FileReader(filename);
        JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        return gson.fromJson(jsonObject.getAsJsonObject("skill_tree"), SkillTree.class);
    }

    /** Write skill tree to JSON
     * @throws IOException if error occurs while writing file
     * Writes the skill tree to output file in pretty-printed JSON format
     */
    public void writeSkillTree() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(getOutputFileName())) {
            gson.toJson(new SkillTreeWrapper(mSkillTree), writer);
        } catch (IOException _) {
        }
    }
}
