package Service;

import javafx.concurrent.Task;

public class MyTask extends Task<Integer> {
    @Override
    public Integer call(){
        int count=0;
        try{
            while (!isCancelled()){
                Thread.sleep(200);
                count++;
                updateValue(count);
            }
        }catch (InterruptedException ignored){}
        return count;
    }
}
