import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;

public class Stroke {
    private ArrayList<int[]>lines;
    private BasicStroke strokeType;
    private Color color;
    Stroke(BasicStroke strokeType){
        this.lines = new ArrayList<int[]>();
        this.strokeType = strokeType;
    }
    public void addLine(int startX, int startY, int endX, int endY){
        int[]x = {startX, startY, endX, endY};
        lines.add(x);
    }
    public ArrayList<int[]> getLines(){
        return lines;
    }
    public void setColor(Color newColor){
        color = newColor;
    }
    public Color getColor(){
        return color;
    }
    public BasicStroke getStrokeType(){
        return strokeType;
    }
}
