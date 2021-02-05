package AI.pms;

import AI.Strategy;
import app.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PMS implements Strategy {

    private Node[] mGLayers;
    private Node[] maxConnectionsNodeLayers;
    private int colours = 4;
    private double averageLeafNodes;
    private HashMap<Long, Integer> times = new HashMap<>();
    private int turn = 0;
    private int spaceLeftHeuristic;
    private int garbageSatisfy;
    private int chainLengthSatisfy;
    private int depth;

    public PMS(int depth, int spaceLeft, int garbageSatisfy, int chainLengthSatisfy){
        this.depth = depth;
        prepare();
        spaceLeftHeuristic = spaceLeft;
        this.garbageSatisfy = garbageSatisfy;
        this.chainLengthSatisfy = chainLengthSatisfy;
    }
    public PMS(int depth, int spaceLeft, int garbageSatisfy){
        this.depth = depth;
        prepare();
        spaceLeftHeuristic = spaceLeft;
        this.garbageSatisfy = garbageSatisfy;
        this.chainLengthSatisfy = Integer.MAX_VALUE;
    }

    private void prepare(){
        mGLayers = new Node[depth];
        maxConnectionsNodeLayers = new Node[depth];
        for (int i = 0; i < mGLayers.length; i ++){
            mGLayers[i] = new Node();
            maxConnectionsNodeLayers[i] = new Node();
        }
    }

    @Override
    public Move makeMove(Board our, Puyo[][] currentPuyo, Board opponent){
        turn ++;
        long start = System.nanoTime();
        prepare();
        Node newRoot = new Node(our, null);
        Date d2 = new Date();
        int leafNodes = recursiveTree(newRoot, 0, mGLayers.length - 1, colours, currentPuyo);
//        System.out.println("Leaf nodes: " + leafNodes);
        averageLeafNodes = (1 - 1.0 / turn) * averageLeafNodes + (1.0 / turn) * leafNodes;
//        System.out.println("Tree made: " + (new Date().getTime() - d2.getTime()) + "ms");
        // Gauging the length of opponent chain
        Chain c = new Chain(opponent.copyBoard());
        c.runChain(opponent.findAllPuyo());
        // Getting the target node to get to
        Node target = selectNode(getHeuristics(c.chainLength(), getSpace(newRoot.getBoard())), c.chainLength() == 0 ? Integer.MAX_VALUE : c.chainLength());
        // Finding the first move to reach that node
        Move m = findNextMove(newRoot, target, currentPuyo[0]);
        // Making the move in the board
        our.dropPuyo(currentPuyo[0], m);
//        System.out.println("Total turn TIME :" + (new Date().getTime() - d.getTime()));
        long roundedTime = System.nanoTime() - start;
        times.put(roundedTime, times.getOrDefault(roundedTime, 0) + 1);
        return m;
    }

    public HashMap<Long, Integer> printStats(){
        System.out.println("Average Leaf Nodes: " + averageLeafNodes);
        return times;
    }

    private int getSpace(Board b){
        int space = 0;
        for (int i = 0; i < b.getNoCols(); i ++){
            for (int j = 0; j < b.getNoRows(); j ++){
                if (b.getPuyo(i,j) == null)
                    space ++;
            }
        }
        return space;
    }

    private ArrayList<Boolean> getHeuristics(int oppChainLength, int emptySpaceLeft){
        // Rather than space left, maybe number of possibilities left?
        // If there is only one column left, the AI can't use that much to create a big chain
        ArrayList<Boolean> result = new ArrayList<>();
        result.add(oppChainLength == 1);
        result.add(emptySpaceLeft <= spaceLeftHeuristic);
        result.add(mGLayers[0].getGarbage() >= garbageSatisfy);
        result.add(mGLayers[0].getChainLength() >= chainLengthSatisfy);
        return result;
    }

    private Move findNextMove(Node root, Node target, Puyo[] puyo){
        Node temp = target;
        while(temp.getParent() != root){
            temp = temp.getParent();
            if (temp == null){
                throw new RuntimeException("Parent is null");
            }
        }
        return findDiff(root, temp, puyo);
    }

    private Move findDiff(Node first, Node second, Puyo[] puyo){
        for (int col = 0; col < 6; col ++){
            for (int rot = 0; rot < 4; rot ++){
                Move m = new Move(col, rot);
                Board b1 = first.getBoard().copyBoard();
                b1.dropPuyo(puyo, m);
                if (b1.equalBoards(second.getBoard())){
                    return m;
                }
            }
        }
        Output o = new Output(new Board[]{first.getBoard(), second.getBoard()});
        throw new RuntimeException(o.printBoards() + "\n" + puyo[0].getColour() + " " + puyo[1].getColour());
    }

    public List<Node> generatePoss(Node parent, Puyo[] puyoPair){
        List<Node> resultStates = new ArrayList<>();

        for (int i = 0; i < 2; i ++){
            for (int col = 0; col < 6; col ++){
                Board temp = parent.getBoard().copyBoard();
                Move m = new Move(col, i * 2);
                if (temp.dropPuyo(puyoPair, m))
                    resultStates.add(new Node(temp, parent));
            }
            for (int row = 0; row < 5; row ++){
                Move m = new Move(row, i * 2 + 1);
                Board temp = parent.getBoard().copyBoard();
                if (temp.dropPuyo(puyoPair, m))
                    resultStates.add(new Node(temp, parent));
            }
        }
        return resultStates;
    }

    private int recursiveTree(Node root, int depth, int maxDepth, int colours, Puyo[][] currentPuyo){
        if (root.getGarbage() == -1) {
            if (depth < maxDepth) {
                List<Node> children = generatePoss(root, currentPuyo[depth]);
                int totalLeafNodes = 0;
                for (Node child : children) {
                    totalLeafNodes += recursiveTree(child, depth + 1, maxDepth, colours, currentPuyo);
                    if (child.getGarbage() >= mGLayers[depth].getGarbage()) {
                        mGLayers[depth] = child;
                    }
                    if (child.getConnections() >= maxConnectionsNodeLayers[depth].getConnections()) {
                        maxConnectionsNodeLayers[depth] = child;
                    }
                }
                return totalLeafNodes;
            } else if (depth == maxDepth) {
                List<Node> children = maxDepth == 2 ? generatePoss(root, currentPuyo[depth]) : generateDepth4Layer(root, colours);
                for (Node child : children) {
                    if (child.getGarbage() >= mGLayers[depth].getGarbage()) {
                        mGLayers[depth] = child;
                    }
                    if (child.getConnections() >= maxConnectionsNodeLayers[depth].getConnections()) {
                        maxConnectionsNodeLayers[depth] = child;
                    }
                }
                return children.size();
            }
        }
        return 0;
    }

    private Node selectNode(List<Boolean> heuristics, int oppLength){
        if (heuristics.contains(true)){
            if (mGLayers[0].getGarbage() > -1)
                return mGLayers[0];
        }
        else {
            Node mostGarbage = new Node();
            for (int i = 1; i < Math.min(mGLayers.length, oppLength); i ++){
                if (mGLayers[i].getGarbage() >= mostGarbage.getGarbage())
                    mostGarbage = mGLayers[i];
            }
            if (mostGarbage.getGarbage() > -1)
                return mostGarbage;
        }
        Node mostConnections = new Node();
        for (int i = 0; i < Math.min(maxConnectionsNodeLayers.length, oppLength); i ++) {
            if (maxConnectionsNodeLayers[i].getConnections() >= mostConnections.getConnections())
                mostConnections = maxConnectionsNodeLayers[i];
        }
        return mostConnections;
    }

    private List<Node> generateDepth4Layer(Node parent, int colours){
        List<Node> children = new ArrayList<>();
        for (int i = 0; i < colours; i ++){
            Puyo[] p = {new Puyo(Colour.values()[i]), new Puyo(Colour.values()[i])};
            children.addAll(generatePoss(parent, p));
        }
        return children;
    }
}
