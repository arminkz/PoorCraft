/**
 * Created by Armin on 5/13/2016.
 */
public class FPSCounter extends Thread {

    private long lastTime;

    private double fps;
    public double getFPS(){
        return fps;
    }

    public void run(){
        while (true){//i am goshad, add a condition for an finishable thread
            lastTime = System.nanoTime();
            try{
                Thread.sleep(1000); // longer than one frame
            }
            catch (InterruptedException e){

            }
            fps = 1000000000.0 / (System.nanoTime() - lastTime);
            lastTime = System.nanoTime();
        }
    }

}
