package kompo.sudoku;


import kompo.sudoku.exceptions.DaoFileException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;


class FileSudokuBoardDaoTest {

    @Test
    void WriteReadTest() throws DaoFileException {
        BacktrackingSudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        File file = new File("test.txt");
        board.solveGame();
        SudokuBoardDaoFactory.getFileDao(file).write(board);
        try(SudokuBoard board2 = SudokuBoardDaoFactory.getFileDao(file).read())
        {
            assertEquals(board,board2);
        }
    }
}