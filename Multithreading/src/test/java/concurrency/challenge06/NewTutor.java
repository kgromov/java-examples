package concurrency.challenge06;

/**
 * Created by konstantin on 23.02.2019.
 */
class NewTutor {
    private NewStudent student;

    public void setStudent(NewStudent student) {
        this.student = student;
    }

    public void studyTime() {

        synchronized (this) {
            System.out.println("Tutor has arrived");
//            synchronized (student) {
                try {
                    // wait for student to arrive
                    this.wait();
                    // release own lock
                    // but still holds the student lock
                } catch (InterruptedException e) {

                }
                student.startStudy();
                System.out.println("Tutor is studying with student");
//            }
        }
    }

    public void getProgressReport() {
        // get progress report
        System.out.println("Tutor gave progress report");
    }
}
