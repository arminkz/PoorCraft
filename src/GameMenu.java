import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by Armin on 7/4/2016.
 */
public class GameMenu extends JPanel {

    Image bg;
    Image flagImg[];
    Image buttonImg[];
    Image buttonAnimImg[];
    Image spImg;
    Image mpImg;
    Image meImg;
    Image opImg;
    Image qImg;

    Clip menuMusic;

    GameWindow gw;

    int animState;
    Timer animTimer;

    public GameMenu(GameWindow gw){

        this.gw = gw;

        bg = (new ImageIcon(this.getClass().getResource("resources/images/menu/menuback.png"))).getImage();
        flagImg = new Image[8];
        for (int i = 0; i < 8; i++) {
            flagImg[i] = (new ImageIcon(this.getClass().getResource("resources/images/menu/flag/"+i+".png"))).getImage();
        }

        buttonAnimImg = new Image[10];
        for (int i = 0; i < 10; i++) {
            buttonAnimImg[i] = (new ImageIcon(this.getClass().getResource("resources/images/menu/button/a"+i+".png"))).getImage();
        }

        buttonImg = new Image[2];
        buttonImg[0] = (new ImageIcon(this.getClass().getResource("resources/images/menu/button/menubutton_Exited.png"))).getImage();
        buttonImg[1] = (new ImageIcon(this.getClass().getResource("resources/images/menu/button/menubutton_Entered.png"))).getImage();

        spImg = (new ImageIcon(this.getClass().getResource("resources/images/menu/button/single_player.png"))).getImage();
        mpImg = (new ImageIcon(this.getClass().getResource("resources/images/menu/button/multi_player.png"))).getImage();
        meImg = (new ImageIcon(this.getClass().getResource("resources/images/menu/button/map_editor.png"))).getImage();
        opImg = (new ImageIcon(this.getClass().getResource("resources/images/menu/button/options.png"))).getImage();
        qImg = (new ImageIcon(this.getClass().getResource("resources/images/menu/button/quit.png"))).getImage();

        setLayout(null);

        HoverAnimationButton singlePlayerButton = new HoverAnimationButton(buttonImg,buttonAnimImg,10);
        singlePlayerButton.setIcon(spImg);
        singlePlayerButton.setLocation(100,100);
        singlePlayerButton.setAction((ActionEvent e) -> {
            gw.initGame();
            menuMusic.stop();
        });
        add(singlePlayerButton);

        HoverAnimationButton multiPlayerButton = new HoverAnimationButton(buttonImg,buttonAnimImg,10);
        multiPlayerButton.setIcon(mpImg);
        multiPlayerButton.setLocation(100,250);
        add(multiPlayerButton);

        HoverAnimationButton mapEditorButton = new HoverAnimationButton(buttonImg,buttonAnimImg,10);
        mapEditorButton.setIcon(meImg);
        mapEditorButton.setLocation(100,400);
        mapEditorButton.setAction((ActionEvent e) -> {
            gw.initMapEditor();
            menuMusic.stop();
        });
        add(mapEditorButton);

        HoverAnimationButton optionsButton = new HoverAnimationButton(buttonImg,buttonAnimImg,10);
        optionsButton.setIcon(opImg);
        optionsButton.setLocation(100,550);
        add(optionsButton);

        HoverAnimationButton quitButton = new HoverAnimationButton(buttonImg,buttonAnimImg,10);
        quitButton.setIcon(qImg);
        quitButton.setLocation(100,700);
        quitButton.setAction(e -> System.exit(0));
        add(quitButton);

        menuMusic = SoundHelper.getClip("intro.wav");
        menuMusic.loop(Clip.LOOP_CONTINUOUSLY);
        //menuMusic.start();

        animTimer = new Timer(100,(ActionEvent e) -> {
           animState = (animState + 1) % 8;
            repaint();
        });
        animTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(bg,0,0,null);

        g.drawImage(flagImg[animState],960,95,null);
    }
}
