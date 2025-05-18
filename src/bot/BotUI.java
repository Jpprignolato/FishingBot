import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.KeyboardFocusManager;
import java.awt.KeyEventDispatcher;


public class BotUI extends JFrame {

    private final BotController botController;
    private final JLabel timerLabel = new JLabel("Próximo ciclo em: --:--");
    private final JLabel statusLabel;

    private Timer countdownTimer;
    private int countdownSeconds = 7 * 60; // 7 minutos em segundos
    private int secondsLeft = 0;

    private Point fishingRodPoint = null;
    private Point riverPoint = null;

    private final Point[] lootPoints = new Point[4];


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

        JButton btnStartLoot = new JButton("Iniciar Loot");
        JButton btnLoot1 = new JButton("Area do Loot 1");
        JButton btnLoot2 = new JButton("Area do Loot 2");
        JButton btnLoot3 = new JButton("Area do Loot 3");
        JButton btnLoot4 = new JButton("Area do Loot 4");

        add(btnLoot1);
        add(btnLoot2);
        add(btnLoot3);
        add(btnLoot4);
        add(btnStartLoot);

        statusLabel = new JLabel("Posições não definidas.");

        add(btnSelectRod);
        add(btnSelectRiver);
        add(btnStartStop);
        add(statusLabel);
        add(timerLabel);

        btnStartLoot.addActionListener(e -> {
            botController.startLootLoop();
            JOptionPane.showMessageDialog(this, "Loop de Loot iniciado.");
        });

        btnLoot1.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Posicione o mouse na AREA DE LOOT 1 e clique OK.");
            botController.setLootPoint(0, MouseInfo.getPointerInfo().getLocation());
        });

        btnLoot2.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Posicione o mouse na AREA DE LOOT 2 e clique OK.");
            botController.setLootPoint(1, MouseInfo.getPointerInfo().getLocation());
        });

        btnLoot3.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Posicione o mouse na AREA DE LOOT 3 e clique OK.");
            botController.setLootPoint(2, MouseInfo.getPointerInfo().getLocation());
        });

        btnLoot4.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Posicione o mouse na AREA DE LOOT 4 e clique OK.");
            botController.setLootPoint(3, MouseInfo.getPointerInfo().getLocation());
        });

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

        // Atalho global para ESC fechar o bot, mesmo minimizado
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    SwingUtilities.invokeLater(() -> {
                        if (botController.isRunning()) {
                            botController.stopBot();
                        }
                        dispose();
                    });
                    return true; // evento consumido, não propaga
                }
                return false; // deixa outros eventos seguirem
            }
        });
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
