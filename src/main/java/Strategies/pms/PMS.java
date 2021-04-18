package Strategies.pms;

import Strategies.Strategy;
import Game.*;

import java.util.ArrayList;
import java.util.List;

public class PMS implements Strategy {

    private Node[] mGLayers;
    private Node[] maxConnectionsNodeLayers;
    private int colours = 4;
    private ArrayList<Long> times = new ArrayList<>();
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
        for (int i = 0; i < depth; i ++){
            mGLayers[i] = new Node();
            maxConnectionsNodeLayers[i] = new Node();
        }
    }

    @Override
    public Move makeMove(Board our, Puyo[][] currentPuyo, Board opponent){
        long start = System.nanoTime();
        prepare();
        Node newRoot = new Node(our, null);
        createTree(newRoot, depth - 1, colours, currentPuyo);
        // Gauging the length of opponent chain
        Chain c = new Chain(opponent.copyBoard());
        c.runChain(opponent.findAllPuyo());
        // Getting the target node to get to
        Node target = selectNode(getHeuristics(c.chainLength(), getSpace(newRoot.getBoard())), c.chainLength() == 0 ? Integer.MAX_VALUE : c.chainLength());
        // Finding the first move to reach that node
        Move m = findNextMove(newRoot, target, currentPuyo[0]);
        // Making the move in the board
        our.dropPuyo(currentPuyo[0], m);
        long roundedTime = System.nanoTime() - start;
        times.add(roundedTime / (1000000));
        return m;
    }

    public ArrayList<Long> getTimes(){
        ArrayList<Long> temp = (ArrayList<Long>) times.clone();
        times.clear();
        return temp;
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
        // If there is only one column left, the Strategies can't use that much to create a big chain
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

    private Node checkBestGarbageP(Node n, Node best){
        if (n.getGarbage() >= best.getGarbage())
            return n;
        else
            return best;
    }
    private Node checkBestConnectionsP(Node n, Node best){
        if (n.getConnections() >= best.getConnections())
            return n;
        else
            return best;
    }

    private void checkBestNode(int depth, Node n){
        if (n.getGarbage() >= mGLayers[depth].getGarbage()) {
            mGLayers[depth] = n;
        }
        if (n.getConnections() >= maxConnectionsNodeLayers[depth].getConnections()) {
            maxConnectionsNodeLayers[depth] = n;
        }
    }

    public List<Node> generatePoss(Node parent, Puyo[] puyoPair){
        return generatePoss(parent, puyoPair, 0);
    }

    private Node[] generatePoss4(Node parent, Puyo[] puyoPair){
        Node bestGarbage = new Node();
        Node bestConnections = new Node();
        Move cols = new Move(0, 0);
        Move rows = new Move(0, 3);
        Board parentBoard = parent.getBoard();
        for (int i = 0; i < 2; i ++){
            for (int col = 0; col < 6; col ++){
                Board temp = parentBoard.copyBoard();
                if (temp.dropPuyo(puyoPair, cols)) {
                    Node n = new Node(temp, parent);
                    bestGarbage = checkBestGarbageP(n, bestGarbage);
                    bestConnections = checkBestConnectionsP(n, bestConnections);
                }
                cols.right();
            }
            for (int row = 0; row < 5; row ++){
                Board temp = parentBoard.copyBoard();
                if (temp.dropPuyo(puyoPair, rows)) {
                    Node n = new Node(temp, parent);
                    bestGarbage = checkBestGarbageP(n, bestGarbage);
                    bestConnections = checkBestConnectionsP(n, bestConnections);
                }
                rows.right();
            }
            cols.rot180();
            rows.rot180();
        }
        return new Node[]{bestGarbage, bestConnections};
    }

    private List<Node> generatePoss(Node parent, Puyo[] puyoPair, int depth){
        List<Node> resultStates = new ArrayList<>();
        Move cols = new Move(0, 0);
        Move rows = new Move(0, 3);
        Board parentBoard = parent.getBoard();
        for (int i = 0; i < 2; i ++){
            for (int col = 0; col < 6; col ++){
                Board temp = parentBoard.copyBoard();
                if (temp.dropPuyo(puyoPair, cols)) {
                    Node n = new Node(temp, parent);
                    checkBestNode(depth, n);
                    resultStates.add(n);
                }
                cols.right();
            }
            for (int row = 0; row < 5; row ++){
                Board temp = parentBoard.copyBoard();
                if (temp.dropPuyo(puyoPair, rows)) {
                    Node n = new Node(temp, parent);
                    checkBestNode(depth, n);
                    resultStates.add(n);
                }
                rows.right();
            }
            cols.rot180();
            rows.rot180();
        }
        return resultStates;
    }

    private void createTree(Node root, int maxDepth, int colours, Puyo[][] currentPuyo){
        ArrayList<Node> current = new ArrayList<>();
        current.add(root);
        int depth = 0;
        while(depth != maxDepth) {
            ArrayList<Node> next = new ArrayList<>();
            for(Node temp: current) {
                if (!new Chain(temp.getBoard()).isPopping())
                    next.addAll(generatePoss(temp, currentPuyo[depth], depth));
            }
            current = next;
            depth ++;
        }
        if (maxDepth <= 2) {
            for (Node temp : current) {
                if (!new Chain(temp.getBoard()).isPopping())
                    generatePoss(temp, currentPuyo[depth], maxDepth);
            }
        }
        else
            generateDepth4Layer(current, colours);
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

    private void generateDepth4Layer(List<Node> parents, int colours){
        ArrayList<Node> bestGNodes = new ArrayList<>();
        ArrayList<Node> bestCNodes = new ArrayList<>();
        parents.parallelStream().forEach((parent) -> {
            if (!new Chain(parent.getBoard()).isPopping()) {
                for (int colour = 0; colour < colours; colour++) {
                    Puyo[] p = {new Puyo(Colour.values()[colour]), new Puyo(Colour.values()[colour])};
                    Node[] bestNodes = generatePoss4(parent, p);
                    try {
                        bestGNodes.add(bestNodes[0]);
                        bestCNodes.add(bestNodes[1]);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        Node highestG = new Node();
        Node highestC = new Node();
        for (Node n: bestGNodes){
            try {
                if (n.getGarbage() >= highestG.getGarbage())
                    highestG = n;
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        for (Node n: bestCNodes){
            try {
                if (n.getConnections() >= highestC.getConnections())
                    highestC = n;
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        mGLayers[3] = highestG;
        maxConnectionsNodeLayers[3] = highestC;
    }
}
