package ru.eltech.client.gui;

import org.springframework.stereotype.Component;
import ru.eltech.api.servcie.HockeyServerService;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;

import static ru.eltech.client.utils.ClientUtils.prepareWindowSizeWithShifts;

@Component
public final class MainFrame extends JFrame {

    private static final String TITLE = "Hockey";

    private final HockeyServerService hockeyServerService;
    private final LeaguePanel leaguePanel;
    private final TeamPanel teamPanel;
    private final PlayerPanel playerPanel;
    private final LogInDialog logInDialog;

    public MainFrame(LeaguePanel leaguePanel, HockeyServerService hockeyServerService, TeamPanel teamPanel, PlayerPanel playerPanel, LogInDialog logInDialog) {
        this.leaguePanel = leaguePanel;
        this.teamPanel = teamPanel;
        this.playerPanel = playerPanel;
        this.logInDialog = logInDialog;
        this.hockeyServerService = hockeyServerService;
    }

    @PostConstruct
    public void init() {
        setTitle(TITLE);
        createGUI();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(
                        MainFrame.this,
                        "Вы действительно хотите выйти?",
                        "Выйти?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(prepareWindowSizeWithShifts(0, 40));
        setVisible(true);
    }

    private void createGUI() {
        setJMenuBar(createMenuBar());
        getContentPane().add(createTabbedPane(), BorderLayout.CENTER);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JButton authorization = new JButton("Войти");
        menuBar.add(authorization);

        authorization.addActionListener(e -> {
            if (!hockeyServerService.isLoggedIn()) {
                logInDialog.setLocationRelativeTo(MainFrame.this);
                logInDialog.setVisible(true);
                if (hockeyServerService.isLoggedIn()) authorization.setText("Выйти");
            }
            else {
                if (JOptionPane.showConfirmDialog(
                        MainFrame.this,
                        "Вы действительно хотите выйти?",
                        "Вопрос",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    hockeyServerService.logout();
                    playerPanel.refreshTableData();
                    teamPanel.refreshTableData();
                    leaguePanel.refreshTableData();
                    authorization.setText("Войти");
                }
            }
        });

        return menuBar;
    }

    private JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Игроки", playerPanel);
        tabbedPane.addTab("Команды", teamPanel);
        tabbedPane.addTab("Лиги", leaguePanel);

        tabbedPane.setSelectedIndex(0);

        return tabbedPane;
    }

}
