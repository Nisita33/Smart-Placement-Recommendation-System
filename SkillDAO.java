import java.sql.*;

/**
 * Helper for working with the skills master table.
 * If a skill name doesn't exist yet, it creates it.
 */
public class SkillDAO {

    public int getOrCreateSkillId(Connection conn, String skillName) throws SQLException {
        String selectSql = "SELECT id FROM skills WHERE skill_name = ?";
        try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
            ps.setString(1, skillName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }

        String insertSql = "INSERT INTO skills (skill_name) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, skillName);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }

        throw new SQLException("Could not create skill: " + skillName);
    }
}
