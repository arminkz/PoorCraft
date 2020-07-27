import java.awt.*;
import java.util.ArrayList;

public class BuildingCommand implements Command {

    private GameObjectMap bmap;
    private Point coords;
    private Building b;

    public BuildingCommand(GameObjectMap bmap,Point coords, Building b){
        this.bmap = bmap;
        this.coords = coords;
        this.b = b;
    }

    @Override
    public void execute() {
        bmap.addBuilding(b,coords.x,coords.y);
    }

    @Override
    public void unexecute() {
        bmap.removeBuilding(b,coords.x,coords.y);
    }
}
