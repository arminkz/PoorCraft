import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.nio.Buffer;

/**
 * Created by Armin on 5/14/2016.
 */
public class ImageHelper {

    public static BufferedImage setOpacity(Image bi,float op){
        BufferedImage res = new BufferedImage(bi.getWidth(null),bi.getHeight(null),BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = res.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,op));
        g2d.drawImage(bi,0,0,null);
        return res;
    }

    /** Tints the given image with the given color.
     * @param loadImg - the image to paint and tint
     * @param color - the color to tint. Alpha value of input color isn't used.
     * @return A tinted version of loadImg */
    public static BufferedImage tint(Image loadImg, Color color) {
        BufferedImage img = new BufferedImage(loadImg.getWidth(null), loadImg.getHeight(null),BufferedImage.TRANSLUCENT);
        final float tintOpacity = 0.35f; // 0.35
        final float imgOpacity = 0.75f;
        Graphics2D g2d = img.createGraphics();

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,imgOpacity));
        //Draw the base image
        g2d.drawImage(loadImg, 0 ,0 ,null);


        //Set the color to a transparent version of the input color

        g2d.setColor(new Color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, tintOpacity));

        //Iterate over every pixel, if it isn't transparent paint over it
        Raster data = img.getData();
        for(int x = data.getMinX(); x < data.getWidth(); x++){
            for(int y = data.getMinY(); y < data.getHeight(); y++){
                int[] pixel = data.getPixel(x, y, new int[4]);
                if(pixel[3] > 0){ //If pixel isn't full alpha. Could also be pixel[3]==255
                    g2d.fillRect(x, y, 1, 1);
                }
            }
        }
        g2d.dispose();
        return img;
    }


    public static BufferedImage toGrayScale(Image loadImg){
        BufferedImage img = toBufferedImage(loadImg);

        //get image width and height
        int width = img.getWidth();
        int height = img.getHeight();

        //convert to grayscale
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int p = img.getRGB(x,y);

                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;

                //calculate average
                int avg = (r+g+b)/3;

                //replace RGB value with avg
                p = (a<<24) | (avg<<16) | (avg<<8) | avg;

                img.setRGB(x, y, p);
            }
        }

        return img;
    }


    public static BufferedImage toBufferedImage(Image i){

        if(i instanceof BufferedImage){
            return (BufferedImage)i;
        }else{
            BufferedImage res = new BufferedImage(i.getWidth(null),i.getHeight(null),BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = res.createGraphics();
            g2d.drawImage(i,0,0,null);
            return res;
        }
    }

}
