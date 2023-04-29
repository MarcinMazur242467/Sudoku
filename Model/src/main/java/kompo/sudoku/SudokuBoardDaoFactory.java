package kompo.sudoku;

import java.io.File;
import java.sql.SQLException;
import kompo.sudoku.exceptions.DaoFileException;

public class  SudokuBoardDaoFactory {


    private SudokuBoardDaoFactory() {
    }

    public static Dao<SudokuBoard> getFileDao(File fileName) {
        return new FileSudokuBoardDao(fileName);
    }

    public static Dao<SudokuBoard> getJdbcDao(String name) throws DaoFileException {
        try (JdbcSudokuBoardDao jdbcSudokuBoardDao = new JdbcSudokuBoardDao(name)) {
            return jdbcSudokuBoardDao;
        } catch (SQLException e) {
            throw new DaoFileException("Database access failure");
        }
    }
}
