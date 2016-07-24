import javax.swing.*;
import java.awt.*;

public class MCX_1 {

   // Max       > 9223372036854775807
   
   // default   > 6144396489148580666
   
   // Time      > 1469139235142 -- 13 digits
   
   public static void main(String[] args) {
   
      JFrame frame = new JFrame("MCX_0 Launcher");
      
      frame.setSize(500,300);
      frame.setLocationRelativeTo(null);
      
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      
      
      frame.setContentPane(new MCX_0());
      frame.getContentPane().setBackground(Color.LIGHT_GRAY);
      
      frame.setVisible(true);
      
     
      
   }

}
