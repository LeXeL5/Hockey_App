package ru.eltech.client.gui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.eltech.api.entity.League;
import ru.eltech.api.servcie.HockeyServerService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

@Component
public class LeaguePanel extends JPanel {

    private final LeagueTableModel tableModel = new LeagueTableModel();
    private final JTable table = new JTable(tableModel);
    private final JTextField filterField = new JTextField();

    @Autowired
    private TeamPanel teamPanel;
    @Autowired
    private HockeyServerService hockeyServerService;

    private JButton addButton;
    private JButton editButton;
    private JButton removeButton;

    public LeaguePanel(HockeyServerService hockeyServerService) {
        this.hockeyServerService = hockeyServerService;
        createGUI();
    }

    private void createGUI() {
        setLayout(new BorderLayout());

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(createLeagueToolBar(), BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        table.removeColumn(table.getColumnModel().getColumn(0));

        refreshTableData();
    }

    private JToolBar createLeagueToolBar() {
        JToolBar toolBar = new JToolBar(SwingConstants.HORIZONTAL);

        toolBar.setFloatable(false);
        toolBar.setFloatable(false);
        addButton = new JButton(new LeaguePanel.AddLeagueAction());
        addButton.setEnabled(false);
        toolBar.add(addButton);
        editButton = new JButton(new LeaguePanel.EditLeagueAction());
        editButton.setEnabled(false);
        toolBar.add(editButton);
        removeButton = new JButton(new LeaguePanel.RemoveLeagueAction());
        removeButton.setEnabled(false);
        toolBar.add(removeButton);

        toolBar.add(new JLabel("   Название: "));
        toolBar.add(filterField);
        filterField.setPreferredSize(new Dimension(100, 25));

        toolBar.add(new JButton(new LeaguePanel.FilterTeamAction()));

        return toolBar;
    }

    public void refreshTableData() {
        boolean isLoggedIn = hockeyServerService.isLoggedIn();
        addButton.setEnabled(isLoggedIn);
        editButton.setEnabled(isLoggedIn);
        removeButton.setEnabled(isLoggedIn);
        List<League> allLeagues = hockeyServerService.loadAllLeague(filterField.getText());
        tableModel.initWith(allLeagues);
        table.revalidate();
        table.repaint();
    }

    private class AddLeagueAction extends AbstractAction {
        AddLeagueAction() {
            putValue(SHORT_DESCRIPTION, "Добавить лигу");
            putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/icons/action_add.gif")));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            EditLeagueDialog editLeagueDialog = new EditLeagueDialog(newLeagueName -> {
                hockeyServerService.saveLeague(newLeagueName);
                refreshTableData();
                teamPanel.refreshTableData();
            });
            editLeagueDialog.setLocationRelativeTo(LeaguePanel.this);
            editLeagueDialog.setVisible(true);
        }
    }

    private class EditLeagueAction extends AbstractAction {
        EditLeagueAction() {
            putValue(SHORT_DESCRIPTION, "Изменить лигу");
            putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/icons/action_edit.gif")));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRowIndex = table.getSelectedRow();
            int rowCount = tableModel.getRowCount();
            if (selectedRowIndex == -1 || selectedRowIndex >= rowCount) {
                JOptionPane.showMessageDialog(
                        LeaguePanel.this,
                        "Для редпктирования выберите лигу!",
                        "Внимание",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Integer selectedLeagueId = (Integer) tableModel.getValueAt(selectedRowIndex, 0);
            String selectedLeagueName = (String) tableModel.getValueAt(selectedRowIndex, 1);

            EditLeagueDialog editLeagueDialog = new EditLeagueDialog(selectedLeagueName, changedLeagueName -> {
                hockeyServerService.updateLeague(selectedLeagueId, changedLeagueName);
                refreshTableData();
                teamPanel.refreshTableData();
            });
            editLeagueDialog.setLocationRelativeTo(LeaguePanel.this);
            editLeagueDialog.setVisible(true);
        }
    }

    private class RemoveLeagueAction extends AbstractAction {
        RemoveLeagueAction() {
            putValue(SHORT_DESCRIPTION, "Удалить лигу");
            putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/icons/action_remove.gif")));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRowIndex = table.getSelectedRow();
            int rowCount = tableModel.getRowCount();
            if (selectedRowIndex == -1 || selectedRowIndex >= rowCount) {
                JOptionPane.showMessageDialog(
                        LeaguePanel.this,
                        "Для удаления выберите лигу!",
                        "Внимание",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Integer selectedLeagueId = (Integer) tableModel.getValueAt(selectedRowIndex, 0);
            String selectedLeagueName = (String) tableModel.getValueAt(selectedRowIndex, 1);

            boolean containLeagueId = hockeyServerService.isContainAnyTeam(selectedLeagueId);
            if (containLeagueId) {
                JOptionPane.showMessageDialog(
                        LeaguePanel.this,
                        "Нельзя удалять лигу, к которой уже относятся какие-то команды!",
                        "Внимание",
                        JOptionPane.WARNING_MESSAGE);
            } else if (JOptionPane.showConfirmDialog(
                    LeaguePanel.this,
                    "Удалить лигу '" + selectedLeagueName + "'?",
                    "Вопрос",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                hockeyServerService.deleteLeagueById(selectedLeagueId);
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
            refreshTableData();
        }
    }
}
