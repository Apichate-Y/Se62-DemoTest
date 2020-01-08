package com.example.demo;

import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class StudentTests {

    private Validator validator;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // ตั้งชื่อ test ให้สอดคล้องกับสิ่งที่ต้อง test
    @Test
    void testStudenIdOKWith8Digits() { // ใส่ข้อมูลปกติ
        Student student = new Student();
        student.setFirstName("Apichate");
        student.setLastName("Yatra");
        student.setStudentId("B5917099");

        student = studentRepository.saveAndFlush(student);

        Optional<Student> found = studentRepository.findById(student.getId());
        assertEquals("Apichate", found.get().getFirstName());
        assertEquals("Yatra", found.get().getLastName());
        assertEquals("B5917099", found.get().getStudentId());
    }

    @Test
    void testStudentLastNameMustNotBeNull() { // นามสกุลมีค่า null
        Student student = new Student();
        student.setFirstName("Apichate");
        student.setLastName(null);
        student.setStudentId("B5917099");

        Set<ConstraintViolation<Student>> result = validator.validate(student);

        // result ต้องมี error 1 ค่าเท่านั้น
        assertEquals(1, result.size());

        // error message ตรงชนิด และถูก field
        ConstraintViolation<Student> v = result.iterator().next();
        assertEquals("must not be null", v.getMessage());
        assertEquals("lastName", v.getPropertyPath().toString());
    }

    @Test
    void testStudentIdMustNotBe7Digits() { //รหัสนักศึกษา 7 หลัก
        Student student = new Student();
        student.setFirstName("Apichate");
        student.setLastName("Yatra");
        student.setStudentId("B591709");

        Set<ConstraintViolation<Student>> result = validator.validate(student);

        // result ต้องมี error 1 ค่าเท่านั้น
        assertEquals(1, result.size());

        // error message ตรงชนิด และถูก field
        ConstraintViolation<Student> v = result.iterator().next();
        assertEquals("must match \"[BMD]\\d{7}\"", v.getMessage());
        assertEquals("studentId", v.getPropertyPath().toString());
    }

    @Test
    void testStudentIdMustNotBe9Digits() { //รหัสนักศึกษา 9 หลัก
        Student student = new Student();
        student.setFirstName("Apichate");
        student.setLastName("Yatra");
        student.setStudentId("B59170999");

        Set<ConstraintViolation<Student>> result = validator.validate(student);

        // result ต้องมี error 1 ค่าเท่านั้น
        assertEquals(1, result.size());

        // error message ตรงชนิด และถูก field
        ConstraintViolation<Student> v = result.iterator().next();
        assertEquals("must match \"[BMD]\\d{7}\"", v.getMessage());
        assertEquals("studentId", v.getPropertyPath().toString());
    }
}