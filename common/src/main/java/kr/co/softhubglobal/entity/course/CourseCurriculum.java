package kr.co.softhubglobal.entity.course;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import kr.co.softhubglobal.entity.common.BaseDateEntity;
import lombok.*;

@Entity
@Table(name = "course_curriculum")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CourseCurriculum extends BaseDateEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "COURSE_TICKET_ID")
    @JsonBackReference
    public CourseTicket courseTicket;

    @Column(name = "TITLE")
    public String title;

    @Column(name = "SUMMARY")
    public String summary;

    @Column(name = "IMAGE_URL")
    public String imageUrl;

}
