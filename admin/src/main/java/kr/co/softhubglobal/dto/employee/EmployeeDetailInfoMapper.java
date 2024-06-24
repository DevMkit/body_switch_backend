package kr.co.softhubglobal.dto.employee;

import kr.co.softhubglobal.entity.employee.Employee;
import kr.co.softhubglobal.entity.employee.EmployeeResponsibility;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class EmployeeDetailInfoMapper implements Function<Employee, EmployeeDTO.EmployeeDetailInfo> {

    @Override
    public EmployeeDTO.EmployeeDetailInfo apply(Employee employee) {
        return new EmployeeDTO.EmployeeDetailInfo(
                employee.getId(),
                employee.getProfileImage(),
                employee.getUser().getUsername(),
                employee.getUser().getName(),
                employee.getBranch().getBranchName(),
                employee.getUser().getPhoneNumber(),
                employee.getBirthDate(),
                employee.getGender(),
                employee.getUser().getEmail(),
                employee.getIntroduction(),
                employee.getResponsibilities().stream().map(EmployeeResponsibility::getName).toList(),
                employee.getEmployeeClassification(),
                employee.getHiredDate(),
                employee.getLeftDate()
        );
    }
}
