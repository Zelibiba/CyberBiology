package Service;

import java.util.Arrays;
import java.util.Random;

/**
 * Класс живых ботов и мёртвой органики.
 */
public class Bot extends Cell {

    /**
     * Максимум энергии, накапливаемой ботом.
     */
    private static final int MAX_ENERGY = 100;
    /**
     * Трата энергии за ход.
     */
    private static final int ENERGY_DRAIN = 1;
    /**
     * Максимальное количество комманд, которое можно выполнить за ход.
     */
    private static final int COMMAND_COUNT = 15;


    /**
     * Конструктор живого бота.
     *
     * @param row    Ряд бота.
     * @param column Колонка бота.
     * @param energy Энергия бота.
     * @param genome Геном бота. null, если необходимо создать случайный.
     */
    public Bot(int row, int column, int energy, Genome genome) {
        super(row, column);
        this.energy = energy;
        this.isAlive = true;
        orientation = Orientation.randomize();
        this.genome = (genome == null) ? new Genome() : new Genome(genome, true);
    }

    /**
     * Ориентация бота.
     */
    private Orientation orientation;
    /**
     * Энергия бота.
     */
    private int energy;
    /**
     * Флаг того, что бот жив и может что-то сделать.
     */
    private boolean isAlive;
    /**
     * Геном бота.
     */
    private final Genome genome;

    /**
     * Метод реагирует на то, что находится в определённом направлении от бота.
     *
     * @param direction Направление, в сторону которого надо смотреть.
     * @return 2 - стена,
     * 3 - пусто,
     * 4 - органика,
     * 5 - бот
     */
    private int reactAtAhead(Orientation direction) {
        Cell cell;
        try {
            cell = Field.getAheadCell(row, column, direction);
        } catch (IndexOutOfBoundsException ex) {
            return 2; // стена
        }
        if (cell instanceof Bot bot) {
            if (bot.isAlive)
                return 5; // бот
            return 4; // органика
        }
        return 3; // пусто
    }

    /**
     * Метод убивает бота, уменьшает его энергию и удаляет из списка ботов {@link Field#bots}.
     */
    private void die() {
        isAlive = false;
        Field.getBots().remove(this);
        energy = energy / 2 + 1;
    }

    /**
     * Команда фотосинтеза.
     *
     * @return false - конец цикла.
     */
    private boolean photosynthesize() {
        energy += Field.getSun(row);
        genome.addToPointer(1);
        return false;
    }

    /**
     * Команда поедания в направлении, указанном в следующем гене.
     *
     * @return false - конец цикла.
     */
    private boolean eat() {
        int value = genome.getGene(1);
        Orientation direction = orientation.spin(value);
        int delta = reactAtAhead(direction);
        if (delta >= 4) {
            Bot food = (Bot) Field.getAheadCell(row, column, direction);
            Field.setEmptyCell(food);
            if (food.isAlive)
                food.die();
            energy += food.energy;
        }
        genome.addToPointer(delta);
        return false;
    }

    /**
     * Команда просмотра клетки в направлении, указанном в следующем гене.
     *
     * @return true - продолжение цикла.
     */
    private boolean look() {
        int value = genome.getGene(1);
        Orientation direction = orientation.spin(value);
        int delta = reactAtAhead(direction);
        genome.addToPointer(delta);
        return true;
    }

    /**
     * Команда поворота бота на угол, указанный в следующем гене.
     *
     * @param relatively Флаг того, поворачивать относительно направления бота или нет.
     * @return true - продолжение цикла.
     */
    private boolean spin(boolean relatively) {
        int value = genome.getGene(1);
        if (relatively)
            orientation = orientation.spin(value);
        else
            orientation = Orientation.getOrientation(value);
        genome.addToPointer(2);
        return true;
    }

    /**
     * Команда передвижения бота в относительном направлении, указанном в следующем гене.
     *
     * @return false - конец цикла.
     */
    private boolean move() {
        int value = genome.getGene(1);
        Orientation direction = orientation.spin(value);
        int delta = reactAtAhead(direction);
        if (delta == 3) {
            Field.setEmptyCell(this);
            Cell cell = Field.getAheadCell(row, column, direction);
            row = cell.row;
            column = cell.column;
            Field.setCell(this);
        }
        genome.addToPointer(2);
        return false;
    }

    /**
     * Команда по-умолчанию. Переводит указатель.
     *
     * @return true - продолжение цикла.
     */
    private boolean defaultCommand() {
        int delta = genome.getGene();
        genome.addToPointer(delta);
        return true;
    }

    /**
     * Метод отпочковывает нового бота рядом, если возможно. Иначе - бот умирает.
     */
    private void gemmate() {
        Cell[] cells = Field.getAdjacentCells(row, column);
        cells = Arrays.stream(cells).filter(cell -> !(cell instanceof Bot)).toArray(Cell[]::new);
        if (cells.length == 0) {
            die();
            return;
        }

        Random rand = new Random();
        Cell cell = cells[rand.nextInt(cells.length)];
        energy /= 2;
        Bot child = new Bot(cell.row, cell.column, energy, genome);
        Field.setCell(child);

        var bots = Field.getBots();
        int index = bots.indexOf(this) - 1;
        index = Math.max(index, 0);
        bots.add(index, child);
    }

    /**
     * Метод выполняет ход бота.
     */
    public void doAction() {
        energy -= ENERGY_DRAIN;
        if (energy >= MAX_ENERGY) {
            energy -= ENERGY_DRAIN;
            gemmate();
            return;
        }
        boolean notEnd = true;
        for (int i = 0; i < COMMAND_COUNT && notEnd; i++) {
            notEnd = switch (genome.getGene()) {
                case 0 -> photosynthesize();
                case 64 -> eat();
                case 65 -> look();
                case 66 -> spin(false);
                case 67 -> spin(true);
                case 68 -> move();
                default -> defaultCommand();
            };
        }
    }
}
