package Controllers;

import Service.Bot;
import Service.Field;
import Service.MyTask;
import Service.Orientation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Arrays;

public class EvolutionController {
    private static final int SCALE=20;
    @FXML
    private Canvas canvas;
    private GraphicsContext gc;

    public static int getHeight(){
        return Field.getHeight()*SCALE;
    }
    public static int getWidth(){
        return Field.getWidth()*SCALE+100;
    }
    @FXML
    protected void onInitClick() {
        canvas.setWidth(Field.getWidth()*SCALE);
        canvas.setHeight(Field.getHeight()*SCALE);
        gc=canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setFont(new Font(13));

        Field.initialize(10);
        draw();
    }


    private MyTask task;
    @FXML
    protected void onClearClick(){
        Field.initialize(0);
        draw();
    }
    @FXML
    protected void OnStartClick() {
        task=new MyTask();
        task.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                System.out.print(t1+": ");
                Field.iterate();
                draw();
            }
        });
        Thread thread=new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
    @FXML
    protected void onStopClick(){
        task.cancel();
    }

    private void draw(){
        gc=canvas.getGraphicsContext2D();
        var field=Field.getField();
        int height= Field.getHeight();
        int width=Field.getWidth();
        int count =0;
        for(int r=0;r<height;r++){
            for(int c=0;c<width;c++){
                if(field[r][c].getNeedToUpdate()) {
                    count++;
                    field[r][c].setNeedToUpdate(false);
                    if (field[r][c] instanceof Bot bot) {
                        gc.setFill(bot.getColor());
                        gc.fillRect(c * SCALE, r * SCALE, SCALE, SCALE);
                        drawOrientation(r, c, bot.getOrientation());
                        gc.setFill(Color.BLACK);
                        gc.strokeText("" + bot.getEnergy(), (c + 0.2) * SCALE, (r + 0.8) * SCALE);
                    }
                    else
                        gc.clearRect(c * SCALE, r * SCALE, SCALE, SCALE);
                }
            }
        }
        System.out.println(count);
    }
    private void drawOrientation(int row, int column, Orientation orientation) {
        double[] start = new double[]{
                (row + 0.5) * SCALE,
                (column + 0.5) * SCALE,
        };
        int[] c=orientation.getDirection();
        double[] end= Arrays.stream(c).mapToDouble(i->((double) i)/2*SCALE).toArray();
        end[0]+=start[0];
        end[1]+=start[1];
        gc.strokeLine(start[1],start[0],end[1],end[0]);
    }
}