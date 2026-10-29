import java.util.*;
class Course { int id;
String name; int credits;
Course(int id, String name, int credits) { this.id = id;
this.name = name; this.credits = credits;
}

}

class Student { int id;
String name; String term;
Set<Integer> registeredCourses = new HashSet<>(); Student(int id, String name, String term) {
this.id = id; this.name = name; this.term = term;
}

}

class Assessment { int id;
int courseId;
String name; double weight;
Assessment(int id, int courseId, String name, double weight) { this.id = id;
this.courseId = courseId; this.name = name; this.weight = weight;
}

}



class ExamSchedule { int courseId;
String date; String time;
ExamSchedule(int courseId, String date, String time) { this.courseId = courseId;
this.date = date; this.time = time;
}

}

class MarkEntry { int studentId; int courseId;
int assessmentId; double marks;
MarkEntry(int studentId, int courseId,

int assessmentId, double marks) {
this.studentId = studentId; this.courseId = courseId; this.assessmentId = assessmentId; this.marks = marks;
}

}

class Grade { double total; String letter; int points;
Grade(double total) { this.total = total;
if (total >= 90) { letter = "O"; points = 10;
} else if (total >= 80) { letter = "A+"; points = 9;
} else if (total >= 70) { letter = "A";
points = 8;

} else if (total >= 60) { letter = "B+"; points = 7;
} else if (total >= 50) { letter = "B";
points = 6;
} else if (total >= 40) { letter = "C";
points = 5;

} else {

letter = "F"; points = 0;
}

}

}



class Transcript { Student student;
Map<Integer, Grade> grades = new HashMap<>(); Transcript(Student student) {
this.student = student;

}

}



