package kr.co.softhubglobal.service.courseTicket;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import kr.co.softhubglobal.entity.branch.Branch;
import kr.co.softhubglobal.entity.center.Center;
import kr.co.softhubglobal.entity.course.CourseClassType;
import kr.co.softhubglobal.entity.course.CourseTicket;
import kr.co.softhubglobal.entity.course.SaleStatus;
import kr.co.softhubglobal.entity.employee.Employee;
import kr.co.softhubglobal.entity.employee.EmployeeResponsibility;
import kr.co.softhubglobal.entity.employee.Responsibilities;
import kr.co.softhubglobal.entity.user.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class CourseTicketSpecifications {

    public static Specification<CourseTicket> courseTicketBranchCenterIdOrHeadCenterIdEqual(Long headCenterId) {
        return (root, query, cb) -> {
            if (headCenterId == null) {
                return cb.conjunction();
            }
            Join<CourseTicket, Branch> branchJoin = root.join("branch", JoinType.INNER);
            Join<Branch, Center> centerJoin = branchJoin.join("center", JoinType.INNER);
            return cb.or(
                    cb.equal(centerJoin.get("id"), headCenterId),
                    cb.equal(centerJoin.get("headCenterId"), headCenterId)
            );
        };
    }

    public static Specification<CourseTicket> courseTicketBranchIdEqual(Long branchId) {
        return (root, query, cb) -> {
            if (branchId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("branch").get("id"), branchId);
        };
    }

    public static Specification<CourseTicket> courseTicketNameLike(String ticketName) {
        return (root, query, cb) -> {
            if (ticketName == null || ticketName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(root.get("ticketName"), "%" + ticketName + "%");
        };
    }

    public static Specification<CourseTicket> courseTicketClassTypeIn(List<CourseClassType> classTypes) {
        return (root, query, cb) -> {
            if (classTypes == null || classTypes.isEmpty()) {
                return cb.conjunction();
            }
            return root.get("classType").in(classTypes);
        };
    }

    public static Specification<CourseTicket> courseTicketSaleStatusIn(List<SaleStatus> saleStatuses) {
        return (root, query, cb) -> {
            if (saleStatuses == null || saleStatuses.isEmpty()) {
                return cb.conjunction();
            }
            return root.get("saleStatus").in(saleStatuses);
        };
    }
}
