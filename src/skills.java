import java.util.ArrayList;
import java.util.List;

class SkillTree {
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

    public <E> SkillNode(String number, String lunge, String s, int i, String s1, List<E> of) {
        id = number;
        name = lunge;
        description = s;
        cost = i;
        effect = s1;
        prerequisites = new ArrayList<>();
    }
}
