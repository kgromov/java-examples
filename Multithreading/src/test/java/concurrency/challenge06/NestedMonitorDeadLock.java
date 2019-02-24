package concurrency.challenge06;

/**
 * Created by konstantin on 23.02.2019.
 */
public class NestedMonitorDeadLock {
    public static void main(String[] args) {
        final NewTutor tutor = new NewTutor();
        final NewStudent student = new NewStudent(tutor);
        tutor.setStudent(student);

        Thread tutorThread = new Thread(() -> tutor.studyTime());
        Thread studentThread = new Thread(() -> student.handInAssignment());

        tutorThread.start();
        studentThread.start();
    }
}

