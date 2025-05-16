package bot;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.concurrent.*;

public class BotController {

    private final Robot robot;
    private ScheduledExecutorService scheduler;

    private Point fishingRodPoint = null;
    private Point riverPoint = null;

    private boolean running = false;

    private Runnable onCycleStartCallback; // Novo callback

    public BotController() throws AWTException {
        this.robot = new Robot();
    }

    public void setFishingRodPoint(Point p) {
        this.fishingRodPoint = p;
    }

    public void setRiverPoint(Point p) {
        this.riverPoint = p;
    }

    public boolean isRunning() {
        return running;
    }

    // Permite definir um callback a ser chamado sempre que um novo ciclo começar
    public void setOnCycleStartCallback(Runnable callback) {
        this.onCycleStartCallback = callback;
    }

    public void startBot() {
        if (fishingRodPoint == null || riverPoint == null) {
            System.out.println("Defina as posições da vara e do rio antes de iniciar.");
            return;
        }
        if (running) {
            System.out.println("Bot já está rodando.");
            return;
        }
        running = true;
        boolean[] firstCycleCompleted = {false}; // usando array para mutabilidade dentro do lambda

        scheduler = Executors.newSingleThreadScheduledExecutor();

        Runnable fishingTask = () -> {
            try {
                System.out.println("Iniciando ciclo de pesca...");

                // Se for o primeiro ciclo (imediato ao iniciar o bot), faz uma vez só
                if (!firstCycleCompleted[0]) {
                    moveAndClick(fishingRodPoint, InputEvent.BUTTON3_DOWN_MASK); // clique direito na vara
                    Thread.sleep(2000);
                    moveAndClick(riverPoint, InputEvent.BUTTON1_DOWN_MASK);     // clique esquerdo no rio
                    firstCycleCompleted[0] = true;
                } else {
                    // Executa dois ciclos
                    for (int i = 0; i < 2; i++) {
                        moveAndClick(fishingRodPoint, InputEvent.BUTTON3_DOWN_MASK);
                        Thread.sleep(2000);
                        moveAndClick(riverPoint, InputEvent.BUTTON1_DOWN_MASK);
                        if (i == 0) Thread.sleep(1000); // espera 1 segundo antes do segundo clique
                    }
                }

                System.out.println("Ciclo concluído. Aguardando próximo ciclo...");

                // Atualiza contador da UI
                if (onCycleStartCallback != null) {
                    onCycleStartCallback.run();
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        // Executa imediatamente a primeira vez
        fishingTask.run();

        // Agendamento a cada 7 minutos (apenas a partir daqui fará dois ciclos)
        scheduler.scheduleAtFixedRate(fishingTask, 7, 7, TimeUnit.MINUTES);
    }



    public void stopBot() {
        if (!running) return;
        running = false;
        if (scheduler != null) {
            scheduler.shutdownNow();
            System.out.println("Bot parado.");
        }
    }

    private void moveAndClick(Point p, int buttonMask) throws InterruptedException {
        robot.mouseMove(p.x, p.y);
        Thread.sleep(100);
        robot.mousePress(buttonMask);
        Thread.sleep(100);
        robot.mouseRelease(buttonMask);
        Thread.sleep(100);
    }
}
