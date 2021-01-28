import java.util.*;

//Board represents a Checker Board, 8x8 or 4x4.
//Transition Model is based on Board & Moves: making a Move on a Board/State will result in a new Board/State.
//Board will also handle most of my game's logistics (e.g. rules, win conditions...).

public class Board {

    enum Piece { //A Piece can be one of four types: Normal White, Normal Black, White King, or Black King. A square can be Empty or has a Piece on.
        Empty, NormalWhite, NormalBlack, WhiteKing, BlackKing
    }
    int LongestCaptureWhiteLength = 0; //Count longest capture move's length for each side, which a Player of that side must make their turn.
    int LongestCaptureBlackLength = 0;

    public String PieceTypeToString(Board.Piece piece) { //Utility method which prints out a letter based on a Piece's type.
        String string = "";
        if (piece == Piece.NormalBlack) string = "b";
        if (piece == Piece.NormalWhite) string = "w";
        if (piece == Piece.BlackKing) string = "B";
        if (piece == Piece.WhiteKing) string = "W";
        return string;
    }

    public void switchCurrentSide() { //Method to switch current Side of a board once a Move is made.
        if (currentSide == Player.Side.Black) currentSide = Player.Side.White;
        else currentSide = Player.Side.Black;
    }

    Piece[][] GameBoard; //2D-Array of Pieces, representing a Checker Board.
    int size; //size of Board = 4 for 4x4, size = 8 for 8x8.
    int NumberOfNormalBlack; //Number of Pieces of each Piece Type.
    int NumberOfNormalWhite;
    int NumberOfBlackKing;
    int NumberOfWhiteKing;
    Player.Side currentSide; //Current Side who needs to make a Move.

    public Board(int size) {
        NumberOfNormalBlack = 0;
        NumberOfNormalWhite = 0;
        NumberOfBlackKing = 0;
        NumberOfWhiteKing = 0;
        this.size = size;
        createNewBoard(size);
    }

    public Board(Piece[][] pieces, Player.Side currentSideNow) { //Secondary constructor which create a new Board based on current positions of Pieces (represented by 2D Array of Pieces) & a Side to be made Current.
        NumberOfNormalBlack = 0;
        NumberOfNormalWhite = 0;
        NumberOfBlackKing = 0;
        NumberOfWhiteKing = 0;
        GameBoard = pieces;
        size = pieces.length - 1;
        for (int i = 1; i < pieces.length; i++) {
            for (int j = 1; j < pieces[i].length; j++) {
                if (pieces[i][j] == Piece.NormalBlack) NumberOfNormalBlack++;
                if (pieces[i][j] == Piece.NormalWhite) NumberOfNormalWhite++;
                if (pieces[i][j] == Piece.BlackKing) NumberOfBlackKing++;
                if (pieces[i][j] == Piece.WhiteKing) NumberOfWhiteKing++;
            }
        }
        currentSide = currentSideNow;
    }

    public void createNewBoard(int size) { //Utility method which will create a new brand new Board; used to create a Board at start of each game.
        GameBoard = new Piece[size + 1][size + 1]; //Create a new 2D Array of Pieces based on input size;
        //Fill out newly created Array with Pieces based on states of initial 8x8 & 4x4 Checker Boards.
        if (size == 4) {
            GameBoard[1][2] = Piece.NormalBlack;
            GameBoard[1][4] = Piece.NormalBlack;
            GameBoard[4][1] = Piece.NormalWhite;
            GameBoard[4][3] = Piece.NormalWhite;
        }
        if (size == 8) {
            GameBoard[1][2] = Piece.NormalBlack;
            GameBoard[1][4] = Piece.NormalBlack;
            GameBoard[1][6] = Piece.NormalBlack;
            GameBoard[1][8] = Piece.NormalBlack;
            GameBoard[2][1] = Piece.NormalBlack;
            GameBoard[2][3] = Piece.NormalBlack;
            GameBoard[2][5] = Piece.NormalBlack;
            GameBoard[2][7] = Piece.NormalBlack;
            GameBoard[3][2] = Piece.NormalBlack;
            GameBoard[3][4] = Piece.NormalBlack;
            GameBoard[3][6] = Piece.NormalBlack;
            GameBoard[3][8] = Piece.NormalBlack;
            GameBoard[6][1] = Piece.NormalWhite;
            GameBoard[6][3] = Piece.NormalWhite;
            GameBoard[6][5] = Piece.NormalWhite;
            GameBoard[6][7] = Piece.NormalWhite;
            GameBoard[7][2] = Piece.NormalWhite;
            GameBoard[7][4] = Piece.NormalWhite;
            GameBoard[7][6] = Piece.NormalWhite;
            GameBoard[7][8] = Piece.NormalWhite;
            GameBoard[8][1] = Piece.NormalWhite;
            GameBoard[8][3] = Piece.NormalWhite;
            GameBoard[8][5] = Piece.NormalWhite;
            GameBoard[8][7] = Piece.NormalWhite;
        }
        for (int i = 1; i < GameBoard.length; i++) {
            for (int j = 1; j < GameBoard[i].length; j++) {
                if (GameBoard[i][j] == null) GameBoard[i][j] = Piece.Empty;
                if (GameBoard[i][j] == Piece.NormalBlack) NumberOfNormalBlack++;
                if (GameBoard[i][j] == Piece.NormalWhite) NumberOfNormalWhite++;
                if (GameBoard[i][j] == Piece.BlackKing) NumberOfBlackKing++;
                if (GameBoard[i][j] == Piece.WhiteKing) NumberOfWhiteKing++;
            }
        }
        currentSide = Player.Side.Black; //Set current Side to Black as Black moves first.
    }

    public boolean checkWinForSide(Player.Side checkingSide) { //Method to check win conditions for a Side.
        boolean temp = false;
        if (checkingSide == Player.Side.White) {
            if (getNumberOfPiecesForSide(Player.Side.Black) == 0) return true; //A side win if its opponent has no pieces left.
            else if (getAllValidMovesForPlayers(Player.Side.Black).size() == 0) temp = true; //or if its opponents cannot make a valid move.
        }
        else if (checkingSide == Player.Side.Black) {
            if (getNumberOfPiecesForSide(Player.Side.White) == 0) return true;
            else if (getAllValidMovesForPlayers(Player.Side.White).size() == 0) temp = true;
        }
        return temp;
    }

    public int getNumberOfPiecesForSide(Player.Side side) { //Utility method which counts number of pieces on a side.
        int temp = 0;
        if (side == Player.Side.White) temp = NumberOfNormalWhite + NumberOfWhiteKing;
        else if (side == Player.Side.Black) temp = NumberOfNormalBlack + NumberOfBlackKing;
        return temp;
    }

    public int getLongestCaptureForASide(Player.Side side) { //Method which returns longest capture length.
        if (side == Player.Side.Black) return LongestCaptureBlackLength;
        else return LongestCaptureWhiteLength;
    }

    public boolean checkIfCanCapture(Player.Side side) { //Method which checks if a Player can capture, since they must do that if they can.
        boolean temp = false;
        List<Move> MoveList = getAllValidMovesForPlayers(side);
        for (Move move : MoveList) {
            if (move.isCaptureMove()) temp = true;
        }
        return temp;
    }

    public Board clone() { //Utility method which clones current Board, retrieving current 2D Array of Pieces & current Side then call Secondary constructor to make a new Board.
        Piece[][] temp = new Piece[GameBoard.length][GameBoard[0].length];
        for (int i = 1; i < GameBoard.length; i++) {
            for (int j = 1; j < GameBoard[i].length; j++) {
                temp[i][j] = GameBoard[i][j];
            }
        }
        Board clone = new Board(temp, currentSide);
        return clone;
    }

    public void updateNumberOfPieces() { //Utility method which update number of Pieces after a Move.
        NumberOfNormalBlack = 0;
        NumberOfNormalWhite = 0;
        NumberOfBlackKing = 0;
        NumberOfWhiteKing = 0;
        for (int i = 1; i < GameBoard.length; i++) {
            for (int j = 1; j < GameBoard[i].length; j++) {
                if (GameBoard[i][j] == Piece.WhiteKing) NumberOfWhiteKing++;
                else if (GameBoard[i][j] == Piece.BlackKing) NumberOfBlackKing++;
                else if (GameBoard[i][j] == Piece.NormalWhite) NumberOfNormalWhite++;
                else if (GameBoard[i][j] == Piece.NormalBlack) NumberOfNormalBlack++;
            }
        }
    }

    public void processMove(Move move, Player.Side side) { //Method which processes a Move made on a Side.
        Action first = move.sequence.get(0);
        Action last = move.sequence.get(move.sequence.size() - 1);
        if (move.isCaptureMove()) { //If a Move is a Capture Move.
            for (Action action : move.sequence) { //Process each Action in Move's sequence one by one (editing values on GameBoard Array).
                Piece onHandPiece = GameBoard[action.start_row][action.start_col];
                GameBoard[action.end_row][action.end_col] = onHandPiece;
                GameBoard[action.start_row][action.start_col] = Piece.Empty;
            }
            LongestCaptureWhiteLength = 0; //Update game statistics & variables afterwards.
            LongestCaptureBlackLength = 0;
            makeKing(last, side);
            updateNumberOfPieces();
            switchCurrentSide();

        }
        else if (!move.isCaptureMove()) { //If a Move is a Normal Move, simply process its single Action.
            Piece currentPiece = GameBoard[first.start_row][first.start_col];
            GameBoard[first.end_row][first.end_col] = currentPiece;
            GameBoard[first.start_row][first.start_col] = Piece.Empty;
            LongestCaptureWhiteLength = 0;
            LongestCaptureBlackLength = 0;
            makeKing(last, side);
            updateNumberOfPieces();
            switchCurrentSide();
        }
    }

    public void makeKing(Action last, Player.Side side) { //Utility method which checks if a last Action from a Side makes a Black or White Normal Pieces Kings;
        if (last.end_row == size && side == Player.Side.Black) {
            if (GameBoard[last.end_row][last.end_col] != Piece.BlackKing) {
                GameBoard[last.end_row][last.end_col] = Piece.BlackKing;
            }
        }
        if (last.end_row == 1 && side == Player.Side.White) {
            if (GameBoard[last.end_row][last.end_col] != Piece.WhiteKing) {
                GameBoard[last.end_row][last.end_col] = Piece.WhiteKing;
            }
        }
    }

    public Piece getPieceType(int row, int col) { //Utility method which returns type of Piece on a square, by retrieving that Piece from GameBoard Array with input coordinates.
        return GameBoard[row][col];
    }

    public boolean isMovingOwnPieces(Move move, Player.Side side) { //A function which checks if a Player is Moving their own Pieces.
        Action first = move.sequence.get(0);
        Piece movingPiece = GameBoard[first.start_row][first.start_col];
        if (side == Player.Side.Black) {
            if (movingPiece == Piece.NormalBlack || movingPiece == Piece.BlackKing) return true;
        }
        if (side == Player.Side.White) {
            if (movingPiece == Piece.NormalWhite || movingPiece == Piece.WhiteKing) return true;
        }
        return false;
    }


    //Series of 4 boolean methods below check if a Single Capture can be made with a Piece on a certain coordinates, based on Checker' rules.
    //There are four directions for Capturing: Front Left, Front Right, Back Left, & Back Right.
    //Each method will check capturability from a certain position on a Board.
    public boolean canCaptureLeftFront(int row, int col) { //Utility method which checks capturability on Left Front.
        Piece pieceType = GameBoard[row][col];
        if (col - 2 >= 1) {
            if (row + 2 <= size && (pieceType == Piece.NormalBlack || pieceType == Piece.BlackKing)) {
                if (GameBoard[row + 2][col - 2] == Piece.Empty && (GameBoard[row + 1][col - 1] == Piece.NormalWhite || GameBoard[row + 1][col - 1] == Piece.WhiteKing))
                    return true;
            }
            if (row - 2 >= 1 && (pieceType == Piece.NormalWhite || pieceType == Piece.WhiteKing)) {
                if (GameBoard[row - 2][col - 2] == Piece.Empty && (GameBoard[row - 1][col - 1] == Piece.NormalBlack || GameBoard[row - 1][col - 1] == Piece.BlackKing))
                    return true;
            }
        }
        return false;
    }

    public boolean canCaptureRightFront(int row, int col) { //Utility method which checks capturability on Right Front.
        Piece pieceType = GameBoard[row][col];
        if (col + 2 <= size) {
            if (row + 2 <= size && (pieceType == Piece.NormalBlack || pieceType == Piece.BlackKing)) {
                if (GameBoard[row + 2][col + 2] == Piece.Empty && (GameBoard[row + 1][col + 1] == Piece.NormalWhite || GameBoard[row + 1][col + 1] == Piece.WhiteKing))
                    return true;
            }
            if (row - 2 >= 1 && (pieceType == Piece.NormalWhite || pieceType == Piece.WhiteKing)) {
                if (GameBoard[row - 2][col + 2] == Piece.Empty && (GameBoard[row - 1][col + 1] == Piece.NormalBlack || GameBoard[row - 1][col + 1] == Piece.BlackKing))
                    return true;
            }
        }
        return false;
    }

    public boolean canCaptureLeftBack(int row, int col) { //Utility method which checks capturability on Left Back.
        Piece pieceType = GameBoard[row][col];
        if (col - 2 >= 1) {
            if (row - 2 >= 1 && pieceType == Piece.BlackKing) {
                if (GameBoard[row - 2][col - 2] == Piece.Empty && (GameBoard[row - 1][col - 1] == Piece.NormalWhite || GameBoard[row - 1][col - 1] == Piece.WhiteKing))
                    return true;
            }
            if (row + 2 <= size && pieceType == Piece.WhiteKing) {
                if (GameBoard[row + 2][col - 2] == Piece.Empty && (GameBoard[row + 1][col - 1] == Piece.NormalBlack || GameBoard[row + 1][col - 1] == Piece.BlackKing))
                    return true;
            }
        }
        return false;
    }

    public boolean canCaptureRightBack(int row, int col) { //Utility method which checks capturability on Right Back.
        Piece pieceType = GameBoard[row][col];
        if (col + 2 <= size) {
            if (row - 2 >= 1 && pieceType == Piece.BlackKing) {
                if (GameBoard[row - 2][col + 2] == Piece.Empty && (GameBoard[row - 1][col + 1] == Piece.NormalWhite || GameBoard[row - 1][col + 1] == Piece.WhiteKing))
                    return true;
            }
            if (row + 2 <= size && pieceType == Piece.WhiteKing) {
                if (GameBoard[row + 2][col + 2] == Piece.Empty && (GameBoard[row + 1][col + 1] == Piece.NormalBlack || GameBoard[row + 1][col + 1] == Piece.BlackKing))
                    return true;
            }
        }
        return false;
    }

    public List<Move> getAllValidMovesForPlayers(Player.Side side) { //Method which returns all available Moves from a Side on current Board.
        boolean containCaptureMove = false;
        List<Move> allPossibleMoves = new ArrayList<>();
        //Call getAllValidMoves (method below) on squares with Pieces on Player Side.
        if (side == Player.Side.Black) {
            for (int i = 1; i < size + 1; i++) {
                for (int j = 1; j < size + 1; j++) {
                    if (GameBoard[i][j] == Piece.NormalBlack || GameBoard[i][j] == Piece.BlackKing) allPossibleMoves.addAll(getAllValidMoves(i, j, true));
                }
            }
        }
        if (side == Player.Side.White) {
            for (int i = 1; i < size + 1; i++) {
                for (int j = 1; j < size + 1; j++) {
                    if (GameBoard[i][j] == Piece.NormalWhite || GameBoard[i][j] == Piece.WhiteKing) allPossibleMoves.addAll(getAllValidMoves(i, j, true));
                }
            }
        }
        int maxLength = 0;
        int maxIndex = 0;
        for (int index = 0; index < allPossibleMoves.size(); index++) { //Check if this Side can capture, if so, find length of longest capture Move.
            if (allPossibleMoves.get(index).isCaptureMove()) {
                containCaptureMove = true;
                if (allPossibleMoves.get(index).length >= maxLength) {
                    maxLength = allPossibleMoves.get(index).length;
                    maxIndex = index;
                }
            }
        }
        if (containCaptureMove) { //Remove all Normal Moves & Capture Moves with length shorter than longest capture's.
            Iterator<Move> iterator = allPossibleMoves.iterator();
            while (iterator.hasNext()) {
                Move temp = iterator.next();
                if (!temp.isCaptureMove()) iterator.remove();
                if (temp.isCaptureMove() && temp.length < maxLength) iterator.remove();
            }
        }
        if (side == Player.Side.White) LongestCaptureWhiteLength = maxLength; //Set longest capture length for Player's Side.
        if (side == Player.Side.Black) LongestCaptureBlackLength = maxLength;
        return allPossibleMoves; //Return List of all available Moves.
    }

    public List<Move> getFollowUpMoves(Action action1, Action action2) { //Method which get all possible Capture Moves after a single Capture Move is made.
        List<Move> followUpMoves = new ArrayList<>();
        Board cloneBoard = clone(); //Create a clone;
        Piece currentPiece = cloneBoard.GameBoard[action1.start_row][action1.start_col]; //Process two Actions of single Capture Move to be made on cloned Board.
        cloneBoard.GameBoard[action2.end_row][action2.end_col] = currentPiece;
        cloneBoard.GameBoard[action1.start_row][action1.start_col] = Piece.Empty;
        cloneBoard.GameBoard[action1.end_row][action1.end_col] = cloneBoard.GameBoard[action2.start_row][action2.start_col] = Piece.Empty;
        List<Move> temp =  cloneBoard.getAllValidMoves(action2.end_row, action2.end_col, false); //Get all available Capture Moves at end coordinates of single Capture Move.
        for (Move move : temp) { //Add two Actions from input to new found follow-up Capture Moves.
            if (move.isCaptureMove()) {
                move.addActionToSequence(0, action1);
                move.addActionToSequence(1, action2);
            }
        }
        followUpMoves.addAll(temp);
        return followUpMoves; //return all new Capture Moves found.
    }

    //Method which return a List of all possible moves.
    //Capturability at all possible directions - depending - on Types) at square/position in Piece 2D Array GameBoard with input coordinates will be checked, using our series of CanCapture boolean above.
    //If Piece at current position is a Normal White or Black Piece, we have to check capturability in Front Left & Front Right.
    //If Piece at current position is a White or Black King Piece, we have to check capturability in Back Left & Back Right.
    //If a possible single Capture Move can be made in a direction, get all possible Capture Moves starting from that position & direction with getFollowUpMoves.
    //If no longer Capture Move can be found, add found Single Capture Move. If longer Capture Moves are found, found maximum length for Capture Move then removing any Move with length less than that.
    //If Piece can still Normally move (cannot find a capture or already captured), add possible Normal Moves to List.
    public List<Move> getAllValidMoves(int row, int col, boolean canNormallyMove) {
        List<Move> MoveList = new ArrayList<>();
        Piece pieceType = GameBoard[row][col]; //Get Piece type of Piece with current coordinates in GameBoard.
        if (pieceType == Piece.Empty) return MoveList; //If that position is Empty, return MoveList (which is null) immediately.
        else if (pieceType == Piece.NormalBlack) { //Check capturability in all possible directions based on Piece Type.
            if (canCaptureLeftFront(row, col)) {
                Action action1 = new Action(row, col, row + 1, col - 1);
                Action action2 = new Action(row + 1, col - 1, row + 2, col - 2);
                MoveList.addAll(getFollowUpMoves(action1, action2));
                if (MoveList.size() == 0) {
                    ArrayList<Action> possibleCapture = new ArrayList<>();
                    possibleCapture.add(action1);
                    possibleCapture.add(action2);
                    MoveList.add(new Move(possibleCapture));
                }
                canNormallyMove = false; //Switch canNormallyMove to false if any Capture Move was found.
            }
            if (canCaptureRightFront(row, col)) {
                Action action1 = new Action(row, col, row + 1, col + 1);
                Action action2 = new Action(row + 1, col + 1, row + 2, col + 2);
                MoveList.addAll(getFollowUpMoves(action1, action2));
                if (MoveList.size() == 0) {
                    ArrayList<Action> possibleCapture = new ArrayList<>();
                    possibleCapture.add(action1);
                    possibleCapture.add(action2);
                    MoveList.add(new Move(possibleCapture));
                }
                canNormallyMove = false;
            }
            if (!canNormallyMove) { //If a Capture Move was found, find max length of Capture Moves, then remove any Move with length smaller than that from MoveList.
                int maxLength = 0;
                Iterator<Move> iterator = MoveList.iterator();
                while (iterator.hasNext()) {
                    Move temp = iterator.next();
                    if (temp.length >= maxLength) maxLength = temp.length;
                }
                while (iterator.hasNext()) {
                    Move temp = iterator.next();
                    if (temp.length < maxLength) iterator.remove();
                }
                return MoveList;
            }
            else if (canNormallyMove) {
                if (row + 1 <= size) {
                    if (col - 1 >= 1 && GameBoard[row + 1][col - 1] == Piece.Empty) MoveList.add(new Move(new Action(row, col, row + 1, col - 1)));
                    if (col + 1 <= size && GameBoard[row + 1][col + 1] == Piece.Empty) MoveList.add(new Move(new Action(row, col, row + 1, col + 1)));
                }
            }
        }

        else if (pieceType == Piece.NormalWhite) {
            if (canCaptureLeftFront(row, col)) {
                Action action1 = new Action(row, col, row - 1, col - 1);
                Action action2 = new Action(row - 1, col - 1, row - 2, col - 2);
                MoveList.addAll(getFollowUpMoves(action1, action2));
                if (MoveList.size() == 0) {
                    ArrayList<Action> possibleCapture = new ArrayList<>();
                    possibleCapture.add(action1);
                    possibleCapture.add(action2);
                    MoveList.add(new Move(possibleCapture));
                }
                canNormallyMove = false;
            }
            if (canCaptureRightFront(row, col)) {
                Action action1 = new Action(row, col, row - 1, col + 1);
                Action action2 = new Action(row - 1, col + 1, row - 2, col + 2);
                MoveList.addAll(getFollowUpMoves(action1, action2));
                if (MoveList.size() == 0) {
                    ArrayList<Action> possibleCapture = new ArrayList<>();
                    possibleCapture.add(action1);
                    possibleCapture.add(action2);
                    MoveList.add(new Move(possibleCapture));
                }
                canNormallyMove = false;
            }
            if (!canNormallyMove) {
                int maxLength = 0;
                Iterator<Move> iterator = MoveList.iterator();
                while (iterator.hasNext()) {
                    Move temp = iterator.next();
                    if (temp.length >= maxLength) maxLength = temp.length;
                }
                while (iterator.hasNext()) {
                    Move temp = iterator.next();
                    if (temp.length < maxLength) iterator.remove();
                }
                return MoveList;
            }
            else if (canNormallyMove) {
                if (row - 1 >= 1) {
                    if (col - 1 >= 1 && GameBoard[row - 1][col - 1] == Piece.Empty) MoveList.add(new Move(new Action(row, col, row - 1, col - 1)));
                    if (col + 1 <= size && GameBoard[row - 1][col + 1] == Piece.Empty) MoveList.add(new Move(new Action(row, col, row - 1, col + 1)));
                }
            }
        }

        else if (pieceType == Piece.BlackKing) {
            if (canCaptureLeftFront(row, col)) {
                Action action1 = new Action(row, col, row + 1, col - 1);
                Action action2 = new Action(row + 1, col - 1, row + 2, col - 2);
                MoveList.addAll(getFollowUpMoves(action1, action2));
                if (MoveList.size() == 0) {
                    ArrayList<Action> possibleCapture = new ArrayList<>();
                    possibleCapture.add(action1);
                    possibleCapture.add(action2);
                    MoveList.add(new Move(possibleCapture));
                }
                canNormallyMove = false;
            }
            if (canCaptureRightFront(row, col)) {
                Action action1 = new Action(row, col, row + 1, col + 1);
                Action action2 = new Action(row + 1, col + 1, row + 2, col + 2);
                MoveList.addAll(getFollowUpMoves(action1, action2));
                if (MoveList.size() == 0) {
                    ArrayList<Action> possibleCapture = new ArrayList<>();
                    possibleCapture.add(action1);
                    possibleCapture.add(action2);
                    MoveList.add(new Move(possibleCapture));
                }
                canNormallyMove = false;
            }
            if (canCaptureLeftBack(row, col)) {
                Action action1 = new Action(row, col, row - 1, col - 1);
                Action action2 = new Action(row - 1, col - 1, row - 2, col - 2);
                MoveList.addAll(getFollowUpMoves(action1, action2));
                if (MoveList.size() == 0) {
                    ArrayList<Action> possibleCapture = new ArrayList<>();
                    possibleCapture.add(action1);
                    possibleCapture.add(action2);
                    MoveList.add(new Move(possibleCapture));
                }
                canNormallyMove = false;
            }
            if (canCaptureRightBack(row, col)) {
                Action action1 = new Action(row, col, row - 1, col + 1);
                Action action2 = new Action(row - 1, col + 1, row - 2, col + 2);
                MoveList.addAll(getFollowUpMoves(action1, action2));
                if (MoveList.size() == 0) {
                    ArrayList<Action> possibleCapture = new ArrayList<>();
                    possibleCapture.add(action1);
                    possibleCapture.add(action2);
                    MoveList.add(new Move(possibleCapture));
                }
                canNormallyMove = false;
            }
            if (!canNormallyMove) {
                int maxLength = 0;
                Iterator<Move> iterator = MoveList.iterator();
                while (iterator.hasNext()) {
                    Move temp = iterator.next();
                    if (temp.length >= maxLength) maxLength = temp.length;
                }
                while (iterator.hasNext()) {
                    Move temp = iterator.next();
                    if (temp.length < maxLength) iterator.remove();
                }
                return MoveList;
            }
            else if (canNormallyMove) {
                if (row + 1 <= size) {
                    if (col - 1 >= 1 && GameBoard[row + 1][col - 1] == Piece.Empty) MoveList.add(new Move(new Action(row, col, row + 1, col - 1)));
                    if (col + 1 <= size && GameBoard[row + 1][col + 1] == Piece.Empty) MoveList.add(new Move(new Action(row, col, row + 1, col + 1)));
                }
                if (row - 1 >= 1) {
                    if (col - 1 >= 1 && GameBoard[row - 1][col - 1] == Piece.Empty) MoveList.add(new Move(new Action(row, col, row - 1, col - 1)));
                    if (col + 1 <= size && GameBoard[row - 1][col + 1] == Piece.Empty) MoveList.add(new Move(new Action(row, col, row - 1, col + 1)));
                }
            }
        }

        else if (pieceType == Piece.WhiteKing) {
            if (canCaptureLeftFront(row, col)) {
                Action action1 = new Action(row, col, row - 1, col - 1);
                Action action2 = new Action(row - 1, col - 1, row - 2, col - 2);
                MoveList.addAll(getFollowUpMoves(action1, action2));
                if (MoveList.size() == 0) {
                    ArrayList<Action> possibleCapture = new ArrayList<>();
                    possibleCapture.add(action1);
                    possibleCapture.add(action2);
                    MoveList.add(new Move(possibleCapture));
                }
                canNormallyMove = false;
            }
            if (canCaptureRightFront(row, col)) {
                Action action1 = new Action(row, col, row - 1, col + 1);
                Action action2 = new Action(row - 1, col + 1, row - 2, col + 2);
                MoveList.addAll(getFollowUpMoves(action1, action2));
                if (MoveList.size() == 0) {
                    ArrayList<Action> possibleCapture = new ArrayList<>();
                    possibleCapture.add(action1);
                    possibleCapture.add(action2);
                    MoveList.add(new Move(possibleCapture));
                }
                canNormallyMove = false;
            }
            if (canCaptureLeftBack(row, col)) {
                Action action1 = new Action(row, col, row + 1, col - 1);
                Action action2 = new Action(row + 1, col - 1, row + 2, col - 2);
                MoveList.addAll(getFollowUpMoves(action1, action2));
                if (MoveList.size() == 0) {
                    ArrayList<Action> possibleCapture = new ArrayList<>();
                    possibleCapture.add(action1);
                    possibleCapture.add(action2);
                    MoveList.add(new Move(possibleCapture));
                }
                canNormallyMove = false;
            }
            if (canCaptureRightBack(row, col)) {
                Action action1 = new Action(row, col, row + 1, col + 1);
                Action action2 = new Action(row + 1, col + 1, row + 2, col + 2);
                MoveList.addAll(getFollowUpMoves(action1, action2));
                if (MoveList.size() == 0) {
                    ArrayList<Action> possibleCapture = new ArrayList<>();
                    possibleCapture.add(action1);
                    possibleCapture.add(action2);
                    MoveList.add(new Move(possibleCapture));
                }
                canNormallyMove = false;
            }
            if (!canNormallyMove) {
                int maxLength = 0;
                Iterator<Move> iterator = MoveList.iterator();
                while (iterator.hasNext()) {
                    Move temp = iterator.next();
                    if (temp.length >= maxLength) maxLength = temp.length;
                }
                while (iterator.hasNext()) {
                    Move temp = iterator.next();
                    if (temp.length < maxLength) iterator.remove();
                }
                return MoveList;
            }
            else if (canNormallyMove) {
                if (row - 1 >= 1) {
                    if (col - 1 >= 1 && GameBoard[row - 1][col - 1] == Piece.Empty) MoveList.add(new Move(new Action(row, col, row - 1, col - 1)));
                    if (col + 1 <= size && GameBoard[row - 1][col + 1] == Piece.Empty) MoveList.add(new Move(new Action(row, col, row - 1, col + 1)));
                }
                if (row + 1 <= size) {
                    if (col - 1 >= 1 && GameBoard[row + 1][col - 1] == Piece.Empty) MoveList.add(new Move(new Action(row, col, row + 1, col - 1)));
                    if (col + 1 <= size && GameBoard[row + 1][col + 1] == Piece.Empty) MoveList.add(new Move(new Action(row, col, row + 1, col + 1)));
                }
            }
        }
        return MoveList;
    }

    @Override
    public String toString() { //Utility method which prints out a Board, based on positions of its Pieces.
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        if (size == 4) stringBuilder.append("  1 2 3 4 ");
        if (size == 8) stringBuilder.append("  1 2 3 4 5 6 7 8");
        stringBuilder.append("\n");
        for (int i = 1; i <= size; i++) {
            for (int j = 0; j <= size; j++) {
                String string = "";
                if (j == 0) {
                    if (i == 1) string = "A";
                    if (i == 2) string = "B";
                    if (i == 3) string = "C";
                    if (i == 4) string = "D";
                    if (i == 5) string = "E";
                    if (i == 6) string = "F";
                    if (i == 7) string = "G";
                    if (i == 8) string = "H";
                }
                else if (GameBoard[i][j] == Piece.NormalWhite) string = "w";
                else if (GameBoard[i][j] == Piece.NormalBlack) string = "b";
                else if (GameBoard[i][j] == Piece.WhiteKing) string = "W";
                else if (GameBoard[i][j] == Piece.BlackKing) string = "B";
                else string = "-";
                stringBuilder.append(string);
                stringBuilder.append(" ");
            }
            stringBuilder.append("\n");
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

}