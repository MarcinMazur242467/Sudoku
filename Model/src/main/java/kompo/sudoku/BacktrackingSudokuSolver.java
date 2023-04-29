package kompo.sudoku;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BacktrackingSudokuSolver implements SudokuSolver, Serializable {

    @Override
    public void solve(SudokuBoard board) {
        fillBoard(board, 0, 0);
    }

    private boolean fillBoard(SudokuBoard board, int row, int col) {
        if (row == 8 && col == 9) {
            return true;
        }

        if (col == 9) {
            row++;
            col = 0;
        }

        if (col == 0 && row == 0) {
            Integer[] tab = {1, 2, 3, 4, 5, 6, 7, 8, 9};

            List<Integer> intList = Arrays.asList(tab);
            Collections.shuffle(intList);
            intList.toArray(tab);
            for (int i = 0; i < 9; i++) {
                board.set(0, i, tab[i]);
            }
            row += 1;
        }

        for (int num = 1; num < 10; num++) {

            if (isSafe(board, row, col, num)) {

                board.set(row, col, num);
                if (fillBoard(board, row, col + 1)) {
                    return true;
                }
            }
            board.set(row, col, 0);
        }
        return false;
    }

    private boolean isSafe(SudokuBoard board, int row, int col, int num) {

        for (int i = 0; i <= 8; i++) {
            if (board.get(row, i) == num) {
                return false;
            }
        }

        for (int i = 0; i <= 8; i++) {
            if (board.get(i, col) == num) {
                return false;
            }
        }

        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.get(i + startRow, j + startCol) == num) {
                    return false;
                }
            }
        }
        return true;
    }
}



