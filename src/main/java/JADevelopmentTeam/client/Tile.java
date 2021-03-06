package JADevelopmentTeam.client;

import JADevelopmentTeam.common.DataPackage;
import JADevelopmentTeam.common.Intersection;
import JADevelopmentTeam.common.TerritoryStates;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Tile extends JPanel {
    private Intersection intersection;
    private int xCoordinate;
    private int yCoordinate;
    private boolean isLast = false;
    private TerritoryStates territory;
    private boolean isFocused;

    public TerritoryStates getTerritory() {
        return territory;
    }

    public void setTerritory(TerritoryStates territory) {
        this.territory = territory;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public Tile(Intersection intersection, int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.intersection = intersection;
        initialize();
    }

    private void initialize() {
        this.setOpaque(true);
        this.setLayout(new CardLayout());
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sendData();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (Client.yourTurn) {
                    isFocused = true;
                    repaint();
                    ClientGui.repaintBoardAndClientGui();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isFocused = false;
                repaint();
                ClientGui.repaintBoardAndClientGui();
            }
        });
    }

    public Intersection getIntersection() {
        return intersection;
    }

    public void setIntersection(Intersection intersection) {
        this.intersection = intersection;
        repaint();
    }


    @Override
    public void paintComponent(Graphics g) {
        int width = this.getWidth();
        int height = this.getHeight();
        Graphics2D g2d = (Graphics2D) g;
        Ellipse2D.Double ellipse = new Ellipse2D.Double(width * 0.05, height * 0.05, width * 0.90, height * 0.90);
        g2d.setBackground(new Color(224, 172, 105));
        if (intersection.exist()) {
            if (intersection.isStoneBlack()) {
                g2d.setPaint(Color.BLACK);
            } else {
                g2d.setPaint(Color.WHITE);
            }
            float alpha;
            if (intersection.isStoneDead()) {
                alpha = 0.5f;
            } else {
                alpha = 1f;
            }
            AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            g2d.setComposite(alphaComposite);
            g2d.fill(ellipse);
            g2d.setPaint(Color.BLACK);
            if (isLast) {
                g2d.setStroke(new BasicStroke(4));
                g2d.setPaint(Color.RED);
            }
            g2d.draw(ellipse);
        } else if (isFocused&&Client.yourTurn){
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            if (Client.userIsBlack) {
                g2d.setPaint(Color.BLACK);
            }else {
                g2d.setPaint(Color.WHITE);
            }
            g2d.fill(ellipse);
            g2d.setPaint(Color.BLACK);
            g2d.draw(ellipse);
        }
        if (territory == TerritoryStates.BlackTerritory || territory == TerritoryStates.WhiteTerritory) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            if (territory == TerritoryStates.BlackTerritory) {
                g2d.setPaint(Color.BLACK);
            } else {
                g2d.setPaint(Color.WHITE);
            }
            g2d.fill(new Rectangle2D.Double(width * 0.35, height * 0.35, width * 0.30, height * 0.30));
        }

    }

    private void sendData() {
        ServerConnector.getInstance();
        ServerConnector.getInstance().sendData(new DataPackage(new Intersection(xCoordinate, yCoordinate), DataPackage.Info.Stone));
        if (!intersection.exist()) {
            System.out.println("klikniete w puste x:" + xCoordinate + " y:" + yCoordinate);
        } else {
            System.out.println("klikniete x:" + xCoordinate + " y:" + yCoordinate);
        }
    }
}
