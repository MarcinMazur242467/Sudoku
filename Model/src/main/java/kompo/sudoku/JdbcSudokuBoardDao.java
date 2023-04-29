package kompo.sudoku;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import kompo.sudoku.exceptions.DaoFileException;


public class JdbcSudokuBoardDao implements Dao<SudokuBoard> {

    private Connection con;

    private final String host = "jdbc:mysql://localhost:3306/sudoku";
    private final String user = "root";
    private final String password = "admin";
    private String boardName;
    private ResourceBundle bundle = ResourceBundle.getBundle("Language");

    public JdbcSudokuBoardDao(String boardName) throws SQLException {
        con = DriverManager.getConnection(host, user, password);
        con.setAutoCommit(false);
        this.boardName = boardName;
    }

    @Override
    public SudokuBoard read() throws DaoFileException {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard sudokuBoard = new SudokuBoard(solver);
        String query = "select wartosc from sudokufields where sudokuBoard_id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (resultSet.next()) {
                        sudokuBoard.set(i, j, resultSet.getInt("wartosc"));
                    }
                }
            }
            con.commit();
            return sudokuBoard;
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new DaoFileException(bundle.getString("daoinfoR"));
        } catch (DaoFileException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void write(SudokuBoard sudokuBoard) throws DaoFileException {
        String query = "insert into sudokuboard (nazwa) values (?)";
        String query2 = "insert into sudokufields (sudokuBoard_id, wartosc) values (?, ?)";
        try (PreparedStatement preparedStatement = con.prepareStatement(query);
             PreparedStatement preparedStatement2 = con.prepareStatement(query2)) {
            preparedStatement.setString(1, boardName);
            preparedStatement.execute();
            preparedStatement2.setInt(1, getId());
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    preparedStatement2.setInt(2, sudokuBoard.get(i, j));
                    preparedStatement2.execute();
                }
            }
            con.commit();
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            throw new DaoFileException(bundle.getString("daoinfoW"));
        } catch (DaoFileException e) {
            throw new RuntimeException(e);
        }
    }

    public int getId() throws DaoFileException {
        String querySelect = "select id from sudokuboard where nazwa = ?";
        try (PreparedStatement preparedStatement3 = con.prepareStatement(querySelect)) {
            preparedStatement3.setString(1, boardName);
            ResultSet resultSet = preparedStatement3.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            throw new DaoFileException(bundle.getString("daoinfoW"));
        }
        return 0;
    }

    @Override
    public void close() {

    }
}
