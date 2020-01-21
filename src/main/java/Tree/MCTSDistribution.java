package Tree;

import Contract.Distribution;

import java.util.HashMap;
import java.util.LinkedHashMap;
import Distribution.*;
import Contract.*;

public class MCTSDistribution implements DistributionFactory {

    private double c = Math.sqrt(2);

    public MCTSDistribution() {
    }

    public MCTSDistribution(double c) {
        this.c = c;
    }

    public Distribution<MCTSNode> getDistribution(MCTSNode node) {
        HashMap<MCTSNode,Double> weightedChildren = new LinkedHashMap<>();
        for(MCTSNode child: node.getChildren()){
            double weight = child.getWinRatio() + c * Math.sqrt(Math.log(node.getGamesPlayed()) / child.getGamesPlayed());
            weightedChildren.put(child,weight);
        }
        return new DistributionTableMethod<MCTSNode>(weightedChildren);
    }
}