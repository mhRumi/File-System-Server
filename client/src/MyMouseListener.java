import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyMouseListener {
    public static MouseListener getMyMouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Get the source of the click which is the JPanel.
                JPanel jPanel = (JPanel) e.getSource();
                Client.deleteJpanel = jPanel;
                // Get the ID of the file.
                int fileId = Integer.parseInt(jPanel.getName());
                // Loop through the file storage and see which file is the selected one.
                for (MyFile myFile : Client.myFiles) {
                    if (myFile.getId() == fileId) {
                        Client.selectedFileId = fileId;
                    }
                }

                Client.popupmenu.show(e.getComponent() , e.getX(), e.getY());
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                JPanel jPanel = (JPanel) e.getSource();
                jPanel.setBackground(new Color(51,150,255 ));
                Client.backgroundColor = jPanel.getBackground();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                JPanel jPanel = (JPanel) e.getSource();
                jPanel.setBackground(Color.white);
            }
        };
    }
}
