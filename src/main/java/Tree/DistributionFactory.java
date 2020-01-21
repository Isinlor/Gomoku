package Tree;

import Contract.Distribution;

public interface DistributionFactory {
    Distribution<MCTSNode> getDistribution(MCTSNode node);
}