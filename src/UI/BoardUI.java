package UI;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.application.Application;




public class BoardUI extends Application{

    private int[][] boardMatrix;
    final int BOARD_SIZE = 15;
    private int win_length ;

    public void start(Stage primaryStage)
    {

        Pane root = new Pane();

        root.setStyle("-fx-border-color: black;");


        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        root.getChildren().add(createBoard());

        primaryStage.show();
    }

    public Parent createBoard() {

        GridPane gameBoard = new GridPane();
        gameBoard.setPrefSize(755, 755);

        for (int i = 0; i <BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {

                Rectangle tile = new Rectangle(50, 50);
                tile.setFill(Color.BURLYWOOD);
                tile.setStroke(Color.BLACK);

                gameBoard.add(new StackPane(tile), j, i);

                //GridPane.setRowIndex(tile, i);
                //GridPane.setColumnIndex(tile, j);
                //gameBoard.getChildren().addAll(tile, text);
                //tile.setOnMouseClicked(event -> drawMove(text));
            }
        }
        return gameBoard;
    }

    public static void main(String[] args){
        launch(args);
    }
}
