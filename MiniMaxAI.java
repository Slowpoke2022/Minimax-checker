import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//This class represents an AI which uses Normal Mini-Max for playing.
//Since we are trying to reach terminal states only, there will be no Heuristic Function method.
//Consider White Player to be a maximizing Player & Black Player to be a minimizing Player.
//Instead, in method MiniMax if a terminal state at Depth Cut-Off limit is found, return either 100 or -100 (if state is a win or a loss for White, respectively).
//If at Depth Limit & no terminal state was found, return 0.
//Method MiniMax will try to explore all states (Boards which can be created from currently available Moves) & their successor states.
//Method MiniMaxMove will run MiniMax for all moves available, then return a Move.

public class MiniMaxAI extends Player implements AIPlayer {

    int NumberOfVisitedStates = 0;
    public MiniMaxAI(String name, Side side, int depthLimit) {
        super(name, side);
        this.depthLimit = depthLimit;
    }
    int depthLimit;

    @Override
    public void makeMove(Board board) { //Method for an AI Player to make a Move based on their used algorithms.
        System.out.println("Next to Play: " + SidetoString(switchSide(board.currentSide))); //Prompt information, similar to example script.
        System.out.println(PlayertoString());
        System.out.println("I am thinking...");
        float StartTime = System.nanoTime();
        Move move = MiniMaxMove(board, depthLimit, getSide()); //Call MiniMax to get best move.
        board.processMove(move, getSide());
        float EndTime = System.nanoTime(); //Make Move then print out time taken, similar to example script.
        System.out.println("Move made: " + board.PieceTypeToString(board.getPieceType(move.sequence.get(move.length - 1).end_row, move.sequence.get(move.length - 1).end_col)) + ": " +  move.toString());
        TurnTime = ((EndTime - StartTime) / 1000000000.f);
        System.out.format("Time taken: %.3f\n", TurnTime);
        TotalTimeTaken += TurnTime;
        TurnTime = 0;
        System.out.println("Number of States visited: " + NumberOfVisitedStates);
        NumberOfVisitedStates = 0;
    }

    public Move MiniMaxMove(Board board, int depth, Side side) { //Method which calls MiniMax to calculate utility values for Boards/States resulting from all available Moves, then return Move with maximum/minimum value.
        List<Integer> HeuristicList = new ArrayList<>();
        List<Move> MoveList = board.getAllValidMovesForPlayers(side); //Get all possible Moves.
        if (MoveList.size() == 0) return null;
        for (int index = 0; index < MoveList.size(); index++) {
            Move move = MoveList.get(index);
            Board clone = board.clone();
            clone.processMove(move, side);
            HeuristicList.add(index, MiniMax(clone, depth - 1, switchSide(side), false));
        }
        double BestHeuristic = -100;
        if (getSide() == Side.White) { //If Player's side is White, get move with maximum utility value.
            for (int index = 0; index < HeuristicList.size(); index++) {
                BestHeuristic = Math.max(BestHeuristic, HeuristicList.get(index)); //Get maximum utility value.
            }
            for (int index = 0; index < HeuristicList.size(); index++) { //Remove any Move with utility value smaller than maximum utility value.
                if (HeuristicList.get(index) < BestHeuristic) {
                    HeuristicList.remove(index);
                    MoveList.remove(index);
                }
            }
        }
        if (getSide() == Side.Black) { //If Player's side is Black, get move with minimum utility value.
            BestHeuristic = 100;
            for (int index = 0; index < HeuristicList.size(); index++) {
                BestHeuristic = Math.min(BestHeuristic, HeuristicList.get(index)); //Get minimum utility value.
            }
            for (int index = 0; index < HeuristicList.size(); index++) { //Remove any Move with utility value larger than minimum utility value.
                if (HeuristicList.get(index) > BestHeuristic) {
                    HeuristicList.remove(index);
                    MoveList.remove(index);
                }
            }
        }
        Random random = new Random();
        Move BestMove = MoveList.get(random.nextInt(HeuristicList.size())); //Make any move with maximum/minimum utility value.
        System.out.println("Best Utility Value: " + BestHeuristic);
        System.out.println("Best State: " + board.PieceTypeToString(board.getPieceType(BestMove.sequence.get(0).start_row, BestMove.sequence.get(0).start_col)) + ": " +  BestMove.toString() + ", value: " + BestHeuristic);
        return BestMove;
    }

    public Integer MiniMax(Board board, int currentDepth, Side side, boolean isMaximizingPlayer) { //Method which explores all possible states/Boards resulted from possible Moves & their successor states, then return utility values based on conditions above.
        if (currentDepth == 0) { //At Depth Limit, return 100, -100, or 0 if state is a win for White, a win for Black, or neither - respectively.
            if (board.checkWinForSide(Side.White)) {
                return 100;
            } else if (board.checkWinForSide(Side.Black)) {
                return -100;
            } else {
                return 0;
            }
        }
        List<Move> MoveList = board.getAllValidMovesForPlayers(side); //Get all available Moves, in order to discover all possible next States/Boards.
        //Processing all Moves in MoveList will return a utility score for current Board/State;
        int value = 0;
        if (isMaximizingPlayer == true) { //If player is currently maximizing, find max.
            value = -100;
            for (Move move : MoveList) {
                Board clone = board.clone();
                clone.processMove(move, side); //Process an available Move on a clone of current Board to get a successor Board/State.
                int temp = MiniMax(clone, currentDepth - 1, switchSide(side), !isMaximizingPlayer); //Successors Boards/States will be minimizing.
                value = Math.max(temp, value);
            }
        }
        if (isMaximizingPlayer == false) { //If player is currently minimizing, find min.
            value = 100;
            for (Move move : MoveList) {
                Board clone = board.clone();
                clone.processMove(move, side);
                int temp = MiniMax(clone, currentDepth - 1, switchSide(side), !isMaximizingPlayer); //Successors Boards/States will be maximizing.
                value = Math.min(temp, value);
            }
        }
        NumberOfVisitedStates++;
        return value;
    }

    @Override
    public Integer HeuristicFunction(Board board) {
        return 0;
    }

}

