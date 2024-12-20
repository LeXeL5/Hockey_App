package ru.eltech.client.gui;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.eltech.api.editClasses.TeamEdit;
import ru.eltech.api.editClasses.TeamLists;
import ru.eltech.api.entity.City;
import ru.eltech.api.entity.League;
import ru.eltech.api.entity.Player;

import static ru.eltech.client.utils.ClientUtils.isBlank;

public class EditTeamDialog extends JDialog {

    private static final String TITLEADD = "Добавление команды";
    private static final String TITLEEDIT = "Редактирование команды";

    private final JTextField nameField = new JTextField();
    private JComboBox league = new JComboBox();
    private JComboBox city = new JComboBox();

    private JList<Player> teamPlayersList = createPlayerList();
    private JList<Player> allPlayersList = createPlayerList();

    private final TeamLists teamList;

    private final TeamEdit prevData;
    private final Consumer<TeamEdit> newTeamConsumer;

    public EditTeamDialog(TeamLists teamList, Consumer<TeamEdit> newTeamConsumer) {
        this(teamList, null, newTeamConsumer);
    }

    public EditTeamDialog(TeamLists teamList, TeamEdit prevData, Consumer<TeamEdit> newTeamConsumer) {
        this.prevData = prevData;
        this.newTeamConsumer = newTeamConsumer;
        this.teamList = teamList;

        for (int i = 0; i < teamList.getLeagueList().size(); i++) {
            league.addItem(teamList.getLeagueList().get(i).getName());
        }

        for (int i = 0; i < teamList.getCityList().size(); i++) {
            city.addItem(teamList.getCityList().get(i).getName());
        }

        if (prevData != null) {
            setTitle(TITLEEDIT);
        } else setTitle(TITLEADD);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel northPanel = new JPanel(new GridLayout(3, 1));

        JPanel namePanel = new JPanel(new BorderLayout());
        JPanel cityPanel = new JPanel(new BorderLayout());
        JPanel leaguePanel = new JPanel(new BorderLayout());

        JPanel listPanel = new JPanel(new GridLayout(1, 2, 5, 5));

        listPanel.add(new JScrollPane(allPlayersList));
        listPanel.add(new JScrollPane(teamPlayersList));

        DefaultListModel<Player> teamPlayersModel = new DefaultListModel<>();
        if (prevData != null) {
            for (Player player : prevData.getListPlayer()) {
                teamPlayersModel.addElement(player);
            }
        }
        teamPlayersList.setModel(teamPlayersModel);

        DefaultListModel<Player> allPlayersModel = new DefaultListModel<>();
        for (Player player : teamList.getPlayerList()) {
            allPlayersModel.addElement(player);
        }
        allPlayersList.setModel(allPlayersModel);

        namePanel.add(new JLabel("Название: "), BorderLayout.WEST);
        leaguePanel.add(new JLabel("Лига:     "), BorderLayout.WEST);
        cityPanel.add(new JLabel("Город:    "), BorderLayout.WEST);

        if (prevData != null) {
            nameField.setText(prevData.getName());
            league.setSelectedItem(prevData.getLeagueName().getName());
            city.setSelectedItem(prevData.getCity().getName());
        }

        namePanel.add(nameField, BorderLayout.CENTER);
        leaguePanel.add(league, BorderLayout.CENTER);
        cityPanel.add(city, BorderLayout.CENTER);

        northPanel.add(namePanel);
        northPanel.add(leaguePanel);
        northPanel.add(cityPanel);

        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(listPanel, BorderLayout.CENTER);
        mainPanel.add(new JButton(new SaveAction()), BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
        setSize(400, 500);
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        allPlayersList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
                    Player selectedValue = allPlayersList.getSelectedValue();
                    ((DefaultListModel<Player>) allPlayersList.getModel()).removeElement(selectedValue);
                    ((DefaultListModel<Player>) teamPlayersList.getModel()).addElement(selectedValue);
                    allPlayersList.revalidate();
                    allPlayersList.repaint();
                    teamPlayersList.revalidate();
                    teamPlayersList.repaint();
                }
            }
        });

        teamPlayersList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
                    Player selectedValue = teamPlayersList.getSelectedValue();
                    ((DefaultListModel<Player>) teamPlayersList.getModel()).removeElement(selectedValue);
                    ((DefaultListModel<Player>) allPlayersList.getModel()).addElement(selectedValue);
                    allPlayersList.revalidate();
                    allPlayersList.repaint();
                    teamPlayersList.revalidate();
                    teamPlayersList.repaint();
                }
            }
        });
    }

    private JList<Player> createPlayerList() {
        JList<Player> playerList = new JList<>();
        playerList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof Player) {
                    JLabel label = (JLabel) renderer;
                    Player player = (Player) value;
                    label.setText(player.getName() + " " + player.getSurname());
                }
                return renderer;
            }
        });
        return playerList;
    }

    private class SaveAction extends AbstractAction {
        SaveAction() {
            putValue(NAME, prevData != null ? "Изменить" : "Добавить");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isBlank(nameField.getText()) ||
                    league.getSelectedItem() == null ||
                    city.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(
                        EditTeamDialog.this,
                        "Не все данные введены!",
                        "Внимание",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }


            League newLeague = new League();
            for (int i = 0; i < teamList.getLeagueList().size(); i++) {
                if (teamList.getLeagueList().get(i).getName().equals(league.getSelectedItem())) {
                    newLeague = teamList.getLeagueList().get(i);
                    break;
                }
            }
            City newCity = new City();
            for (int i = 0; i < teamList.getCityList().size(); i++) {
                if (teamList.getCityList().get(i).getName().equals(city.getSelectedItem())) {
                    newCity = teamList.getCityList().get(i);
                    break;
                }
            }


            List<Player> newList = new ArrayList<>();
            for (int i = 0; i < teamPlayersList.getModel().getSize(); i++) {
                newList.add(teamPlayersList.getModel().getElementAt(i));
            }
            TeamEdit teamEdit = new TeamEdit(nameField.getText(), newLeague, newCity, newList);
            newTeamConsumer.accept(teamEdit);
            dispose();
        }
    }
}
