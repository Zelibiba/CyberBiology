package Service;

/**
 * Класс клетки на поле.
 */
public class Cell {
    public Cell(int row, int column){
        this.row=row;
        this.column=column;
    }

    /**
     * Ряд клетки.
     */
    protected int row;
    /**
     * Столбик клетки.
     */
    protected int column;
}
