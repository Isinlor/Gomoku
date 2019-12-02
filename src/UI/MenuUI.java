package UI;

import Board.*;
import Contract.BoardCell;
import Contract.Game;
import Contract.Move;
import Contract.Player;
import Player.Players;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.application.Application;

import java.util.Collection;

import static javafx.application.Application.launch;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

public class MenuUI extends Application{

    private BoardCell[][] boardState;
    private int boardSize = 15;
    private String p1Name,p2Name;
    private HBox root;
    private VBox infoPanel;
    private final int BOARD_PANEL_SIZE = 750;
    private Label currentPlayer;
    private Label justPlayer;
    private Label loading;
    private Scene menuScene;
    private boolean pvpMode = true;
    private String aiMode = null;
    private boolean aiRunning = false;

    private Player aiPlayer = null;


    private Game game = null;
    private SimpleBoard board;


    public void start(Stage primaryStage)
    {

        //MENU SCENE
        BorderPane menuPane = new BorderPane();
        menuPane.setStyle("-fx-background-color: #808B96 ;");
        VBox menu = new VBox();
        menu.setSpacing(10);
        menu.setPadding(new Insets(0, 20, 10, 20));
        menu.setAlignment(Pos.CENTER);
        Text title = new Text("Gomoku");
        title.setFont(new Font("Helvetica",30));
        //NEW GAME SCENE!!! ---------------------------------------------------------------------
        Button newGame = new Button("New Game");
        newGame.setOnAction(actionEvent -> {
            //NAME SELECT SCENE STUFF
            BorderPane nameSelect = new BorderPane();
            nameSelect.setStyle("-fx-background-color: #808B96 ;");
            VBox nameSelectBox = new VBox();
            nameSelectBox.setSpacing(10);
            nameSelectBox.setPadding(new Insets(0, 20, 10, 20));
            nameSelectBox.setAlignment(Pos.CENTER);

            Text newGameTitle = new Text("New Game");
            newGameTitle.setFont(new Font("Helvetica",30));

            Text boardSizeText = new Text("Board size: ");
            boardSizeText.setFont(new Font("Helvetica",16));
            TextField boardSizeTextField = new TextField("15");

            Text chooseMode = new Text("Choose how you want to play:");
            chooseMode.setFont(new Font("Helvetica",16));
            Text nameSelectExplaination = new Text("Please fill in the name(s) and click continue:");
            nameSelectExplaination.setFont(new Font("Helvetica",16));
            Text p1NameText = new Text("Player 1 name: ");
            p1NameText.setFont(new Font("Helvetica",16));
            TextField p1TextField = new TextField("Player 1");
            Text p2NameText = new Text("Player 2 name: ");
            p2NameText.setFont(new Font("Helvetica",16));
            TextField p2TextField = new TextField("Player 2");

            Text aiChooseText = new Text("Select the ai you want to use below:");
            aiChooseText.setFont(new Font("Helvetica",16));
            ComboBox aiOptions = new ComboBox();
            Collection<String> ais = Players.getNames();
            ais.remove("human");
            aiOptions.getItems().addAll(ais);
            EventHandler<ActionEvent> aiOptionsEvent = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e)
                {
                    aiMode = (String) aiOptions.getValue();
                }};
            aiOptions.setOnAction(aiOptionsEvent);

            HBox buttons = new HBox();
            buttons.setAlignment(Pos.CENTER);
            Button backToMenu = new Button("Back");
            backToMenu.setOnAction(actionEvent2 -> {
                primaryStage.setScene(menuScene);
            });
            Button continueToGame = new Button("Continue");
            continueToGame.setOnAction(actionEvent2 -> {
                if(pvpMode==false&&aiMode.equals(null)){
                    //No ai selected so no continuing
                }else {
                    p1Name = p1TextField.getCharacters().toString();
                    p2Name = p2TextField.getCharacters().toString();
                    //NEW GAME SCENE AFTER NAME SELECT!
                    root = new HBox();
                    root.setStyle("-fx-border-color: black;");
                    infoPanel = new VBox();
                    justPlayer = new Label();
                    justPlayer.setStyle("-fx-font:18 arial;");
                    justPlayer.setText("Player: ");
                    currentPlayer = new Label();
                    currentPlayer.setStyle("-fx-font:14 arial;");
                    loading = new Label();
                    loading.setStyle("-fx-font:12 arial;");
                    infoPanel.setPrefSize(BOARD_PANEL_SIZE / 4, BOARD_PANEL_SIZE);
                    infoPanel.setStyle("-fx-background-color: #808080;");
                    Scene scene = new Scene(root, 950, 767);
                    primaryStage.setScene(scene);

                    if(aiMode!=null) {
                        aiPlayer = Players.get(aiMode.toLowerCase());
                    }

                    int boardSize = Integer.parseInt(boardSizeTextField.getCharacters().toString());

                    board = new SimpleBoard(boardSize);
                    updateBoard();

                }
            });
            //Back and continue buttons
            HBox modeSelect = new HBox();
            modeSelect.setSpacing(10);
            modeSelect.setPadding(new Insets(0, 20, 10, 20));
            modeSelect.setAlignment(Pos.CENTER);
            Button pvp = new Button("2 Player");
            pvp.setOnAction(actionEvent3 -> {
                pvpMode = true;
                nameSelectBox.getChildren().clear();
                nameSelectBox.getChildren().addAll(newGameTitle,boardSizeText,boardSizeTextField,chooseMode,modeSelect,nameSelectExplaination,p1NameText,
                        p1TextField,p2NameText,p2TextField,buttons);
            });
            Button ai = new Button("AI Opponent");
            ai.setOnAction(actionEvent4 -> {
                pvpMode = false;
                nameSelectBox.getChildren().clear();
                nameSelectBox.getChildren().addAll(newGameTitle,boardSizeText,boardSizeTextField,chooseMode,modeSelect,aiChooseText,aiOptions,nameSelectExplaination,p1NameText,
                        p1TextField,buttons);
            });
            modeSelect.getChildren().addAll(pvp,ai);
            buttons.setSpacing(10);
            buttons.setPadding(new Insets(0, 20, 10, 20));
            buttons.getChildren().addAll(backToMenu,continueToGame);
            nameSelectBox.getChildren().addAll(newGameTitle,boardSizeText,boardSizeTextField,chooseMode,modeSelect,nameSelectExplaination,p1NameText,
                    p1TextField,p2NameText,p2TextField,buttons);
            nameSelect.setCenter(nameSelectBox);
            Scene nameSelectScene = new Scene(nameSelect,1000, 600);
            primaryStage.setScene(nameSelectScene);
            primaryStage.show();

        });
        //HOW TO PLAY SCENE! -------------------------------------------------------------------
        Button howTo = new Button("How to play");
        howTo.setOnAction(actionEvent ->  {
            BorderPane pane = new BorderPane();
            pane.setStyle("-fx-background-color: #808B96 ;");
            Text howToPlayTitle = new Text("How to play");
            howToPlayTitle.setFont(new Font("Helvetica",30));

            Text howToPlayText = new Text("Gomoku or Go-moku or Five in line, is a traditional oriental game, originally from China. In Japanese language Go means five, and moku pieces (or eyes or dots).\n" +
                    "\n" +
                    "Black plays first, and players alternate in placing a stone of their color on an empty intersection. The winner is the first player to get an unbroken row of five stones horizontally, vertically, or diagonally.");
            howToPlayText.setWrappingWidth(600);
            howToPlayText.setFont(new Font("Helvetica",20));
            Button backToMenu = new Button("Back");
            backToMenu.setOnAction(actionEvent2 -> {
                primaryStage.setScene(menuScene);
            });
            VBox howToBox = new VBox();
            howToBox.setAlignment(Pos.CENTER);
            howToBox.setSpacing(10);
            howToBox.getChildren().addAll(howToPlayTitle,howToPlayText,backToMenu);
            pane.setCenter(howToBox);
            Scene howToScene = new Scene(pane,1000, 600,Color.BROWN);
            primaryStage.setScene(howToScene);
            primaryStage.show();
        });

        //---------------------------------------------------------------------------------------
        menu.getChildren().addAll(title,newGame,howTo);
        menuPane.setCenter(menu);
        menuScene = new Scene(menuPane ,800, 600);
        primaryStage.setTitle("Gomoku");
        primaryStage.setScene(menuScene);
        primaryStage.show();

    }

    Runnable aiPlayerRunnable = () -> {
        board.move(aiPlayer.getMove(board));
        Platform.runLater(new Runnable()
        {
            @Override
            public void run() {
                aiRunning = false;
                loading.setText("");
                updateBoard();
            }
        });
    };


    public void updateBoard(){
        boardSize = board.getSize();
        GridPane gameBoard = new GridPane();
        gameBoard.setPrefSize(BOARD_PANEL_SIZE,BOARD_PANEL_SIZE);
        if(board.getCurrentColor().name().equals("White")){
            currentPlayer.setText(p1Name);}
        else{
            currentPlayer.setText(p2Name);
        }
        currentPlayer.setTextFill(Color.web(board.getCurrentColor().name().toLowerCase()));
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {

                Rectangle tile = new Rectangle(750/boardSize, 750/boardSize);
                tile.setFill(Color.BURLYWOOD);
                tile.setStroke(BLACK);

                final int cell_x = x;
                final int cell_y = y;

                if(!aiRunning) {
                    tile.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {

                            System.out.printf("Mouse entered cell [%d, %d]%n", cell_x, cell_y);
                            try {
                                board.move(new Move(cell_x, cell_y));
                                if (aiPlayer != null && !board.hasWinner()) {
                                    aiRunning = true;
                                    loading.setText("Loading...");
                                    Thread thread = new Thread(aiPlayerRunnable);
                                    thread.start();
                                }
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }

                            updateBoard();

                        }
                    });
                }

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

        infoPanel.getChildren().setAll(justPlayer, currentPlayer, loading);
        root.getChildren().setAll(gameBoard, infoPanel);
    }

    public String getP1Name(){return p1Name;}
    public String getP2Name(){return p2Name;}

    public static void main(String[] args){
        launch(args);
    }
}
