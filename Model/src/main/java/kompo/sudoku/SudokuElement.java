package kompo.sudoku;

import java.io.Serializable;
import java.util.List;
import kompo.sudoku.exceptions.CloneException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class SudokuElement implements Serializable, Cloneable {
    private final List<SudokuField> part;


    public SudokuElement(final List<SudokuField> part) {
        this.part = part;
    }


    public boolean verify() {
        for (int i = 0; i < 9; i++) {
            for (int j = i + 1; j < 9; j++) {
                if (part.get(i).getValue() == part.get(j).getValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SudokuElement that)) {
            return false;
        }

        return new EqualsBuilder().append(part, that.part).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(part).toHashCode();
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            string.append(part.get(i).getValue());
            string.append("\t");
        }
        return string.toString();
    }

    @Override
    protected Object clone() throws CloneException {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new CloneException("Clone failure!");
        }
    }
}
