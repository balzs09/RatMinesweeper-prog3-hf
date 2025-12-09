package com.example;

import java.util.List;
import java.util.ArrayList;
/**
 * @brief Absztrakt táblaosztály az aknakereső játékhoz.
 *
 * A Table osztály reprezentál egy játékteret, amely sorokból és oszlopokból áll,  
 * és mezőket (Field) tartalmaz. Emellett tartalmazza azt is, hogy hány bombamező
 * szerepel a táblában.
 * Az osztály felelős a mezők tárolásáért,
 * szomszédsági viszonyok kezeléséért, valamint a bombák elhelyezésének
 * logikájáért.
 * A konkrét játékmódok (DefaultTable, RatTable) ebből az osztályból származnak,
 * és megvalósítják a játékmód-specifikus funkciókat,
 * mint például a szomszédpozíciók meghatározása vagy a bombák kijelölése.
 */
public abstract class Table {
    protected int rows;
    protected int columns;
    protected List<Field> fields = new ArrayList<Field>();
    protected int allMines;

    /**
     * A tábla osztály konstruktora.
     * A tagváltozók megkapják a paraméterben átadott érékeket.
     * A tábla méretéhez igazodva minden pozícióhoz hozzáadódik egy új, bomba
     * nélküli mező.
     * 
     * @param rowNumber    sorok száma
     * @param columnNumber oszlopok száma
     * @param mineNumber   bombák száma
     */
    public Table(int rowNumber, int columnNumber, int mineNumber) {
        rows = rowNumber;
        columns = columnNumber;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++)
                fields.add(new Field(false));
        }
        allMines = mineNumber;
    }

    /**
     * Visszatér a mezővel a field listából a pozíció alapján
     * 
     * @param p a mező pozíciója
     * @return a fieldben taláható mező, amely megfelel a pozíciónak
     */
    public Field getFieldByPosition(Position p) {
        return fields.get(p.getRow() * columns + p.getColumn());
    }

    /**
     * Visszatér a mező pozíciójával.
     * A fields listában keresi meg a paraméterként megadott mezőt.
     * 
     * @param f a mező
     * @return a mezőhöz tartozó pozíció;
     *         amennyiben nem található meg a mező a fields listában, (-1,-1)
     *         pozícióval tér vissza.
     */
    public Position getPositionByField(Field f) {
        for (int i = 0; i < fields.size(); i++) {
            if (fields.get(i).equals(f))
                return new Position(i / columns, i % columns);
        }
        return new Position(-1, -1);
    }

    /**
     * Ez a metódus egy pozícióval szomszédos pozíciókkal tér vissza.
     * Abban az esetben, ha a szomszédos pozíció szerepel a táblán (nem lépi túl a
     * tábla méreteit) hozzáadódik a szomszéd pozíciók listához.
     * 
     * @param currentPosition jelenlegi pozició, melynek szeretnénk megtudni a
     *                        szomszédjait
     * @return szomszédpozíciók listája
     */
    public List<Position> getNeighbors(Position currentPosition) {
        List<Position> neighborPositions = new ArrayList<>();
        int[][] neighborIndexes = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 },
                { 1, 1 } };
        for (int[] d : neighborIndexes) {
            Position neighborPosition = new Position(currentPosition.getRow() + d[0],
                    currentPosition.getColumn() + d[1]);
            if (neighborPosition.getRow() >= 0 && neighborPosition.getRow() < rows
                    && neighborPosition.getColumn() >= 0 && neighborPosition.getColumn() < columns)
                neighborPositions.add(neighborPosition);
        }
        return neighborPositions;
    }

    /**
     * Ez a metódus a DefaultTable és RatTable osztályokban van megvalósítva.
     * Mindkét estetben a játékmód táblájának megfelelő pozíciókkal tér vissza.
     * 
     * @param p jelenlegi pozíció, melynek szeretnénk megtudni a szomszédjait
     * @return szomszédpozíciók listája
     */
    public abstract List<Position> getNeighborPositions(Position p);

    /**
     * Ez a metódus a DefaultTable és RatTable osztályokban van megvalósítva.
     * Ellenőrzi, hogy egy mező szomszédjaiban összsen hány bomba található.
     * 
     */
    public abstract void checkNeighbors();

    /**
     * Visszatér azokkal a mezőkkel amelyek nem szomszédosak a paraméterként
     * kiválasztottal.
     * Egy új availableFields lista megkapja a fields lista elemeit.
     * Innen törlődnek a meagdott pozícióval szomszédos pozíciókhoz tartozó mezők.
     * Valamint törlődik a megadott pozícióhoz tartozó mezők is
     * 
     * @param selectedPosition a kiválasztott mező
     * @return összes mező-(kiválasztott+szomszédok) listája
     */
    public ArrayList<Field> getavailableFields(Position selectedPosition) {
        ArrayList<Field> availableFields = new ArrayList<>(fields);
        availableFields.remove(getFieldByPosition(selectedPosition));
        List<Position> neighbors = getNeighborPositions(selectedPosition);
        for (Position neighborPosition : neighbors) {
            if (neighborPosition.getRow() >= 0 && neighborPosition.getRow() < rows
                    && neighborPosition.getColumn() >= 0 && neighborPosition.getColumn() < columns)
                availableFields.remove(getFieldByPosition(neighborPosition));

        }
        return availableFields;
    }

    /**
     * Ez a metódus a DefaultTable és RatTable osztályokban van megvalósítva.
     * Kiválasztja azokat a mezőket, amelyek bombát tartalmaznak.
     * A bombát tartalmazó mezők között nem lehet olyan,
     * ami az elsőként kiválasztott pozíció szomszédja.
     * 
     * @param availableFields az elérhető pozíciók
     */
    public abstract void selectingMines(ArrayList<Field> availableFields);

    /**
     * Ez a metódus felfedi az üres mezővel szomszédos mezőket.
     * Abban az esetben, ha a szomszédos mező már be van jelölve zászlóként, 
     * a mező nem lesz felfedve.
     * Ha a szomszédok közül is van egy mező amely üres,
     * annak szomszédait is felfedi
     * A visited set-ben tárolja a megtalált üres mezőket.
     * Ez azért szükséges, hogy ugyanarra a mezőre ne legyen többször elvégezve a
     * metódus, végtelen ciklust generálva.
     * 
     * @param f       kiválasztott üres mező
     * @param visited azon üres mezők, amelyekre a függvény már meg volt hívva.
     */


    /**
     * Visszatér a bombát tartalmazó mezők számával
     * 
     * @return bombát tartalmazó mezők száma
     */
    public int getAllMines() {
        return allMines;
    }

    /**
     * Ez a metódus a DefaultTable és RatTable osztályokban van megvalósítva.
     * A függvény visszatér az egyetlen bombát tartalmazó mezők számával
     * 
     * @return az egyetlen bombát tartalmazó mezők száma
     */
    public abstract int getOneMineFields();

    /**
     * A függvény visszatér a tábla sorainak számával
     * 
     * @return tábla sorainak száma
     */
    public int getRows() {
        return rows;
    }

    /**
     * A függvény visszatér a tábla oszlopainak számával
     * 
     * @return tábla oszlopainak száma
     */
    public int getColumns() {
        return columns;
    }

}
