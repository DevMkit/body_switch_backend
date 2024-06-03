package kr.co.softhubglobal.service;

import kr.co.softhubglobal.dto.course.CourseClassDTO;
import kr.co.softhubglobal.entity.branch.Branch;
import kr.co.softhubglobal.entity.branch.BranchExerciseRoom;
import kr.co.softhubglobal.entity.course.CourseClass;
import kr.co.softhubglobal.entity.course.CourseClassTime;
import kr.co.softhubglobal.entity.course.CourseTicket;
import kr.co.softhubglobal.entity.employee.Employee;
import kr.co.softhubglobal.exception.customExceptions.DuplicateResourceException;
import kr.co.softhubglobal.exception.customExceptions.ResourceNotFoundException;
import kr.co.softhubglobal.repository.*;
import kr.co.softhubglobal.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CourseClassService {

    private final CourseClassRepository courseClassRepository;
    private final CourseClassTimeRepository courseClassTimeRepository;
    private final BranchRepository branchRepository;
    private final BranchExerciseRoomRepository branchExerciseRoomRepository;
    private final EmployeeRepository employeeRepository;
    private final CourseTicketRepository courseTicketRepository;
    private final ObjectValidator<CourseClassDTO.CourseClassCreateRequest> courseClassCreateRequestObjectValidator;
    private final ObjectValidator<CourseClassDTO.CourseClassTimeCreateInfo> courseClassTimeCreateInfoObjectValidator;
    private final MessageSource messageSource;

    public void createCourseClass(CourseClassDTO.CourseClassCreateRequest courseClassCreateRequest) {

        courseClassCreateRequestObjectValidator.validate(courseClassCreateRequest);
        courseClassCreateRequest.getClassTimeInfoList().forEach(courseClassTimeCreateInfoObjectValidator::validate);

        Branch branch = branchRepository.findById(courseClassCreateRequest.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("branch.id.not.exist", new Object[]{courseClassCreateRequest.getBranchId()}, Locale.ENGLISH)));

        BranchExerciseRoom branchExerciseRoom = branchExerciseRoomRepository.findById(courseClassCreateRequest.getExerciseRoomId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("branch.exercise.room.id.not.exist", new Object[]{courseClassCreateRequest.getExerciseRoomId()}, Locale.ENGLISH)));

        Employee employee = employeeRepository.findById(courseClassCreateRequest.getTrainerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("employee.id.not.exist", new Object[]{courseClassCreateRequest.getTrainerId()}, Locale.ENGLISH)));

        CourseTicket courseTicket = courseTicketRepository.findById(courseClassCreateRequest.getTicketId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("course.ticket.id.not.exist", new Object[]{courseClassCreateRequest.getTicketId()}, Locale.ENGLISH)));

        courseClassCreateRequest.getClassTimeInfoList().forEach(courseClassTimeCreateInfo -> {
            if(!courseClassTimeRepository.existsByScheduledClassTime(
                    branchExerciseRoom.getId(),
                    courseClassCreateRequest.getRegistrationStartDate(),
                    courseClassCreateRequest.getRegistrationEndDate(),
                    courseClassTimeCreateInfo.getDayOfWeek().toString(),
                    courseClassTimeCreateInfo.getStartTime(),
                    courseClassTimeCreateInfo.getEndTime()
            ).isEmpty()) {
                throw new DuplicateResourceException(
                        messageSource.getMessage(
                                "course.class.time.already.exist",
                                new Object[]{
                                        branchExerciseRoom.getId(),
                                        courseClassCreateRequest.getRegistrationStartDate(),
                                        courseClassCreateRequest.getRegistrationEndDate(),
                                        courseClassTimeCreateInfo.getDayOfWeek().toString(),
                                        courseClassTimeCreateInfo.getStartTime(),
                                        courseClassTimeCreateInfo.getEndTime()
                                },
                                Locale.ENGLISH)
                );
            }
        });

        CourseClass courseClass = new CourseClass();
        courseClass.setBranch(branch);
        courseClass.setClassType(courseClassCreateRequest.getCourseClassType());
        courseClass.setClassName(courseClass.getClassName());
        courseClass.setExerciseRoom(branchExerciseRoom);
        courseClass.setRegistrationStartDate(courseClassCreateRequest.getRegistrationStartDate());
        courseClass.setRegistrationEndDate(courseClassCreateRequest.getRegistrationEndDate());
        courseClass.setEmployee(employee);
        courseClass.setCourseTicket(courseTicket);
        courseClass.setPrivateMemberId(courseClassCreateRequest.getMemberId());
        courseClass.setMembersMaxCount(courseClassCreateRequest.getMembersMaxCount());
        courseClass.setMemo(courseClassCreateRequest.getMemo());
        courseClass.setCourseClassTimeList(courseClassCreateRequest.getClassTimeInfoList()
                .stream()
                .map(courseClassTimeCreateInfo -> CourseClassTime.builder()
                        .courseClass(courseClass)
                        .dayOfWeek(courseClassTimeCreateInfo.getDayOfWeek())
                        .startTime(courseClassTimeCreateInfo.getStartTime())
                        .endTime(courseClassTimeCreateInfo.getEndTime())
                        .build())
                .toList());

        courseClassRepository.save(courseClass);
    }
}
