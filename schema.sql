-- ============================================
-- SMART PLACEMENT RECOMMENDATION SYSTEM
-- Database Schema
-- ============================================

CREATE DATABASE IF NOT EXISTS placement_system;
USE placement_system;

DROP TABLE IF EXISTS student_skills;
DROP TABLE IF EXISTS company_skills;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS companies;
DROP TABLE IF EXISTS skills;

-- Table 1: Students
CREATE TABLE students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    cgpa FLOAT NOT NULL,
    branch VARCHAR(50)
);

-- Table 2: Skills (master list)
CREATE TABLE skills (
    id INT PRIMARY KEY AUTO_INCREMENT,
    skill_name VARCHAR(50) UNIQUE NOT NULL
);

-- Table 3: Student <-> Skills (many-to-many)
CREATE TABLE student_skills (
    student_id INT,
    skill_id INT,
    PRIMARY KEY (student_id, skill_id),
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE
);

-- Table 4: Companies
CREATE TABLE companies (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    min_cgpa FLOAT NOT NULL
);

-- Table 5: Company <-> Required Skills (many-to-many)
CREATE TABLE company_skills (
    company_id INT,
    skill_id INT,
    PRIMARY KEY (company_id, skill_id),
    FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE
);
