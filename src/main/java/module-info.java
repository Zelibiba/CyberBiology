module com.cyberbiology.cyberbiology {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.cyberbiology.cyberbiology to javafx.fxml;
    exports com.cyberbiology.cyberbiology;
    exports Controllers;
    opens Controllers to javafx.fxml;
}