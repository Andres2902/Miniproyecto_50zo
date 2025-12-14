module org.example.eiscuno {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    
    opens org.example.eiscuno to javafx.fxml;
    opens org.example.eiscuno.controller to javafx.fxml;
    opens org.example.eiscuno.model.card to javafx.base;
    opens org.example.eiscuno.model.player to javafx.base;
    opens org.example.eiscuno.model.deck to javafx.base;
    opens org.example.eiscuno.model.table to javafx.base;
    opens org.example.eiscuno.model.game to javafx.base;
    opens org.example.eiscuno.model.machine to javafx.base;

    exports org.example.eiscuno;
    exports org.example.eiscuno.controller;
    exports org.example.eiscuno.model.card;
    exports org.example.eiscuno.model.player;
    exports org.example.eiscuno.model.deck;
    exports org.example.eiscuno.model.table;
    exports org.example.eiscuno.model.game;
    exports org.example.eiscuno.model.machine;
    exports org.example.eiscuno.model.exceptions;

}