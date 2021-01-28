import java.util.*;

//Class Action represents simply moving a piece from one Square to another Square on a Board;
//Move consists of a sequence of Actions;

class Action {

    int start_row;
    int start_col;
    int end_row;
    int end_col;

    public Action(int start_row, int start_col, int end_row, int end_col) {
        this.start_row = start_row;
        this.start_col = start_col;
        this.end_row = end_row;
        this.end_col = end_col;
    }

    @Override
    public String toString() {
        String string = "";
        System.out.println("(" + start_row+ ", " + start_col + ")" + " to " + "(" + end_row+ ", " + end_col + ")");
        return string;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object instanceof Action) {
            if (((Action) object).start_row == this.start_row && ((Action) object).start_col == this.start_col && ((Action) object).end_row == this.end_row && ((Action) object).end_col == this.end_col) return true;
        }
        if (!(object instanceof Action)) return false;
        return false;
    }
}

//Move represents a Move on Checker Board (Normal Move or Capture Move).
//Move consists of a sequence of Actions, represented by an ArrayList of Actions.
//A Move's length is determined by this sequence's size.
//A Move can either be a Normal Move or a Capture Move

public class Move {

    ArrayList<Action> sequence;
    int length;

    public Move(ArrayList<Action> sequenceOfActions) {
        sequence = sequenceOfActions;
        length = sequenceOfActions.size();
    }

    public Move(Action action) {
        sequence = new ArrayList<>();
        sequence.add(action);
        length = 1;
    }

    public void addActionToSequence(int index, Action action) {
        sequence.add(index, action);
        length++;
    }

    public boolean isCaptureMove() {
        if (length > 1) return true;
        return false;
    }

    @Override
    public boolean equals(Object object) { //Override equals, so that ArrayList sequence can be used as I want (e.g. contains method).
        if (object == this) return true;
        if (object instanceof Move) {
            if (((Move) object).sequence.equals(this.sequence) && ((Move) object).length == this.length) return true;
        }
        if (!(object instanceof Move)) return false;
        return false;
    }

    public String RowToString(int rowNumber) {
        String string = "";
        if(rowNumber == 1) string ="A";
        if(rowNumber == 2) string ="B";
        if(rowNumber == 3) string ="C";
        if(rowNumber == 4) string ="D";
        if(rowNumber == 5) string ="E";
        if(rowNumber == 6) string ="F";
        if(rowNumber == 7) string ="G";
        if(rowNumber == 8) string ="H";
        return string;
    }

    @Override
    public String toString() { //Utility method which converts a Move to a string.
        String string = "";
        if (length == 1) {
            string += RowToString(sequence.get(0).start_row);
            string += Integer.toString(sequence.get(0).start_col);
            string += "-";
            string += RowToString(sequence.get(0).end_row);
            string += Integer.toString(sequence.get(0).end_col);
        }
        if (length > 1) {
            string += RowToString(sequence.get(0).start_row);
            string += Integer.toString(sequence.get(0).start_col);
            for (int index = 1; index < length; index++) {
                if (index % 2 == 0) continue;
                String endCoordinates = "";
                endCoordinates += "x";
                endCoordinates += RowToString(sequence.get(index).end_row);
                endCoordinates+= Integer.toString(sequence.get(index).end_col);
                if (!string.contains(endCoordinates)) string += endCoordinates;
            }
        }
        return string;
    }

}