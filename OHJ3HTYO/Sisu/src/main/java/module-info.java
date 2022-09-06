module fi.tuni.prog3.sisu {
    requires transitive javafx.controls;
    requires javafx.web;
    // requires java.desktop;
    requires com.google.gson;
    requires javafx.fxml;

    exports fi.tuni.prog3.sisu;

    opens fi.tuni.prog3.sisu to javafx.fxml, com.google.gson;
}
