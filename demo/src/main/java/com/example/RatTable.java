package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
public class RatTable extends Table {
    private int oneMineFields;
    private int twoMineFields;
    private int threeMineFields;
    private Rat rat;

    public RatTable(int rownumber, int columnumber, int mineNumber) {
        super(rownumber, columnumber, mineNumber);
        oneMineFields = allMines / 2;
        twoMineFields = allMines / 4;
        threeMineFields = allMines / 4;
    }
    
    private List<Position> getNeighborsWrapAround(Position currentPosition) {
        List<Position> neighborPositions = new ArrayList<>();
        int[][] neighborIndexes = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 },
                { 1, 1 } };
        for (int[] d : neighborIndexes) {
            Position neighborPosition = new Position((currentPosition.getRow() + d[0])%rows,
                    (currentPosition.getColumn() + d[1])%columns);
            neighborPositions.add(neighborPosition);
        }
        return neighborPositions;
    }

    // In this case, if a field is on the border of the Table, its neighbor will be
    // from the other side of the table.
    // This can be done with the
    @Override
    public void checkNeighbors(){
       for(int i=0;i<fields.size();i++){
        if(fields.get(i).getIsMine()==false){
            int mineNeighbors=0;
            Position currentPosition = new Position(i / columns, i % columns);
            List<Position> neighbors= getNeighborsWrapAround(currentPosition);
            for(Position neighborPosition: neighbors){
                if(getFieldByPosition(neighborPosition).getIsMine()) 
                    mineNeighbors+=getFieldByPosition(neighborPosition).getMineNumber();
            }
            fields.get(i).setNumberOfNeighbors(mineNeighbors);
        } 
      }
    }

    @Override
    public void selectingMines(ArrayList<Field> availableFields) {
        Collections.shuffle(availableFields);
        for (int i = 0; i < allMines; i++) {
            Field mineField = availableFields.get(i);
            if (i < oneMineFields)
                mineField.setMineNumber(1);
            else if (i < oneMineFields + twoMineFields)
                mineField.setMineNumber(2);
            else
                mineField.setMineNumber(3);
        }
    }
    //Reconstructing the positions list, by the parent hashmap
    public List<Position> reconstructPath(Map<Position,Position> parent){
        List<Position> path= new ArrayList<>();
        Position current=rat.getGoalPosition();
        while(current!=null){
            path.add(current);
            current=parent.get(current);
        }
        Collections.reverse(path);
        return path;

    }
    //BFS search for shortest path.
    public List<Position> findShortestPath(){
      Queue<Position> queue= new LinkedList<>();
      Map<Position,Position> parent= new HashMap<>();
      Set<Position> visited= new HashSet<>();
      queue.add(rat.getCurrentPosition());
      visited.add(rat.getCurrentPosition());
      while(!queue.isEmpty()){
        Position current= queue.poll();
        if(current.equals(rat.getGoalPosition()))
            return reconstructPath(parent);
        for(Position next : getNeighbors(current)){
            if(!visited.contains(current)){
                queue.add(next);
                visited.add(current);
                parent.put(next,current);
            }
        }
      }
      return new ArrayList<>(); //no path found

    }
    @Override
    public int getOneMineFields() {
        return oneMineFields;
    }

    @Override
    public int getTwoMineFields() {
        return twoMineFields;
    }

    @Override
    public int getThreeMineFields() {
        return threeMineFields;
    }
}
