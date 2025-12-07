package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultTable extends Table {

    /**
     * A DefaultTable meghívja az ősosztálya, a Table konstruktorát.
     * 
     * @param rows     sorok száma
     * @param columns  oszlopok száma
     * @param allMines bombák száma
     */
    public DefaultTable(int rows, int columns, int allMines) {
        super(rows, columns, allMines);
    }

    /**
     * Ellenőrzi, hogy egy mező szomszédjaiban összesen hány bomba található.
     * Azon mezők vannak ellenőrizve, amelyek nem tartalmaznak bombák.
     * Ha egy mező szomszédja tartalmaz bombát növeli a mezővel szomszédos bombák
     * számlálóját eggyel.
     * A mező szomszédos bombáinak száma a számláló értéke lesz
     * 
     */
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
                        if (getFieldByPosition(neighborPosition).getIsMine())
                            mineNeighbors++;
                    }
                }
                fields.get(i).setNumberOfNeighbors(mineNeighbors);
            }

        }
    }

    /**
     * Ez a metódus visszatér egy mező szomszédjainak listájával.
     * A getNeighbors metódust hívja meg a szomszédok listájának eléréséhez.
     * 
     * @param p jelenlegi pozíció, melynek szeretnénk megtudni a szomszédjait
     * @return szomszédpozíciók listája
     */
    @Override
    public List<Position> getNeighborPositions(Position p) {
        return getNeighbors(p);
    }

    /**
     * Kiválasztja azokat a mezőket, amelyek bombát tartalmaznak.
     * A bombát tartalmazó mezők között nem lehet olyan,
     * ami az elsőként kiválasztott pozíció szomszédja.
     * Az elérhető mezők össze vannak keverve annak érdekében ,
     * hogy a táblán véletlenszerűen szerepljenek a bombák.
     * A táblához tartozó bombák számával egyenlő mezőnek beállítja a hozzá tartozó
     * bombák számát 1-re.
     * 
     * @param availableFields az elérhető pozíciók
     */
    @Override
    public void selectingMines(ArrayList<Field> availableFields) {

        Collections.shuffle(availableFields);
        for (int i = 0; i < allMines; i++) {
            availableFields.get(i).setMineNumber(1);
        }
    }

    /**
     * A metódus visszatér az egyetlen bombát tartalmazó mezők számával
     * 
     * @return az egyetlen bombát tartalmazó mezők száma
     */
    @Override
    public int getOneMineFields() {
        return allMines;
    }

}
