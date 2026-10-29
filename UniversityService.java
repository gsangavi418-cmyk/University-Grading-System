import java.util.*;
public class UniversityService {

Map<Integer, Course> courses = new LinkedHashMap<>(); Map<Integer, Student> students = new LinkedHashMap<>(); Map<Integer, Assessment> assessments = new LinkedHashMap<>(); Map<Integer, ExamSchedule> schedules = new LinkedHashMap<>(); Map<String, MarkEntry> marks = new LinkedHashMap<>(); Map<String, Grade> publishedGrades = new LinkedHashMap<>();
 

 


int nextAssessmentId = 1;

String key(int studentId, int courseId, int assessmentId) { return studentId + "-" + courseId + "-" + assessmentId;
}

String gradeKey(int studentId, int courseId) { return studentId + "-" + courseId;
}

void addCourse(int id, String name, int credits) { if (courses.containsKey(id) || credits <= 0) {
System.out.println("Invalid course ID or credits."); return;
}

courses.put(id, new Course(id, name, credits)); System.out.println("Course added successfully.");
}

void addStudent(int id, String name, String term) { if (students.containsKey(id)) {
System.out.println("Student ID already exists."); return;
}

students.put(id, new Student(id, name, term)); System.out.println("Student added successfully.");
}

void registerStudent(int studentId, int courseId) { Student student = students.get(studentId);
 

 
if (student == null || !courses.containsKey(courseId)) { System.out.println("Student or course not found."); return;
}

student.registeredCourses.add(courseId); System.out.println("Student registered for the course.");
}

void createAssessment(int courseId, String name, double weight) { if (!courses.containsKey(courseId) || weight <= 0 || weight > 100) {
System.out.println("Invalid course or weightage."); return;
}

for (Assessment a : assessments.values()) {

if (a.courseId == courseId && a.name.equalsIgnoreCase(name)) { System.out.println("Assessment name already exists.");
return;

}

}

Assessment a = new Assessment( nextAssessmentId++, courseId, name, weight
);

assessments.put(a.id, a);

System.out.println("Assessment created. Assessment ID: " + a.id);

}

void scheduleExam(int courseId, String date, String time) { if (!courses.containsKey(courseId)) {
System.out.println("Course not found.");
 

 
return;

}

schedules.put(courseId, new ExamSchedule(courseId, date, time)); System.out.println("Exam scheduled successfully.");
}

void recordMarks(int studentId, int courseId, int assessmentId, double score) {
Student student = students.get(studentId);

Assessment assessment = assessments.get(assessmentId); if (student == null || !courses.containsKey(courseId)) {
System.out.println("Student or course not found."); return;
}

if (!student.registeredCourses.contains(courseId)) { System.out.println("Student is not registered for this course."); return;
}

if (assessment == null || assessment.courseId != courseId) { System.out.println("Assessment does not belong to this course."); return;
}

if (score < 0 || score > 100) {

System.out.println("Marks must be between 0 and 100."); return;
}

marks.put(key(studentId, courseId, assessmentId),

new MarkEntry(studentId, courseId, assessmentId, score));
 

 


publishedGrades.remove(gradeKey(studentId, courseId)); System.out.println("Marks recorded successfully.");
}

double getTotalWeight(int courseId) { double total = 0;
for (Assessment a : assessments.values()) { if (a.courseId == courseId) {
total += a.weight;

}

}

return total;

}

Grade calculateGrade(int studentId, int courseId) { double totalWeight = getTotalWeight(courseId); if (Math.abs(totalWeight - 100.0) > 0.001) {
System.out.println("Assessment weightage must total 100."); return null;
}

double totalMarks = 0;

int assessmentCount = 0;

for (Assessment a : assessments.values()) { if (a.courseId == courseId) {
MarkEntry m = marks.get( key(studentId, courseId, a.id)
);
 

 
if (m == null) {

System.out.println("Marks missing for assessment: "

+ a.name);

return null;

}

totalMarks += m.marks * a.weight / 100.0; assessmentCount++;
}

}

if (assessmentCount == 0) {

System.out.println("No assessments created for this course."); return null;
}

return new Grade(totalMarks);

}

void publishGrades(int courseId) {

if (!courses.containsKey(courseId)) { System.out.println("Course not found."); return;
}

boolean found = false;

for (Student s : students.values()) {

if (s.registeredCourses.contains(courseId)) { Grade g = calculateGrade(s.id, courseId); if (g != null) {
publishedGrades.put(gradeKey(s.id, courseId), g); found = true;
 

 
}

}

}

if (found) {

System.out.println("Grades published successfully.");

} else {

System.out.println("No grades could be published.");

}

}

void showGradeSheet(int courseId) { Course course = courses.get(courseId); if (course == null) {
System.out.println("Course not found."); return;
}

System.out.println("\nGRADE SHEET: " + course.name); System.out.println("	");
System.out.printf("%-10s %-18s %-10s %-8s%n", "StudentID", "StudentName", "Total", "Grade");
for (Student s : students.values()) {

if (s.registeredCourses.contains(courseId)) {

Grade g = publishedGrades.get(gradeKey(s.id, courseId)); if (g != null) {
System.out.printf("%-10d %-18s %-10.2f %-8s%n", s.id, s.name, g.total, g.letter);
}

}
 

 
}

}

void generateTranscript(int studentId) { Student s = students.get(studentId); if (s == null) {
System.out.println("Student not found."); return;
}

double creditPoints = 0; int totalCredits = 0;
System.out.println("\n========== TRANSCRIPT =========="); System.out.println("Student ID : " + s.id); System.out.println("Name		: " + s.name); System.out.println("Term	: " + s.term);
System.out.println("	");

System.out.printf("%-8s %-18s %-10s %-8s %-8s%n", "CourseID", "Course", "Total", "Grade", "Credits");
for (int courseId : s.registeredCourses) { Course c = courses.get(courseId);
Grade g = publishedGrades.get(gradeKey(s.id, courseId)); if (g != null) {
System.out.printf("%-8d %-18s %-10.2f %-8s %-8d%n", c.id, c.name, g.total, g.letter, c.credits);
creditPoints += g.points * c.credits; totalCredits += c.credits;
}

}
 

 


if (totalCredits > 0) {

double gpa = creditPoints / totalCredits; System.out.printf("GPA : %.2f%n", gpa); System.out.printf("CGPA : %.2f%n", gpa);
} else {

System.out.println("GPA/CGPA unavailable. Publish grades first.");

} System.out.println("==========================================");
}

void showAssessmentAnalytics(int courseId) { if (!courses.containsKey(courseId)) {
System.out.println("Course not found."); return;
}

System.out.println("\nASSESSMENT ANALYTICS"); System.out.println("	");
for (Assessment a : assessments.values()) { if (a.courseId == courseId) {
int count = 0; double sum = 0;
for (Student s : students.values()) {

if (s.registeredCourses.contains(courseId)) { MarkEntry m = marks.get(
key(s.id, courseId, a.id)

);
 

 
if (m != null) {

sum += m.marks; count++;
}

}

}

double average = count > 0 ? sum / count : 0; System.out.println("Assessment: " + a.name); System.out.println("Weightage : " + a.weight + "%"); System.out.println("Marks count: " + count); System.out.printf("Average marks: %.2f%n", average); System.out.println("	");
}

}

}

void showExamSchedule() { if (schedules.isEmpty()) {
System.out.println("No exams scheduled."); return;
}

System.out.println("\nEXAM SCHEDULE"); for (ExamSchedule e : schedules.values()) {
Course c = courses.get(e.courseId); System.out.println("Course: " + c.name
+ " | Date: " + e.date

+ " | Time: " + e.time);
}
}

}
