package validation;

import domain.Student;

import java.util.Objects;

public class StudentValidator implements Validator<Student> {

    /**
     * Valideaza un student
     * @param entity - studentul pe care il valideaza
     * @throws ValidationException - daca studentul nu e valid
     */
    @Override
    public void validate(Student entity) throws ValidationException {
        if(Objects.isNull(entity.getID()) || entity.getID().isEmpty()){
            throw new ValidationException("Id incorect!");
        }
        if(entity.getGrupa() < 0) {
            throw new ValidationException("Grupa incorecta!");
        }
        if(Objects.isNull(entity.getEmail()) || entity.getEmail().isEmpty()) {
            throw new ValidationException("Email incorect!");
        }
        if(Objects.isNull(entity.getNume()) || entity.getNume().isEmpty()) {
            throw new ValidationException("Nume incorect!");
        }
    }
}
