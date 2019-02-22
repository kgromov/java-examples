package concurrency.challenge05;

class Student {

    private Tutor tutor;

    Student(Tutor tutor) {
        this.tutor = tutor;
    }

    public synchronized void startStudy() {
        // study
        System.out.println("Student is studying");
    }

    public void handInAssignment() {
        synchronized (this) {
            tutor.getProgressReport();
            System.out.println("Student handed in assignment");
        }
    }
}
