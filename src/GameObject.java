import java.io.Serializable;

public abstract class GameObject implements Serializable{

    protected GameObject(){
        collider = new Collider(this);
    }

    protected GameObject(Sprite s){
        objectSprite = s;
        collider = new Collider(this);
    }

    private Sprite objectSprite;

    public Sprite getObjectSprite() {
        return objectSprite;
    }

    public Collider collider;

    protected boolean selectable = false;
    private boolean selected = false;

    public boolean isSelected() {
        return selected;
    }

    public void select(){
        if(selectable){
            selected = true;
        }
    }

    public void deselect(){
        if(selectable){
            selected = false;
        }
    }

}
