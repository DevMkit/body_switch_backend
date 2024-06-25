package kr.co.softhubglobal.service.member;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import kr.co.softhubglobal.entity.course.CourseClassType;
import kr.co.softhubglobal.entity.course.CourseTicket;
import kr.co.softhubglobal.entity.course.CourseTrainer;
import kr.co.softhubglobal.entity.employee.Employee;
import kr.co.softhubglobal.entity.employee.EmployeeResponsibility;
import kr.co.softhubglobal.entity.member.Gender;
import kr.co.softhubglobal.entity.member.Member;
import kr.co.softhubglobal.entity.member.MemberCourseTicket;
import kr.co.softhubglobal.entity.user.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class MemberSpecifications {

    public static Specification<Member> memberCourseTicketBranchIdEqual(Long branchId) {
        return (root, query, cb) -> {
            if (branchId == null) {
                return cb.conjunction();
            }
            Join<Member, MemberCourseTicket> memberCourseTicketJoin = root.join("courseTickets", JoinType.INNER);
            Join<MemberCourseTicket, CourseTicket> courseTicketJoin = memberCourseTicketJoin.join("courseTicket", JoinType.INNER);
            return cb.equal(courseTicketJoin.get("branch").get("id"), branchId);
        };
    }

    public static Specification<Member> memberNameOrUsernameOrPhoneNumberLike(String searchInput) {
        return (root, query, cb) -> {
            if (searchInput == null || searchInput.isEmpty()) {
                return cb.conjunction();
            }
            Join<Member, User> memberUserJoin = root.join("user", JoinType.INNER);
            String likePattern = "%" + searchInput + "%";
            return cb.or(
                    cb.like(memberUserJoin.get("name"), likePattern),
                    cb.like(memberUserJoin.get("username"), likePattern),
                    cb.like(memberUserJoin.get("phoneNumber"), likePattern)
            );
        };
    }

    public static Specification<Member> memberCourseTicketTypeIn(List<CourseClassType> classTypes) {
        return (root, query, cb) -> {
            if (classTypes == null || classTypes.isEmpty()) {
                return cb.conjunction();
            }
            Join<Member, MemberCourseTicket> memberCourseTicketJoin = root.join("courseTickets", JoinType.INNER);
            return memberCourseTicketJoin.get("courseTicket").get("classType").in(classTypes);
        };
    }

    public static Specification<Member> memberCourseTicketIdEqual(Long courseTicketId) {
        return (root, query, cb) -> {
            if (courseTicketId == null) {
                return cb.conjunction();
            }
            Join<Member, MemberCourseTicket> memberCourseTicketJoin = root.join("courseTickets", JoinType.INNER);
            return cb.equal(memberCourseTicketJoin.get("courseTicket").get("id"), courseTicketId);
        };
    }

    public static Specification<Member> memberCourseTicketTrainerIdEqual(Long courseTrainerId) {
        return (root, query, cb) -> {
            if (courseTrainerId == null) {
                return cb.conjunction();
            }
            Join<Member, MemberCourseTicket> memberCourseTicketJoin = root.join("courseTickets", JoinType.INNER);
            Join<MemberCourseTicket, CourseTicket> courseTicketJoin = memberCourseTicketJoin.join("courseTicket", JoinType.INNER);
            Join<CourseTicket, CourseTrainer> courseTrainerJoin = courseTicketJoin.join("courseTrainers", JoinType.INNER);
            return cb.equal(courseTrainerJoin.get("id"), courseTrainerId);
        };
    }

    public static Specification<Member> memberGenderIn(List<Gender> genders) {
        return (root, query, cb) -> {
            if (genders == null || genders.isEmpty()) {
                return cb.conjunction();
            }
            return root.get("gender").in(genders);
        };
    }

}
