package ru.eltech.client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;
import java.util.List;
import ru.eltech.api.editClasses.PlayerEdit;
import ru.eltech.api.entity.PlayerRole;

import static ru.eltech.client.utils.ClientUtils.isBlank;
import static ru.eltech.client.utils.ClientUtils.isInteger;
import static ru.eltech.client.utils.ClientUtils.toStringSafe;

public class EditPlayerDialog extends JDialog {
    private static final String TITLEADD = "Добавление игрока";
    private static final String TITLEEDIT = "Редактирование игрока";

    private final JComboBox playerRoleName = new JComboBox();
    private final JTextField nameField = new JTextField();
    private final JTextField surnameField = new JTextField();
    private final JTextField ageField = new JTextField();
    private final JTextField countGamesField = new JTextField();
    private final JTextField countPointsField = new JTextField();
    private final JTextField effectivenessField = new JTextField();
    private final JTextField playerNumberField = new JTextField();

    private final List<PlayerRole> playerRoleList;
    private final PlayerEdit prevData;
    private final Consumer<PlayerEdit> newPlayerNameConsumer;

    public EditPlayerDialog(List<PlayerRole> playerRoleList, Consumer<PlayerEdit> newPlayerNameConsumer) {
        this(playerRoleList, null, newPlayerNameConsumer);
    }

    public EditPlayerDialog(List<PlayerRole> playerRoleList, PlayerEdit playerEdit, Consumer<PlayerEdit> newPlayerNameConsumer) {
        this.newPlayerNameConsumer = newPlayerNameConsumer;
        this.playerRoleList = playerRoleList;
        prevData = playerEdit;

        if (prevData != null) {setTitle(TITLEEDIT);}
        else setTitle(TITLEADD);

        for (int i = 0; i < playerRoleList.size(); i++) {
            playerRoleName.addItem(playerRoleList.get(i).getName());
        }

        JPanel mainPanel = new JPanel(new GridLayout(9, 1));

        JPanel namePanel = new JPanel(new BorderLayout());
        JPanel surnamePanel = new JPanel(new BorderLayout());
        JPanel agePanel = new JPanel(new BorderLayout());
        JPanel rolePanel = new JPanel(new BorderLayout());
        JPanel countGamesPanel = new JPanel(new BorderLayout());
        JPanel countPointsPanel = new JPanel(new BorderLayout());
        JPanel effectivenessPanel = new JPanel(new BorderLayout());
        JPanel playerNumberPanel = new JPanel(new BorderLayout());

        namePanel.add(new JLabel("Имя:"), BorderLayout.WEST);
        surnamePanel.add(new JLabel("Фамилия:"), BorderLayout.WEST);
        agePanel.add(new JLabel("Возраст:"), BorderLayout.WEST);
        rolePanel.add(new JLabel("Амплуа:"), BorderLayout.WEST);
        countGamesPanel.add(new JLabel("Кол-во игр:"), BorderLayout.WEST);
        countPointsPanel.add(new JLabel("Кол-во очков:"), BorderLayout.WEST);
        effectivenessPanel.add(new JLabel("Эффективность:"), BorderLayout.WEST);
        playerNumberPanel.add(new JLabel("Номер игрока:"), BorderLayout.WEST);

        if (prevData != null) {
            nameField.setText(prevData.getName());
            surnameField.setText(prevData.getSurname());
            ageField.setText(toStringSafe(prevData.getAge()));
            playerRoleName.setSelectedItem(prevData.getPlayerRole().getName());
            countGamesField.setText(toStringSafe(prevData.getCountGames()));
            countPointsField.setText(toStringSafe(prevData.getCountPoints()));
            effectivenessField.setText(toStringSafe(prevData.getEffectiveness()));
            playerNumberField.setText(toStringSafe(prevData.getPlayerNumber()));
        }

        namePanel.add(nameField, BorderLayout.CENTER);
        surnamePanel.add(surnameField, BorderLayout.CENTER);
        agePanel.add(ageField, BorderLayout.CENTER);
        rolePanel.add(playerRoleName, BorderLayout.CENTER);
        countGamesPanel.add(countGamesField, BorderLayout.CENTER);
        countPointsPanel.add(countPointsField, BorderLayout.CENTER);
        effectivenessPanel.add(effectivenessField, BorderLayout.CENTER);
        playerNumberPanel.add(playerNumberField, BorderLayout.CENTER);

        mainPanel.add(namePanel);
        mainPanel.add(surnamePanel);
        mainPanel.add(agePanel);
        mainPanel.add(rolePanel);
        mainPanel.add(countGamesPanel);
        mainPanel.add(countPointsPanel);
        mainPanel.add(effectivenessPanel);
        mainPanel.add(playerNumberPanel);
        mainPanel.add(new JButton(new EditPlayerDialog.SaveAction()));

        getContentPane().add(mainPanel);
        setSize(400, 270);
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private class SaveAction extends AbstractAction {
        SaveAction() {
            putValue(NAME, prevData != null ? "Изменить" : "Добавить");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isBlank(nameField.getText())
                    || isBlank(surnameField.getText())
                    || isBlank(ageField.getText())
                    || isBlank(countGamesField.getText())
                    || isBlank(countPointsField.getText())
                    || isBlank(effectivenessField.getText())
                    || isBlank(playerNumberField.getText())
                    || playerRoleName.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(
                        EditPlayerDialog.this,
                        "Не все данные введены!",
                        "Внимание",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!isInteger(ageField.getText()) ||
                    !isInteger(countGamesField.getText()) ||
                    !isInteger(countPointsField.getText()) ||
                    !isInteger(effectivenessField.getText()) ||
                    !isInteger(playerNumberField.getText())) {
                JOptionPane.showMessageDialog(
                        EditPlayerDialog.this,
                        "Введены некорректные данные!",
                        "Внимание",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            PlayerRole playerRole = new PlayerRole();
            for (int i = 0; i < playerRoleList.size(); i++) {
                if (playerRoleList.get(i).getName().equals(playerRoleName.getSelectedItem())) {
                    playerRole = playerRoleList.get(i);
                    break;
                }
            }

            PlayerEdit playerEdit = new PlayerEdit(
                    nameField.getText(),
                    surnameField.getText(),
                    Integer.parseInt(ageField.getText()),
                    playerRole,
                    Integer.parseInt(countGamesField.getText()),
                    Integer.parseInt(countPointsField.getText()),
                    Integer.parseInt(effectivenessField.getText()),
                    Integer.parseInt(playerNumberField.getText())
            );
            newPlayerNameConsumer.accept(playerEdit);
            dispose();
        }
    }
}
