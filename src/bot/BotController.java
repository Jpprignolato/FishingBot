package bot;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class BotController {

    private static boolean attacking = false;
    private static Thread attackThread;

    // Clica com botão esquerdo na vara (ação simples)
    public static void startFishing(int x, int y) {
        try {
            Robot robot = new Robot();

            robot.mouseMove(x, y);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            System.out.println("Clique feito na vara em X=" + x + ", Y=" + y);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    // Novo método: clique direito na vara e esquerdo no rio
    public static void useFishingRod(int rodX, int rodY, int riverX, int riverY) {
        try {
            Robot robot = new Robot();

            // Clique direito na vara
            robot.mouseMove(rodX, rodY);
            Thread.sleep(100);
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
            System.out.println("Clique direito na vara em X=" + rodX + ", Y=" + rodY);

            Thread.sleep(500);

            // Clique esquerdo no rio
            robot.mouseMove(riverX, riverY);
            Thread.sleep(100);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            System.out.println("Clique esquerdo no rio em X=" + riverX + ", Y=" + riverY);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Ativa/desativa os ataques automáticos (F1 a F12)
    public static void toggleAttacks() {
        if (attacking) {
            attacking = false;
            System.out.println("Ataques desativados.");
        } else {
            attacking = true;
            System.out.println("Ataques ativados.");
            attackThread = new Thread(() -> {
                try {
                    Robot robot = new Robot();
                    while (attacking) {
                        for (int key = KeyEvent.VK_F1; key <= KeyEvent.VK_F12; key++) {
                            robot.keyPress(key);
                            robot.keyRelease(key);
                            Thread.sleep(100);
                        }
                        Thread.sleep(1000); // Espera entre ciclos
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            attackThread.start();
        }
    }
}
