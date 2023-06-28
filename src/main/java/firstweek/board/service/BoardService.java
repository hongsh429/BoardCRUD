package firstweek.board.service;

import firstweek.board.dto.BoardRequestDto;
import firstweek.board.dto.BoardResponseDto;
import firstweek.board.entity.Board;
import firstweek.board.jwt.JwtUtil;
import firstweek.board.repository.BoardRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final HttpServletRequest request;
    private final JwtUtil jwtUtil;


    public BoardResponseDto createBoard(BoardRequestDto requestDto) {

        String tokenFromHeader = jwtUtil.getTokenFromHeader(request);
        if (tokenFromHeader != null) {

            Claims info = jwtUtil.getUserInfoFromToken(tokenFromHeader);
            String username = info.getSubject();

            Board board = new Board(requestDto, username);

            Board savedBoard = boardRepository.save(board);
            BoardResponseDto boardResponseDto = new BoardResponseDto(savedBoard);
            return boardResponseDto;
        } else {
            throw new IllegalArgumentException("유효한 토큰이 아닙니다");
        }
    }

    public List<BoardResponseDto> getBoards() {

        List<Board> BoardList = boardRepository.findAllByOrderByCreateAtDesc();
        return BoardList.stream().map(BoardResponseDto::new).toList();
    }

    public BoardResponseDto findBoardById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        BoardResponseDto boardResponseDto = new BoardResponseDto(board);
        return boardResponseDto;
    }

    @Transactional
    public BoardResponseDto editBoard(Long id, BoardRequestDto requestDto) {
        String tokenFromHeader = jwtUtil.getTokenFromHeader(request);
        if (tokenFromHeader != null) {

            Claims info = jwtUtil.getUserInfoFromToken(tokenFromHeader);
            String username = info.getSubject();
            // 유효한 토큰이면서 해당 사용자가 작성한 게시글만 수정 가능
            Board findBoard = boardRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다"));

            // 사용자 이름 일치 여부
            if (findBoard.getUsername().equals(username)) {

                // 변경감지
                findBoard.update(requestDto);
                Board board = boardRepository.findById(id).get();
                return new BoardResponseDto(board);
            } else {
                throw new IllegalArgumentException("해당 사용자가 작성한 글이 아닙니다.");
            }

        } else {
            throw new IllegalArgumentException("유효한 토큰이 아닙니다");
        }
    }

    @Transactional
    public Map<String, Object> deleteBoard(Long id) {
        Map<String, Object> map = new HashMap<>();
        String msg;
        int status;
        String tokenFromHeader = jwtUtil.getTokenFromHeader(request);
        if (tokenFromHeader != null) {
            Claims info = jwtUtil.getUserInfoFromToken(tokenFromHeader);
            String username = info.getSubject();
            Board findBoard = boardRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("조회된 게시글이 없습니다"));

            if (findBoard.getUsername().equals(username)) {
                boardRepository.delete(findBoard);
                msg = "삭제 성공";
                status = HttpServletResponse.SC_OK;
                map.put("msg", msg);
                map.put("status", status);
                return map;
            }
            throw new IllegalArgumentException("해당 게시글은 사용자가 작성한 글이 아닙니다");
        } else {
            throw new IllegalArgumentException("유효한 토큰이 아닙니다");
        }
    }
}
