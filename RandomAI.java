import java.util.List;
import java.util.Random;

//This class represents an AI which makes random Move.
//Once all Moves for this AI's side are found, a random index smaller than list's size of available Move list  will be "rolled".
//Move with this index will then be made.

public class RandomAI extends Player implements AIPlayer {

    public RandomAI(String name, Side side) {
        super(name, side);
    } 

    @Override
    public Integer HeuristicFunction(Board board) {
        return 0;
    }

    @Override
    public void makeMove(Board board) { //Method for an AI Player to make a Move based on their used algorithms.
        System.out.println("Next to Play: " + SidetoString(switchSide(board.currentSide))); //Prompt information, similar to example script.
        System.out.println(PlayertoString());
        System.out.println("This is probably a good move!");
        Random random = new Random();
        List<Move> MovePool = board.getAllValidMovesForPlayers(getSide()); //Get all available Moves.
        float StartTime = System.nanoTime();
        Move move = MovePool.get(random.nextInt(MovePool.size())); //Get a random Move.
        float EndTime = System.nanoTime();
        board.processMove(move, getSide()); //Make Move then print out time taken, similar to example script.
        System.out.println("Move made: " + board.PieceTypeToString(board.getPieceType(move.sequence.get(move.length - 1).end_row, move.sequence.get(move.length - 1).end_col)) + ": " +  move.toString());
        TurnTime = ((EndTime - StartTime) / 1000000000.f);
        System.out.format("Time taken: %.3f\n", TurnTime);
        TotalTimeTaken += TurnTime;
        TurnTime = 0;
    }

}
