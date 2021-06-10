import javax.swing.border.AbstractBorder;
import java.awt.*;

public class CustomeBorder extends AbstractBorder {
    Color color;
    public CustomeBorder(Color color) {
        this.color = color;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y,
                            int width, int height) {
        // TODO Auto-generated method stubs
        super.paintBorder(c, g, x, y, width, height);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setStroke(new BasicStroke(12));
        g2d.setColor(color);
        g2d.drawRoundRect(x, y, width - 1, height - 1, 25, 25);
    }
}