package kompo.sudoku;

import kompo.sudoku.exceptions.CloneException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

class SudokuBoardTest {
    int[] nums1to9 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    private ResourceBundle bundle = ResourceBundle.getBundle("Language");

    private void horizontalCheck(int[][] testBoard) {
        int counter = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int k = j + 1; k < 9; k++) {
                    if (testBoard[i][j] == testBoard[i][k]) {
                        counter++;
                    }
                }
            }
        }
        if (counter != 0) {
            fail("Cyfry powatarzają się w pionie!");
        }
    }

    private void verticalCheck(int[][] testBoard) {
        int counter = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int k = j + 1; k < 9; k++) {
                    if (testBoard[j][i] == testBoard[k][i]) {
                        counter++;
                    }
                }
            }
        }
        if (counter != 0) {
            fail("Cyfry powatarzają się w poziomie!");
        }
    }

    private void squareCheck(int[][] testBoard) {
        int counter = 0;
        //First two for loops set indexes i and j on first(top-left) cell of 3x3 square
        for (int i = 0; i < 9; i = i + 3) {
            for (int j = 0; j < 9; j = j + 3) {
                //Index for loops through 1-9 array
                for (int index = 0; index < 9; index++) {
                    for (int k = 0; k < 3; k++) {
                        for (int l = 0; l < 3; l++) {
                            //k and l are looping inside the 3x3 squares
                            int col = i + k;
                            int row = l + j;
                            if (testBoard[col][row] == nums1to9[index]) {
                                counter++;
                            }
                        }
                    }
                    assertEquals(1, counter, "Cyfry powtarzaja sie w boxie 3x3");
                    counter = 0;
                }
            }
        }
    }

    @Test
    public void sudokuBoardConstructorTest() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        assertNotEquals(board, null);
    }

    @Test
    void SudokuBoardGetMethodTest() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        int testValue = 1;
        board.set(0, 0, testValue);
        assertEquals(board.get(0, 0), testValue);
    }

    @Test
    void SudokuBoardSetMethodTest() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        board.set(0, 0, 1);
        assertNotEquals(board.get(0, 0), 9);
    }

    @Test
    void SolveGameTest() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        board.solveGame();
        assertNotEquals(board, null);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.get(i, j) == 0)
                    Assertions.fail();
            }
        }
    }

    @Test
    void getBoxTest() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        board.solveGame();
        List<SudokuField> tab = Arrays.asList(new SudokuField[9]);
        for (int i = 0; i < 9; i++) {
            tab.set(i, new SudokuField());
        }
        SudokuBox box = board.getBox(0, 0);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tab.get(3 * i + j).setValue(board.get(i, j));
            }
        }
        SudokuBox testBox = new SudokuBox(tab);
        assertEquals(testBox.verify(), box.verify());
    }

    @Test
    void getBoxTest2() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        board.solveGame();
        SudokuBox box1 = board.getBox(1, 1);
        SudokuBox box2 = board.getBox(0, 0);
        assertEquals(box2.verify(), box1.verify());
    }

    @Test
    void getRowTest() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        board.solveGame();
        List<SudokuField> tab = Arrays.asList(new SudokuField[9]);
        for (int i = 0; i < 9; i++) {
            tab.set(i, new SudokuField());
        }
        SudokuRow row = board.getRow(0);
        for (int i = 0; i < 9; i++) {
            tab.get(i).setValue(board.get(0, i));
        }
        SudokuRow testrow = new SudokuRow(tab);
        assertEquals(testrow.verify(), row.verify());
    }

    @Test
    void getColumnTest() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        board.solveGame();
        List<SudokuField> tab = Arrays.asList(new SudokuField[9]);
        for (int i = 0; i < 9; i++) {
            tab.set(i, new SudokuField());
        }
        SudokuColumn col = board.getColumn(0);
        for (int i = 0; i < 9; i++) {
            tab.get(i).setValue(board.get(i, 0));
        }
        SudokuColumn testrow = new SudokuColumn(tab);
        assertEquals(testrow.verify(), col.verify());
    }

    @Test
    void isIdentical() {
        BacktrackingSudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard sudokuBoard = new SudokuBoard(solver);
        sudokuBoard.solveGame();
        int[][] testBoard1 = new int[9][9];
        int[][] testBoard2 = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                testBoard1[i][j] = sudokuBoard.get(i, j);
            }
        }
        sudokuBoard.solveGame();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                testBoard2[i][j] = sudokuBoard.get(i, j);
            }
        }
        assertNotEquals(testBoard1, testBoard2, "Plansze są takie same!");
    }

    @Test
    void checkBoard() {
        BacktrackingSudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard sudokuBoard = new SudokuBoard(solver);
        sudokuBoard.solveGame();
        int[][] testBoard = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                testBoard[i][j] = sudokuBoard.get(i, j);
            }
        }
        horizontalCheck(testBoard);
        verticalCheck(testBoard);
        squareCheck(testBoard);

    }

    @Test
    void toStringTest() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        board.set(0, 0, 1);
        board.set(0, 1, 2);
        board.set(0, 2, 3);
        String string = board.toString();
        assertTrue(string.contains("1\t2\t3\t0"));
    }

    @Test
    void hashCodeTest() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        board.solveGame();
        int hascode = board.hashCode();
        board.set(0, 0, 0);
        assertTrue(board.hashCode() != hascode);
    }

    @Test
    void equalsTestCase() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        board.solveGame();
        assertEquals(board, board);
    }

    @Test
    void sameHashCodeTest() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board1 = new SudokuBoard(solver);
        SudokuBoard board2 = new SudokuBoard(solver);
        board1.solveGame();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board2.set(i, j, board1.get(i, j));
            }
        }
        assertEquals(board1.hashCode(), board2.hashCode());
    }

    @Test
    public void cloneTestCase() throws CloneException {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board1 = new SudokuBoard(solver);
        board1.solveGame();
        SudokuBoard board2 = board1.clone();
        assertEquals(board1, board2);
        assertEquals(board1.getSudokuField(0, 0), board2.getSudokuField(0, 0));
        board1.set(0, 0, 0);
        assertNotEquals(board1.getSudokuField(0, 0), board2.getSudokuField(0, 0));
    }
}

