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



UniversityGradingSystem.java import java.util.Scanner;
public class UniversityGradingSystem {

static Scanner sc = new Scanner(System.in);

static UniversityService service = new UniversityService(); static int readInt(String message) {
System.out.print(message);

return Integer.parseInt(sc.nextLine());

}

static double readDouble(String message) { System.out.print(message);
return Double.parseDouble(sc.nextLine());

}

static String readText(String message) { System.out.print(message);
return sc.nextLine();

}

public static void main(String[] args) { int choice;
do {

System.out.println("\n===== UNIVERSITY EXAMS & GRADING SYSTEM =====");

System.out.println("1. Add Course"); System.out.println("2. Add Student"); System.out.println("3. Register Student for Course");
 

 
System.out.println("4. Create Assessment"); System.out.println("5. Schedule Exam"); System.out.println("6. Record Marks"); System.out.println("7. Publish Grades"); System.out.println("8. Show Grade Sheet"); System.out.println("9. Generate Transcript"); System.out.println("10. Assessment Analytics"); System.out.println("11. Resit / Improvement Eligibility"); System.out.println("12. View Exam Schedule"); System.out.println("0. Exit");
try {

choice = readInt("Enter your choice: "); switch (choice) {
case 1: {

int id = readInt("Enter course ID: ");

String name = readText("Enter course name: "); int credits = readInt("Enter credits: "); service.addCourse(id, name, credits);
break;

}

case 2: {

int id = readInt("Enter student ID: ");

String name = readText("Enter student name: "); String term = readText("Enter term/semester: "); service.addStudent(id, name, term);
break;

}
 

 


case 3: {

int studentId = readInt("Enter student ID: "); int courseId = readInt("Enter course ID: "); service.registerStudent(studentId, courseId); break;
}

case 4: {

int courseId = readInt("Enter course ID: ");

String name = readText("Enter assessment name: "); double weight = readDouble("Enter weightage (%): "); service.createAssessment(courseId, name, weight); break;
}

case 5: {

int courseId = readInt("Enter course ID: ");

String date = readText("Enter exam date (DD-MM-YYYY): "); String time = readText("Enter exam time: "); service.scheduleExam(courseId, date, time);
break;

}

case 6: {

int studentId = readInt("Enter student ID: "); int courseId = readInt("Enter course ID: ");
int assessmentId = readInt("Enter assessment ID: "); double marks = readDouble("Enter marks (0-100): ");
 

 
service.recordMarks(

studentId, courseId, assessmentId, marks

);

break;

}

case 7: {

int courseId = readInt("Enter course ID: "); service.publishGrades(courseId);
break;

}

case 8: {

int courseId = readInt("Enter course ID: "); service.showGradeSheet(courseId); break;
}

case 9: {

int studentId = readInt("Enter student ID: "); service.generateTranscript(studentId); break;
}

case 10: {

int courseId = readInt("Enter course ID: "); service.showAssessmentAnalytics(courseId); break;
}

case 11: {

int studentId = readInt("Enter student ID: ");
 

 
int courseId = readInt("Enter course ID: "); Grade g = service.publishedGrades.get(
studentId + "-" + courseId

);

if (g == null) { System.out.println(
"Publish grades first to check eligibility."

);

} else if (g.total < 40) { System.out.println(
"Student is eligible for RESIT."

);

} else if (g.total < 90) { System.out.println(
"Student is eligible for IMPROVEMENT."

);

} else {

System.out.println(

"No resit/improvement needed."

);

}

System.out.println(

"To update marks, use Record Marks and publish again."

);

break;

}
 

 
case 12: { service.showExamSchedule(); break;
}

case 0:

System.out.println("Exiting the system. Thank you!"); break;
default:

System.out.println("Invalid choice. Try again.");

}

} catch (NumberFormatException e) { System.out.println("Invalid input. Enter a valid number."); choice = -1;
} catch (Exception e) {

System.out.println("Error: " + e.getMessage()); choice = -1;
}

} while (choice != 0); sc.close();
}

}







