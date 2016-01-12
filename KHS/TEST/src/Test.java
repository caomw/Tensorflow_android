import java.util.*;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.io.File;
import org.json.simple.JSONArray;
 
public class Test extends Component {
    int[] arr = new int[784];
  public static void main(String[] foo) {
    new Test();
  }
 
  public int printPixelARGB(int pixel) {
    int alpha = (pixel >> 24) & 0xff;
    int red = (pixel >> 16) & 0xff;
    int green = (pixel >> 8) & 0xff;
    int blue = (pixel) & 0xff;
    int reverse_avg = (255-(red+green+blue)/3);
    //System.out.println("argb: " + alpha + ", " + red + ", " + green + ", " + blue);
    //System.out.println(reverse_avg);
    return reverse_avg;
  }
 
  private void marchThroughImage(BufferedImage image) {
    int w = image.getWidth();
    int h = image.getHeight();
    //System.out.println("width, height: " + w + ", " + h);

    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        //System.out.println("x,y: " + j + ", " + i);
        int pixel = image.getRGB(j, i);
        arr[28*i+j]=printPixelARGB(pixel);
        //System.out.println("");
      }
    }
  }
 
  public Test() {
	//String inputImagePath = "images/2.png";
	String inputImagePath = "C:/Users/Hyoungseok/workspace/TEST/bin/images/2.png";
	String outputImagePath = "C:/Users/Hyoungseok/workspace/TEST/bin/images/2_adjusted.png";
	 int scaledWidth = 28;
     int scaledHeight = 28;
     try {
		ImageResizer.resize(inputImagePath, outputImagePath, scaledWidth, scaledHeight);
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		System.out.println("Error resizing the image.");
		e1.printStackTrace();
	}
    try {
      // get the BufferedImage, using the ImageIO class
      File outputFile = new File(outputImagePath);
      BufferedImage image = 
      ImageIO.read(outputFile);
      marchThroughImage(image);
      ArrayList<String> list = new ArrayList<String>();

      
      
      for (int i=0; i<784; i++){
    	  list.add(String.valueOf(arr[i]));
    	  //System.out.println(arr[i]);
      }
      JSONArray jsArray = new JSONArray(list);
      
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
 
}


