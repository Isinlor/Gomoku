package UI;

import Board.Board;
import Contract.BoardCell;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.application.Application;




public class BoardUI extends Application{

    private BoardCell[][] boardState;
    private int boardSize = 15;
    private Pane root;
    private Board board;

    public void start(Stage primaryStage)
    {
        root = new Pane();
        root.setStyle("-fx-border-color: black;");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        board = new Board(15);
//        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                System.out.println(event.getSceneX());
//                System.out.println(event.getSceneY());
//                int x =(int) event.getSceneX()/50;
//                int y =(int) event.getSceneY()/50;
//                System.out.println(x);
//                System.out.println(y);
//                board.move(y,x);
//                root.getChildren().add(updateBoard(board));
//            }
//        });
        updateBoard();
        primaryStage.show();

    }

  

    public void updateBoard(){
        boardSize = board.getSize();
        GridPane gameBoard = new GridPane();
        gameBoard.setPrefSize(750, 750);
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {

                Rectangle tile = new Rectangle(50, 50);
                tile.setFill(Color.BURLYWOOD);
                tile.setStroke(Color.BLACK);

                final int cell_x = x;
                final int cell_y = y;

                tile.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.printf("Mouse enetered cell [%d, %d]%n", cell_x, cell_y);
                        board.move(cell_x,cell_y);
                        updateBoard();
                    }
                });

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

        root.getChildren().removeAll();
        root.getChildren().add(gameBoard);
    }
    // Black circle unicode = U+25CF
    // White circle: U+25CB


    public static void main(String[] args){
        launch(args);
    }
}
