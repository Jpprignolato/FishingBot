package bot;

import javax.swing.*;
import java.awt.*;

public class BotUI extends JFrame {

    private final BotController botController;
    private final JLabel timerLabel = new JLabel("Próximo ciclo em: --:--");
    private final JLabel statusLabel;

    private Timer countdownTimer;
    private int countdownSeconds = 7 * 60; // 7 minutos em segundos
    private int secondsLeft = 0;

    private Point fishingRodPoint = null;
    private Point riverPoint = null;

    public BotUI(BotController botController) {
        this.botController = botController;

        setTitle("Pokemon Tibia Fishing Bot");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JButton btnSelectRod = new JButton("Selecionar Vara");
        JButton btnSelectRiver = new JButton("Selecionar Rio");
        JButton btnStartStop = new JButton("Iniciar Bot");

        statusLabel = new JLabel("Posições não definidas.");

        add(btnSelectRod);
        add(btnSelectRiver);
        add(btnStartStop);
        add(statusLabel);
        add(timerLabel);


        btnSelectRod.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Posicione o mouse sobre a VARA e clique OK.");
            fishingRodPoint = MouseInfo.getPointerInfo().getLocation();
            botController.setFishingRodPoint(fishingRodPoint);
            updateStatus();
        });

        btnSelectRiver.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Posicione o mouse sobre o RIO e clique OK.");
            riverPoint = MouseInfo.getPointerInfo().getLocation();
            botController.setRiverPoint(riverPoint);
            updateStatus();
        });

        btnStartStop.addActionListener(e -> {
            if (!botController.isRunning()) {
                botController.startBot();
                btnStartStop.setText("Parar Bot");
                statusLabel.setText("Bot rodando...");
            } else {
                botController.stopBot();
                btnStartStop.setText("Iniciar Bot");
                statusLabel.setText("Bot parado.");
                secondsLeft = 0;
                timerLabel.setText("Próximo ciclo em: --:--");
            }
        });

        // Atualiza o timer a cada segundo
        countdownTimer = new Timer(1000, e -> updateCountdown());
        countdownTimer.start();

        // Informa ao controller para reiniciar o timer a cada ciclo
        botController.setOnCycleStartCallback(() -> secondsLeft = 7 * 60);

        setVisible(true);
    }

    private void updateStatus() {
        String rod = (fishingRodPoint != null) ? "Vara: (" + fishingRodPoint.x + "," + fishingRodPoint.y + ")" : "Vara: não definida";
        String river = (riverPoint != null) ? "Rio: (" + riverPoint.x + "," + riverPoint.y + ")" : "Rio: não definido";
        statusLabel.setText(rod + " | " + river);
    }

    private void updateCountdown() {
        if (botController.isRunning()) {
            if (secondsLeft > 0) {
                secondsLeft--;
                int minutes = secondsLeft / 60;
                int seconds = secondsLeft % 60;
                timerLabel.setText(String.format("Próximo ciclo em: %02d:%02d", minutes, seconds));
            } else {
                timerLabel.setText("Executando ciclo...");
            }
        } else {
            timerLabel.setText("Bot parado.");
        }
    }

    public void restartCountdownTimer() {
        if (countdownTimer != null) {
            countdownTimer.stop();
        }

        countdownSeconds = 7 * 60; // reinicia para 7 minutos

        countdownTimer = new Timer(1000, e -> {
            countdownSeconds--;
            int minutes = countdownSeconds / 60;
            int seconds = countdownSeconds % 60;
            timerLabel.setText(String.format("Próximo ciclo em: %02d:%02d", minutes, seconds));

            if (countdownSeconds <= 0) {
                countdownTimer.stop();
            }
        });
        countdownTimer.start();
    }
}
