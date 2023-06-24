package firstweek.board.service;

import firstweek.board.dto.BoardRequestDto;
import firstweek.board.dto.BoardResponseDto;
import firstweek.board.dto.BoardSearch;
import firstweek.board.entity.Board;
import firstweek.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardResponseDto createBoard(BoardRequestDto requestDto) {

        Board board = new Board(requestDto);

        Board savedBoard = boardRepository.save(board);

        BoardResponseDto boardResponseDto = new BoardResponseDto(savedBoard);

        return boardResponseDto;
    }

    public List<BoardResponseDto> getBoards() {

        List<Board> BoardList = boardRepository.findAllByOrderByCreateAtDesc();
        return BoardList.stream().map(BoardResponseDto::new).toList();
    }

    public BoardSearch findBoardById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        BoardSearch boardSearch = new BoardSearch(board);
        return boardSearch;
    }

    @Transactional
    public BoardResponseDto editBoard(Long id, BoardRequestDto requestDto) {
        Board findBoard = boardRepository.findById(id).get();
        if (requestDto.getPassword().equals(findBoard.getPassword())) {

            findBoard.setContents(requestDto.getContents());
            findBoard.setName(requestDto.getName());
            findBoard.setTitle(requestDto.getTitle());
            return new BoardResponseDto(findBoard);
        } else throw new IllegalArgumentException("비밀번호가 다릅니다");

    }

    @Transactional
    public String deleteBoard(Long id, Object password) {
        Board findBoard = boardRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("조회된 게시글이 없스빈다"));
        if(findBoard.getPassword().equals(password.toString())){
            boardRepository.delete(findBoard);
            return "ok";
        }
        return "failure";
    }
}
