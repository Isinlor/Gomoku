package UI;

import Board.Board;
import Contract.BoardCell;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.application.Application;




public class BoardUI extends Application{

    private BoardCell[][] boardState;
    private int boardSize = 15;


    public void start(Stage primaryStage)
    {

        Pane root = new Pane();

        root.setStyle("-fx-border-color: black;");


        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        Board board = new Board(3);
        root.getChildren().add(updateBoard(board));

        primaryStage.show();
    }

  

    public Parent updateBoard(Board board){
        boardSize = board.getSize();
        GridPane gameBoard = new GridPane();
        gameBoard.setPrefSize(755, 755);



        for (int x = 0; x <boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {

                Rectangle tile = new Rectangle(50, 50);
                tile.setFill(Color.BURLYWOOD);
                tile.setStroke(Color.BLACK);

                switch (board.getCell(x, y)) {
                    case Empty:
                        gameBoard.add(new StackPane(tile), y, x);
                        break;
                    case Black:
                        gameBoard.add(new Text("⬤"), y, x);
                        break;
                    case White:
                        gameBoard.add(new Text("◯"), y, x);
                        break;
                }




            }
        }

        return gameBoard;
    }
    // Black circle unicode = U+25CF
    // White circle: U+25CB


    public static void main(String[] args){
        launch(args);
    }
}
