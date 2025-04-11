import java.util.ArrayList;
import java.util.List;

/**
 * SkillTreeWrapper - Wrapper class for JSON serialization
 */
class SkillTreeWrapper
{
    SkillTree skill_tree; // This ensures the correct JSON format

    /**
     * Constructor
     *
     * @param skillTree SkillTree to wrap
     */
    public SkillTreeWrapper(SkillTree skillTree)
    {
        this.skill_tree = skillTree;
    }
}

/**
 * SkillTree - Represents a complete skill tree structure
 */
public class SkillTree
{
    String name;
    List<SkillNode> nodes;
}

/**
 * SkillNode - Represents a single node in the skill tree
 */
class SkillNode
{
    String id;
    String name;
    String description;
    int cost;
    String effect;
    List<String> prerequisites;

    /**
     * Constructor
     *
     * @param i   Node ID
     * @param nam Node name
     * @param des Node description
     * @param c   Node cost
     * @param ef  Node effect
     * @param pre List of prerequisite node IDs
     */
    public SkillNode(String i, String nam, String des, int c, String ef, List<String> pre)
    {
        id = i;
        name = nam;
        description = des;
        cost = c;
        effect = ef;
        this.prerequisites = pre != null ? pre : new ArrayList<>();
    }
}
