package kompo.sudoku;

import kompo.sudoku.exceptions.CloneException;
import kompo.sudoku.exceptions.NicWskaznikException;
import org.junit.jupiter.api.Test;

import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

class SudokuFieldTest {
    private ResourceBundle bundle = ResourceBundle.getBundle("Language");
    @Test
    void testEquals() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        SudokuField field = new SudokuField();
        assertTrue(field.equals(field));
        assertFalse(field.equals(board));
    }

    @Test
    void testHashCode() {
        SudokuField field = new SudokuField();
        int hashCode = field.hashCode();
        field.setValue(1);
        assertTrue(field.hashCode() != hashCode);
    }

    @Test
    void testToString() {
        SudokuField field = new SudokuField();
        field.setValue(1);
        assertEquals("1",field.toString());
    }
    @Test
    void sameHashCodeTest(){
        SudokuField field1 = new SudokuField();
        SudokuField field2 = new SudokuField();
        field1.setValue(1);
        field2.setValue(1);
        assertTrue(field1.hashCode() == field2.hashCode());
    }

    @Test
    public void nullException() throws NicWskaznikException {
        SudokuField field = new SudokuField();
        field.setValue(1);
        assertThrows(NicWskaznikException.class,()->{field.compareTo(null);});
    }

    @Test
    public void cloneTestCase() throws CloneException {
        SudokuField field = new SudokuField();
        field.setValue(1);
        SudokuField new_field = (SudokuField) field.clone();
        assertEquals(field,new_field);
    }

    @Test
    public void compareToTestCase(){
        SudokuField field1 = new SudokuField();
        field1.setValue(1);
        SudokuField field2 = new SudokuField();
        field2.setValue(2);
        SudokuField field3 = new SudokuField();
        field3.setValue(1);
        assertEquals(field1.compareTo(field2),-1);
        assertEquals(field2.compareTo(field1),1);
        assertEquals(field1.compareTo(field3),0);


    }

}