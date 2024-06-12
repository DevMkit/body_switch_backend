package kr.co.softhubglobal.service;

import kr.co.softhubglobal.dto.center.CenterBranchInfoMapper;
import kr.co.softhubglobal.dto.center.CenterDTO;
import kr.co.softhubglobal.entity.branch.BranchType;
import kr.co.softhubglobal.entity.center.CenterManager;
import kr.co.softhubglobal.entity.center.ManagerType;
import kr.co.softhubglobal.entity.common.Restrictions;
import kr.co.softhubglobal.entity.user.Role;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.exception.customExceptions.ResourceNotFoundException;
import kr.co.softhubglobal.repository.BranchRepository;
import kr.co.softhubglobal.repository.CenterManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CenterService {

    private final CenterManagerRepository centerManagerRepository;
    private final BranchRepository branchRepository;
    private final CenterBranchInfoMapper centerBranchInfoMapper;
    private final MessageSource messageSource;

    public List<CenterDTO.CenterBranchInfo> getAllCenterBranches(User user){

//        if(user.getRole().equals(Role.SUPERADMIN) || user.getRole().equals(Role.OPERATOR)) {
//            Restrictions restrictions = new Restrictions();
//            restrictions.eq("branchType", BranchType.MAIN);
//            return branchRepository.findAll().stream()
//                    .map(centerBranchInfoMapper)
//                    .toList();
//        }
//
//        if(user.getRole().equals(Role.MANAGER)) {
//            CenterManager centerManager = centerManagerRepository.findByUser(user)
//                    .orElseThrow(() -> new ResourceNotFoundException(
//                            messageSource.getMessage("center.manager.id.not.exist", new Object[]{user.getId()}, Locale.ENGLISH)));
//            if(centerManager.getManagerType().equals(ManagerType.STORE)) {
//                if(centerManager.getBranch() == null) {
//                    return List.of(new CenterDTO.CenterBranchInfo(
//                            centerManager.getCenter().getId(),
//                            null,
//                            null,
//                            centerManager.getCenter().getRepresentativeNumber(),
//                            centerManager.getUser().getName(),
//                            centerManager.getUser().getPhoneNumber(),
//                            centerManager.getUser().getEmail(),
//                            BranchType.NOT_SET
//                    ));
//                } else {
//                    return centerManager.getCenter().getBranches().stream().map(centerBranchInfoMapper).toList();
//                }
//            } else {
//                return List.of(new CenterDTO.CenterBranchInfo(
//                        centerManager.getCenter().getId(),
//                        centerManager.getBranch().getId(),
//                        centerManager.getBranch().getBranchName(),
//                        centerManager.getBranch().getCenter().getRepresentativeNumber(),
//                        centerManager.getUser().getName(),
//                        centerManager.getUser().getPhoneNumber(),
//                        centerManager.getUser().getEmail(),
//                        centerManager.getBranch().getBranchType()
//                ));
//            }
//        }

        return List.of();
    }
}
