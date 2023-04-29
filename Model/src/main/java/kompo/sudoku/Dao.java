package kompo.sudoku;

import kompo.sudoku.exceptions.DaoFileException;

public interface Dao<T> extends AutoCloseable {
    T read() throws DaoFileException;

    void write(T t) throws DaoFileException;
}
