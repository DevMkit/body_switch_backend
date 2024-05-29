package kr.co.softhubglobal.repository;

import kr.co.softhubglobal.entity.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
