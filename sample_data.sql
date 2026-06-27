-- ============================================
-- SAMPLE DATA
-- Run this AFTER schema.sql
-- ============================================

USE placement_system;

-- ---------- Skills master list ----------
INSERT INTO skills (skill_name) VALUES
('Java'),
('Python'),
('SQL'),
('React'),
('DSA'),
('Communication');

-- ---------- Students ----------
INSERT INTO students (name, cgpa, branch) VALUES
('Rahul Sharma', 8.2, 'CSE'),
('Priya Verma', 7.1, 'IT'),
('Aman Khan', 9.0, 'CSE');

-- Rahul (id=1): Java, SQL, DSA
INSERT INTO student_skills (student_id, skill_id) VALUES
(1, 1), (1, 3), (1, 5);

-- Priya (id=2): Python, SQL
INSERT INTO student_skills (student_id, skill_id) VALUES
(2, 2), (2, 3);

-- Aman (id=3): Java, Python, SQL, React, DSA
INSERT INTO student_skills (student_id, skill_id) VALUES
(3, 1), (3, 2), (3, 3), (3, 4), (3, 5);

-- ---------- Companies ----------
INSERT INTO companies (name, min_cgpa) VALUES
('Infosys', 7.0),
('TCS', 6.5),
('Wipro', 8.5),
('Capgemini', 7.5);

-- Infosys (id=1) needs: Java, SQL
INSERT INTO company_skills (company_id, skill_id) VALUES
(1, 1), (1, 3);

-- TCS (id=2) needs: Python, SQL
INSERT INTO company_skills (company_id, skill_id) VALUES
(2, 2), (2, 3);

-- Wipro (id=3) needs: Java, DSA
INSERT INTO company_skills (company_id, skill_id) VALUES
(3, 1), (3, 5);

-- Capgemini (id=4) needs: Java, React, DSA
INSERT INTO company_skills (company_id, skill_id) VALUES
(4, 1), (4, 4), (4, 5);
