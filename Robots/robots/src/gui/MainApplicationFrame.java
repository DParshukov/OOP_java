package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.beans.PropertyVetoException;

import log.Logger;

public class MainApplicationFrame extends JFrame {

    private final JDesktopPane desktopPane = new JDesktopPane();
    private final WindowStateManager stateManager = new WindowStateManager();

    private final RobotModel robotModel = new RobotModel();

    private LogWindow logWindow;
    private GameWindow gameWindow;
    private RobotCoordinatesWindow coordinatesWindow;

    public MainApplicationFrame() {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

        setContentPane(desktopPane);
        stateManager.load();

        logWindow = createLogWindow();
        addWindow(logWindow);
        restoreWindowState(logWindow, "logWindow");

        gameWindow = new GameWindow(robotModel);
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);
        restoreWindowState(gameWindow, "gameWindow");

        coordinatesWindow = new RobotCoordinatesWindow(robotModel);
        coordinatesWindow.setSize(280, 120);
        coordinatesWindow.setLocation(420, 10);
        addWindow(coordinatesWindow);
        restoreWindowState(coordinatesWindow, "coordinatesWindow");

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private void confirmExit() {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Вы уверены, что хотите выйти?",
                "Подтверждение выхода",
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            saveWindowState(logWindow, "logWindow");
            saveWindowState(gameWindow, "gameWindow");
            saveWindowState(coordinatesWindow, "coordinatesWindow");
            stateManager.save();
            dispose();
            System.exit(0);
        }
    }

    private void saveWindowState(JInternalFrame frame, String prefix) {
        stateManager.setInt(prefix, "x", frame.getX());
        stateManager.setInt(prefix, "y", frame.getY());
        stateManager.setInt(prefix, "width", frame.getWidth());
        stateManager.setInt(prefix, "height", frame.getHeight());
        stateManager.setBoolean(prefix, "iconified", frame.isIcon());
    }

    private void restoreWindowState(JInternalFrame frame, String prefix) {
        int x = stateManager.getInt(prefix, "x", frame.getX());
        int y = stateManager.getInt(prefix, "y", frame.getY());
        int width = stateManager.getInt(prefix, "width", frame.getWidth());
        int height = stateManager.getInt(prefix, "height", frame.getHeight());
        boolean iconified = stateManager.getBoolean(prefix, "iconified", false);

        frame.setBounds(x, y, width, height);
        try {
            frame.setIcon(iconified);
        } catch (PropertyVetoException e) {
            // оставляем как есть
        }
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }

        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription("Тестовые команды");

        {
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> Logger.debug("Новая строка"));
            testMenu.add(addLogMessageItem);
        }

        JMenu fileMenu = new JMenu("Файл");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        {
            JMenuItem exitItem = new JMenuItem("Выход", KeyEvent.VK_X);
            exitItem.addActionListener((event) -> confirmExit());
            fileMenu.add(exitItem);
        }

        menuBar.add(fileMenu);
        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        return menuBar;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // ignore
        }
    }
}
