package ru.eltech.client.gui;

import ru.eltech.api.entity.Team;
import javax.swing.table.AbstractTableModel;
import java.util.Arrays;
import java.util.List;

public class TeamTableModel extends AbstractTableModel {

    private static final List<String> COLUMNS = Arrays.asList(
            "id", "Название", "id Лиги", "Лига", "id Города", "Город", "Список игроков"
    );
    private static final List<Class<?>> TYPES = Arrays.asList(
            Integer.class, String.class, Integer.class, String.class, Integer.class, String.class, String.class
    );

    private List<Team> data;

    public void initWith(List<Team> data) {
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return COLUMNS.get(columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return TYPES.get(columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Team team = data.get(rowIndex);

        switch (columnIndex) {
            case 0: return team.getId();
            case 1: return team.getName();
            case 2: return team.getLeagueId();
            case 3: return team.getLeagueName();
            case 4: return team.getCityId();
            case 5: return team.getCityName();
            case 6: return team.getListPlayers();
            default: return null;
        }
    }
}
