package kompo.sudoku;

import java.io.Serializable;
import kompo.sudoku.exceptions.NicWskaznikException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SudokuField implements Serializable, Cloneable, Comparable<SudokuField> {

    private static final long serialVersionUID = 2448670988101542591L;
    private int value;

    public void setValue(int value) {
        this.value = value;

    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SudokuField that)) {
            return false;
        }

        return new EqualsBuilder().append(value, that.value).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(value).toHashCode();
    }

    @Override
    public String toString() {
        return "" + value;
    }

    @Override
    public int compareTo(SudokuField o) throws NicWskaznikException {
        if (o == null) {
            throw new NicWskaznikException("Null was detected!");
        }
        return Integer.compare(this.getValue(), o.getValue());
    }

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
