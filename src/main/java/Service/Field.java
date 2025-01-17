package Service;

import java.util.ArrayList;
import java.util.Random;

/**
 * Класс поля симуляции.
 */
public class Field {
    /**
     * Высота поля.
     */
    private static final int HEIGHT = 120;
    /**
     * Ширина поля.
     */
    private static final int WIDTH = 400;

    /**
     * Инициальзирует ботов на поле.
     */
    public static void initialize(int count) {
        for(int r=0;r<HEIGHT;r++){
            for(int c=0;c<WIDTH;c++)
                field[r][c]=new Cell(r,c);
        }
        bots.clear();
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            int row = rand.nextInt(HEIGHT);
            int column = rand.nextInt(WIDTH);
            Bot bot = new Bot(row, column, 50, null);
            setCell(bot);
            bots.add(bot);
        }
        random=new Random();
    }
    public static int iterate(){
        Bot[] cBots=bots.toArray(Bot[]::new);
        for(Bot bot : cBots)
            bot.doAction();
        return ++iteration;
    }
    private static int iteration=0;

    public static int getHeight(){
        return HEIGHT;
    }
    public static int getWidth(){
        return WIDTH;
    }

    /**
     * Лист активных ботов.
     */
    private static ArrayList<Bot> bots = new ArrayList<>();

    /**
     * @return {@link Field#bots}
     */
    public static ArrayList<Bot> getBots() {
        return bots;
    }

    /**
     * Массив поля.
     */
    private static Cell[][] field = new Cell[HEIGHT][WIDTH];

    /**
     * @return {@link Field#field}
     */
    public static Cell[][] getField() {
        return field;
    }

    /**
     * Устанавливает клетку на координаты, которые в ней указаны.
     */
    public static void setCell(Cell cell) {
        field[cell.row][cell.column] = cell;
    }

    /**
     * Замещает данную клетку пустой клеткой.
     */
    public static void setEmptyCell(Cell cell){
        setCell(new Cell(cell.row, cell.column));
    }

    /**
     * @param row    Ряд клетки.
     * @param column Столбец клетки. Может быть перехлёстным.
     * @return Клетка поля с координатами row и column.
     */
    private static Cell getCell(int row, int column) {
        if ((column < 0) || (column >= WIDTH))
            column -= (int) Math.copySign(WIDTH, column);
        return field[row][column];
    }

    /**
     * @param row    Ряд клетки.
     * @param column Столбец клетки.
     * @return Массив всех соседних клеток.
     */
    public static Cell[] getAdjacentCells(int row, int column) {
        ArrayList<Cell> cells = new ArrayList<>(9);
        for (Orientation direction : Orientation.values()) {
            int[] coord = direction.getDirection();
            int r = row + coord[0];
            int c = column + coord[1];
            if ((r >= 0) && (r < HEIGHT))
                cells.add(getCell(r, c));
        }
        return cells.toArray(Cell[]::new);
    }

    /**
     * @param row       Ряд клетки.
     * @param column    Колонка клетки.
     * @param direction Направление просмотра.
     * @return Клетка, находящаяся в заданном направлении от данной.
     */
    public static Cell getAheadCell(int row, int column, Orientation direction) throws IndexOutOfBoundsException {
        int[] coord = direction.getDirection();
        int r = row + coord[0];
        int c = column + coord[1];
        return getCell(r, c);
    }


    private static Random random;
    /**
     * @param row Уровень.
     * @return Количество солнесной энергии на уровне.
     */
    public static int getSun(int row) {
        return random.nextInt(4,8+1);
    }
}
