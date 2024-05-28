package kr.co.softhubglobal.service;

import kr.co.softhubglobal.dto.branch.BranchDTO;
import kr.co.softhubglobal.entity.branch.Branch;
import kr.co.softhubglobal.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;

    public List<Branch> getAllBranches(){
        return branchRepository.findAll();
    }

    public void createBranch(BranchDTO.BranchCreateRequest branchCreateRequest) {

    }
}
