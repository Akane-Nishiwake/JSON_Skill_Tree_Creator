import java.util.ArrayList;
import java.util.List;

class SkillTreeWrapper {
    SkillTree skill_tree; // This ensures the correct JSON format

    public SkillTreeWrapper(SkillTree skillTree) {
        this.skill_tree = skillTree;
    }
}
public class SkillTree {
    String name;
    List<SkillNode> nodes;
}

class SkillNode {
    String id;
    String name;
    String description;
    int cost;
    String effect;
    List<String> prerequisites;

    public SkillNode(String i, String nam, String des, int c, String ef, List<String> pre) {
        id = i;
        name = nam;
        description = des;
        cost = c;
        effect = ef;
        this.prerequisites = pre != null ? pre : new ArrayList<>();
    }
}
