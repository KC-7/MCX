import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MCX_0 extends JPanel  {

   private JButton generateButton;
   private JLabel seedLabel;
   private JTextField seedField;

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
   
      int[] seedBuilder = new int[5];  
   
      String clientSeed = seedField.getText();
      seedBuilder[0] = clientSeed.hashCode();   
      seedBuilder[1] = seedBuilder[0] * (100 - (clientSeed.substring(0,1).hashCode()));
      seedBuilder[2] = Math.abs(seedBuilder[0]);
     // seedBuilder[3] = seedBuilder[2] * 
      
      p(clientSeed);
      //p(seedBuilder[0]); 
      //p(seedBuilder[1]);
      p(seedBuilder[2]);
   
   }
   
   
   private class Listener implements ActionListener {  
      private String ID;
   
      private Listener(String ID) {
         
         this.ID = ID;
      }   
   
      @Override
      public void actionPerformed(ActionEvent e) {
      
         if(ID.equals("generateButton")) {
            generate();
            
         
         }
      }
   } 
   
   private void p(Object o) {
      o = (String)(o+"");
      System.out.println(o);
   }
}
