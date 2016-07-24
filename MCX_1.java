import javax.swing.*;
import java.awt.*;

public class MCX_1 {

   // Max       > 922 3372 0368 5477 5807 -- 19 digits -- 9223372036854775807
   
   // default   > 710 3658 5667 4179 6864.................7103658566741796864
   
   // Time      > .......1 4691 3923 5142 -- 13 digits
   
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
