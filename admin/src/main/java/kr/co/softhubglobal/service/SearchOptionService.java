package kr.co.softhubglobal.service;

import kr.co.softhubglobal.dto.searchOption.SearchOptionDTO;
import kr.co.softhubglobal.entity.center.CenterManager;
import kr.co.softhubglobal.entity.center.CenterType;
import kr.co.softhubglobal.entity.common.Restrictions;
import kr.co.softhubglobal.entity.course.CourseTicket;
import kr.co.softhubglobal.entity.course.CourseTrainer;
import kr.co.softhubglobal.entity.user.Role;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.exception.customExceptions.ResourceNotFoundException;
import kr.co.softhubglobal.repository.CenterManagerRepository;
import kr.co.softhubglobal.repository.CenterRepository;
import kr.co.softhubglobal.repository.CourseTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class SearchOptionService {

    private final CenterRepository centerRepository;
    private final CenterManagerRepository centerManagerRepository;
    private final CourseTicketRepository courseTicketRepository;
    private final MessageSource messageSource;

    public List<SearchOptionDTO.HeadBranchSelectionInfo> getHeadBranchSelectionOptions(User user) {

        Restrictions restrictions = new Restrictions();
        restrictions.eq("centerType", CenterType.HEAD);
        if(user.getRole().equals(Role.MANAGER)) {
            restrictions.eq("centerManager.user", user);
        }
        return centerRepository.findAll(restrictions.output())
                .stream()
                .filter(center -> center.getBranch() != null)
                .map(SearchOptionDTO.HeadBranchSelectionInfo::new)
                .toList();
    }

    public List<SearchOptionDTO.BranchSelectionInfo> getBranchSelectionOptions(User user, Long headBranchId) {
        Restrictions restrictions = new Restrictions(Restrictions.Conn.OR);
        restrictions.eq("id", headBranchId);
        Restrictions restrictions1 = new Restrictions();
        restrictions1.eq("headCenterId", headBranchId);
        restrictions.addChild(restrictions1);
        if(user.getRole().equals(Role.MANAGER)) {
            CenterManager centerManager = centerManagerRepository.findByUser(user)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            messageSource.getMessage("center.manager.id.not.exist", new Object[]{user.getId()}, Locale.ENGLISH)));
            if(centerManager.getCenter().getCenterType().equals(CenterType.BRANCH)) {
                restrictions = new Restrictions();
                restrictions.eq("centerManager.user", user);
            }
        }
        return centerRepository.findAll(restrictions.output())
                .stream()
                .filter(center -> center.getBranch() != null)
                .map(SearchOptionDTO.BranchSelectionInfo::new)
                .toList();
    }

    public List<SearchOptionDTO.CourseTicketSelectionInfo> getCourseTicketSelectionOptions(Long branchId) {
        Restrictions restrictions = new Restrictions();
        if(branchId != null) {
            restrictions.eq("branch.id", branchId);
        }
        return courseTicketRepository.findAll(restrictions.output())
                .stream()
                .map(SearchOptionDTO.CourseTicketSelectionInfo::new)
                .toList();
    }

    public List<SearchOptionDTO.CourseTicketTrainerSelectionInfo> getCourseTicketTrainerSelectionOptions(Long courseTicketId) {

        CourseTicket courseTicket = courseTicketRepository.findById(courseTicketId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("course.ticket.id.not.exist", new Object[]{courseTicketId}, Locale.ENGLISH)));

        return courseTicket.getCourseTrainers()
                .stream()
                .map(CourseTrainer::getEmployee)
                .map(SearchOptionDTO.CourseTicketTrainerSelectionInfo::new)
                .toList();
    }
}
