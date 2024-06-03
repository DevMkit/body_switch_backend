package kr.co.softhubglobal.service;

import kr.co.softhubglobal.dto.branch.BranchDTO;
import kr.co.softhubglobal.dto.branch.BranchInfoMapper;
import kr.co.softhubglobal.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final BranchInfoMapper branchInfoMapper;

    public List<BranchDTO.BranchInfo> getBranches() {
        return branchRepository.findAll()
                .stream()
                .map(branchInfoMapper)
                .toList();
    }
}
