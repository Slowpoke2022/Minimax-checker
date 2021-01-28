import java.util.*;

//Main.java contains main method, being responsible for running my entire game.
//Contains a set of static boolean representing opponent types, Players to be created, int size for Board's size, & int DepthLimit for depth cut-off for Mini-Max.
//static variables will be assigned their values with input from Scanner.
//Once there is all needed data, static method createPlayers will create a Human Player & an AI Player, based on input received.
//From there, main method's game loop will process

public class Main {

    static boolean RandomOpponent = false;
    static boolean NormalMiniMaxOpponent = false;
    static boolean AlphaBetaMiniMaxOpponent = false;
    static boolean HMiniMaxOpponent = false;
    static Player HumanPlayer = null;
    static Player AIPlayer = null;
    static int DepthLimit = 0;
    static Player.Side PlayerSide = Player.Side.White;
    static int size = 0;

    public static void createPlayers() { //Method which creates new Players based on values received from Scanner input.
        HumanPlayer = new RandomAI("You", PlayerSide);
        if (RandomOpponent) AIPlayer = new RandomAI("Random AI", switchSide(PlayerSide));
        if (NormalMiniMaxOpponent) AIPlayer = new MiniMaxAI("Normal Mini-Max AI", switchSide(PlayerSide), DepthLimit);
        if (AlphaBetaMiniMaxOpponent) AIPlayer = new AlphaBetaMiniMaxAI("Alpha Beta Pruning Mini-Max AI", switchSide(PlayerSide), DepthLimit);
        if (HMiniMaxOpponent) AIPlayer = new HMiniMaxAI("H Mini-Max AI", switchSide(PlayerSide), DepthLimit);
    }

    public static Player.Side switchSide(Player.Side side) {
        if (side == Player.Side.Black) return Player.Side.White;
        else return Player.Side.Black;
    }

    public static void main(String[] args) throws InterruptedException { //main method which runs my game.
        Scanner console = new Scanner(System.in);
        System.out.println("Checkers by Minh Duy Pham!");
        System.out.println("Choose your Board:");
        System.out.println("1. Standard 8x8 (Type in 8).");
        System.out.println("2. Small 4x4 (Type in 4).");
        System.out.print("Your choice? ");
        size = console.nextInt();
        while (size != 4 && size != 8) {
            System.out.print("Please choose a valid Size (8 or 4 only)! ");
            size = console.nextInt();
        }
        Board playingBoard = new Board(size);
        System.out.println("Choose Opponent: ");
        System.out.println("1. An AI which plays randomly (Type in 1).");
        System.out.println("2. An AI which plays with Mini-Max Algorithm (Type in 2).");
        System.out.println("3. An AI which plays with Alpha-Beta Pruning Mini-Max Algorithm (Type in 3).");
        System.out.println("4. An AI which plays with H Mini-Max with a fixed Depth cut off (Type in 4).");
        System.out.print("Your choice? ");
        int chooseOpponent = console.nextInt();
        while (chooseOpponent != 1 && chooseOpponent != 2 && chooseOpponent != 3 && chooseOpponent != 4) {
            System.out.print("Please choose your Opponent (Type in 1, 2, 3 or 4)! ");
            chooseOpponent = console.nextInt();
        }
        if (chooseOpponent == 1) RandomOpponent = true;
        if (chooseOpponent == 2) NormalMiniMaxOpponent = true;
        if (chooseOpponent == 3) AlphaBetaMiniMaxOpponent = true;
        if (chooseOpponent == 4) HMiniMaxOpponent = true;
        if (HMiniMaxOpponent == true || AlphaBetaMiniMaxOpponent == true || NormalMiniMaxOpponent == true) {
            System.out.print("What will be your Depth Limit (Type an Integer)? ");
            DepthLimit = console.nextInt();
        }
        System.out.print("Are you playing as White or Black (Type in White or Black)? ");
        String chosenSide = console.next();
        while (!chosenSide.equals("White") && !chosenSide.equals("Black")) {
            System.out.print("Please choose your side (White or Black)! ");
            chosenSide = console.next();
        }
        if (chosenSide.equals("White")) PlayerSide = Player.Side.White;
        if (chosenSide.equals("Black")) PlayerSide = Player.Side.Black;
        System.out.println("Now we can start!" + "\n");
        createPlayers();
        Thread.sleep(5000);
        System.out.println(playingBoard.toString());
        while (!playingBoard.checkWinForSide(PlayerSide) && !playingBoard.checkWinForSide(switchSide(PlayerSide))) { //Game Loop.
            //Allow Players to take Move based on current Side of Board;
            if (PlayerSide != playingBoard.currentSide) {
                AIPlayer.makeMove(playingBoard);
                System.out.println(playingBoard.toString());
                Thread.sleep(5000);
                if (playingBoard.checkWinForSide(switchSide(PlayerSide))) break;
            }
            if (PlayerSide == playingBoard.currentSide) {
                HumanPlayer.makeMoveForHumanPlayers(playingBoard, console);
                //HumanPlayer.makeMove(playingBoard);
                System.out.println(playingBoard.toString());
                Thread.sleep(5000);
                if (playingBoard.checkWinForSide(PlayerSide)) break;
            }
        }
        //Print out Total Time Taken & Winning Side once Game Loop ends (e.g. a Side had won);
        if (playingBoard.checkWinForSide(PlayerSide)) System.out.println("You win!");
        if (!playingBoard.checkWinForSide(PlayerSide)) System.out.println("You lose!");
        System.out.println(HumanPlayer.getSide().toString() + "'s Total Time Taken: " + HumanPlayer.TotalTimeTaken);
        System.out.println(AIPlayer.getSide().toString() + "'s Total Time Taken: " + AIPlayer.TotalTimeTaken);
    }

}