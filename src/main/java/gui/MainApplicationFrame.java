package gui;

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.*;

import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final StateManager stateManager;
    private LogWindow logWindow;
    private GameWindow gameWindow;
    private Rectangle normalBounds = new Rectangle();




    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.


        stateManager = new StateManager();
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);
        normalBounds=getBounds();

        setContentPane(desktopPane);


         logWindow = createLogWindow();
        addWindow(logWindow);

         gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        /**
         *  Перехватить событие закрытия окна
         */
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                confirmExit();
            }
        });
        addWindowStateListener(e -> {
            if ((e.getNewState() & Frame.MAXIMIZED_BOTH) == 0) {

                normalBounds = getBounds();
            }
        });


        setVisible(true);
        restoreState();

    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());

        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }



    private void saveState(){

        boolean isMaximized = (getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH;
        Rectangle boundsToSave = isMaximized ? normalBounds : getBounds();
        stateManager.saveState("mainFrame.x", Integer.toString(boundsToSave.x));
        stateManager.saveState("mainFrame.y", Integer.toString(boundsToSave.y));
        stateManager.saveState("mainFrame.width", Integer.toString(boundsToSave.width));
        stateManager.saveState("mainFrame.height", Integer.toString(boundsToSave.height));
        stateManager.saveState("mainFrame.isMaximized", Boolean.toString(isMaximized));


            stateManager.saveState("logWindow.x", Integer.toString(logWindow.getX()));
            stateManager.saveState("logWindow.y", Integer.toString(logWindow.getY()));
            stateManager.saveState("logWindow.width", Integer.toString(logWindow.getWidth()));
            stateManager.saveState("logWindow.height", Integer.toString(logWindow.getHeight()));
            stateManager.saveState("logWindow.isMinimized",Boolean.toString(logWindow.isIcon()));


        stateManager.saveState("gameWindow.x", Integer.toString(gameWindow.getX()));
        stateManager.saveState("gameWindow.y", Integer.toString(gameWindow.getY()));
        stateManager.saveState("gameWindow.width", Integer.toString(gameWindow.getWidth()));
        stateManager.saveState("gameWindow.height", Integer.toString(gameWindow.getHeight()));
        stateManager.saveState("gameWindow.isMinimized", Boolean.toString(gameWindow.isIcon()));


        stateManager.saveToFile();
    }

    private  void restoreState() {
        stateManager.loadFromFile();

            int x = Integer.parseInt(stateManager.loadState("mainFrame.x", "50"));
            int y = Integer.parseInt(stateManager.loadState("mainFrame.y", "50"));
            int width = Integer.parseInt(stateManager.loadState("mainFrame.width",
                    Integer.toString(Toolkit.getDefaultToolkit().getScreenSize().width - 100)));
            int height = Integer.parseInt(stateManager.loadState("mainFrame.height",
                    Integer.toString(Toolkit.getDefaultToolkit().getScreenSize().height - 100)));

        Rectangle bounds = new Rectangle(x, y, width, height);
        validateBounds(bounds);
            normalBounds=bounds;

        boolean isMaximized = Boolean.parseBoolean(stateManager.loadState("mainFrame.isMaximized", "false"));

        //планирует запуск приложения состояния в EDT после обработки текущей очереди событий.
        EventQueue.invokeLater(()->{;if (isMaximized) {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        else{
            setBounds(bounds);
        }});

        int logX = Integer.parseInt(stateManager.loadState("logWindow.x", "10"));
        int logY = Integer.parseInt(stateManager.loadState("logWindow.y", "10"));
        int logWidth = Integer.parseInt(stateManager.loadState("logWindow.width", "300"));
        int logHeight = Integer.parseInt(stateManager.loadState("logWindow.height", "800"));
        boolean logMinimized = Boolean.parseBoolean(stateManager.loadState("logWindow.isMinimized" , "false"));
        logWindow.setBounds(logX, logY, logWidth, logHeight);
        try {
            logWindow.setIcon(logMinimized);
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
        }

        int gameX = Integer.parseInt(stateManager.loadState("gameWindow.x", "100"));
        int gameY = Integer.parseInt(stateManager.loadState("gameWindow.y", "100"));
        int gameWidth = Integer.parseInt(stateManager.loadState("gameWindow.width", "400"));
        int gameHeight = Integer.parseInt(stateManager.loadState("gameWindow.height", "400"));

        boolean gameMinimized = Boolean.parseBoolean(stateManager.loadState("gameWindow.isMinimized", "false"));
        gameWindow.setBounds(gameX, gameY, gameWidth, gameHeight);
        try {
            gameWindow.setIcon(gameMinimized);
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
        }



    }

    /**
     * Проверяет и корректирует заданные границы, чтобы убедиться, что они соответствуют максимальным размерам экрана
     * и остаются видимыми. Это предотвращает размещение рамки за пределами экрана или ее слишком маленький/большой размер.
     *
     */
    private void validateBounds(Rectangle bounds) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle screenBounds = ge.getMaximumWindowBounds();

        bounds.width = Math.max(300, Math.min(bounds.width, screenBounds.width));
        bounds.height = Math.max(400, Math.min(bounds.height, screenBounds.height));

        bounds.x = Math.max(screenBounds.x,
                Math.min(bounds.x, screenBounds.x + screenBounds.width - bounds.width));
        bounds.y = Math.max(screenBounds.y,
                Math.min(bounds.y, screenBounds.y + screenBounds.height - bounds.height));
    }



    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(lookAndFeelMenu());
        menuBar.add(test());
        menuBar.add(makeExit());
        return menuBar;

    }

    private JMenu makeExit() {
        JMenu fileMenu = new JMenu("Выход");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.add(makeExitMenuItem());
        return fileMenu;
    }

    private JMenuItem makeExitMenuItem() {
        JMenuItem exitMenuItem = new JMenuItem("выход", KeyEvent.VK_X);
        exitMenuItem.addActionListener((event) -> confirmExit());

        return exitMenuItem;
    }

    private JMenu lookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");
        lookAndFeelMenu.add(systemLookAndFeel());
        lookAndFeelMenu.add(crossplatfromLookAndFeel());

        return lookAndFeelMenu;
    }

    private JMenuItem systemLookAndFeel() {
        JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
        systemLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        return systemLookAndFeel;
    }

    private JMenuItem crossplatfromLookAndFeel() {
        JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
        crossplatformLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        return crossplatformLookAndFeel;

    }

    private JMenu test() {
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");
        testMenu.add(addLogMessage());
        return testMenu;
    }

        private JMenuItem addLogMessage(){
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("Новая строка");
            });
            return addLogMessageItem;
        }


        private void setLookAndFeel (String className){
            try {
                UIManager.setLookAndFeel(className);
                SwingUtilities.updateComponentTreeUI(this);
            } catch (ClassNotFoundException | InstantiationException
                     | IllegalAccessException | UnsupportedLookAndFeelException e) {
                // just ignore
            }
        }

        /**
         * Отображает диалоговое окно подтверждения с помощью JOptionPane.
         */
        private void confirmExit () {
            int Output = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to exit",
                    "Exit Application",
                    JOptionPane.YES_NO_OPTION
            );
            if (Output == JOptionPane.YES_OPTION) {
                saveState();
                System.exit(0);
            }
        }
    }
