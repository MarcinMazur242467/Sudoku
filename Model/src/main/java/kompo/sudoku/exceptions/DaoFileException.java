package kompo.sudoku.exceptions;

import java.io.IOException;

public class DaoFileException extends IOException {
    public DaoFileException(final String message) {
        super(message);
    }
}
