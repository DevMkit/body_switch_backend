package kr.co.softhubglobal.service;

import kr.co.softhubglobal.dto.PageableDTO;
import kr.co.softhubglobal.dto.center.CenterDetailInfoMapper;
import kr.co.softhubglobal.dto.center.CenterInfoMapper;
import kr.co.softhubglobal.dto.center.CenterDTO;
import kr.co.softhubglobal.entity.center.Center;
import kr.co.softhubglobal.entity.center.CenterType;
import kr.co.softhubglobal.entity.common.Restrictions;
import kr.co.softhubglobal.entity.user.Role;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.exception.customExceptions.ResourceNotFoundException;
import kr.co.softhubglobal.repository.CenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CenterService {

    private final CenterRepository centerRepository;
    private final CenterInfoMapper centerInfoMapper;
    private final CenterDetailInfoMapper centerDetailInfoMapper;
    private final MessageSource messageSource;

    public PageableDTO.Response getAllCenterBranches(
            CenterDTO.CenterSearchRequest centerSearchRequest,
            User user
    ) {
        Restrictions restrictions = new Restrictions();
        if(user.getRole().equals(Role.SUPERADMIN) || user.getRole().equals(Role.OPERATOR)) {
            restrictions.eq("centerType", CenterType.HEAD);
            if(centerSearchRequest.getBranchName() != null) {
                restrictions.like("branch.branchName", "%" + centerSearchRequest.getBranchName() + "%");
            }
            if(centerSearchRequest.getType() != null) {
                restrictions.eq("centerType", centerSearchRequest.getType());
            }
        }

        if(user.getRole().equals(Role.MANAGER)) {
            Center center = centerRepository.findByCenterManagerUser(user)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            messageSource.getMessage("center.not.found", null, Locale.ENGLISH )));
            restrictions = new Restrictions(Restrictions.Conn.OR);
            Restrictions restrictions1 = new Restrictions();
            Restrictions restrictions2 = new Restrictions();
            restrictions1.eq("centerManager.user", user);
            if(center.getCenterType().equals(CenterType.HEAD)) {
                restrictions2.eq("headCenterId", center.getId());
            }
            if(centerSearchRequest.getBranchName() != null) {
                restrictions1.like("branch.branchName", "%" + centerSearchRequest.getBranchName() + "%");
                restrictions2.like("branch.branchName", "%" + centerSearchRequest.getBranchName() + "%");
            }
            if(centerSearchRequest.getType() != null) {
                restrictions1.eq("centerType", centerSearchRequest.getType());
                restrictions2.eq("centerType", centerSearchRequest.getType());
            }
            restrictions.addChild(restrictions1);
            restrictions.addChild(restrictions2);
        }

        PageRequest pageRequest = PageRequest.of(centerSearchRequest.getPage() - 1, centerSearchRequest.getLimit());
        Page<Center> result = centerRepository.findAll(restrictions.output(), pageRequest);

        PageableDTO.Response response = new PageableDTO.Response();
        response.setTotalElements(result.getTotalElements());
        response.setNumber(result.getNumber() + 1);
        response.setSize(result.getSize());
        response.setContent(
                result.getContent()
                        .stream()
                        .map(centerInfoMapper)
                        .collect(Collectors.toList())
        );

        return response;
    }

    public CenterDTO.CenterDetailInfo getCenterDetailInfoById(Long centerId) {
        return centerRepository.findById(centerId)
                .map(centerDetailInfoMapper)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("center.id.not.exist", new Object[]{centerId}, Locale.ENGLISH))
                );
    }
}
