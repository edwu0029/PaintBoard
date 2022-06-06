import java.awt.Font;
import java.awt.Color;

public class Text {
    private int x;
    private int y;
    private String textString;
    private Font font;
    private Color color;
    Text(int x, int y, String textString, Font font, Color color){
        this.x = x;
        this.y = y;
        this.textString = textString;
        this.font = font;
        this.color = color;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public String getTextString(){
        return textString;
    }
    public Font getFont(){
        return font;
    }
    public Color getColor(){
        return color;
    }
}
