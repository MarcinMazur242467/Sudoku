package view;

import java.util.ListResourceBundle;

public class Authors extends ListResourceBundle {
    protected Object[][] getContents() {
        return new Object[][] {
                {"IMIE1","Piotr"},
                {"IMIE2","Marcin"},
                {"NAZWISKO1","Płeska"},
                {"NAZWISKO2","Mazur"},
                {"NRINDEKSU1","242499"},
                {"NRINDEKSU2","242467"}
        };
    }
}