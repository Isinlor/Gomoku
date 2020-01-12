package NeuralNetwork;

import Contract.BoardState;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.EvaluativeListener;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.SamplingDataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.primitives.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Train {

    private static ArrayList<TrainingGame> loadGames(String filePath) {
        ArrayList games = new ArrayList<TrainingGame>();

        try {
            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            TrainingGame game;
            while((game = (TrainingGame)in.readObject()) !=null ) {
                games.add(game);
            }
            in.close();
            fileIn.close();
        } catch (EOFException e) {

        } catch (IOException | ClassNotFoundException i)  {
            i.printStackTrace();
        }
        System.out.println("Loaded games: " + games.size());
        return games;
    }

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/games.ser";
        if(args.length>0) {
            filePath = args[0];
        }

        List<TrainingGame> games = loadGames(filePath);
        List<TrainingGame> testGames = games.subList(0, 100);
        games = games.subList(100, 103);

        DataSetIterator iterator = getDataSetIterator(games);
        DataSetIterator testIterator = getDataSetIterator(testGames);

        final int boardSize = 9;
        int numEpochs = 50; // number of epochs to perform
        MultiLayerNetwork model = Model.get(boardSize);

        System.out.println(model.evaluateRegression(iterator).stats());
        System.out.println(iterator.next());

        model.setListeners(new ScoreIterationListener(10));  //print the score with every iteration
        model.setListeners(new EvaluativeListener(iterator, 10, model.evaluateRegression(iterator)));  //print the score with every iteration

        model.fit(iterator, numEpochs);

        model.save(new File("src/main/resources/model.dl4j"), true);

        System.out.println(model.output(iterator));

        System.out.println(model.evaluateRegression(testIterator).stats());

    }

    public static DataSetIterator getDataSetIterator(List<TrainingGame> games) {
        DataSet dataset = getDataSet(games);
        return new SamplingDataSetIterator(dataset, 3, 10);
    }

    public static DataSet getDataSet(List<TrainingGame> games) {
        int rows = 0;
        int black = 0;
        int white = 0;
        int draw = 0;
        for(TrainingGame game: games) {
            rows += game.getHistory().size();
            if(game.getWinner() != null) {
                switch (game.getWinner()) {
                    case Black:
                        black++;
                        break;
                    case White:
                        white++;
                        break;
                }
            } else {
                draw++;
            }

        }
        int rowLength = games.get(0).getHistory().get(0).toVector().length;

//        System.out.println("Black: " + black + " White: " + white + " Draw: " + draw);

        INDArray features = Nd4j.zeros(rows, rowLength);
        INDArray labels = Nd4j.zeros(rows, 1);

        int row = 0;
        ArrayList<Pair<INDArray, INDArray>> pairs = new ArrayList<>();
        for(TrainingGame game:games) {
            for(BoardState boardState: game.getHistory()) {
                int gameOutcome;
                if(game.winner==null) {
                    gameOutcome = 0;
                } else{
                    gameOutcome = game.winner==boardState.getCurrentPlayer() ? 1 : -1;
                }
                float [] boardStateVector = boardState.toVector();

                features.putRow(row, Nd4j.create(boardStateVector, new int[]{1, boardStateVector.length}));
                labels.putRow(row, Nd4j.create(new float[]{gameOutcome}, new int[]{1, 1}));

                Pair<INDArray, INDArray> pair = new Pair<INDArray, INDArray>(
                        Nd4j.create(boardStateVector, new int[]{1, boardStateVector.length}),
                        Nd4j.create(new float[]{gameOutcome}, new int[]{1, 1})
                );
                pairs.add(pair);
                row++;
            }
        }
//        INDArrayDataSetIterator iterator = new INDArrayDataSetIterator(pairs, 15);

        return new DataSet(features, labels);
    }
}
