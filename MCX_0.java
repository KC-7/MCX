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
      } else if (isString(seed)) {
         seed = String.valueOf(seed.hashCode());
      }   
      
      try {
         HX h = new HX(seed);
         root = h.getHash();
      } catch(Exception e) {
         p("WARN -- " + e);
      }   
      
      if (String.valueOf(root).length() < 19) {
         String st = String.valueOf(Math.abs(root));
         
         int i = 5;
         String digit = st.substring(i, i+1);
         while (digit.equals("0") || digit.equals("9")) {
            i++;
            digit = st.substring(i, i+1);
         }
         st = digit + st;
         root = (Long.parseLong(st) *  (root/Math.abs(root)));
      }   
      
      p(seed);
      p(root);
   }
   
     
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
   
   private boolean isString(String input) {
      boolean isInt;
      boolean isLong;
      try {
         Integer.parseInt(input);
         isInt = true;
      } catch(Exception e) {
         isInt = false;
      }
      
      try {
         Long.parseLong(input);
         isLong = true;
      } catch(Exception e) {
         isLong = false; 
      }
      
      return (!isInt && !isLong);   
   }
           
}
