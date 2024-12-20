package ru.eltech.client.gui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.eltech.api.editClasses.PlayerFilter;
import ru.eltech.api.editClasses.PlayerEdit;
import ru.eltech.api.entity.Player;
import ru.eltech.api.entity.PlayerRole;
import ru.eltech.api.servcie.HockeyServerService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

import static ru.eltech.client.utils.ClientUtils.isInteger;

@Component
public class PlayerPanel extends JPanel {
    private final PlayerTableModel tableModel = new PlayerTableModel();
    private final JTable table = new JTable(tableModel);

    private final JTextField filterNameField = new JTextField();
    private final JTextField filterSurnameField = new JTextField();
    private final JTextField filterAgeField = new JTextField();
    private final JTextField filterRoleField = new JTextField();
    private final JTextField filterGamesField = new JTextField();
    private final JTextField filterPointsField = new JTextField();
    private final JTextField filterEffectField = new JTextField();
    private final JTextField filterNumberField = new JTextField();



    private List<PlayerRole> playerRoles;

    @Autowired
    private TeamPanel teamPanel;
    @Autowired
    private HockeyServerService hockeyServerService;

    private JButton addButton;
    private JButton editButton;
    private JButton removeButton;

    public PlayerPanel(HockeyServerService hockeyServerService) {
        this.hockeyServerService = hockeyServerService;
        createGUI();
    }

    private void createGUI() {
        setLayout(new BorderLayout());

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(createPlayerToolBar(), BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        table.removeColumn(table.getColumnModel().getColumn(0));
        table.removeColumn(table.getColumnModel().getColumn(2));

        refreshTableData();
    }

    private JToolBar createPlayerToolBar() {
        JToolBar toolBar = new JToolBar(SwingConstants.HORIZONTAL);

        toolBar.setFloatable(false);
        addButton = new JButton(new AddPlayerAction());
        addButton.setEnabled(false);
        toolBar.add(addButton);
        editButton = new JButton(new EditPlayerAction());
        editButton.setEnabled(false);
        toolBar.add(editButton);
        removeButton = new JButton(new RemovePlayerAction());
        removeButton.setEnabled(false);
        toolBar.add(removeButton);

        toolBar.add(new JLabel("   Фамилия: "));
        toolBar.add(filterNameField);
        filterNameField.setPreferredSize(new Dimension(100, 25));
        toolBar.add(new JLabel("   Имя: "));
        toolBar.add(filterSurnameField);
        filterSurnameField.setPreferredSize(new Dimension(100, 25));
        toolBar.add(new JLabel("   Амплуа: "));
        toolBar.add(filterRoleField);
        filterRoleField.setPreferredSize(new Dimension(100, 25));
        toolBar.add(new JLabel("   Возраст: "));
        toolBar.add(filterAgeField);
        filterAgeField.setPreferredSize(new Dimension(100, 25));
        toolBar.add(new JLabel("   Кол-во игр: "));
        toolBar.add(filterGamesField);
        filterGamesField.setPreferredSize(new Dimension(100, 25));
        toolBar.add(new JLabel("   Кол-во очков: "));
        toolBar.add(filterPointsField);
        filterPointsField.setPreferredSize(new Dimension(100, 25));
        toolBar.add(new JLabel("   Эффективность: "));
        toolBar.add(filterEffectField);
        filterEffectField.setPreferredSize(new Dimension(100, 25));
        toolBar.add(new JLabel("   Номер игрока: "));
        toolBar.add(filterNumberField);
        filterNumberField.setPreferredSize(new Dimension(100, 25));

        toolBar.add(new JButton(new PlayerPanel.FilterTeamAction()));

        return toolBar;
    }

    public void refreshTableData() {
        boolean isLoggedIn = hockeyServerService.isLoggedIn();
        addButton.setEnabled(isLoggedIn);
        editButton.setEnabled(isLoggedIn);
        removeButton.setEnabled(isLoggedIn);
        PlayerFilter playerFilter = new PlayerFilter(
                filterNameField.getText(),
                filterSurnameField.getText(),
                filterAgeField.getText(),
                filterRoleField.getText(),
                filterGamesField.getText(),
                filterPointsField.getText(),
                filterEffectField.getText(),
                filterNumberField.getText());
        List<Player> allPlayers = hockeyServerService.loadAllPlayers(playerFilter);
        tableModel.initWith(allPlayers);
        table.revalidate();
        table.repaint();
    }

    private class AddPlayerAction extends AbstractAction {
        AddPlayerAction() {
            putValue(SHORT_DESCRIPTION, "Добавить игрока");
            putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/icons/action_add.gif")));
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            playerRoles = hockeyServerService.findAllPlayerRole();
            EditPlayerDialog editPlayerDialog = new EditPlayerDialog(playerRoles, newPlayer -> {
                hockeyServerService.savePlayer(newPlayer);
                refreshTableData();
                teamPanel.refreshTableData();
            });
            editPlayerDialog.setLocationRelativeTo(PlayerPanel.this);
            editPlayerDialog.setVisible(true);
        }
    }

    private class EditPlayerAction extends AbstractAction {
        EditPlayerAction() {
            putValue(SHORT_DESCRIPTION, "Изменить игрока");
            putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/icons/action_edit.gif")));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRowIndex = table.getSelectedRow();
            int rowCount = tableModel.getRowCount();
            if (selectedRowIndex == -1 || selectedRowIndex >= rowCount) {
                JOptionPane.showMessageDialog(
                        PlayerPanel.this,
                        "Для редaктирования выберите игрока!",
                        "Внимание",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Integer selectedPlayerId = (Integer) tableModel.getValueAt(selectedRowIndex, 0);
            String selectedPlayerName = (String) tableModel.getValueAt(selectedRowIndex, 1);
            String selectedPlayerSurName = (String) tableModel.getValueAt(selectedRowIndex, 2);
            Integer selectedPlayerRoleId = (Integer) tableModel.getValueAt(selectedRowIndex, 3);
            String selectedPlayerRoleName = (String) tableModel.getValueAt(selectedRowIndex, 4);
            PlayerRole playerRole = new PlayerRole();
            playerRole.setId(selectedPlayerRoleId);
            playerRole.setName(selectedPlayerRoleName);
            Integer selectedPlayerAge = (Integer) tableModel.getValueAt(selectedRowIndex, 5);
            Integer selectedPlayerCountGames = (Integer) tableModel.getValueAt(selectedRowIndex, 6);
            Integer selectedPlayerIdCountPoints = (Integer) tableModel.getValueAt(selectedRowIndex, 7);
            Integer selectedPlayerEffectiveness = (Integer) tableModel.getValueAt(selectedRowIndex, 8);
            Integer selectedPlayerNumber = (Integer) tableModel.getValueAt(selectedRowIndex, 9);

            PlayerEdit playerEdit = new PlayerEdit(
                    selectedPlayerName,
                    selectedPlayerSurName,
                    selectedPlayerAge,
                    playerRole,
                    selectedPlayerCountGames,
                    selectedPlayerIdCountPoints,
                    selectedPlayerEffectiveness,
                    selectedPlayerNumber
            );
            playerRoles = hockeyServerService.findAllPlayerRole();

            EditPlayerDialog editPlayerDialog = new EditPlayerDialog(playerRoles, playerEdit, changedPlayer -> {
                hockeyServerService.updatePlayer(selectedPlayerId, changedPlayer);
                refreshTableData();
                teamPanel.refreshTableData();
            });
            editPlayerDialog.setLocationRelativeTo(PlayerPanel.this);
            editPlayerDialog.setVisible(true);
        }
    }

    private class RemovePlayerAction extends AbstractAction {
        RemovePlayerAction() {
            putValue(SHORT_DESCRIPTION, "Удалить игрока");
            putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/icons/action_remove.gif")));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRowIndex = table.getSelectedRow();
            int rowCount = tableModel.getRowCount();
            if (selectedRowIndex == -1 || selectedRowIndex >= rowCount) {
                JOptionPane.showMessageDialog(
                        PlayerPanel.this,
                        "Для удаления выберите игрока!",
                        "Внимание",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Integer selectedPlayerId = (Integer) tableModel.getValueAt(selectedRowIndex, 0);
            String selectedPlayerName = (String) tableModel.getValueAt(selectedRowIndex, 1);

            if (JOptionPane.showConfirmDialog(
                    PlayerPanel.this,
                    "Удалить игрока '" + selectedPlayerName + "'?",
                    "Вопрос",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                hockeyServerService.deletePlayerById(selectedPlayerId);
                refreshTableData();
                teamPanel.refreshTableData();
            }
        }
    }

    private class FilterTeamAction extends AbstractAction {
        FilterTeamAction() {
            putValue(SHORT_DESCRIPTION, "Фильтровать команды");
            putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/icons/action_refresh.gif")));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isInteger(filterAgeField.getText()) &&
                isInteger(filterGamesField.getText()) &&
                isInteger(filterPointsField.getText()) &&
                isInteger(filterEffectField.getText()) &&
                isInteger(filterNumberField.getText())) {
                refreshTableData();
            }
            else {
                JOptionPane.showMessageDialog(
                        PlayerPanel.this,
                        "Введены некорректные данные!",
                        "Внимание",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
