import java.util.*;

//A Player has a name, an amount of time taken each turn (rest to 0 at end of turn), & total time spent playing.
//An AI Player will make a Move differently compared to a Human Player, so we have two different function to make a Move on a Board.

public class Player {

    String name;
    float TotalTimeTaken;
    float TurnTime;
    enum Side {
        Black, White
    }
    Side side;

    public Player(String name, Side side) {
        this.name = name;
        this.side = side;
        TotalTimeTaken = 0;
    }

    public Side switchSide(Side side) {
        if (side == Side.Black) return Side.White;
        else return Side.Black;
    }

    public void makeMoveForHumanPlayers(Board board, Scanner console) { //Method for a Human Player to make a Move with input from a Scanner console.
        System.out.println("Next to Play: " + SidetoString(switchSide(board.currentSide))); //Prompt information, similar to example script.
        System.out.print(PlayertoString() + "\n");
        System.out.println("Enter your Move!");
        System.out.print("Type Start-End for Normal Moves (e.g. A2-B3), StartxEnd for Single Captures (e.g. A2xC4), & StartxEnd1xEnd2 for Chain Captures (e.g. A2xC4xD2): ");
        List<Move> MovePool = board.getAllValidMovesForPlayers(getSide());
        float StartTime = System.nanoTime();
        String string = console.next();
        while (CoordinatesToMove(string) == null) { //Prompt information in case Human Player has wrong input, which will make CoordinatesToMove return null.
            if (CoordinatesToMove(string) == null) {
                System.out.print("Wrong syntax! Please type your Move in as instructed above! ");
                string = console.next();
            }
        }
        while (!MovePool.contains(CoordinatesToMove(string))) { //Prompt information in case Human Player makes an invalid move.
            System.out.println("Error: You Had Made an Invalid Move!");
            if (board.isMovingOwnPieces(CoordinatesToMove(string), getSide()) == false) System.out.println("You are not moving your pieces!");
            else if (string.contains("-") && board.checkIfCanCapture(getSide()) == true) System.out.println("You failed to make a Possible Capture!");
            else if (string.contains("x") && CoordinatesToMove(string).length < board.getLongestCaptureForASide(getSide())) System.out.println("You failed to make your Longest Possible Capture!");
            System.out.println("\n" + "Type Hint for a Hint! Else try to make another move!");
            string = console.next();
            if (string.equals("Hint")) {
                Random random = new Random();
                System.out.println("Valid Move: " + MovePool.get(random.nextInt(MovePool.size())).toString());
                string = console.next();
                }
            }
        Move move = CoordinatesToMove(string); //Make Move then print out time taken, similar to example script.
        float EndTime = System.nanoTime();
        board.processMove(move, getSide());
        System.out.println("Move made: " + board.PieceTypeToString(board.getPieceType(move.sequence.get(move.length - 1).start_row, move.sequence.get(move.length - 1).start_col)) + ": " +  move.toString());
        TurnTime = ((EndTime - StartTime) / 1000000000.f);
        System.out.format("Time taken: %.3f\n", TurnTime);
        TotalTimeTaken += TurnTime;
        TurnTime = 0;
    }

    public void makeMove(Board board) {} //Method makeMove for AI players, will be overrode in AI classes.

    public Side getSide() {
        return side;
    }

    public String SidetoString(Side currentSide) {
        String temp = "";
        if (currentSide == Side.Black) temp = "Black";
        if (currentSide == Side.White) temp = "White";
        return temp;
    }

    public String PlayertoString() { //Utility method for printing out a Player's name & side.
        String temp = "Player: ";
        temp += name;
        temp += " ";
        temp += "\n";
        temp += "Side: " + SidetoString(side);
        return temp;
    }

    public Move CoordinatesToMove(String ConsoleInputString) { //Method which converts a String from Console into a Move to be made.
        Move move = null;
        ArrayList<Integer> rowArrayList = new ArrayList(); //ArrayLists for adding coordinates from String.
        ArrayList<Integer> colArrayList = new ArrayList();
        String regex = ""; //regex for splitting to get coordinates - "-" for Normal Move, "x" for Capture Move.
        if (ConsoleInputString.contains("-")) regex = "-";
        else if (ConsoleInputString.contains("x")) regex = "x";
        else if (!ConsoleInputString.contains("-") && !ConsoleInputString.contains("x")) return move;
        String[] coordinates = ConsoleInputString.split(regex); //Splitting & getting coordinates
        for (int index = 0; index < coordinates.length; index++) {
            if (coordinates[index].charAt(0) == 'A') rowArrayList.add(1);
            else if (coordinates[index].charAt(0) == 'B') rowArrayList.add(2);
            else if (coordinates[index].charAt(0) == 'C') rowArrayList.add(3);
            else if (coordinates[index].charAt(0) == 'D') rowArrayList.add(4);
            else if (coordinates[index].charAt(0) == 'E') rowArrayList.add(5);
            else if (coordinates[index].charAt(0) == 'F') rowArrayList.add(6);
            else if (coordinates[index].charAt(0) == 'G') rowArrayList.add(7);
            else if (coordinates[index].charAt(0) == 'H') rowArrayList.add(8);
            else return null;
            int colValue = Character.getNumericValue(coordinates[index].charAt(1));
            if (colValue != 1 && colValue != 2 && colValue != 3 && colValue != 4 && colValue != 5 && colValue != 6 && colValue !=7 && colValue != 8) return null;
            else colArrayList.add(colValue);
        }
        //Create & return new Move from coordinates taken.
        if (regex.equals("-")) {
            Action action = new Action(rowArrayList.get(0), colArrayList.get(0), rowArrayList.get(1), colArrayList.get(1));
            move = new Move(action);
        }
        else if (regex.equals("x")) {
            ArrayList<Action> SequenceOfAction = new ArrayList<>();
            for (int index = 0; index <= rowArrayList.size() - 2; index++) {
                int midRow = (rowArrayList.get(index) + rowArrayList.get(index + 1)) / 2;
                int midCol = (colArrayList.get(index) + colArrayList.get(index + 1)) / 2;
                Action action1 = new Action(rowArrayList.get(index), colArrayList.get(index), midRow, midCol);
                Action action2 = new Action(midRow, midCol, rowArrayList.get(index + 1), colArrayList.get(index + 1));
                SequenceOfAction.add(action1);
                SequenceOfAction.add(action2);
            }
            move = new Move(SequenceOfAction);
            System.out.println(move.toString());
        }
        return move;
    }

}