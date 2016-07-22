import javax.swing.*;
import java.awt.*;

public class MCX_1 {

   // Max       > 922 3372 0368 5477 5807 -- 19 digits -- 9223372036854775807
   
   // Default   > 128 7166 3078 6946 0480.................1287166307869460480
   // "yuounny" > 797 6596 0107 1189 7088.................7976596010711897088
   
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
