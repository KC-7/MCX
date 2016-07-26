import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MCX_0 extends JPanel  {

   private JButton generateButton;
   private JLabel seedLabel;
   private JTextField seedField;
   private JPanel gameFrame;

   private JLabel[][] grid;
   
   private String seed;
   private long root;
   private Object[] tree;
   
   // T1

   public MCX_0() {
   
      this.setLayout(new FlowLayout());
      
      generateButton = new JButton("Generate"); 
      generateButton.setPreferredSize(new Dimension(450,40));  
      generateButton.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               createRoot();
               createWorld();
               renderWorld();
            }});
         
      seedLabel = new JLabel("Seed:");
          
      seedField = new JTextField("default");
      seedField.setPreferredSize(new Dimension(100,20));
      seedField.addKeyListener(
         new KeyAdapter() {
            public void keyTyped(KeyEvent e) { 
               if (seedField.getText().length() >= 14 )
                  e.consume(); 
            }});
      
      gameFrame = new JPanel(true);
      gameFrame.setPreferredSize(new Dimension(448,224));
      gameFrame.setLayout(new GridLayout(28,14));
      gameFrame.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
      
      grid = new JLabel[28][14];
      
      for (JLabel[] row : grid) {
         for (JLabel cell : row) {
            cell = new JLabel();
            gameFrame.add(cell);
         }   
      }
           
      this.add(generateButton);
      this.add(seedLabel);
      this.add(seedField);   
      this.add(gameFrame);     
   } 
   
   // T2    
      
   private void createRoot() {
      seed = seedField.getText();
      
      if (seed.equals("")) seed = String.valueOf(System.currentTimeMillis());
      else if (isString(seed)) seed = String.valueOf(seed.hashCode());
      
      try {
         HX h = new HX(seed);
         root = h.getHash();
      } 
      catch(Exception e) {
         p("WARN -- " + e);
      }   
      
      if (String.valueOf(Math.abs(root)).length() < 19) {
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
   
   private void createWorld() {
      tree = new Object[20];
      boolean positive = root > 0;  
      String stringRoot = String.valueOf(Math.abs(root));
      int[] rootArray = stringRoot.chars().map(Character::getNumericValue).toArray();
      
      if (positive) tree[0] = "forest";   
      else tree[0] = "plains";     
   }
   
   private void renderWorld() {
   
      for (JLabel[] row : grid) {
         for (JLabel cell : row) {
            
         }   
      }
   }
   
   // T3
   
   private void p(Object o) {
      o = String.valueOf(o);
      System.out.println(o);
   }
   
   private boolean isString(String input) {
      boolean isInt;
      boolean isLong;
      try {
         Integer.parseInt(input);
         isInt = true;
      } 
      catch(Exception e) {
         isInt = false;
      }
      
      try {
         Long.parseLong(input);
         isLong = true;
      } 
      catch(Exception e) {
         isLong = false; 
      }
      return !(isInt || isLong);   
   }
           
}
