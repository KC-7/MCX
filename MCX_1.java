import javax.swing.*;
import java.awt.*;

public class MCX_1 {

   // stams icle, stams icla
   // Max     > 922 3372 0368 5477 5807 -- 19 digits
   // Default > ..3 2159 3076 1566 8708 -- 17 digits
   // Time    > .......1 4691 3923 5142 -- 13 digits
   
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
