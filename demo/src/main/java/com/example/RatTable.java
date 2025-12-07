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
    private List<Position> shortestPath;

    /**
     * A RatTable meghívja az ősosztálya, a Table konstruktorát.
     * Az egyetlen bombát tartalmazó mezők száma az összes bomba felével lesz
     * egyenlő.
     * A két bombát tartalmazó mezők száma az összes bomba/4-el egyenlő.
     * Ugyanaz igaz a 3 bombát tartalmazó mezőkre, mint a 2 bombásokra.
     * 
     * @param rows     sorok száma
     * @param columns  oszlopok száma
     * @param allMines bombák száma
     */
    public RatTable(int rownumber, int columnumber, int mineNumber) {
        super(rownumber, columnumber, mineNumber);
        oneMineFields = allMines / 2;
        twoMineFields = allMines / 4;
        threeMineFields = allMines / 4;
    }

    /**
     * A tábla Rat objektuma megkapja egy új, létrehozott Rat értékét.
     * A Rat konstruktorában a kezdőpozíciót kapja meg.
     * 
     * @param firstPosition a Rat kezdőpozíciója
     */
    public void setRat(Position firstPosition) {
        rat = new Rat(firstPosition);
    }

    /**
     * A RatTable egy pozíciójának szomszédaival tér vissza.
     * Ebben az esetben, a tábla másik felével is szomszédosak a tábla szélső mezői.
     * 
     * @param currentPosition
     * @return egy pozíció szomszédos pozícióinak listája
     */
    private List<Position> getNeighborsWrapAround(Position currentPosition) {
        List<Position> neighborPositions = new ArrayList<>();
        int[][] neighborIndexes = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 },
                { 1, 1 } };
        for (int[] d : neighborIndexes) {
            Position neighborPosition = new Position((currentPosition.getRow() + d[0] + rows) % rows,
                    (currentPosition.getColumn() + d[1] + columns) % columns);
            neighborPositions.add(neighborPosition);
        }
        return neighborPositions;
    }

    /**
     * Ez a metódus visszatér egy mező szomszédjainak listájával.
     * A getNeighborsWrapAround metódust hívja meg a szomszédok listájának
     * eléréséhez.
     * 
     * @param p jelenlegi pozíció, melynek szeretnénk megtudni a szomszédjait
     * @return szomszédpozíciók listája
     */
    @Override
    public List<Position> getNeighborPositions(Position p) {
        return getNeighborsWrapAround(p);
    }

    /**
     * Ellenőrzi, hogy egy mező szomszédjaiban összesen hány bomba található.
     * Azon mezők vannak ellenőrizve, amelyek nem tartalmaznak bombák.
     * Ha egy mező szomszédja tartalmaz bombát növeli a mezővel szomszédos bombák
     * számlálóját annyival amennyi bombát tartalmaz a szomszédos mező.
     * A mező szomszédos bombáinak száma a számláló értéke lesz.
     * A szomszédok között ebben a számításban is beletartoznak a szélső pozíciókhoz
     * tartozó másik oldalon lévő pozíciók.
     * 
     */
    @Override
    public void checkNeighbors() {
        for (int i = 0; i < fields.size(); i++) {
            if (fields.get(i).getIsMine() == false) {
                int mineNeighbors = 0;
                Position currentPosition = new Position(i / columns, i % columns);
                List<Position> neighbors = getNeighborsWrapAround(currentPosition);
                for (Position neighborPosition : neighbors) {
                    if (getFieldByPosition(neighborPosition).getIsMine())
                        mineNeighbors += getFieldByPosition(neighborPosition).getMineNumber();
                }
                fields.get(i).setNumberOfNeighbors(mineNeighbors);
            }
        }
    }

    /**
     * Kiválasztja azokat a mezőket, amelyek bombát tartalmaznak.
     * A bombát tartalmazó mezők között nem lehet olyan,
     * ami az elsőként kiválasztott pozíció szomszédja.
     * Az elérhető mezők össze vannak keverve annak érdekében ,
     * hogy a táblán véletlenszerűen szerepljenek a bombák.
     * A táblához tartozó bombák számával egyenlő mezőnek felének beállítja a hozzá
     * tartozó bombák számát 1-re,
     * a negyedét 2-re és az utolsó negyedét 3-ra.
     * 
     * @param availableFields az elérhető pozíciók
     */
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

    /**
     * Viszaépíti a findShortestPath által megkeresett legrövidebb utat.
     * A parent HashMapban minden pozíció kulcsa az előtte lévőknek.
     * így ezt végigjárva hozzáadhatjuk a pozíciókat az úthoz.
     * A végén meg kell fordítani az utat, hogy a megfelelő sorrendben legyenek
     * benne a pozíciók.
     * 
     * @param parent a legrövidebb út pozícióinak HashMapje
     * @return a legrövidebb út pozícióinak listája
     */
    public List<Position> reconstructPath(Map<Position, Position> parent) {
        List<Position> path = new ArrayList<>();
        Position current = rat.getGoalPosition();
        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }
        Collections.reverse(path);
        return path;

    }

    /**
     * Ez a függvény visszaadja egy pozíció olyan szomszédjait amelyek nincsenek
     * egyik irányból távolabb az egér célpozíciójától.
     * Ez azért fontos, hogy a legrövidebb útba ne kerüljenek olyan pozíciók,
     * amelyek
     * az azelőtti pozícióhoz képest távolabb vannak a céltól valamelyik irányban.
     * 
     * @param current a jelenlegi pozíció
     * @return szomszédos pozíciók listája, amelyek nincsenek távolabb a céltól
     *         egyik irányból se
     */
    public List<Position> getRightDirectionNeighbors(Position current) {
        List<Position> directionNeighbors = new ArrayList<>();
        Position ratGoalPosition = rat.getGoalPosition();
        int columnDistance = Math.abs(ratGoalPosition.getColumn() - current.getColumn());
        int rowDistance = Math.abs(ratGoalPosition.getRow() - current.getRow());
        for (Position next : getNeighbors(current)) {
            if (Math.abs(ratGoalPosition.getColumn() - next.getColumn()) <= columnDistance
                    && Math.abs(ratGoalPosition.getRow() - next.getRow()) <= rowDistance)
                directionNeighbors.add(next);
        }
        return directionNeighbors;

    }

    /**
     * A legrövidebb út megtalálása BFS algoritmust használva.
     * Az egér jelenlegi pozíciójához képest keresi meg a célpozícióhoz a
     * legrövidebb utat.
     * 
     * A metódus működése:
     * - A BFS egy queue-t használ a bejárási sorrend biztosításához.
     * - A visited halmaz megakadályozza, hogy ugyanazt a pozíciót többször
     * vizsgálja (így elkerülhetők a ciklusok).
     * - A parent map minden csúcs esetén eltárolja, hogy melyik pozícióból
     * értük el — ez teszi lehetővé az út rekonstrukcióját a cél elérésekor.
     * 
     * @return A célpozícióhoz vezető út legrövidebb út pozíciónak listája
     *         vagy üres lista, ha nem létezik elérhető út
     */
    // BFS search for shortest path.
    public List<Position> findShortestPath() {
        Queue<Position> queue = new LinkedList<>();
        Map<Position, Position> parent = new HashMap<>();
        Set<Position> visited = new HashSet<>();
        queue.add(rat.getCurrentPosition());
        visited.add(rat.getCurrentPosition());
        while (!queue.isEmpty()) {
            Position current = queue.poll();
            if (current.equals(rat.getGoalPosition()))
                return reconstructPath(parent);
            for (Position next : getRightDirectionNeighbors(current)) {
                if (!visited.contains(next)) {
                    queue.add(next);
                    visited.add(next);
                    parent.put(next, current);
                }
            }
        }
        return new ArrayList<>();

    }

    /**
     * A shortestPath tagváltozó megkapja a findShortestPath metódus által
     * visszatérített listát.
     * 
     */
    public void setShortestPath() {
        shortestPath = findShortestPath();
    }

    /**
     * Visszatéríti a legrövidebb utat
     * 
     * @return legrövidebb út pozícióinak listája
     */
    public List<Position> getShortestPath() {
        return shortestPath;
    }

    /**
     * Visszatéríti a táblához tartozó Rat objektumot.
     * 
     * @return táblához tartozó Rat objektum
     */
    public Rat getRat() {
        return rat;
    }

    /**
     * A metódus visszatér az egyetlen bombát tartalmazó mezők számával
     * 
     * @return az egyetlen bombát tartalmazó mezők száma
     */
    @Override
    public int getOneMineFields() {
        return oneMineFields;
    }

    /**
     * A metódus visszatér a két bombát tartalmazó mezők számával
     * 
     * @return a két bombát tartalmazó mezők száma
     */

    public int getTwoMineFields() {
        return twoMineFields;
    }

    /**
     * A metódus visszatér a három bombát tartalmazó mezők számával
     * 
     * @return a három bombát tartalmazó mezők száma
     */
    public int getThreeMineFields() {
        return threeMineFields;
    }

}
