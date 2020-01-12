package UI;

import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
//import Contract.*;
import Board.SimpleBoard;

public class GameOver {

    public static void display(SimpleBoard board, MenuUI boardUI)
    {
        Stage gameover=new Stage();

        gameover.initModality(Modality.APPLICATION_MODAL);
        gameover.setTitle("Game Over!");
        Label label = new Label(board.getWinner() + " has won!");
        Button button = new Button("Close");

        button.setOnAction(e -> {
            gameover.close();
        });

        VBox layout= new VBox(15);

        layout.getChildren().addAll(label, button);

        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 250);

        gameover.setScene(scene);

        gameover.initStyle(StageStyle.UNDECORATED);

        gameover.show();

    }

    public static void display(SimpleBoard board, BoardUI boardUI)
    {
        Stage gameover=new Stage();

        gameover.initModality(Modality.APPLICATION_MODAL);
        gameover.setTitle("Game Over!");

        Label label = new Label(board.getWinner() + " has won!!");

        Button button = new Button("New Game");


        button.setOnAction(e -> {
            board.resetBoard();
            gameover.close();
            boardUI.updateBoard();
        });


        VBox layout= new VBox(15);


        layout.getChildren().addAll(label, button);

        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 250);


        gameover.setScene(scene);

        gameover.initStyle(StageStyle.UNDECORATED);

        gameover.show();

    }

}
