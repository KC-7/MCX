import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MCX_0 extends JPanel  {

   private JButton generateButton;
   private JLabel seedLabel, display;
   private JTextField seedField;
   
   private String seed;
   private long root;

   public MCX_0() {
      
      this.setLayout(new BorderLayout());
   
      JPanel panel_button = new JPanel();
      panel_button.setBackground(Color.LIGHT_GRAY);
      
      panel_button.add(form_generateButton());     
      
      JPanel panel_seed = new JPanel();
      panel_seed.setBackground(Color.LIGHT_GRAY);
      
      panel_seed.add(form_seedLabel());
      panel_seed.add(form_seedField());
      
      this.add(panel_button, BorderLayout.NORTH);
      this.add(panel_seed, BorderLayout.WEST);
          
   }     
      
   
   private JComponent form_generateButton() {
      generateButton = new JButton("Generate"); 
      generateButton.setPreferredSize(new Dimension(450,30));
      
      generateButton.setFont(new Font("Sans Serif", Font.PLAIN, 14));
      generateButton.setBackground(Color.LIGHT_GRAY);
      
      generateButton.addActionListener(new Listener("generateButton"));   
      
      return generateButton;  
   }
   
   private JComponent form_seedLabel() {  
      seedLabel = new JLabel("   Seed:  ");
      
      seedLabel.setFont(new Font("Sans Serif", Font.PLAIN, 14));
      
      return seedLabel;  
   }
   
   private JComponent form_seedField() {   
      seedField = new JTextField("default", 15);
      seedField.setHorizontalAlignment(SwingConstants.RIGHT);
      
      seedField.setFont(new Font("Sans Serif", Font.PLAIN, 14));
      
      return seedField;   
   }
     
   
   private void generate() {
   
      seed = seedField.getText();
      
      if (seed.equals("")) {
         long time = System.currentTimeMillis();
         seed = String.valueOf(time);
         
      } else if (!isInteger(seed)) {
         seed = String.valueOf(seed.hashCode());
      }   
      
      try {
         HX h = new HX(seed);
         root = h.getHash();
      } 
      catch(Exception e) {
         System.out.println("WARN -- " + e);
      }   
      
      System.out.println(seed + " > " + root);
   }
   
   /*
   private void generateOld() {
   
      long[] seedBuilder = new long[5];  
   
      String clientSeed = seedField.getText(); 
      if (clientSeed.equals("")) clientSeed = String.valueOf(System.currentTimeMillis());
      
      // Basic hash of seed                     
      seedBuilder[0] = (long)clientSeed.hashCode();                  
      
      // comboID = primary-char hash * concluding-char hash
      long comboID = clientSeed.substring(0,1).hashCode() * clientSeed.substring(clientSeed.length()-1).hashCode();                         
      
      // Basic hash * comboID
      seedBuilder[1] = seedBuilder[0] * comboID; 
      
      // Absolute value             
      seedBuilder[2] = Math.abs(seedBuilder[1]);                     
      
      // Raised to the 1.27
      seedBuilder[3] = (long)(Math.pow(seedBuilder[2], 1.27));
      
      // Multiplied by 9.9 until digits = 19
      seedBuilder[4] = (long)(Math.pow(9.9,(19-(seedBuilder[3]+"").length())) * seedBuilder[3]);
      
      p(clientSeed);
      p(seedBuilder[4]);
      if (Long.MAX_VALUE - seedBuilder[4] < new Long("10")) 
         p(true);
   }
   */
   
   private class Listener implements ActionListener {  
      private String ID;
   
      private Listener(String ID) {
         
         this.ID = ID;
      }   
   
      @Override
      public void actionPerformed(ActionEvent e) {
      
         switch(ID) { 
            case "generateButton":
               generate();
               break;
         }
      }
   } 
   
   private void p(Object o) {
      o = o+"";
      System.out.println(o);
   }
   
   private boolean isInteger(String input) {
      try {
         Integer.parseInt(input);
         return true;
      } catch(Exception e) {
         return false;
      }
   }
           
}
