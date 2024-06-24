package kr.co.softhubglobal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import kr.co.softhubglobal.entity.employee.Employee;
import org.springframework.lang.NonNull;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    @NonNull
    Page<Employee> findAll(@NonNull Specification<Employee> specification, @NonNull Pageable pageable);
}
