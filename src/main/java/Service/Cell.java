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
    protected boolean needToUpdate=true;
    public boolean getNeedToUpdate(){
        return needToUpdate;
    }
    public void setNeedToUpdate(boolean value){
        needToUpdate=value;
    }
}
