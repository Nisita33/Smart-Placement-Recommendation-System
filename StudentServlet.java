import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * GET  /api/students        -> list of all students (for dashboard dropdown)
 * POST /api/students        -> register a new student
 *   form fields expected: name, cgpa, branch, skills (repeated, e.g. skills=Java&skills=SQL)
 */
@WebServlet("/api/students")
public class StudentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            StudentDAO dao = new StudentDAO();
            List<Student> students = dao.getAllStudentsBasic();

            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Student s : students) {
                if (!first) sb.append(",");
                sb.append("{\"id\":").append(s.getId())
                  .append(",\"name\":").append(JsonUtil.quote(s.getName()))
                  .append(",\"cgpa\":").append(s.getCgpa())
                  .append(",\"branch\":").append(JsonUtil.quote(s.getBranch()))
                  .append("}");
                first = false;
            }
            sb.append("]");

            resp.getWriter().write(sb.toString());

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":" + JsonUtil.quote(e.getMessage()) + "}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String name = req.getParameter("name");
            String cgpaStr = req.getParameter("cgpa");
            String branch = req.getParameter("branch");
            String[] skillsArr = req.getParameterValues("skills");

            if (name == null || cgpaStr == null || branch == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"name, cgpa and branch are required\"}");
                return;
            }

            float cgpa = Float.parseFloat(cgpaStr);
            List<String> skills = new ArrayList<>();
            if (skillsArr != null) {
                for (String s : skillsArr) skills.add(s);
            }

            StudentDAO dao = new StudentDAO();
            int newId = dao.registerStudent(name, cgpa, branch, skills);

            resp.getWriter().write("{\"id\":" + newId + "}");

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"cgpa must be a number\"}");
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":" + JsonUtil.quote(e.getMessage()) + "}");
        }
    }
}
