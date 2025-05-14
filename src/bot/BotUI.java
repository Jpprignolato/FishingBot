package bot;

import javax.swing.*;
import java.awt.*;

public class BotUI {

    private static int fishingX;
    private static int fishingY;
    private static int riverX;
    private static int riverY;
    private static boolean attacksActive = false;

    public static void launch() {
        JFrame frame = new JFrame("Fishing Bot - OTPokemon");

        JButton startButton = new JButton("Iniciar Bot de Pesca");
        JButton selectRodButton = new JButton("Selecionar Coordenada da Vara");
        JButton selectRiverButton = new JButton("Selecionar Coordenada do Rio");
        JButton toggleAttackButton = new JButton("Ativar Ataques");

        JLabel statusLabel = new JLabel("Status: Ataques Desativados");
        statusLabel.setForeground(Color.RED);

        startButton.addActionListener(e -> {
            if (fishingX == 0 || riverX == 0) {
                JOptionPane.showMessageDialog(frame, "Selecione as coordenadas da vara e do rio primeiro.");
                return;
            }

            BotController.useFishingRod(fishingX, fishingY, riverX, riverY);
        });

        selectRodButton.addActionListener(e -> {
            CoordinatePicker.pick((x, y) -> {
                fishingX = x;
                fishingY = y;
                System.out.println("Coordenada da vara salva: X=" + fishingX + " Y=" + fishingY);
            });
        });

        selectRiverButton.addActionListener(e -> {
            CoordinatePicker.pick((x, y) -> {
                riverX = x;
                riverY = y;
                System.out.println("Coordenada do rio salva: X=" + riverX + " Y=" + riverY);
            });
        });

        toggleAttackButton.addActionListener(e -> {
            attacksActive = !attacksActive;
            BotController.toggleAttacks();

            if (attacksActive) {
                toggleAttackButton.setText("Desativar Ataques");
                statusLabel.setText("Status: Ataques Ativos");
                statusLabel.setForeground(Color.GREEN.darker());
            } else {
                toggleAttackButton.setText("Ativar Ataques");
                statusLabel.setText("Status: Ataques Desativados");
                statusLabel.setForeground(Color.RED);
            }
        });

        frame.setLayout(new FlowLayout());
        frame.add(startButton);
        frame.add(selectRodButton);
        frame.add(selectRiverButton);
        frame.add(toggleAttackButton);
        frame.add(statusLabel);

        frame.setSize(450, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
