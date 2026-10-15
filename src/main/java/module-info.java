module com.brakid.gui {
    requires java.base;
    requires com.google.common;
    requires org.slf4j;
    requires javafx.graphics;
    requires javafx.controls;
    requires tools.jackson.databind;
    requires tools.jackson.datatype.guava;

    opens com.brakid.types to tools.jackson.databind;
        
    exports com.brakid.gui;
    exports com.brakid.types;
}