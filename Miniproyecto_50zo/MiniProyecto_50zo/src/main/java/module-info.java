module org.example.cincuentazo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.example.cincuentazo to javafx.fxml;
    opens org.example.cincuentazo.controller to javafx.fxml;
    opens org.example.cincuentazo.model.card to javafx.base;
    opens org.example.cincuentazo.model.player to javafx.base;
    opens org.example.cincuentazo.model.deck to javafx.base;
    opens org.example.cincuentazo.model.table to javafx.base;
    opens org.example.cincuentazo.model.game to javafx.base;
    opens org.example.cincuentazo.model.machine to javafx.base;

    exports org.example.cincuentazo;
    exports org.example.cincuentazo.controller;
    exports org.example.cincuentazo.model.card;
    exports org.example.cincuentazo.model.player;
    exports org.example.cincuentazo.model.deck;
    exports org.example.cincuentazo.model.table;
    exports org.example.cincuentazo.model.game;
    exports org.example.cincuentazo.model.machine;
    exports org.example.cincuentazo.model.exceptions;

}