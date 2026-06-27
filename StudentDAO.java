import java.sql.*;
import java.util.List;

/**
 * Loads a Student (with skills) from the database.
 */
public class StudentDAO {

    /**
     * Inserts a new student and links their skills. Used by the
     * registration form on the website. Returns the new student's ID.
     */
    public int registerStudent(String name, float cgpa, String branch, List<String> skillNames) throws SQLException {
        String insertStudentSql = "INSERT INTO students (name, cgpa, branch) VALUES (?, ?, ?)";
        String linkSkillSql = "INSERT INTO student_skills (student_id, skill_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            int newStudentId;
            try (PreparedStatement ps = conn.prepareStatement(insertStudentSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, name);
                ps.setFloat(2, cgpa);
                ps.setString(3, branch);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException("Failed to insert student");
                    }
                    newStudentId = keys.getInt(1);
                }
            }

            SkillDAO skillDAO = new SkillDAO();
            try (PreparedStatement ps = conn.prepareStatement(linkSkillSql)) {
                for (String skillName : skillNames) {
                    int skillId = skillDAO.getOrCreateSkillId(conn, skillName.trim());
                    ps.setInt(1, newStudentId);
                    ps.setInt(2, skillId);
                    ps.executeUpdate();
                }
            }

            conn.commit();
            return newStudentId;
        }
    }

    /**
     * Returns a lightweight list of all students (id + name only) — used
     * to populate the dropdown on the dashboard page.
     */
    public java.util.List<Student> getAllStudentsBasic() throws SQLException {
        java.util.List<Student> list = new java.util.ArrayList<>();
        String sql = "SELECT id, name, cgpa, branch FROM students ORDER BY name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Student(rs.getInt("id"), rs.getString("name"), rs.getFloat("cgpa"), rs.getString("branch")));
            }
        }
        return list;
    }

    public Student getStudentById(int studentId) throws SQLException {
        Student student = null;

        String studentSql = "SELECT id, name, cgpa, branch FROM students WHERE id = ?";
        String skillsSql =
                "SELECT s.skill_name FROM skills s " +
                "JOIN student_skills ss ON s.id = ss.skill_id " +
                "WHERE ss.student_id = ?";

        try (Connection conn = DBConnection.getConnection()) {

            // 1. Fetch base student info
            try (PreparedStatement ps = conn.prepareStatement(studentSql)) {
                ps.setInt(1, studentId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        student = new Student(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getFloat("cgpa"),
                                rs.getString("branch")
                        );
                    }
                }
            }

            if (student == null) return null; // student not found

            // 2. Fetch their skills
            try (PreparedStatement ps = conn.prepareStatement(skillsSql)) {
                ps.setInt(1, studentId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        student.addSkill(rs.getString("skill_name"));
                    }
                }
            }
        }

        return student;
    }
}
