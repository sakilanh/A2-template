package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;




public class Decider {

    private enum Task {
        START, FLY, LEFT, RIGHT, SCAN, RADAR_FRONT, RADAR_LEFT, RADAR_RIGHT, STOP, RADAR
    }
    private Task task = Task.START;

    private enum Needle {
        N, E, S, W
    }
    private Needle facing = Needle.E;

    public String[][] decide() {
        return algorithm();
    }

    private int counter = 0;
    public String[][] algorithm() {
        Task[] algo ={Task.FLY, Task.SCAN, Task.RIGHT, Task.RADAR_FRONT, Task.LEFT, Task.SCAN, Task.RIGHT, Task.SCAN, Task.LEFT, Task.SCAN, Task.FLY, Task.SCAN, Task.STOP};
        if (counter >= algo.length) {
            task = Task.STOP;
        } else {
            task = algo[counter];
        }
        counter += 1;
        return getCommand();
    }

    public String[][] getCommand() {
        switch (task) {
            case FLY:
                return new String[][] {{"fly"}};
            case LEFT:
                facing = turnLeft(facing);
                return new String[][] {{"heading"}, {"direction", facing.toString()}};
            case RIGHT:
                facing = turnRight(facing);
                return new String[][] {{"heading"}, {"direction", facing.toString()}};
            case SCAN:
                return new String[][] {{"scan"}};
            case RADAR_FRONT:
                return new String[][] {{"echo"}, {"direction", facing.toString()}};
            case RADAR_LEFT:
                return new String[][] {{"echo"}, {"direction", turnLeft(facing).toString()}};
            case RADAR_RIGHT:
                return new String[][] {{"echo"}, {"direction", turnRight(facing).toString()}};
            case STOP:
                return new String[][] {{"stop"}};
            default:
                return new String[][] {{"stop"}};
        }
    }

    public Needle turnLeft(Needle current) {
        Needle turned = current;
        switch (current) {
            case N:
                turned = Needle.W; break;
            case E:
                turned = Needle.N; break;
            case S:
                turned = Needle.E; break;
            case W:
                turned = Needle.S; break;
        }
        return turned;
    }
    public Needle turnRight(Needle current) {
        Needle turned = current;
        switch (current) {
            case N:
                turned = Needle.E; break;
            case E:
                turned = Needle.S; break;
            case S:
                turned = Needle.W; break;
            case W:
                turned = Needle.N; break;
        }
        return turned;
    }

    //analyising the response
    private int budget = 1000;
    public String analyse(JSONObject response) {
        budget -= response.getInt("cost");
        return ""+budget;
    }

}