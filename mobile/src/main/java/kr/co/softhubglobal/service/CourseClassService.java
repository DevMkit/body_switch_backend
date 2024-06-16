package kr.co.softhubglobal.service;

import kr.co.softhubglobal.dto.course.CourseClassDTO;
import kr.co.softhubglobal.dto.course.CourseClassInfoMapper;
import kr.co.softhubglobal.entity.course.CourseClass;
import kr.co.softhubglobal.repository.CourseClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseClassService {

    private final CourseClassRepository courseClassRepository;
    private final CourseClassInfoMapper courseClassInfoMapper;

    public List<CourseClassDTO.CourseClassInfo> getCourseClasses() {
        return courseClassRepository.findAll()
                .stream()
                .map(courseClassInfoMapper)
                .toList();
    }
}
