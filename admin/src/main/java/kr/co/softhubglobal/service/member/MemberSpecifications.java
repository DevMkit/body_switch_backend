package kr.co.softhubglobal.service.member;

import jakarta.persistence.criteria.*;
import kr.co.softhubglobal.dto.member.MemberDTO;
import kr.co.softhubglobal.entity.branch.Branch;
import kr.co.softhubglobal.entity.center.Center;
import kr.co.softhubglobal.entity.course.CourseClassType;
import kr.co.softhubglobal.entity.course.CourseTicket;
import kr.co.softhubglobal.entity.course.CourseTrainer;
import kr.co.softhubglobal.entity.member.*;
import kr.co.softhubglobal.entity.user.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MemberSpecifications {

    public static Specification<Member> memberCourseTicketBranchCenterIdOrCourseTicketBranchCenterHeadCenterIdEqual(Long headCenterId) {
        return (root, query, cb) -> {
            if (headCenterId == null) {
                return cb.conjunction();
            }
            Join<Member, MemberCourseTicket> memberCourseTicketJoin = root.join("courseTickets", JoinType.INNER);
            Join<MemberCourseTicket, CourseTicket> courseTicketJoin = memberCourseTicketJoin.join("courseTicket", JoinType.INNER);
            Join<CourseTicket, Branch> branchJoin = courseTicketJoin.join("branch", JoinType.INNER);
            Join<Branch, Center> centerJoin = branchJoin.join("center", JoinType.INNER);
            return cb.or(
                    cb.equal(centerJoin.get("id"), headCenterId),
                    cb.equal(centerJoin.get("headCenterId"), headCenterId)
            );
        };
    }

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

    public static Specification<Member> memberTypesEqual(List<MemberDTO.MemberType> memberTypes) {
        return (root, query, cb) -> {
            if (memberTypes == null || memberTypes.isEmpty()) {
                return cb.conjunction();
            }
            List<Predicate> predicates = new ArrayList<>();
            Join<Member, MemberCourseTicket> memberCourseTicketJoin = root.join("courseTickets", JoinType.LEFT);
            for (MemberDTO.MemberType memberType : memberTypes) {
                switch (memberType) {
                    case ACTIVE -> predicates.add(cb.equal(root.get("status"), MemberStatus.ACTIVE));
                    case STOPPED -> predicates.add(cb.equal(memberCourseTicketJoin.get("status"), MemberCourseTicketStatus.STOPPED));
                    case EXPIRED -> predicates.add(cb.equal(memberCourseTicketJoin.get("status"), MemberCourseTicketStatus.INACTIVE));
                    case WITHDREW -> predicates.add(cb.equal(root.get("status"), MemberStatus.WITHDREW));
                }
            }
            return cb.or(predicates.toArray(new Predicate[0]));
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

    public static Specification<Member> memberAgeInRanges(List<MemberDTO.AgeRange> ageRanges) {
        return (root, query, cb) -> {
            if (ageRanges == null || ageRanges.isEmpty()) {
                return cb.conjunction();
            }
            List<Predicate> predicates = new ArrayList<>();
            for (MemberDTO.AgeRange ageRange : ageRanges) {
                predicates.add(cb.between(root.get("age"), ageRange.getMin(), ageRange.getMax()));
            }
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Member> memberIsSmsReceive(Boolean isSMSReceive) {
        return (root, query, cb) -> {
            if (isSMSReceive == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("isSMSReceive"), isSMSReceive);
        };
    }

    public static Specification<Member> memberCourseTicketRemainingCount(List<MemberDTO.RemainingCount> remainingCounts) {
        return (root, query, cb) -> {
            if (remainingCounts == null || remainingCounts.isEmpty()) {
                return cb.conjunction();
            }
            List<Predicate> predicates = new ArrayList<>();
            Join<Member, MemberCourseTicket> memberCourseTicketJoin = root.join("courseTickets", JoinType.INNER);
            Join<MemberCourseTicket, CourseTicket> courseTicketJoin = memberCourseTicketJoin.join("courseTicket", JoinType.INNER);
            Expression<Integer> remainingCountExpression = cb.diff(courseTicketJoin.get("usageCount"), memberCourseTicketJoin.get("usedCount"));
            for (MemberDTO.RemainingCount remainingCount : remainingCounts) {
                predicates.add(cb.lessThanOrEqualTo(remainingCountExpression, remainingCount.getValue()));
            }
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Member> memberCourseTicketExpireDateBetween(LocalDate ticketExpireDateFrom, LocalDate ticketExpireDateTo) {
        return (root, query, cb) -> {
            if (ticketExpireDateFrom == null || ticketExpireDateTo == null) {
                return cb.conjunction();
            }
            Join<Member, MemberCourseTicket> memberCourseTicketJoin = root.join("courseTickets", JoinType.INNER);
            return cb.between(memberCourseTicketJoin.get("expireDate"), ticketExpireDateFrom, ticketExpireDateTo);
        };
    }
}
