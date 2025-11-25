package com.example;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

public abstract class Table {
    protected int rows;
    protected int columns;
    protected List<Field> fields = new ArrayList<Field>();
    protected Set<Field> revealedFields= new HashSet<Field>();
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
    
    public  Position getPositionByField(Field f) {
        for(int i=0;i<fields.size();i++){
            if(fields.get(i).equals(f)) return new Position(i/columns, i%columns);
        }
        return new Position(-1,-1);
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
    public abstract List<Position> getNeighborPositions(Position p);
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
    
    public void RevealNeighborsOfEmptyFields(Field f,Set<Field> visited){
        if(visited.contains(f)) return;
        visited.add(f);
        if(f.getNumberOfNeighbors()==0){
            List<Position> neighborPositions= getNeighbors(getPositionByField(f));
            for(Position neighbor :neighborPositions){
               Field neighborField=getFieldByPosition(neighbor);
              if(!neighborField.getFlagged()) neighborField.setRevealed(true);
              RevealNeighborsOfEmptyFields(neighborField,visited);
            }
        }
    }
    public int getAllMines() {
        return allMines;
    }

    public abstract int getOneMineFields();

    public int getRows(){
        return rows;
    }
    public int getColumns(){
        return columns;
    }


}
