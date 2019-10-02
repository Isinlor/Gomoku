package UI;

import Board.SimpleBoard;
import Contract.BoardCell;
import Contract.Move;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.application.Application;

import static javafx.scene.paint.Color.*;


public class BoardUI extends Application{

    private BoardCell[][] boardState;
    private int boardSize = 15;
    private HBox root;
    private VBox infoPanel;
    private SimpleBoard board;
    private final int BOARD_PANEL_SIZE = 750;
    private Label currentPlayer;
    private Label justPlayer;

    public void start(Stage primaryStage)
    {
        root = new HBox();
        root.setStyle("-fx-border-color: black;");
        infoPanel = new VBox();
        justPlayer = new Label();
        justPlayer.setStyle("-fx-font:18 arial;");
        justPlayer.setText("Player: ");
        currentPlayer = new Label();
        currentPlayer.setStyle("-fx-font:14 arial;");
        infoPanel.setPrefSize(BOARD_PANEL_SIZE/4,BOARD_PANEL_SIZE);
        infoPanel.setStyle("-fx-background-color: #808080;");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        board = new SimpleBoard(15);
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
        gameBoard.setPrefSize(BOARD_PANEL_SIZE,BOARD_PANEL_SIZE);
        currentPlayer.setText(board.getCurrentColor().name());
        currentPlayer.setTextFill(Color.web(board.getCurrentColor().name().toLowerCase()));
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {

                Rectangle tile = new Rectangle(50, 50);
                tile.setFill(Color.BURLYWOOD);
                tile.setStroke(BLACK);

                final int cell_x = x;
                final int cell_y = y;

                tile.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.printf("Mouse entered cell [%d, %d]%n", cell_x, cell_y);
                        board.move(new Move(cell_x,cell_y));
                        updateBoard();
                    }
                });

                gameBoard.add(new StackPane(tile), y, x);

                BoardCell color = board.getCell(x, y);
                if(color!=BoardCell.Empty) {

                    Circle circle = new Circle(0, 0, 15);
                    switch (board.getCell(x, y)) {
                        case Black:
                            circle.setFill(BLACK);
                            break;
                        case White:
                            circle.setFill(WHITE);
                            break;
                    }
                    gameBoard.add(circle, y, x);
                    gameBoard.setHalignment(circle, HPos.CENTER); // To align horizontally in the cell
                    gameBoard.setValignment(circle, VPos.CENTER); // To align vertically in the cell


                }
            }
        }
        if(board.hasWinner()){
            System.out.println(board.getWinner() + " won!");
            GameOver.display(board, this);

        }

        infoPanel.getChildren().setAll(justPlayer, currentPlayer);
        root.getChildren().setAll(gameBoard, infoPanel);
    }
    // Black circle unicode = U+25CF
    // White circle: U+25CB


    public static void main(String[] args){
        launch(args);
    }
}
