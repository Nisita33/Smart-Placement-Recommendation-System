import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * GET /api/companies -> list of all companies + their min CGPA + required skills.
 */
@WebServlet("/api/companies")
public class CompanyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            CompanyDAO dao = new CompanyDAO();
            List<Company> companies = dao.getAllCompanies();

            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Company c : companies) {
                if (!first) sb.append(",");
                sb.append("{\"id\":").append(c.getId())
                  .append(",\"name\":").append(JsonUtil.quote(c.getName()))
                  .append(",\"minCgpa\":").append(c.getMinCgpa())
                  .append(",\"requiredSkills\":[");
                boolean firstSkill = true;
                for (String skill : c.getRequiredSkills()) {
                    if (!firstSkill) sb.append(",");
                    sb.append(JsonUtil.quote(skill));
                    firstSkill = false;
                }
                sb.append("]}");
                first = false;
            }
            sb.append("]");

            resp.getWriter().write(sb.toString());

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":" + JsonUtil.quote(e.getMessage()) + "}");
        }
    }
}
