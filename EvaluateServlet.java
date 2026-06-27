import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * GET /api/evaluate?studentId=1
 *
 * Runs LogicService against a student and returns JSON like:
 * {
 *   "student": { "id":1, "name":"Rahul Sharma", "cgpa":8.2, "branch":"CSE" },
 *   "eligible": [ {"name":"Infosys","score":25} ],
 *   "notEligible": [ {"name":"TCS","reason":"Missing skill(s): python"} ],
 *   "recommended": [ {"name":"Infosys","score":25} ]
 * }
 */
@WebServlet("/api/evaluate")
public class EvaluateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String studentIdParam = req.getParameter("studentId");
        if (studentIdParam == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"studentId is required\"}");
            return;
        }

        try {
            int studentId = Integer.parseInt(studentIdParam);

            StudentDAO studentDAO = new StudentDAO();
            CompanyDAO companyDAO = new CompanyDAO();
            LogicService logic = new LogicService();

            Student student = studentDAO.getStudentById(studentId);
            if (student == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"student not found\"}");
                return;
            }

            List<Company> companies = companyDAO.getAllCompanies();
            List<EligibilityResult> results = logic.evaluateAll(student, companies);
            List<EligibilityResult> recommended = logic.getRecommendations(results);

            String json = buildJson(student, results, recommended);

            try (PrintWriter out = resp.getWriter()) {
                out.write(json);
            }

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"studentId must be a number\"}");
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":" + JsonUtil.quote(e.getMessage()) + "}");
        }
    }

    private String buildJson(Student student, List<EligibilityResult> results, List<EligibilityResult> recommended) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        // student block
        sb.append("\"student\":{")
          .append("\"id\":").append(student.getId()).append(",")
          .append("\"name\":").append(JsonUtil.quote(student.getName())).append(",")
          .append("\"cgpa\":").append(student.getCgpa()).append(",")
          .append("\"branch\":").append(JsonUtil.quote(student.getBranch()))
          .append("},");

        // eligible
        sb.append("\"eligible\":[");
        boolean first = true;
        for (EligibilityResult r : results) {
            if (!r.isEligible()) continue;
            if (!first) sb.append(",");
            sb.append("{\"name\":").append(JsonUtil.quote(r.getCompany().getName()))
              .append(",\"score\":").append(r.getScore()).append("}");
            first = false;
        }
        sb.append("],");

        // not eligible
        sb.append("\"notEligible\":[");
        first = true;
        for (EligibilityResult r : results) {
            if (r.isEligible()) continue;
            if (!first) sb.append(",");
            sb.append("{\"name\":").append(JsonUtil.quote(r.getCompany().getName()))
              .append(",\"reason\":").append(JsonUtil.quote(r.getReason())).append("}");
            first = false;
        }
        sb.append("],");

        // recommended (already sorted by score desc)
        sb.append("\"recommended\":[");
        first = true;
        for (EligibilityResult r : recommended) {
            if (!first) sb.append(",");
            sb.append("{\"name\":").append(JsonUtil.quote(r.getCompany().getName()))
              .append(",\"score\":").append(r.getScore()).append("}");
            first = false;
        }
        sb.append("]");

        sb.append("}");
        return sb.toString();
    }
}
