package org.example.data;

import lombok.RequiredArgsConstructor;
import org.example.model.AcademicRecord;
import org.example.model.Student;
import org.example.model.StudyGroup;
import org.example.model.StudyPlan;
import org.example.service.AcademicRecordService;
import org.example.service.StudentService;
import org.example.service.StudyGroupService;
import org.example.service.StudyPlanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class FileDataLoader {
    private final StudyGroupService studyGroupService;
    private final StudyPlanService studyPlanService;
    private final StudentService studentService;
    private final AcademicRecordService academicRecordService;
    private String[] disciplines = {"физика", "математика", "русский", "литература", "геометрия", "информатика"};
    private String[] group = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

    public void loadStudyPlan() {
        for (int i = 0; i < disciplines.length; i++) {
            StudyPlan studyPlan = new StudyPlan();
            studyPlan.setName(disciplines[i]);
            studyPlanService.save(studyPlan);
        }
    }

    public void loadStudyGroup() {
        for (int i = 0; i < group.length; i++) {
            StudyGroup studyGroup = new StudyGroup();
            studyGroup.setName(group[i]);
            studyGroupService.save(studyGroup);
        }
    }

    public void loadData() {
        loadStudyPlan();
        loadStudyGroup();
        String firstName = null;
        String lastName = null;
        Integer age = 0;
        Long groupId = 1L;
        Long studentId = 1L;
        Long studyPlanId = 1L;
        try {
            List<String> allLines = Files.readAllLines(Paths.get("src/students.csv"));
            for (int i = 1; i < allLines.size(); i++) {
                String[] line = allLines.get(i).split(",");
                firstName = line[0];
                lastName = line[1];
                age = Integer.parseInt(line[2]);
                groupId = Long.parseLong(line[3]);
                Student student = new Student(firstName, lastName, age);
                student.setStudyGroup(studyGroupService.get(groupId));
                studentService.save(student);
                for (int j = 4; j < line.length; j++) {
                    AcademicRecord academicRecord = new AcademicRecord();
                    academicRecord.setGrade(Integer.parseInt(line[j]));
                    academicRecord.setStudent(studentService.get(studentId));
                    academicRecord.setStudyPlan(studyPlanService.get(studyPlanId));
                    academicRecordService.save(academicRecord);
                    studyPlanId++;
                }
                studyPlanId = 1L;
                studentId++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
