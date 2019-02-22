package concurrency.challenge05;

/**
 * Deadlock with over-synchronization
 */
public class DeadLock {

    public static void main(String[] args) {
        Tutor tutor = new Tutor();
        Student student = new Student(tutor);
        tutor.setStudent(student);

        Thread tutorThread = new Thread(tutor::studyTime);
        Thread studentThread = new Thread(student::handInAssignment);

        tutorThread.start();
        studentThread.start();
    }
}

