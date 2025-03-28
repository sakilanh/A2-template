package ca.mcmaster.se2aa4.island.teamXXX;

import ca.mcmaster.se2aa4.island.teamXXX.Decider.Needle;
import ca.mcmaster.se2aa4.island.teamXXX.Decider.Task;

public class Coordinate {
    private int[] position = {0, 0};

    public int[] getPosition() {
        return position;
    }

    public void update(Needle direction, Task move) {
        int[] vector = {0, 0};
        switch (move) {
            case FLY:
                vector = getFrontVector(direction); break;
            case LEFT:
                vector = getLeftVector(direction); break;
            case RIGHT:
                vector = getRightVector(direction); break;
            //default:
            //    vector = new int[] {0, 0};
        }
        position[0] += vector[0];
        position[1] += vector[1];
    }

    private int[] getFrontVector(Needle direction) {
        switch(direction) {
            case N:
                return new int[] {0, -1};
            case E:
                return new int[] {1, 0};
            case S:
                return new int[] {0, 1};
            case W:
                return new int[] {-1, 0};
            default:
                return new int[] {0, 0};
        }
    }

    private int[] getLeftVector(Needle direction) {
        switch(direction) {
            case N:
                return new int[] {-1, -1};
            case E:
                return new int[] {1, -1};
            case S:
                return new int[] {1, 1};
            case W:
                return new int[] {-1, 1};
            default:
                return new int[] {0, 0};
        }
    }

    private int[] getRightVector(Needle direction) {
        switch(direction) {
            case N:
                return new int[] {1, -1};
            case E:
                return new int[] {1, 1};
            case S:
                return new int[] {-1, 1};
            case W:
                return new int[] {-1, -1};
            default:
                return new int[] {0, 0};
        }
    }


}