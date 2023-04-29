package kompo.sudoku;

import kompo.sudoku.exceptions.CloneException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuElementTest {
    private ResourceBundle bundle = ResourceBundle.getBundle("Language");
    @Test
    public void ConstructorTest() {
        List<SudokuField> tab = Arrays.asList(new SudokuField[9]);
        for (int i = 0; i < 9; i++) {
            tab.set(i, new SudokuField());
        }
        SudokuBox element = new SudokuBox(tab);
        Assertions.assertNotEquals(null, element);
    }

    @Test
    public void ElementVerificationPositiveTest() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        board.solveGame();
        SudokuBox box = board.getBox(0, 0);
        SudokuColumn col = board.getColumn(0);
        SudokuRow row = board.getRow(0);
        Assertions.assertTrue(box.verify());
        Assertions.assertTrue(row.verify());
        Assertions.assertTrue(col.verify());
    }

    @Test
    public void ElementVerificationNegativeTest() {
        List<SudokuField> tab = Arrays.asList(new SudokuField[9]);

        for (int i = 0; i < 9; i++) {
            tab.set(i, new SudokuField());
            tab.get(i).setValue(10);
        }
        SudokuBox box = new SudokuBox(tab);
        SudokuBox row = new SudokuBox(tab);
        SudokuBox col = new SudokuBox(tab);
        Assertions.assertFalse(box.verify());
        Assertions.assertFalse(row.verify());
        Assertions.assertFalse(col.verify());
    }

    @Test
    void equalsTestCase() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        board.solveGame();
        SudokuBox box = board.getBox(0, 0);
        assertEquals(box, box);
    }

    @Test
    void sameHashCodeTest(){
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        board.solveGame();
        SudokuBox box1  = board.getBox(0,0);
        SudokuBox box2 = board.getBox(0,0);
        assertEquals(box1.hashCode(), box2.hashCode());
    }
    @Test
    void hashCodeTest(){
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board  = new SudokuBoard(solver);
        board.solveGame();
        SudokuBox box = board.getBox(0,0);
        int hashCode = box.hashCode();
        board.set(0,0,0);
        assertTrue(box.hashCode() != hashCode);
    }
    @Test
    void toStringTest(){
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        board.set(0,0,1);
        board.set(0,1,2);
        board.set(0,2,3);
        SudokuRow row = board.getRow(0);
        String string = row.toString();
        assertTrue(string.contains("1\t2\t3\t0"));
    }
    @Test
    void equalsTestFalseCase() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        board.solveGame();
        SudokuBox box1 = board.getBox(0, 0);
        SudokuBox box2 = board.getBox(3,3);
        assertNotEquals(box1, box2);
    }
    @Test
    public void cloneTestCase() throws CloneException {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        board.solveGame();
        SudokuBox box = board.getBox(0,0);
        SudokuBox box_copy = (SudokuBox) box.clone();
        SudokuRow row = board.getRow(0);
        SudokuRow row_copy = (SudokuRow) row.clone();
        SudokuColumn col = board.getColumn(0);
        SudokuColumn col_copy = (SudokuColumn) col.clone();
        assertEquals(box, box_copy);
        assertEquals(row, row_copy);
        assertEquals(col, col_copy);
    }
}
