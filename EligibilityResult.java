import java.util.List;

/**
 * Holds the outcome of checking ONE student against ONE company.
 */
public class EligibilityResult {
    private Company company;
    private boolean eligible;
    private List<String> missingSkills;
    private String reason;   // why not eligible (if applicable)
    private int score;       // recommendation score (only meaningful if eligible)

    public EligibilityResult(Company company, boolean eligible, List<String> missingSkills, String reason, int score) {
        this.company = company;
        this.eligible = eligible;
        this.missingSkills = missingSkills;
        this.reason = reason;
        this.score = score;
    }

    public Company getCompany() { return company; }
    public boolean isEligible() { return eligible; }
    public List<String> getMissingSkills() { return missingSkills; }
    public String getReason() { return reason; }
    public int getScore() { return score; }
}
