package com.example;

import java.util.ArrayList;
import java.util.Collections;

public class DefaultTable extends Table {
    public DefaultTable(int rows, int columns, int allMines) {
        super(rows, columns, allMines);
    }

    // This function checks the neighbors of the fields that are not a mine.
    // If a neighbor is a mine it adds to the counted number.
    // The fields number will be set to this counted number.
    @Override
    public void checkNeighbors() {
        for (int i = 0; i < fields.size(); i++) {
            if (fields.get(i).getIsMine() == false) {
                int mineNeighbors = 0;
                Position currentPosition = new Position(i / columns, i % columns);
                int[][] neighbors = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 },
                        { 1, 1 } };
                for (int[] d : neighbors) {
                    Position neighborPosition = new Position(currentPosition.getRow() + d[0],
                            currentPosition.getColumn() + d[1]);
                    if (neighborPosition.getRow() >= 0 && neighborPosition.getRow() < rows
                            && neighborPosition.getColumn() >= 0 && neighborPosition.getColumn() < columns) {
                        if (getByPosition(neighborPosition).getIsMine())
                            mineNeighbors++;
                    }
                }
                fields.get(i).setNumberOfNeighbors(mineNeighbors);
            }

        }
    }

    @Override
    public void selectingMines(ArrayList<Field> availableFields) {

        Collections.shuffle(availableFields);
        for (int i = 0; i < allMines; i++) {
            availableFields.get(i).setMineNumber(1);
        }
    }

    @Override
    public int getOneMineFields() {
        return allMines;
    }

    @Override
    public int getTwoMineFields() {
        return 0;
    }

    @Override
    public int getThreeMineFields() {
        return 0;
    }
}
