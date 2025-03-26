import com.google.gson.Gson;
import java.io.*;
import java.util.ArrayList;

public class JSON_Parser
{


    JSON_Parser()
    {
        // Creating an object of Gson class
        Gson gson = new Gson();

        skills _skills = new skills();

       //File file = new File("../SkillTree.json");

        ArrayList<String> sample = new ArrayList<>();
        for(int i = 0; i < 2; i++)
        {
            sample.add("1");
        }
        // Generating emp object from _skills json
        skills _skillsGenerated = gson.fromJson(
                "../SkillTree.json", skills.class);

        // Print and display the employee been generated
        System.out.println(
                "Generated employee from json is "
                        + _skillsGenerated);
//        _skills.setId("1");
//        _skills.setName("Skill Tree");
//        _skills.setDescription("");
//        _skills.setCost(30);
//        _skills.setEffect("");
//        _skills.setPrerequisites(sample);
//
//        // Generating json from _skills object
//        String _skillsJson = gson.toJson(_skills);
//        System.out.println("Emp json is " + _skillsJson);
//
//        // Changing one of the attributes of emp object
//        _skills.setEffect("Java");


    }
}
