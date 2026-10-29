import java.util.Scanner;
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
