/**
 * Othello class - The backend of the magic that is Othello.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*; 
import javax.swing.event.*;
import javax.swing.text.html.*;


class GamePanel extends JPanel implements MouseListener {
    GameBoard board;
    int gameLevel;
    ImageIcon bg_img = new ImageIcon(Othello.class.getResource("bg.jpg")),
              button_black,
              button_white;
    Image bg = bg_img.getImage();
    JLabel score_black, score_white;
    String gameTheme;
    Move hint = null;
    boolean inputEnabled, active;

    public GamePanel(GameBoard board, JLabel score_black, JLabel score_white, String theme, int level) {
        super();
        this.board = board;
        this.score_black = score_black;
        this.score_white = score_white;
        gameLevel = level;
        setTheme(theme);
        addMouseListener(this);
        inputEnabled = true;
        active = true;
    }

    public void setTheme(String gameTheme) {
        hint = null;
        this.gameTheme = gameTheme;
        if (gameTheme.equals("Classic")) {
            button_black = new ImageIcon(Othello.class.getResource("btn_black.png"));
            button_white = new ImageIcon(Othello.class.getResource("btn_white.png"));
            //setBackground(Color.green);
        } else if (gameTheme.equals("Snazzy")) {
            button_black = new ImageIcon(Othello.class.getResource("btn_blue.jpg"));
            button_white = new ImageIcon(Othello.class.getResource("btn_red.jpg"));
            //setBackground(Color.white);
        } else {
            gameTheme = "Plain"; // Base theme "Plain"
            //setBackground(Color.green);
        }
        repaint();
    }

    public void setLevel(int level) {
        if ((level > 1) && (level < 7)) {
            gameLevel = level;
        }
    }

    public void drawPanel(Graphics g) {
        // int currentWidth = getWidth();
        // int currentHeight = getHeight();
        for (int i = 1; i < 8; i++) {
            g.drawLine(i * Othello.Square_L, 0, i * Othello.Square_L, Othello.Height);
        }
        g.drawLine(Othello.Width, 0, Othello.Width, Othello.Height);
        for (int i = 1; i < 8; i++) {
            g.drawLine(0, i * Othello.Square_L, Othello.Width, i * Othello.Square_L);
        }
        g.drawLine(0, Othello.Height, Othello.Width, Othello.Height);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                switch (board.get(i, j)) {
                    case white:  
                        if (gameTheme.equals("Plain")) {
                            g.setColor(Color.white);
                            g.fillOval(1 + i * Othello.Square_L, 1 + j * Othello.Square_L, Othello.Square_L - 1, Othello.Square_L - 1);
                        } else {
                            g.drawImage(button_white.getImage(),
                                    1 + i * Othello.Square_L,
                                    1 + j * Othello.Square_L, this);
                        } 
                        break;
                    case black:  
                        if (gameTheme.equals("Plain")) {
                            g.setColor(Color.black);
                            g.fillOval(1 + i * Othello.Square_L, 1 + j * Othello.Square_L, Othello.Square_L - 1, Othello.Square_L - 1);
                        } else {
                            g.drawImage(button_black.getImage(), 1 + i * Othello.Square_L, 1 + j * Othello.Square_L, this);
                        } 
                        break;
                }
            }
        }
        if (hint != null) {
            g.setColor(Color.darkGray);
            g.drawOval(hint.i * Othello.Square_L + 3, hint.j * Othello.Square_L + 3, Othello.Square_L - 6, Othello.Square_L - 6);
        }
    }   
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(bg, 0, 0, null); 
        drawPanel(g);
    }

    public Dimension getPreferredSize() {
        return new Dimension(Othello.Width, Othello.Height);
    }

    public void showWinner() {
        inputEnabled = false;
        active = false;
        if (board.counter[0] > board.counter[1]) {
            JOptionPane.showMessageDialog(this, "You win!", Othello.NAME, JOptionPane.INFORMATION_MESSAGE);
        } else if (board.counter[0] < board.counter[1]) {
            JOptionPane.showMessageDialog(this, "I win!", Othello.NAME, JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Drawn!", Othello.NAME, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void setHint(Move hint) {
        this.hint = hint;
    }

    public void clear() {
        board.clear();
        score_black.setText(Integer.toString(board.getCounter(TKind.black)));
        score_white.setText(Integer.toString(board.getCounter(TKind.white)));
        inputEnabled = true;
        active = true;
    }

    public void computerMove() {
        if (board.gameEnd()) {
            showWinner();
            return;
        }
        Move move = new Move();

        if (board.findMove(TKind.white, gameLevel, move)) {
            board.move(move, TKind.white);
            score_black.setText(Integer.toString(board.getCounter(TKind.black)));
            score_white.setText(Integer.toString(board.getCounter(TKind.white)));
            repaint();
            if (board.gameEnd()) {
                showWinner();
            } else if (!board.userCanMove(TKind.black)) {
                JOptionPane.showMessageDialog(this, "You pass...", Othello.NAME,
                        JOptionPane.INFORMATION_MESSAGE);
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        computerMove();
                    }
                });
            }
        } else if (board.userCanMove(TKind.black)) {
            JOptionPane.showMessageDialog(this, "I pass...", Othello.NAME,
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            showWinner();
        }
    }

    public void mouseClicked(MouseEvent e) {
        // generato quando il mouse viene premuto e subito rilasciato (click)

        if (inputEnabled) {
            hint = null;
            int i = e.getX() / Othello.Square_L;
            int j = e.getY() / Othello.Square_L;

            if ((i < 8) && (j < 8) && (board.get(i, j) == TKind.nil)
                    && (board.move(new Move(i, j), TKind.black) != 0)) {
                score_black.setText(
                        Integer.toString(board.getCounter(TKind.black)));
                score_white.setText(
                        Integer.toString(board.getCounter(TKind.white)));
                repaint();
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() { 
                        Cursor savedCursor = getCursor();

                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        computerMove();
                        setCursor(savedCursor);     
                    }
                });
            } else {
                JOptionPane.showMessageDialog(this, "Illegal move", Othello.NAME,
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void mouseEntered(MouseEvent e) {// generato quando il mouse entra nella finestra
    }

    public void mouseExited(MouseEvent e) {// generato quando il mouse esce dalla finestra
    }

    public void mousePressed(MouseEvent e) {// generato nell'istante in cui il mouse viene premuto
    }

    public void mouseReleased(MouseEvent e) {// generato quando il mouse viene rilasciato, anche a seguito di click
    }

}

public class Othello extends JFrame implements ActionListener {

    // Constants (final static variables)
    final static int Square_L = 33,         // Length in pixels of a grid tile.
                     Width = 8 * Square_L,  // Width of the game board.
                     Height = 8 * Square_L; // Height of the game board.
    final static float VER = 0.3f;          // Version
    final static String NAME = "Othello",
                 AUTHOR = "Gabriel Nahmias",
                 DATE_START = "October 1st, 2013",
                 DATE_REV = "October 17th, 2013",
                 REV_HISTORY = "v0.2: October 11th, 2013\nv0.1: " + DATE_START + "\n",
                 ABOUT = NAME + "\n\nv" + VER + ": " + DATE_REV + "\n" + REV_HISTORY + "\n" + AUTHOR,
                 // Files
                 FILE_DOCS = "docs.html",
                 // Defaults
                 DEF_THEME = "Classic";

    static GamePanel gpanel;
    static JMenuItem hint;
    static boolean helpActive = false;

    GameBoard board;
    JEditorPane editorPane;
    static JLabel score_black, score_white;
    JMenu level, theme;

    public Othello() {
        super(NAME);
        // Game starts with 2 pieces of each color.
        score_black = new JLabel("2");
        score_black.setForeground(Color.blue);
        score_black.setFont(new Font("Dialog", Font.BOLD, 16));
        score_white = new JLabel("2");
        score_white.setForeground(Color.red);
        score_white.setFont(new Font("Dialog", Font.BOLD, 16));
        board = new GameBoard();
        gpanel = new GamePanel(board, score_black, score_white, DEF_THEME, 3);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupMenuBar();
        gpanel.setMinimumSize(new Dimension(Othello.Width, Othello.Height));

        JPanel status = new JPanel();

        status.setLayout(new BorderLayout());
        status.add(score_black, BorderLayout.WEST);
        status.add(score_white, BorderLayout.EAST);
        // status.setMinimumSize(new Dimension(100, 30));
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, gpanel,
                status);

        splitPane.setOneTouchExpandable(false);
        getContentPane().add(splitPane);

        pack();
        setVisible(true);
        setResizable(false);
    }

    // Top-level menu: File, Edit, Help
    void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(buildGameMenu());
        menuBar.add(buildHelpMenu());
        setJMenuBar(menuBar);   
    }

    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem) (e.getSource());
        String action = source.getText();
        // Handle level selection.
        if (action.equals("2")) {
            gpanel.setLevel(2);
        } else if (action.equals("3")) {
            gpanel.setLevel(3);
        } else if (action.equals("4")) {
            gpanel.setLevel(4);
        } else if (action.equals("5")) {
            gpanel.setLevel(5);
        } else if (action.equals("6")) {
            gpanel.setLevel(6);
        // Now, theme selection.
        } else if (action.equals("Classic")) {
            gpanel.setTheme(action);
        } else if (action.equals("Snazzy")) {
            gpanel.setTheme(action);
        } else if (action.equals("Plain")) {
            gpanel.setTheme(action);
        }
    }

    protected JMenu buildGameMenu() {
        boolean[] themeCheck = new boolean[]{false, false, false};
        JMenu game = new JMenu("Options"),
              level = new JMenu("Level"),
              theme = new JMenu("Theme");
        // For some reason, Java bitches about the Hint menu item being declared final, so whatever.
        final JMenuItem hint = new JMenuItem("Hint");
        JMenuItem newWin = new JMenuItem("New"),
                  quit = new JMenuItem("Quit"),
                  undo = new JMenuItem("Undo");
        newWin.setMnemonic('N');
        undo.setMnemonic('U');
        hint.setMnemonic('H');
        quit.setMnemonic('N');
        // Start off with Undo disabled.
        undo.setEnabled(false);
        // Build level sub-menu.
        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem("2");

        group.add(rbMenuItem);
        level.add(rbMenuItem);
        rbMenuItem.addActionListener(this);
        rbMenuItem = new JRadioButtonMenuItem("3", true);
        group.add(rbMenuItem);
        level.add(rbMenuItem);
        rbMenuItem.addActionListener(this);
        rbMenuItem = new JRadioButtonMenuItem("4");
        group.add(rbMenuItem);
        level.add(rbMenuItem);
        rbMenuItem.addActionListener(this);
        rbMenuItem = new JRadioButtonMenuItem("5");
        group.add(rbMenuItem);
        level.add(rbMenuItem);
        rbMenuItem.addActionListener(this);
        rbMenuItem = new JRadioButtonMenuItem("6");
        group.add(rbMenuItem);
        level.add(rbMenuItem);
        rbMenuItem.addActionListener(this);

        // Build theme submenu and, according to the themeCheck array, select specified default theme.
        // TODO: Add configuration file that saves choices for such settings
        
        group = new ButtonGroup();
        rbMenuItem = new JRadioButtonMenuItem("Classic", themeCheck[0]);
        group.add(rbMenuItem);
        theme.add(rbMenuItem);
        rbMenuItem.addActionListener(this);
        rbMenuItem = new JRadioButtonMenuItem("Snazzy", themeCheck[1]);
        group.add(rbMenuItem);
        theme.add(rbMenuItem);
        rbMenuItem.addActionListener(this);
        rbMenuItem = new JRadioButtonMenuItem("Plain", themeCheck[2]);
        group.add(rbMenuItem);
        theme.add(rbMenuItem);
        rbMenuItem.addActionListener(this);

        // New
        newWin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gpanel.clear();
                hint.setEnabled(true);
                repaint();
            }
        });
        
        // Quit
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        // Hint
        hint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (gpanel.active) {
                    Move move = new Move();

                    if (board.findMove(TKind.black, gpanel.gameLevel, move)) {
                        gpanel.setHint(move);
                    }
                    repaint();

                    /* if (board.move(move,TKind.black) != 0) {
                     score_black.setText(Integer.toString(board.getCounter(TKind.black)));
                     score_white.setText(Integer.toString(board.getCounter(TKind.white)));
                     repaint();
                     javax.swing.SwingUtilities.invokeLater(new Runnable() {
                     public void run() {
                     Cursor savedCursor = getCursor();
                     setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                     gpanel.computerMove();
                     setCursor(savedCursor);     
                     }
                     });
                     }   
                     */
                } else {
                    hint.setEnabled(false);
                }
            }
        });
        
        game.add(newWin);
        game.addSeparator();
        game.add(undo);
        game.add(hint);
        game.addSeparator();
        game.add(level);
        game.add(theme);
        game.addSeparator();
        game.add(quit);
        return game;
    }

    protected JMenu buildHelpMenu() {
        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About " + NAME + "...");
        JMenuItem openHelp = new JMenuItem("Documentation");

        // Begin "Help"
        openHelp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createEditorPane();
            }
        });
        // End "Help"

        // Begin "About"
        about.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ImageIcon icon = new ImageIcon(
                        Othello.class.getResource("logo.jpg"));

                JOptionPane.showMessageDialog(null, ABOUT, "About " + NAME,
                        JOptionPane.PLAIN_MESSAGE, icon);
            }
        });
        // End "About"

        help.add(openHelp);
        help.add(about);

        return help;
    }

    protected void createEditorPane() {
        if (helpActive) {
            return;
        }
        editorPane = new JEditorPane(); 
        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(
                new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (e instanceof HTMLFrameHyperlinkEvent) {
                        ((HTMLDocument) editorPane.getDocument()).processHTMLFrameHyperlinkEvent(
                                (HTMLFrameHyperlinkEvent) e);
                    } else {
                        try {
                            editorPane.setPage(e.getURL());
                        } catch (java.io.IOException ioe) {
                            System.out.println("IOE: " + ioe);
                        }
                    }
                }
            }
        });
        java.net.URL helpURL = Othello.class.getResource(FILE_DOCS);

        if (helpURL != null) {
            try {
                editorPane.setPage(helpURL);
                new HelpWindow(editorPane);
            } catch (java.io.IOException e) {
                System.err.println("Attempted to read a bad URL: " + helpURL);
            }
        } else {
            System.err.println("Couldn't find file: " + FILE_DOCS);
        }

        return;
    }

    public class HelpWindow extends JFrame {

        public HelpWindow(JEditorPane editorPane) {
            super("Help Window");
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    Othello.helpActive = false;
                    setVisible(false);
                }
            });

            JScrollPane editorScrollPane = new JScrollPane(editorPane);

            editorScrollPane.setVerticalScrollBarPolicy(
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            getContentPane().add(editorScrollPane);
            setSize(600, 400);
            setVisible(true);
            helpActive = true;
        }
    }

    public HyperlinkListener createHyperLinkListener1() {
        return new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (e instanceof HTMLFrameHyperlinkEvent) {
                        ((HTMLDocument) editorPane.getDocument()).processHTMLFrameHyperlinkEvent(
                                (HTMLFrameHyperlinkEvent) e);
                    } else {
                        try {
                            editorPane.setPage(e.getURL());
                        } catch (java.io.IOException ioe) {
                            System.out.println("IOE: " + ioe);
                        }
                    }
                }
            }
        };
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {}
        Othello game = new Othello();
    }

}
