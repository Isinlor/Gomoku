package Tree;

import Contract.Distribution;
import Distribution.DistributionTableMethod;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class MCTSBest implements DistributionFactory {

    private double c = Math.sqrt(2);

    public MCTSBest() {
    }

    public MCTSBest(double c) {
        this.c = c;
    }

    static class MCTSBestDistribution<T> implements Distribution<T> {
        private T node;

        MCTSBestDistribution(T node) {
            this.node = node;
        }

        public T sample() {
            return node;
        }
    }

    public Distribution<MCTSNode> getDistribution(MCTSNode node) {
        MCTSNode bestNode = null;
        double bestWeight = Double.NEGATIVE_INFINITY;
        for(MCTSNode child: node.getChildren()){
            double weight = child.getWinRatio() + c * Math.sqrt(Math.log(node.getGamesPlayed()) / child.getGamesPlayed());
            if(weight > bestWeight) {
                bestNode = child;
                bestWeight = weight;
            }
        }
        return new MCTSBestDistribution<MCTSNode>(bestNode);
    }
}