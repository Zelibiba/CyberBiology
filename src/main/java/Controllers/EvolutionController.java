package Controllers;

import Service.Bot;
import Service.Field;
import Service.LifeTask;
import Service.Orientation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Arrays;

public class EvolutionController {
    private static int SCALE=8;


    @FXML
    protected ScrollPane scrollPane;
    @FXML
    protected Canvas canvas;
    @FXML
    protected Button clearButton;
    @FXML
    protected Button initButton;
    @FXML
    protected Button startButton;
    @FXML
    protected Button stopButton;
    @FXML
    protected Label delayLabel;
    @FXML
    protected Slider slider;
    @FXML
    Label iterationLabel;
    private GraphicsContext gc;


    public void initialize(){
        scrollPane.addEventFilter(ScrollEvent.ANY, event -> {
            SCALE+=(int)(event.getDeltaY()/40);
            canvas.setWidth(Field.getWidth()*SCALE);
            canvas.setHeight(Field.getHeight()*SCALE);
            gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
            draw(true);
            event.consume();
        });
        slider.valueProperty().addListener((observableValue, oldValue, newValue) ->{
            int delay=newValue.intValue();
            delayLabel.setText("Задержка: "+delay);
            if(task!=null)
                task.setDelay(delay);
        });
        delayLabel.setText("Задержка: "+(int)slider.getValue());


        canvas.setWidth(Field.getWidth()*SCALE);
        canvas.setHeight(Field.getHeight()*SCALE);
        gc=canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setFont(new Font(10));
//        scrollPane.setHmax(canvas.getWidth());
//        scrollPane.setVmax(canvas.getHeight());
        onInitClick();
    }

    @FXML
    protected void onInitClick() {
        Field.initialize(10);
        draw(true);
    }
    @FXML
    protected void onClearClick(){
        Field.initialize(0);
        draw(true);
    }

    private LifeTask task;
    int count=1;
    @FXML
    protected void OnStartClick() {
        clearButton.setDisable(true);
        initButton.setDisable(true);
        startButton.setDisable(true);
        stopButton.setDisable(false);

        task=new LifeTask((int)slider.getValue());
        task.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            iterationLabel.setText("Итерация: "+newValue);
            if(count!= newValue){
                slider.setValue(slider.getValue()+10*(newValue-count));
                count=newValue;
            }
            count++;
            draw(false);
        });
        Thread thread=new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
    @FXML
    protected void onStopClick(){
        clearButton.setDisable(false);
        initButton.setDisable(false);
        startButton.setDisable(false);
        stopButton.setDisable(true);
        task.cancel();
    }
    @FXML
    public void onScroll(ScrollEvent event) {
        if(event.getDeltaY()>0)
            slider.increment();
        else
            slider.decrement();
    }


    private void draw(boolean all) {
        var field = Field.getField();
        int height = Field.getHeight();
        int width = Field.getWidth();
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (all || field[r][c].getNeedToUpdate()) {
                    if (field[r][c] instanceof Bot bot) {
                        gc.setFill(bot.getColor());
                        gc.fillRect(c * SCALE, r * SCALE, SCALE, SCALE );
                    } else
                        gc.clearRect(c * SCALE+1, r * SCALE+1, SCALE-1, SCALE-1);
                }
            }
        }
        if (SCALE >= 20) {
            for (int r = 0; r < height; r++) {
                for (int c = 0; c < width; c++) {
                    if ((all || field[r][c].getNeedToUpdate()) && field[r][c] instanceof Bot bot) {
                        if (bot.getIsAlive())
                            drawOrientation(r, c, bot.getOrientation());
                        gc.setFill(Color.BLACK);
                        gc.strokeText("" + bot.getEnergy(), (c + 0.1) * SCALE, (r + 0.7) * SCALE);
                    }
                }
            }
        }
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (field[r][c].getNeedToUpdate())
                    field[r][c].setNeedToUpdate(false);
            }
        }
    }
    private void drawOrientation(int row, int column, Orientation orientation) {
        double[] start = new double[]{
                (row + 0.5) * SCALE,
                (column + 0.5) * SCALE,
        };
        int[] c=orientation.getDirection();
        double[] end= Arrays.stream(c).mapToDouble(i->((double) i)/2*SCALE).map(i->(i>=0)?((i>0)?i-3:0):i+3).toArray();

        end[0]+=start[0];
        end[1]+=start[1];
        gc.strokeLine(start[1],start[0],end[1],end[0]);
    }

    public void onCanvasClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount()==2)
            System.out.println(scrollPane.getHvalue()+", "+scrollPane.getVvalue());
    }
}