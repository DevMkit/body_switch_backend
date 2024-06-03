package kr.co.softhubglobal.service;

import kr.co.softhubglobal.dto.course.CourseTicketDTO;
import kr.co.softhubglobal.dto.course.CourseTicketInfoMapper;
import kr.co.softhubglobal.entity.branch.Branch;
import kr.co.softhubglobal.entity.course.*;
import kr.co.softhubglobal.entity.employee.Employee;
import kr.co.softhubglobal.exception.customExceptions.ResourceNotFoundException;
import kr.co.softhubglobal.repository.BranchRepository;
import kr.co.softhubglobal.repository.CourseTicketRepository;
import kr.co.softhubglobal.repository.EmployeeRepository;
import kr.co.softhubglobal.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseTicketService {

    private final CourseTicketRepository courseTicketRepository;
    private final BranchRepository branchRepository;
    private final EmployeeRepository employeeRepository;
    private final CourseTicketInfoMapper courseTicketInfoMapper;
    private final ObjectValidator<CourseTicketDTO.CourseTicketCreateRequest> courseTicketCreateRequestObjectValidator;
    private final MessageSource messageSource;

    public List<CourseTicketDTO.CourseTicketInfo> getAllCourseTickets() {
        return courseTicketRepository.findAll()
                .stream()
                .map(courseTicketInfoMapper)
                .toList();
    }

    public void createCourseTicket(CourseTicketDTO.CourseTicketCreateRequest courseTicketCreateRequest) {

        courseTicketCreateRequestObjectValidator.validate(courseTicketCreateRequest);

        Branch branch = branchRepository.findById(courseTicketCreateRequest.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("branch.id.not.exist", new Object[]{courseTicketCreateRequest.getBranchId()}, Locale.ENGLISH)));

        CourseTicket courseTicket = new CourseTicket();
        courseTicket.setBranch(branch);
        courseTicket.setClassType(courseTicketCreateRequest.getCourseClassType());
        courseTicket.setTicketName(courseTicketCreateRequest.getTicketName());
        courseTicket.setRegularPrice(courseTicketCreateRequest.getRegularPrice());
        courseTicket.setDiscountRate(courseTicketCreateRequest.getDiscountRate());
        courseTicket.setFinalPrice(courseTicketCreateRequest.getFinalPrice());
        courseTicket.setSaleStartDate(courseTicketCreateRequest.getSaleStartDate());
        courseTicket.setSaleEndDate(courseTicketCreateRequest.getSaleEndDate());
        courseTicket.setHasSaleEndDate(courseTicketCreateRequest.isHasSaleEndDate());
        courseTicket.setSaleStatus(SaleStatus.APPROVAL_PENDING);
        courseTicket.setUsagePeriod(courseTicketCreateRequest.getUsagePeriod());
        courseTicket.setUsageCount(courseTicketCreateRequest.getUsageCount());
        courseTicket.setCourseTrainers(
                courseTicketCreateRequest.getTrainers()
                        .stream()
                        .map(trainerId -> {
                            Optional<Employee> optionalTrainer = employeeRepository.findById(trainerId);
                            return optionalTrainer.map(employee -> CourseTrainer.builder()
                                    .courseTicket(courseTicket)
                                    .employee(employee)
                                    .build()).orElse(null);
                        })
                        .toList()
        );
        courseTicket.setCourseTicketImages(
                courseTicketCreateRequest.getImages()
                        .stream()
                        .map(image -> CourseTicketImage.builder()
                                .courseTicket(courseTicket)
                                .imageUrl(image)
                                .build())
                        .toList()
        );
        courseTicket.setCourseCurriculumList(
                courseTicketCreateRequest.getCurriculums()
                        .stream()
                        .map(courseTicketCurriculumCreateInfo -> CourseCurriculum.builder()
                                .courseTicket(courseTicket)
                                .title(courseTicketCurriculumCreateInfo.getTitle())
                                .summary(courseTicketCurriculumCreateInfo.getSummary())
                                .imageUrl(courseTicketCurriculumCreateInfo.getImage())
                                .build())
                        .toList()
        );
        courseTicket.setClassDetail(courseTicketCreateRequest.getClassDetail());

        courseTicketRepository.save(courseTicket);
    }
}
