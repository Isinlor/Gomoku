package NeuralNetwork;

import Contract.BoardState;
import Contract.ExtendedBoardState;
import Protobuf.DatasetProtos;
import com.google.common.primitives.Doubles;

import java.io.*;
import java.util.*;

public class GenerateCNNDataSet {

    public static void main(String[] args) throws IOException {
        String resourcePath = "src/main/resources/";
        String filePathIn = resourcePath + "training_games_mcts400_forced3_all.ser";
        if (args.length > 0) {
            filePathIn = args[0];
        }
        List<TrainingGame> games = loadGames(filePathIn);
        DatasetProtos.DataSet dataset =  generateDataset(games);
        FileOutputStream output = null;

        try {
            output = new FileOutputStream("src/main/resources/dataset_mcts400_forced3_all.small.bin");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            dataset.writeTo(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<TrainingGame> loadGames(String filePath) {
        try {
            File file = new File(filePath);
            FileInputStream fi = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fi);
            ArrayList<TrainingGame> games = (ArrayList<TrainingGame>) oi.readObject();
            oi.close();
            fi.close();
            System.out.println("Loaded games: " + games.size());
            return games;

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private static DatasetProtos.DataSet generateDataset(List<TrainingGame> games) {
        DatasetProtos.DataSet.Builder datasetBuilder = DatasetProtos.DataSet.newBuilder();

        int gameId = 0;
        for (TrainingGame game : games) {
            for (ExtendedBoardState boardState : game.getHistory()) {
                double gameOutcome;
                if (game.winner == null) {
                    gameOutcome = 0;
                } else {
                    gameOutcome = game.winner == boardState.getCurrentPlayer() ? 1 : -1;
                }
//                for (int r = 0; r < 4; r++) {
                    double[][][] boardStateMatrix = boardState.toMultiDimensionalMatrix();
                    DatasetProtos.DataInstance.Builder dataInstance = DatasetProtos.DataInstance.newBuilder();

//                    double[] uniformPolicy = new double[boardStateMatrix.length * boardStateMatrix.length];
//                    Arrays.fill(uniformPolicy, 1/uniformPolicy.length);

                    double[] policy = boardState.toPolicyVector();
                    double[] flattenedState = Arrays.stream(boardStateMatrix)
                            .flatMap(Arrays::stream)
                            .flatMapToDouble(Arrays::stream)
                            .toArray();
                    dataInstance.setGameId(gameId);
                    dataInstance.addAllPolicy(Doubles.asList(policy));
                    dataInstance.addAllState(Doubles.asList(flattenedState));
                    dataInstance.setValue(gameOutcome);
                    datasetBuilder.addData(dataInstance.build());
//                }
            }

            gameId++;
        }

        return datasetBuilder.build();
    }

}
