import java.awt.BasicStroke;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

public class Stroke implements Serializable{
    private ArrayList<int[]>lines;
    private int thickness;
    private Color color;
    Stroke(int thickness){
        this.lines = new ArrayList<int[]>();
        this.thickness = thickness;
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
    public int getThickness(){
        return thickness;
    }
}
