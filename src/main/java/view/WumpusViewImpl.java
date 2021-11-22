package view;

import model.game.GameInitialConfigurations;
import presenter.WumpusPresenter;
import presenter.WumpusPresenterImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class WumpusViewImpl extends JPanel implements WumpusView {

    WumpusPresenter wumpusPresenter;

    final int[][] cavesCoordinates = {{334, 20}, {609, 220}, {499, 540}, {169, 540}, {62, 220},
            {169, 255}, {232, 168}, {334, 136}, {435, 168}, {499, 255}, {499, 361},
            {435, 447}, {334, 480}, {232, 447}, {169, 361}, {254, 336}, {285, 238},
            {387, 238}, {418, 336}, {334, 393}};

    final int caveSize = 45;
    final int playerSize = 16;

    final int invalidCave=-1;

    Graphics2D g;

    private boolean gameStarting=true;

    public WumpusViewImpl() {

        wumpusPresenter = new WumpusPresenterImpl();
        wumpusPresenter.startNewGame();

        setPreferredSize(new Dimension(721, 687));
        setBackground(Color.white);
        setForeground(Color.lightGray);
        setFont(new Font("SansSerif", Font.PLAIN, 18));
        setFocusable(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int mouseClickXAxis = e.getX();
                int mouseClickYAxis = e.getY();
                handleMouseClick(mouseClickXAxis, mouseClickYAxis, isLeftMouseButton(e), isRightMouseButton(e));
            }

        });
    }

    void drawPlayer() {
        int x = cavesCoordinates[wumpusPresenter.getPlayerCave()][0] + (getCaveSize() - getPlayerSize()) / 2;
        int y = cavesCoordinates[wumpusPresenter.getPlayerCave()][1] + (getCaveSize() - getPlayerSize()) - 2;

        Path2D player = new Path2D.Double();
        player.moveTo(x, y);
        player.lineTo(x + getPlayerSize(), y);
        player.lineTo(x + getPlayerSize() / 2, y - getPlayerSize());
        player.closePath();

        g.setColor(Color.white);
        g.fill(player);
        g.setStroke(new BasicStroke(1));
        g.setColor(Color.black);
        g.draw(player);
    }

    void drawStartScreen() {
        g.setColor(new Color(0xDDFFFFFF, true));
        g.fillRect(0, 0, getWidth(), getHeight() - 60);

        g.setColor(Color.darkGray);
        g.setFont(new Font("SansSerif", Font.BOLD, 48));
        g.drawString("Hunt the Wumpus", 160, 240);

        g.setFont(getFont());
        g.drawString("left-click to move, right-click to shoot", 210, 310);
        g.drawString("be aware that hazards may be in the same cave", 175, 345);
        g.drawString("click to start", 310, 380);
    }

    void drawCaves() {

        g.setColor(Color.darkGray);
        g.setStroke(new BasicStroke(2));

        for (int i = 0; i < GameInitialConfigurations.CAVE_LINKS.length; i++) {
            for (int link : GameInitialConfigurations.CAVE_LINKS[i]) {
                int x1 = cavesCoordinates[i][0] + getCaveSize() / 2;
                int y1 = cavesCoordinates[i][1] + getCaveSize() / 2;
                int x2 = cavesCoordinates[link][0] + getCaveSize() / 2;
                int y2 = cavesCoordinates[link][1] + getCaveSize() / 2;
                g.drawLine(x1, y1, x2, y2);
            }
        }

        g.setColor(Color.orange);
        for (int[] r : cavesCoordinates)
            g.fillOval(r[0], r[1], getCaveSize(), getCaveSize());

        if (!wumpusPresenter.isGameOver()) {
            g.setColor(Color.magenta);
            for (int link : GameInitialConfigurations.CAVE_LINKS[wumpusPresenter.getPlayerCave()])
                g.fillOval(cavesCoordinates[link][0], cavesCoordinates[link][1], getCaveSize(), getCaveSize());
        }

        g.setColor(Color.darkGray);
        for (int[] r : cavesCoordinates)
            g.drawOval(r[0], r[1], getCaveSize(), getCaveSize());
    }

    void drawMessage() {
        if (!wumpusPresenter.isGameOver())
            g.drawString("Arrows  " + wumpusPresenter.getNumberOfArrows(), 610, 30);

        if (wumpusPresenter.getWarnings() != null) {
            g.setColor(Color.black);

            List<String> messages = wumpusPresenter.getWarnings();
            // collapse identical messages
            messages = messages.stream().distinct().collect(toList());

            // concat at most three
            String msg = messages.stream().limit(3).collect(joining(" & "));
            g.drawString(msg, 20, getHeight() - 40);

            // if there's more, print underneath
            if (messages.size() > 3) {
                g.drawString("& " + messages.get(3), 20, getHeight() - 17);
            }

            messages.clear();
        }
    }

    @Override
    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        drawCaves();
        drawMap();
        drawMessage();
    }

    private void drawMap() {
        if (gameStarting||wumpusPresenter.isGameOver()) {
            drawStartScreen();
            gameStarting=false;
        } else {
            drawPlayer();
        }
    }

    @Override
    public void render() {
        repaint();
    }

    private void handleMouseClick(int mouseClickXAxis, int mouseClickYAxis, boolean leftClick, boolean rightClick) {
        if (wumpusPresenter.isGameOver()) {
            wumpusPresenter.startNewGame();
        } else  {
            continueGame(mouseClickXAxis, mouseClickYAxis, leftClick, rightClick) ;
        }
        render();
    }

    private void continueGame(int mouseClickXAxis, int mouseClickYAxis, boolean leftClick, boolean rightClick) {
        int selectedCave = getSelectedCaveBasedOnMouseClickLocation(mouseClickXAxis, mouseClickYAxis);
        if(selectedCave != invalidCave){
            executeActionBasedOnMouseButtonClick(leftClick, rightClick, selectedCave);
        }
    }

    private int getSelectedCaveBasedOnMouseClickLocation(int mouseClickXAxis, int mouseClickYAxis) {
        int selectedCave = invalidCave;
        for (int caveNumber = 0; caveNumber< cavesCoordinates.length; caveNumber++) {
            int[] cave= cavesCoordinates[caveNumber];
            int xAxisOfCaveLinkedToCurrentCave = cave[0];
            int yAxisOfCaveLinkedToCurrentCave = cave[1];
            if (isMouseClickWithinCorrectCave(mouseClickXAxis, mouseClickYAxis, xAxisOfCaveLinkedToCurrentCave, yAxisOfCaveLinkedToCurrentCave)) {
                selectedCave = caveNumber;
                break;
            }
        }
        return selectedCave;
    }


    private boolean isMouseClickWithinCorrectCave(int mouseClickXAxis, int mouseClickYAxis, int caveXAxis, int caveYAxis) {
        return (mouseClickXAxis > caveXAxis && mouseClickXAxis < caveXAxis + getCaveSize())
                && (mouseClickYAxis > caveYAxis && mouseClickYAxis < caveYAxis + getCaveSize());
    }

    private void executeActionBasedOnMouseButtonClick(boolean leftClick, boolean rightClick, int selectedCave) {
        if (leftClick) {
            wumpusPresenter.move(selectedCave);
        } else if (rightClick) {
            wumpusPresenter.shoot(selectedCave);
        }
    }

    public int getCaveSize() {
        return caveSize;
    }
    
    public int getPlayerSize() {
        return playerSize;
    }
}
