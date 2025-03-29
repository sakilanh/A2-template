package ca.mcmaster.se2aa4.island.teamXXX;

public class Compass {

    //Compass to keep track of direction
    public enum Needle {
        N, E, S, W
    }
    private Needle facing = Needle.E;

    //return the needle as string
    public Needle getNeedle() {
        return facing;
    }

    //turnLeft and turnRight facilitate compass turning
    public void turnLeft() {
        switch (facing) {
            case N:
                facing = Needle.W; break;
            case E:
                facing = Needle.N; break;
            case S:
                facing = Needle.E; break;
            case W:
                facing = Needle.S; break;
        }
    }
    public void turnRight() {
        switch (facing) {
            case N:
                facing = Needle.E; break;
            case E:
                facing = Needle.S; break;
            case S:
                facing = Needle.W; break;
            case W:
                facing = Needle.N; break;
        }
    }

    //getLeft and getRight tell you what is to the side but do not turn
    public String getLeft() {
        switch (facing) {
            case N:
                return "W";
            case E:
                return "N";
            case S:
                return "E";
            case W:
                return "S";
            default:
                return "-";
        }
    }
    public String getRight() {
        switch (facing) {
            case N:
                return "W";
            case E:
                return "S";
            case S:
                return "W";
            case W:
                return "N";
            default:
                return "-";
        }
    }
}