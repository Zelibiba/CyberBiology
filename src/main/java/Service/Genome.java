package Service;

import java.util.Random;

/**
 * Класс генома бота.
 */
public class Genome {

    /**
     * Размер генома.
     */
    private static final int SIZE = 64;
    /**
     * Максимальное значение генома.
     */
    private static final int MAX_GENE = 68;
    /**
     * Шанс мутирования при копировании генома.
     */
    private static final int MUTATION_CHANCE = 50;

    public Genome() {
        Random rand=new Random();
        massive = new byte[SIZE];
        for (int i = 0; i < SIZE; i++)
            massive[i] = (byte)rand.nextInt(MAX_GENE+1);

        for(int i=0;i<massive.length/4;i++)
            massive[rand.nextInt(SIZE)]=0;
    }

    /**
     * Копирует геном с шансом мутации.
     * @param genome Источник генома.
     * @param mutation Включает шанс мутации.
     */
    public Genome(Genome genome, boolean mutation) {

        massive = genome.massive.clone();
        if (mutation) {
            Random rand = new Random();
            if (rand.nextInt(100) < MUTATION_CHANCE)
                massive[rand.nextInt(SIZE)] = (byte) rand.nextInt(MAX_GENE + 1);
        }
    }

    private final byte[] massive;
    private int pointer = 0;

    /**
     * Добавляет к указателю на ген число.
     */
    public void addToPointer(int value) {
        pointer = (pointer + value) % SIZE;
    }

    /**
     * Возвращает ген, на который указывает указатель.
     */
    public int getGene() {
        return massive[pointer];
    }

    /**
     * Возвращает ген, расположенный на расстоянии delta вперёд от текущего положения указателя.
     * @param delta Расттояние от гена до указателя.
     */
    public int getGene(int delta) {
        return massive[(pointer + delta) % SIZE];
    }
}
