package kr.co.softhubglobal.service.courseTicket;

import kr.co.softhubglobal.dto.PageableDTO;
import kr.co.softhubglobal.dto.course.CourseTicketDTO;
import kr.co.softhubglobal.dto.course.CourseTicketInfoMapper;
import kr.co.softhubglobal.entity.branch.Branch;
import kr.co.softhubglobal.entity.course.*;
import kr.co.softhubglobal.entity.employee.Employee;
import kr.co.softhubglobal.exception.customExceptions.ResourceNotFoundException;
import kr.co.softhubglobal.repository.BranchRepository;
import kr.co.softhubglobal.repository.CourseTicketRepository;
import kr.co.softhubglobal.repository.EmployeeRepository;
import kr.co.softhubglobal.utils.FileUploader;
import kr.co.softhubglobal.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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

    public PageableDTO.Response getAllCourseTickets(CourseTicketDTO.CourseTicketSearchRequest courseTicketSearchRequest) {

        Specification<CourseTicket> specification = Specification
                .where(CourseTicketSpecifications.courseTicketBranchCenterIdOrHeadCenterIdEqual(courseTicketSearchRequest.getHeadBranchId()))
                .and(CourseTicketSpecifications.courseTicketBranchIdEqual(courseTicketSearchRequest.getBranchId()))
                .and(CourseTicketSpecifications.courseTicketNameLike(courseTicketSearchRequest.getTicketName()))
                .and(CourseTicketSpecifications.courseTicketClassTypeIn(courseTicketSearchRequest.getClassTypes()))
                .and(CourseTicketSpecifications.courseTicketSaleStatusIn(courseTicketSearchRequest.getSaleStatuses()));

        PageRequest pageRequest = PageRequest.of(courseTicketSearchRequest.getPage() - 1, courseTicketSearchRequest.getLimit());

        Page<CourseTicket> result = courseTicketRepository.findAll(specification, pageRequest);

        PageableDTO.Response response = new PageableDTO.Response();
        response.setTotalElements(result.getTotalElements());
        response.setNumber(result.getNumber() + 1);
        response.setSize(result.getSize());
        response.setContent(
                result.getContent()
                        .stream()
                        .map(courseTicketInfoMapper)
                        .toList()
        );

        return response;
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
        if(courseTicketCreateRequest.getTrainers() != null && !courseTicketCreateRequest.getTrainers().isEmpty()) {
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
        }
        if(courseTicketCreateRequest.getImages() != null && !courseTicketCreateRequest.getImages().isEmpty()) {
            courseTicket.setCourseTicketImages(
                    courseTicketCreateRequest.getImages()
                            .stream()
                            .map(image -> CourseTicketImage.builder()
                                    .courseTicket(courseTicket)
                                    .imageUrl(FileUploader.uploadFile(image))
                                    .build())
                            .toList()
            );
        }
        if(courseTicketCreateRequest.getCurriculums() != null && !courseTicketCreateRequest.getCurriculums().isEmpty()) {
            courseTicket.setCourseCurriculumList(
                    courseTicketCreateRequest.getCurriculums()
                            .stream()
                            .map(courseTicketCurriculumCreateInfo -> CourseCurriculum.builder()
                                    .courseTicket(courseTicket)
                                    .title(courseTicketCurriculumCreateInfo.getTitle())
                                    .summary(courseTicketCurriculumCreateInfo.getSummary())
                                    .imageUrl(FileUploader.uploadFile(courseTicketCurriculumCreateInfo.getImage()))
                                    .build())
                            .toList()
            );
        }
        courseTicket.setClassDetail(courseTicketCreateRequest.getClassDetail());

        courseTicketRepository.save(courseTicket);
    }
}
