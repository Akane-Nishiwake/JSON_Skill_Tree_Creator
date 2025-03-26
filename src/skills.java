import java.util.ArrayList;

public class skills
{
    //class members
    private String id;
    private String name;
    private String description;
    private int cost;
    private String effect;
    private ArrayList<String> prerequisites;

    //constructor
    skills() {}

    //setters
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setCost(int cost) {
        this.cost = cost;
    }
    public void setEffect(String effect) {
        this.effect = effect;
    }
    public void setPrerequisites(ArrayList<String> prerequisites) {
        this.prerequisites = prerequisites;
    }

    //getters
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public int getCost() {
        return cost;
    }
    public String getEffect() {
        return effect;
    }
    public ArrayList<String> getPrerequisites() {
        return prerequisites;
    }

    @Override public String toString() {
        return "Skills [id=" + id + ", name=" + name + ", description=" +
                description + ", cost=" + cost + ", effect=" + effect +
                ", prerequisites=" + prerequisites + "]";
    }
}
