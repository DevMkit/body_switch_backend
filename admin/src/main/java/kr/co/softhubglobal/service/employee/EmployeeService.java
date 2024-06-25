package kr.co.softhubglobal.service.employee;

import kr.co.softhubglobal.dto.PageableDTO;
import kr.co.softhubglobal.dto.employee.EmployeeDTO;
import kr.co.softhubglobal.dto.employee.EmployeeDetailInfoMapper;
import kr.co.softhubglobal.dto.employee.EmployeeInfoMapper;
import kr.co.softhubglobal.entity.branch.Branch;
import kr.co.softhubglobal.entity.branch.BranchWorkHours;
import kr.co.softhubglobal.entity.employee.Employee;
import kr.co.softhubglobal.entity.employee.EmployeeResponsibility;
import kr.co.softhubglobal.entity.employee.Responsibilities;
import kr.co.softhubglobal.entity.user.Role;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.exception.customExceptions.DuplicateResourceException;
import kr.co.softhubglobal.exception.customExceptions.ResourceNotFoundException;
import kr.co.softhubglobal.repository.BranchRepository;
import kr.co.softhubglobal.repository.EmployeeRepository;
import kr.co.softhubglobal.repository.UserRepository;
import kr.co.softhubglobal.utils.FileUploader;
import kr.co.softhubglobal.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final BranchRepository branchRepository;
    private final EmployeeInfoMapper employeeInfoMapper;
    private final EmployeeDetailInfoMapper employeeDetailInfoMapper;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;
    private final ObjectValidator<EmployeeDTO.EmployeeCreateRequest> employeeCreateRequestObjectValidator;
    private final ObjectValidator<EmployeeDTO.EmployeeUpdateRequest> employeeUpdateRequestObjectValidator;

    public PageableDTO.Response getAllEmployees(EmployeeDTO.EmployeeSearchRequest employeeSearchRequest) {

        Specification<Employee> specification = Specification
                .where(EmployeeSpecifications.employeeBranchIdEqual(employeeSearchRequest.getBranchId()))
                .and(EmployeeSpecifications.employeeNameLike(employeeSearchRequest.getName()))
                .and(EmployeeSpecifications.responsibilitiesIn(employeeSearchRequest.getResponsibilities()))
                .and(EmployeeSpecifications.classificationIn(employeeSearchRequest.getClassifications()));

        PageRequest pageRequest = PageRequest.of(
                employeeSearchRequest.getPage() - 1,
                employeeSearchRequest.getLimit(),
                Sort.by("registeredDate").descending()
        );
        Page<Employee> result = employeeRepository.findAll(specification, pageRequest);

        PageableDTO.Response response = new PageableDTO.Response();
        response.setTotalElements(result.getTotalElements());
        response.setNumber(result.getNumber() + 1);
        response.setSize(result.getSize());
        response.setContent(
                result.getContent()
                        .stream()
                        .map(employeeInfoMapper)
                        .toList()
        );
        return response;
    }

    public void createEmployee(EmployeeDTO.EmployeeCreateRequest employeeCreateRequest) {
        employeeCreateRequestObjectValidator.validate(employeeCreateRequest);

        if(userRepository.existsByUsernameAndRoleIn(employeeCreateRequest.getUsername(), List.of(Role.MEMBER, Role.EMPLOYEE))) {
            throw new DuplicateResourceException(
                    messageSource.getMessage("employee.username.already.exists", new Object[]{employeeCreateRequest.getUsername()}, Locale.ENGLISH));
        }

        Branch branch = branchRepository.findById(employeeCreateRequest.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("branch.id.not.exist", new Object[]{employeeCreateRequest.getBranchId()}, Locale.ENGLISH)));

        Employee employee = new Employee();
        employee.setUser(User.builder()
                .username(employeeCreateRequest.getUsername())
                .name(employeeCreateRequest.getName())
                .phoneNumber(employeeCreateRequest.getPhoneNumber())
                .email(employeeCreateRequest.getEmail())
                .password(passwordEncoder.encode(employeeCreateRequest.getPassword()))
                .role(Role.EMPLOYEE)
                .build()
        );
        employee.setProfileImage(FileUploader.uploadFile(employeeCreateRequest.getProfileImage()));
        employee.setBirthDate(employeeCreateRequest.getBirthDate());
        employee.setGender(employeeCreateRequest.getGender());
        employee.setBranch(branch);
        employee.setIntroduction(employee.getIntroduction());
        employee.setResponsibilities(employeeCreateRequest.getResponsibilities()
                .stream()
                .map(responsibility -> EmployeeResponsibility.builder()
                        .employee(employee)
                        .name(Responsibilities.valueOf(responsibility))
                        .build()
                )
                .toList()
        );
        employee.setEmployeeClassification(employeeCreateRequest.getEmployeeClassification());
        employee.setHiredDate(employeeCreateRequest.getHiredDate());
        employee.setLeftDate(employeeCreateRequest.getLeftDate());

        employeeRepository.save(employee);
    }

    public EmployeeDTO.EmployeeDetailInfo getEmployeeDetailInfoById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .map(employeeDetailInfoMapper)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("employee.id.not.exist", new Object[]{employeeId}, Locale.ENGLISH)));
    }

    public void updateEmployeeById(
            Long employeeId,
            EmployeeDTO.EmployeeUpdateRequest employeeUpdateRequest
    ) {
        employeeUpdateRequestObjectValidator.validate(employeeUpdateRequest);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("employee.id.not.exist", new Object[]{employeeId}, Locale.ENGLISH)));

        if(employeeUpdateRequest.getProfileImage() != null) {
            employee.setProfileImage(FileUploader.uploadFile(employeeUpdateRequest.getProfileImage()));
        }
        if(employeeUpdateRequest.getName() != null) {
            employee.getUser().setName(employeeUpdateRequest.getName());
        }
        if(employeeUpdateRequest.getPhoneNumber() != null) {
            employee.getUser().setPhoneNumber(employeeUpdateRequest.getPhoneNumber());
        }
        if(employeeUpdateRequest.getPassword() != null) {
            employee.getUser().setPassword(passwordEncoder.encode(employeeUpdateRequest.getPassword()));
        }
        if(employeeUpdateRequest.getBirthDate() != null) {
            employee.setBirthDate(employeeUpdateRequest.getBirthDate());
        }
        if(employeeUpdateRequest.getEmail() != null) {
            employee.getUser().setEmail(employeeUpdateRequest.getEmail());
        }
        if(employeeUpdateRequest.getGender() != null) {
            employee.setGender(employeeUpdateRequest.getGender());
        }
        if(employeeUpdateRequest.getIntroduction() != null) {
            employee.setIntroduction(employeeUpdateRequest.getIntroduction());
        }
        if(employeeUpdateRequest.getResponsibilities() != null && !employeeUpdateRequest.getResponsibilities().isEmpty()) {
            employee.getResponsibilities().clear();
            employee.getResponsibilities().addAll(
                    employeeUpdateRequest.getResponsibilities()
                            .stream()
                            .map(responsibility -> EmployeeResponsibility.builder()
                                    .employee(employee)
                                    .name(Responsibilities.valueOf(responsibility))
                                    .build())
                            .toList()
            );
        }
        if(employeeUpdateRequest.getEmployeeClassification() != null) {
            employee.setEmployeeClassification(employeeUpdateRequest.getEmployeeClassification());
        }
        if(employeeUpdateRequest.getHiredDate() != null) {
            employee.setHiredDate(employeeUpdateRequest.getHiredDate());
        }
        if(employeeUpdateRequest.getLeftDate() != null) {
            employee.setLeftDate(employeeUpdateRequest.getLeftDate());
        }
        employeeRepository.save(employee);
    }
}
