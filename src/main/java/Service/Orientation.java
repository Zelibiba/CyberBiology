package Service;

import java.util.Random;

/**
 * Перечисление ориентации бота.
 */
public enum Orientation {
    UP(0), UPRIGHT(1), RIGHT(2), DOWNRIGHT(3),
    DOWN(4), DOWNLEFT(5), LEFT(6), UPLEFT(7);

    private static final Orientation[] MASSIVE = Orientation.values();
    private static final int SIZE = MASSIVE.length;
    private static final int[][] DIRECTIONS = new int[][]{
            new int[]{-1, 0},
            new int[]{-1, 1},
            new int[]{0, 1},
            new int[]{1, 1},
            new int[]{1, 0},
            new int[]{1, -1},
            new int[]{0, -1},
            new int[]{-1, -1},
    };

    Orientation(int value) {
        this.value = value;
    }

    private int value;

    /**
     * Метод возвращает вектор, соответсвующий данному направлению.
     */
    public int[] getDirection() {
        return DIRECTIONS[value];
    }

    /**
     * Метод поворачивает ориентацию по часовой стрелке.
     *
     * @param angle Индекс угла поворота.
     * @return Поввёрнутая ориентация.
     */
    public Orientation spin(int angle) {
        return MASSIVE[(value + angle) % SIZE];
    }

    /**
     * Возвращает случайную ориентацию.
     */
    public static Orientation randomize() {
        Random rand = new Random();
        return MASSIVE[rand.nextInt(SIZE)];
    }

    /**
     * Возвращает ориентацию, соответствующую углу.
     */
    public static Orientation getOrientation(int angle) {
        return MASSIVE[angle % SIZE];
    }
}
