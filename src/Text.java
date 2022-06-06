import java.awt.Font;

public class Text {
    private int x;
    private int y;
    private String textString;
    private Font font;
    Text(int x, int y, String textString, Font font){
        this.x = x;
        this.y = y;
        this.textString = textString;
        this.font = font;
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
}
