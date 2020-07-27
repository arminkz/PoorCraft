import javax.swing.*;
import java.awt.event.ActionEvent;

public class FrameAnimator {

    Timer anim;
    long time = 0;

    public FrameAnimator(){
        Timer anim = new Timer(10,(ActionEvent e) -> {
            time++;
            if(time == 1000){
                time = 0;
            }
        });
        anim.start();

    }

    public int getFrameNo(int maxFrames,int frameTime) {
        return (int)((time/frameTime) % maxFrames);
    }
}
