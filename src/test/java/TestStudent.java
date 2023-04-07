import domain.Student;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

public class TestStudent {

    private Service service;

    @Before
    public void setup() {
        StudentValidator studentValidator = new StudentValidator();
        StudentXMLRepo studentXMLRepo = new StudentXMLRepo("fisiere/Studenti.xml");

        TemaValidator temaValidator = new TemaValidator();
        TemaXMLRepo temaXMLRepo = new TemaXMLRepo("fisiere/Teme.xml");

        NotaValidator notaValidator = new NotaValidator(studentXMLRepo, temaXMLRepo);
        NotaXMLRepo notaXMLRepo = new NotaXMLRepo("fisiere/Note.xml");

        service = new Service(studentXMLRepo, studentValidator, temaXMLRepo, temaValidator, notaXMLRepo, notaValidator);
    }

    @After
    public void teardown() {
        List<Student> students = StreamSupport.stream(service.getAllStudenti()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());

        for (Student s : students) {
            service.deleteStudent(s.getID());
        }
        service = null;
    }

    @Test
    public void testSaveStudent_groupNumberBelowLowerBound() {
        var student = new Student("001", "Name1", -1, "wtvr@mail.com");

        try {
            var res = service.addStudent(student);
        } catch (Exception e) {
            assertEquals("Grupa incorecta!", e.getMessage());
        }
    }

    @Test
    public void testSaveStudent_groupNumberAboveLowerBound() {
        var student = new Student("002", "Nume2", 0, "wtvr@dom.com");

        try {
            var res = service.addStudent(student);
            assertEquals(student, res);
        } catch (Exception e) {
        }

    }

    @Test
    public void testSaveStudent_nameNotEmpty() {
        var student = new Student("003", "Name3", 936, "name3@mail.com");

        try {
            var res = service.addStudent(student);
            assertEquals(student, res);
        } catch (Exception e) {
        }
    }

    @Test
    public void testSaveStudent_nameEmpty() {
        var student = new Student("a", "", 936, "a@mail.com");

        try {
            var res = service.addStudent(student);
        } catch (Exception e) {
            assertEquals("Nume incorect!", e.getMessage());
        }
    }

    @Test
    public void testSaveStudent_nameIsNull() {
        var student = new Student("a", null, 936, "a@mail.com");

        try {
            var res = service.addStudent(student);
        } catch (Exception e) {
            assertEquals("Nume incorect!", e.getMessage());
        }
    }

    @Test
    public void testSaveStudent_mailNotEmpty() {
        var student = new Student("013", "Name3", 936, "name13@mail.com");

        try {
            var res = service.addStudent(student);
            assertEquals(student, res);
        } catch (Exception e) {
        }
    }

    @Test
    public void testSaveStudent_mailEmpty() {
        var student = new Student("0014", "spe", 936, "");

        try {
            var res = service.addStudent(student);
        } catch (Exception e) {
            assertEquals("Email incorect!", e.getMessage());
        }
    }

    @Test
    public void testSaveStudent_mailIsNull() {
        var student = new Student("005", "spe", 936, null);

        try {
            var res = service.addStudent(student);
        } catch (Exception e) {
            assertEquals("Email incorect!", e.getMessage());
        }
    }

    @Test
    public void testSaveStudent_idNotEmptyAndNotDuplicate() {
        var student = new Student("004", "Name4", 936, "name3@mail.com");

        try {
            var res = service.addStudent(student);
            assertEquals(student, res);
        } catch (Exception e) {
        }

    }

    @Test
    public void testSaveStudent_idNotEmptyAndDuplicate() {
        var student = new Student("004", "b", 936, "b@mail.com");

        try {
            var firstSave = service.addStudent(student);
            assertEquals(student, firstSave);
            var secondSave = service.addStudent(student);
        } catch (Exception e) {
            assertEquals("Id incorect!", e.getMessage());
        }
    }

    @Test
    public void testSaveStudent_idEmpty() {
        var student = new Student("", "b", 936, "nothing@mail.com");

        try {
            var res = service.addStudent(student);
        } catch (Exception e) {
            assertEquals("Id incorect!", e.getMessage());
        }

        //assertNull(res);
    }

    @Test
    public void testSaveStudent_idIsNull() {
        var student = new Student(null, "b", 936, "nothing@mail.com");

        try {
            var res = service.addStudent(student);
        } catch (Exception e) {
            assertEquals("Id incorect!", e.getMessage());
        }

        List<Student> students = StreamSupport.stream(service.getAllStudenti().spliterator(), false).collect(Collectors.toList());
        assertEquals(0, students.stream().filter(s -> s.getID() == null).count());
    }
}
