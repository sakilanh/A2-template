package ca.mcmaster.se2aa4.island.teamXXX;

import ca.mcmaster.se2aa4.island.teamXXX.Decider.Needle;

public class Coordinate {
    private int[] pos = new int[] {0, 0};

    public void update(Needle direction) {
        int[] vector = getVector(direction);
        pos[0] += vector[0];
        pos[1] += vector[1];
    }

    public int[] getPosition() {
        return pos.clone();
    }

    private int[] getVector(Needle direction) {
        switch (direction) {
            case N: return new int[] {0, -1};
            case E: return new int[] {1, 0};
            case S: return new int[] {0, 1};
            case W: return new int[] {-1, 0};
            default: return new int[] {0, 0};
        }
    }
}