import java.sql.SQLException;
import java.util.List;

/**
 * Demo entry point.
 * Run this after setting up the DB and loading sample_data.sql.
 *
 * Usage: java Main <studentId>
 * Example: java Main 1
 */
public class Main {

    public static void main(String[] args) {
        int studentId = (args.length > 0) ? Integer.parseInt(args[0]) : 1;

        try {
            StudentDAO studentDAO = new StudentDAO();
            CompanyDAO companyDAO = new CompanyDAO();
            LogicService logic = new LogicService();

            Student student = studentDAO.getStudentById(studentId);
            if (student == null) {
                System.out.println("No student found with id " + studentId);
                return;
            }

            List<Company> companies = companyDAO.getAllCompanies();
            List<EligibilityResult> results = logic.evaluateAll(student, companies);
            List<EligibilityResult> recommended = logic.getRecommendations(results);

            System.out.println("=================================================");
            System.out.println("Report for: " + student);
            System.out.println("=================================================\n");

            System.out.println("Eligible:");
            for (EligibilityResult r : results) {
                if (r.isEligible()) {
                    System.out.println("- " + r.getCompany().getName());
                }
            }

            System.out.println("\nNot Eligible:");
            for (EligibilityResult r : results) {
                if (!r.isEligible()) {
                    System.out.println("- " + r.getCompany().getName() + " -> " + r.getReason());
                }
            }

            System.out.println("\nRecommended:");
            int rank = 1;
            for (EligibilityResult r : recommended) {
                System.out.println(rank + ". " + r.getCompany().getName() + " (Score: " + r.getScore() + ")");
                rank++;
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
