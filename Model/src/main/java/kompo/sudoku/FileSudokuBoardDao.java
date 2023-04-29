package kompo.sudoku;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import kompo.sudoku.exceptions.DaoFileException;



public class FileSudokuBoardDao extends IOException implements Dao<SudokuBoard>, AutoCloseable {

    private final File fileName;

    public FileSudokuBoardDao(File fileName) {
        this.fileName = fileName;
    }

    @Override
    public SudokuBoard read() throws DaoFileException {
        try (FileInputStream fileIn = new FileInputStream(fileName);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
            Object obj = objectIn.readObject();
            return (SudokuBoard) obj;
        } catch (ClassNotFoundException | IOException e) {
            throw new DaoFileException("File has not been loaded successfully");
        }
    }

    @Override
    public void write(SudokuBoard sudokuBoard) throws DaoFileException {
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
            objectOut.writeObject(sudokuBoard);
        } catch (IOException e) {
            throw new DaoFileException("File has not been saved successfully");
        }
    }

    @Override
    public void close() {
    }
}
