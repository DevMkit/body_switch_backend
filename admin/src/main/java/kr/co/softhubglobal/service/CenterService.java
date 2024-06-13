package kr.co.softhubglobal.service;

import kr.co.softhubglobal.dto.center.CenterInfoMapper;
import kr.co.softhubglobal.dto.center.CenterDTO;
import kr.co.softhubglobal.entity.branch.BranchType;
import kr.co.softhubglobal.entity.center.Center;
import kr.co.softhubglobal.entity.center.CenterManager;
import kr.co.softhubglobal.entity.center.CenterType;
import kr.co.softhubglobal.entity.common.Restrictions;
import kr.co.softhubglobal.entity.user.Role;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.exception.customExceptions.ResourceNotFoundException;
import kr.co.softhubglobal.repository.BranchRepository;
import kr.co.softhubglobal.repository.CenterManagerRepository;
import kr.co.softhubglobal.repository.CenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CenterService {

    private final CenterRepository centerRepository;
    private final CenterManagerRepository centerManagerRepository;
    private final BranchRepository branchRepository;
    private final CenterInfoMapper centerInfoMapper;
    private final MessageSource messageSource;

    public List<CenterDTO.CenterInfo> getAllCenterBranches(User user){

        if(user.getRole().equals(Role.SUPERADMIN) || user.getRole().equals(Role.OPERATOR)) {
            Restrictions restrictions = new Restrictions();
            restrictions.eq("centerType", CenterType.HEAD);
            return centerRepository.findAll(restrictions.output()).stream()
                    .map(centerInfoMapper)
                    .toList();
        }

        if(user.getRole().equals(Role.MANAGER)) {
            Center center = centerRepository.findByCenterManagerUser(user)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            messageSource.getMessage("center.not.found", null, Locale.ENGLISH )));
            Restrictions restrictions = new Restrictions(Restrictions.Conn.OR);
            Restrictions restrictions1 = new Restrictions();
            restrictions.eq("centerManager.user", user);
            if(center.getCenterType().equals(CenterType.HEAD)) {
                restrictions1.eq("headCenterId", center.getId());
                restrictions.addChild(restrictions1);
            }
            return centerRepository.findAll(restrictions.output()).stream()
                    .map(centerInfoMapper)
                    .toList();
        }

        return List.of();
    }
}
