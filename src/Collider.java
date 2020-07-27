import java.awt.*;
import java.io.Serializable;

/**
 * Created by Armin on 7/1/2016.
 */
public class Collider implements Serializable {

    public Rectangle rectangle;
    public int zindex;
    public GameObject gameObject;

    public Collider(GameObject gameObject){
        this.gameObject = gameObject;
    }

}
