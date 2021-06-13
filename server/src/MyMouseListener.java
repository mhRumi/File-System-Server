import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.MouseEvent;

public class MyMouseListener {
    public static java.awt.event.MouseListener getMyMouseListener() {
        return new java.awt.event.MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (SwingUtilities.isRightMouseButton(e)) {

                    int row = Main.m_tree.getClosestRowForLocation(e.getX(), e.getY());
                    Main.m_tree.setSelectionRow(row);
                    Main.popupmenu.show(e.getComponent(), e.getX(), e.getY());
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }
}
