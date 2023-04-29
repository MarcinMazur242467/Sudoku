package kompo.sudoku;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import kompo.sudoku.exceptions.CloneException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SudokuBoard implements Serializable, Cloneable,AutoCloseable {
    private final List<List<SudokuField>> board = Arrays.asList(new List[9]);

    private final SudokuSolver sudokuSolver;

    public SudokuBoard(SudokuSolver sudokuSolver) {
        this.sudokuSolver = sudokuSolver;
        for (int i = 0; i < 9; i++) {
            board.set(i, Arrays.asList(new SudokuField[9]));
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board.get(i).set(j, new SudokuField());
            }
        }
    }

    public int get(int x, int y) {
        return board.get(x).get(y).getValue();
    }

    public SudokuField getSudokuField(int x, int y) {
        return board.get(x).get(y);
    }

    public void set(int x, int y, int n) {
        board.get(x).get(y).setValue(n);
        checkBoard();
    }

    private boolean verticalCheck(int[] tab) {
        int counter = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int k = 0; k < 9; k++) {
                    if (board.get(i).get(j).getValue() == tab[k]) {
                        counter++;
                    }
                }
                if (counter != 1) {
                    return false;
                }
                counter = 0;
            }
        }
        return true;
    }

    private boolean horizontalCheck(int[] tab) {
        int counter = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int k = 0; k < 9; k++) {
                    if (board.get(i).get(j).getValue() == tab[k]) {
                        counter++;
                    }
                }
                if (counter != 1) {
                    return false;
                }
                counter = 0;
            }
        }
        return true;
    }

    private boolean squareCheck(int[] tab) {
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
                            if (board.get(col).get(row).getValue() == tab[index]) {
                                counter++;
                            }
                        }
                    }
                    if (counter != 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkBoard() {
        int[] nums1to9 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        return verticalCheck(nums1to9) && horizontalCheck(nums1to9) && squareCheck(nums1to9);
    }

    public void solveGame() {
        sudokuSolver.solve(this);
    }

    public SudokuBox getBox(int x, int y) {
        List<SudokuField> box = Arrays.asList(new SudokuField[9]);
        int x0 = (Math.floorDiv(x, 3)) * 3;
        int y0 = (Math.floorDiv(y, 3)) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                box.set(3 * i + j, board.get(i + y0).get(j + x0));
            }
        }
        return new SudokuBox(box);
    }

    public SudokuRow getRow(int y) {
        List<SudokuField> rows = Arrays.asList(new SudokuField[9]);
        for (int i = 0; i < 9; i++) {
            rows.set(i, board.get(y).get(i));
        }
        return new SudokuRow(rows);
    }

    public SudokuColumn getColumn(int x) {
        List<SudokuField> col = Arrays.asList(new SudokuField[9]);
        for (int i = 0; i < 9; i++) {
            col.set(i, board.get(x).get(i));
        }
        return new SudokuColumn(col);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SudokuBoard that)) {
            return false;
        }

        return new EqualsBuilder().append(board, that.board).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(board).toHashCode();
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                string.append(this.get(i, j));
                string.append("\t");
            }
            string.append("\n");
        }
        return string.toString();
    }

    @Override
    public void close() {

    }

    @Override
    public final SudokuBoard clone() throws CloneException {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard clone = new SudokuBoard(solver);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                clone.board.get(i).set(j, (SudokuField) getSudokuField(i, j).clone());
            }
        }
        return clone;
    }
}
