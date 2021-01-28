//Interface AIPlayer represents an AI Player.
//Each AI Player will make Moves based on their own algorithms, so they have a different Heuristic Function each.

public interface AIPlayer {
    Integer HeuristicFunction(Board board);
}
