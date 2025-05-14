package bot;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CoordinatePicker {

    public interface CoordinateListener {
        void onCoordinateSelected(int x, int y);
    }

    public static void pick(CoordinateListener listener) {
        JFrame frame = new JFrame();
        frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setOpacity(0.2f);
        frame.setAlwaysOnTop(true);

        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                listener.onCoordinateSelected(x, y);
                frame.dispose();
            }
        });

        frame.setVisible(true);
    }
}
