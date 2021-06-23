import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ProgressBar extends JFrame {
    int length = 0;

    JProgressBar current = new JProgressBar(0, 100);
    int num = 0;

    public ProgressBar(int length) {
        this.length = length;
        JPanel pane = new JPanel();
        current.setValue(0);
        current.setStringPainted(true);
        pane.add(current);
        setContentPane(pane);

    }

    //to iterate so that it looks like progress bar  
    public void iterate() {
        SwingWorker worker = new SwingWorker<Object, Object>() {
            @Override
            protected Object doInBackground() throws Exception {
                while (num < length) {
//                    current.setValue(num);
//                    try {
//                        Thread.sleep(125);
//                    } catch (InterruptedException e) {
//                    }
                    num += 95;
                    int p = Math.round(((float)Math.min(num, 2000) / 2000f) * 100f);
                    setProgress(p);
                }
                return null;
            }
        };
        worker.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String name = evt.getPropertyName();
                if ("progress".equals(name)) {
                    SwingWorker worker = (SwingWorker) evt.getSource();
                    current.setValue(worker.getProgress());
                }
            }
        });
        worker.execute();
    }

}