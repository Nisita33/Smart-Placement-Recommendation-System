import java.sql.*;
import java.util.*;

/**
 * Loads all Companies (with their required skills) from the database.
 */
public class CompanyDAO {

    public List<Company> getAllCompanies() throws SQLException {
        Map<Integer, Company> companyMap = new LinkedHashMap<>();

        String companySql = "SELECT id, name, min_cgpa FROM companies";
        String skillsSql =
                "SELECT cs.company_id, s.skill_name FROM skills s " +
                "JOIN company_skills cs ON s.id = cs.skill_id";

        try (Connection conn = DBConnection.getConnection()) {

            // 1. Fetch all companies
            try (PreparedStatement ps = conn.prepareStatement(companySql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Company c = new Company(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getFloat("min_cgpa")
                    );
                    companyMap.put(c.getId(), c);
                }
            }

            // 2. Attach required skills to each company
            try (PreparedStatement ps = conn.prepareStatement(skillsSql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int companyId = rs.getInt("company_id");
                    String skill = rs.getString("skill_name");
                    Company c = companyMap.get(companyId);
                    if (c != null) c.addRequiredSkill(skill);
                }
            }
        }

        return new ArrayList<>(companyMap.values());
    }
}
