package kr.co.softhubglobal.service;

import kr.co.softhubglobal.dto.employee.EmployeeDTO;
import kr.co.softhubglobal.dto.employee.EmployeeInfoMapper;
import kr.co.softhubglobal.entity.branch.Branch;
import kr.co.softhubglobal.entity.employee.Employee;
import kr.co.softhubglobal.entity.employee.EmployeeResponsibility;
import kr.co.softhubglobal.entity.user.Role;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.exception.customExceptions.DuplicateResourceException;
import kr.co.softhubglobal.exception.customExceptions.ResourceNotFoundException;
import kr.co.softhubglobal.repository.BranchRepository;
import kr.co.softhubglobal.repository.EmployeeRepository;
import kr.co.softhubglobal.repository.UserRepository;
import kr.co.softhubglobal.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
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
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;
    private final ObjectValidator<EmployeeDTO.EmployeeCreateRequest> employeeCreateRequestObjectValidator;

    public List<EmployeeDTO.EmployeeInfo> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeInfoMapper)
                .toList();
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
        employee.setBirthDate(employeeCreateRequest.getBirthDate());
        employee.setGender(employeeCreateRequest.getGender());
        employee.setGender(employeeCreateRequest.getGender());
        employee.setBranch(branch);
        employee.setIntroduction(employee.getIntroduction());
        employee.setResponsibilities(employeeCreateRequest.getResponsibilities()
                .stream()
                .map(responsibility -> EmployeeResponsibility.builder()
                        .employee(employee)
                        .name(responsibility.toString())
                        .build()
                )
                .toList()
        );
        employee.setEmployeeClassification(employeeCreateRequest.getEmployeeClassification());
        employee.setHiredDate(employeeCreateRequest.getHiredDate());
        employee.setLeftDate(employeeCreateRequest.getLeftDate());

        employeeRepository.save(employee);
    }
}
