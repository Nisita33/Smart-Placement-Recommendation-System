import java.util.HashSet;
import java.util.Set;

/**
 * Represents a company's hiring criteria.
 */
public class Company {
    private int id;
    private String name;
    private float minCgpa;
    private Set<String> requiredSkills = new HashSet<>();

    public Company(int id, String name, float minCgpa) {
        this.id = id;
        this.name = name;
        this.minCgpa = minCgpa;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public float getMinCgpa() { return minCgpa; }
    public Set<String> getRequiredSkills() { return requiredSkills; }

    public void addRequiredSkill(String skill) {
        requiredSkills.add(skill.toLowerCase());
    }

    @Override
    public String toString() {
        return name + " (Min CGPA: " + minCgpa + ", Required: " + requiredSkills + ")";
    }
}
