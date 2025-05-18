import java.awt.*;

public class FishingBot {
    public static void main(String[] args) {
        try {
            BotController controller = new BotController();
            BotUI ui = new BotUI(controller); // declarei 'ui' aqui

            controller.setOnCycleStartCallback(ui::restartCountdownTimer); // agora funciona corretamente

        } catch (AWTException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
