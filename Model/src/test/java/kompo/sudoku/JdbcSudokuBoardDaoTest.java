package kompo.sudoku;


import kompo.sudoku.exceptions.DaoFileException;
import kompo.sudoku.exceptions.DatabaseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.UUID;


class JdbcSudokuBoardDaoTest {

    private ResourceBundle bundle = ResourceBundle.getBundle("Language");

    @Test
    void WriteReadTest() throws DatabaseException {
        UUID uuid = UUID.randomUUID();
        BacktrackingSudokuSolver sudokuSolver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(sudokuSolver);
        board.solveGame();
        try(JdbcSudokuBoardDao jdbcSudokuBoardDao = new JdbcSudokuBoardDao(uuid.toString());
        ){
            jdbcSudokuBoardDao.write(board);
            SudokuBoard board1 = jdbcSudokuBoardDao.read();
            assertEquals(board, board1);
            assertNotSame(board, board1);
        } catch (SQLException | DaoFileException e) {
            throw new DatabaseException(bundle.getString("db"));
        }
    }
}