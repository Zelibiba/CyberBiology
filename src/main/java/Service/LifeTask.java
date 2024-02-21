package Service;

import javafx.concurrent.Task;

public class LifeTask extends Task<Integer> {
    public LifeTask(int delay){
        this.delay=delay;
    }

    private int delay;
    public void setDelay(int value){
        delay=value;
    }
    @Override
    public Integer call(){
        try{
            while (!isCancelled()) {
                Thread.sleep(delay);
                updateValue(Field.iterate());
            }
        }catch (InterruptedException ignored){}
        return 0;
    }
}
