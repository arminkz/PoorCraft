import java.awt.image.RGBImageFilter;

/**
 * Created by Armin on 6/21/2016.
 */
public class CustomImageFilter extends RGBImageFilter {

    enum FilterType {
        Green,
        Red
    }

    private FilterType f;

    public CustomImageFilter(FilterType f){
        this.f = f;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        int g = (rgb & 0x00FF00);
        return g;
    }

}
