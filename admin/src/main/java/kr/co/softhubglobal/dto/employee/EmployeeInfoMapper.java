package kr.co.softhubglobal.dto.employee;

import kr.co.softhubglobal.entity.employee.Employee;
import kr.co.softhubglobal.entity.employee.EmployeeResponsibilities;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class EmployeeInfoMapper implements Function<Employee, EmployeeDTO.EmployeeInfo> {

    @Override
    public EmployeeDTO.EmployeeInfo apply(Employee employee) {
        return new EmployeeDTO.EmployeeInfo(
                employee.getUser().getName(),
                employee.getGender().toString(),
                employee.getUser().getPhoneNumber(),
                employee.getResponsibilities().stream().map(EmployeeResponsibilities::getName).toList(),
                employee.getHiredDate(),
                employee.getEmployeeClassification().toString()
        );
    }
}
