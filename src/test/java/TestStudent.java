import domain.Student;
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

public class TestStudent {

    private static final String VALID_ID = "student#1234";
    private static final String INVALID_ID = null;

    private static final String INVALID_ID_EMPTY = "";
    private static final String VALID_NAME = "name123";
    private static final String INVALID_NAME = "";
    private static final int VALID_GROUP = 933;

    private static final String VALID_EMAIL = "mail@domain.com";

    private Service service;

    @Before
    public void setup() {
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        String filenameStudent = "fisiere/Studenti.xml";
        String filenameTema = "fisiere/Teme.xml";
        String filenameNota = "fisiere/Note.xml";

        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);

        List<Student> students = StreamSupport
                .stream(service.getAllStudenti().spliterator(), false)
                .collect(Collectors.toList());

        for (Student s: students) {
            service.deleteStudent(s.getID());
        }
    }

    // EC for studentId: {null, ""} = invalid, {any text}=valid

    @Test
    public void saveStudent_validId_studentSaved() {
        // arrange
        Student student = new Student(VALID_ID, VALID_NAME, VALID_GROUP, VALID_EMAIL);

        // act
        service.addStudent(student);

        // assert
        List<Student> students = StreamSupport
                .stream(service.getAllStudenti().spliterator(), false)
                .collect(Collectors.toList());

        Assert.assertEquals(1, students.stream().filter(s -> s.getID() == VALID_ID).count());
    }

    @Test
    public void saveStudent_invalidId_studentNotSaved() {
        // arrange
        Student student = new Student(INVALID_ID, VALID_NAME, VALID_GROUP, VALID_EMAIL);

        // act
        try {
            service.addStudent(student);

        }
        catch (Exception e) {

        }

        // assert

        List<Student> students = StreamSupport
                .stream(service.getAllStudenti().spliterator(), false)
                .collect(Collectors.toList());

        Assert.assertEquals(0, students.stream().filter(s -> s.getID() == VALID_ID).count());
    }
}
