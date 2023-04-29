package kompo.sudoku;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BacktrackingSolverTest {
    @Test
    public void SolveTest(){
        BacktrackingSudokuSolver Solver = new BacktrackingSudokuSolver();
        SudokuBoard board1 = new SudokuBoard(Solver);
        SudokuBoard board2 = new SudokuBoard(Solver);
        Solver.solve(board1);
        board2.solveGame();
        SudokuBoard board = new SudokuBoard(Solver);
        assertNotEquals(board,board1);
        assertNotEquals(board,board2);
    }
}
