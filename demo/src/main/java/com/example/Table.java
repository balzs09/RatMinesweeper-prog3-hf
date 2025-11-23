package com.example;

import java.util.List;
import java.util.ArrayList;

public abstract class Table {
    protected int rows;
    protected int columns;
    protected List<Field> fields = new ArrayList<Field>();
    protected int allMines;

    public Table(int rowNumber, int columnNumber, int mineNumber) {
        rows = rowNumber;
        columns = columnNumber;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++)
                fields.add(new Field(false));
        }
        allMines = mineNumber;
    }

    // Returns the field in fields list by its position.
    public Field getFieldByPosition(Position p) {
        return fields.get(p.getRow() * columns + p.getColumn());
    }

    public List<Position> getNeighbors(Position currentPosition) {
        List<Position> neighborPositions = new ArrayList<>();
        int[][] neighborIndexes = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 },
                { 1, 1 } };
        for (int[] d : neighborIndexes) {
            Position neighborPosition = new Position(currentPosition.getRow() + d[0],
                    currentPosition.getColumn() + d[1]);
            neighborPositions.add(neighborPosition);
        }
        return neighborPositions;
    }

    // This function checks the neighbors of the fields that are not a mine.
    public abstract void checkNeighbors();

    // This function returns with the list of fields, that are not neighbors to the
    // selected one.
    // These can be minefields.
    public ArrayList<Field> getavailableFields(Position selectedPosition) {
        ArrayList<Field> availableFields = new ArrayList<>(fields);
        availableFields.remove(getFieldByPosition(selectedPosition));
        List<Position> neighbors = getNeighbors(selectedPosition);
        for (Position neighborPosition : neighbors) {
            if (neighborPosition.getRow() >= 0 && neighborPosition.getRow() < rows
                    && neighborPosition.getColumn() >= 0 && neighborPosition.getColumn() < columns)
                availableFields.remove(getFieldByPosition(neighborPosition));

        }
        return availableFields;
    }

    public abstract void selectingMines(ArrayList<Field> availableFields);

    public int getAllMines() {
        return allMines;
    }

    public abstract int getOneMineFields();

    // Relevant in the RatTable class
    public abstract int getTwoMineFields();

    public abstract int getThreeMineFields();

}
