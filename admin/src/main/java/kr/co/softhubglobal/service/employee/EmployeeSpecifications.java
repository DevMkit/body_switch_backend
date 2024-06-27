package kr.co.softhubglobal.service.employee;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import kr.co.softhubglobal.entity.branch.Branch;
import kr.co.softhubglobal.entity.center.Center;
import kr.co.softhubglobal.entity.course.CourseTicket;
import kr.co.softhubglobal.entity.employee.Employee;
import kr.co.softhubglobal.entity.employee.EmployeeClassification;
import kr.co.softhubglobal.entity.employee.EmployeeResponsibility;
import kr.co.softhubglobal.entity.employee.Responsibilities;
import kr.co.softhubglobal.entity.member.Member;
import kr.co.softhubglobal.entity.user.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class EmployeeSpecifications {

    public static Specification<Employee> employeeBranchCenterIdOrHeadCenterIdEqual(Long headCenterId) {
        return (root, query, cb) -> {
            if (headCenterId == null) {
                return cb.conjunction();
            }
            Join<Employee, Branch> branchJoin = root.join("branch", JoinType.INNER);
            Join<Branch, Center> centerJoin = branchJoin.join("center", JoinType.INNER);
            return cb.or(
                    cb.equal(centerJoin.get("id"), headCenterId),
                    cb.equal(centerJoin.get("headCenterId"), headCenterId)
            );
        };
    }

    public static Specification<Employee> employeeBranchIdEqual(Long branchId) {
        return (root, query, cb) -> {
            if (branchId == null) {
                return cb.conjunction();
            }
            Join<Employee, Branch> branchJoin = root.join("branch", JoinType.INNER);
            return cb.equal(branchJoin.get("id"), branchId);
        };
    }

    public static Specification<Employee> employeeNameLikeOrPhoneNumber(String searchInput) {
        return (root, query, cb) -> {
            if (searchInput == null || searchInput.isEmpty()) {
                return cb.conjunction();
            }
            Join<Employee, User> userJoin = root.join("user", JoinType.INNER);
            String likePattern = "%" + searchInput + "%";
            return cb.or(
                    cb.like(userJoin.get("name"), likePattern),
                    cb.like(userJoin.get("phoneNumber"), likePattern)
            );
        };
    }

    public static Specification<Employee> responsibilitiesIn(List<Responsibilities> responsibilityNames) {
        return (root, query, cb) -> {
            if (responsibilityNames == null || responsibilityNames.isEmpty()) {
                return cb.conjunction();
            }
            Join<Employee, EmployeeResponsibility> responsibilitiesJoin = root.join("responsibilities", JoinType.INNER);
            return responsibilitiesJoin.get("name").in(responsibilityNames);
        };
    }

    public static Specification<Employee> classificationIn(List<EmployeeClassification> classifications) {
        return (root, query, cb) -> {
            if (classifications == null || classifications.isEmpty()) {
                return cb.conjunction();
            }
            return root.get("employeeClassification").in(classifications);
        };
    }
}
