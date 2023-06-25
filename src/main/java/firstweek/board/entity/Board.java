package firstweek.board.entity;

import firstweek.board.dto.BoardRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "board",uniqueConstraints = @UniqueConstraint(name = "password-uk", columnNames = {"password"}))
@NoArgsConstructor // setter로 하는 상황이 생길 가능성? 때문일까?
public class Board extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "b_id")
    private Long id;

    @Column(length = 20)
    private String name;

    @Column(length = 20)
    private String password;

    @Column(name = "b_title", length = 100, nullable = false)
    private String title;

//    @Column(name = "b_contents", columnDefinition = "TEXT")
    @Lob
    @Column(name="b_contents")
    private String contents;

    public Board(BoardRequestDto requestDto) {
        this.name = requestDto.getName();
        this.password = requestDto.getPassword();
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }
}
