
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;





public class ListModelExample extends JPanel {

  JList list;

  DefaultListModel model;

  int counter = 15;
  
  
  private class Email{
      
      
      
  }
  
  
  
    private class EmailInfo {
          
        public String subject ;
      //  public Date last_accessed;
        public String to ;
        public String filename;
        
        
        public EmailInfo(String emailTo, String emailSubject, String emailFileName ) {
            to = emailTo;
            subject = emailSubject;
            filename = emailFileName;
          //  last_accessed = emailLastAccessed;
        }

        public String toString() {
            
            
            for (int i = to.length(); i < 40; i++)
            to = to + " ";
            
            return to + "   " + subject;
            
        }
        
        public String getFilename() {
            return filename;
        }        
        
        
        
    }
    

  

  public ListModelExample() {
    setLayout(new BorderLayout());
    model = new DefaultListModel();
    
  /*   String[] data = {"one", "two", "three", "four"};
     for(int i=0; i<data.length; i++) {
         model.addElement(new EmailInfo("deyvid.william@gmail.com","Subject 1"));
      }*/
    
     model.addElement(new EmailInfo("deyvid.william@gmail.com","Subject 1","file1.xml"));
     model.addElement(new EmailInfo("prankaj1@yahoo.com","Subject 2","file2.xml"));
     model.addElement(new EmailInfo("romeo.honvo@gmail.com","Subject 3","file3.xml"));
     
     
    list = new JList(model);
    JScrollPane pane = new JScrollPane(list);
    JButton addButton = new JButton("Add Element");
    JButton removeButton = new JButton("Remove Element");
    
    
    JButton print = new JButton("Print");
    
    
    String[] columnNames = {"Sender", "Subject"};
    Object[][] data = {
        {"Kathy", "Smith"
        },
        {"John", "Doe"}
    };

     JTable table = new JTable(data, columnNames);
    
    
    
    
 /*   for (int i = 0; i < 15; i++)
      model.addElement("Element " + i);
*/
    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        model.addElement("Element " + counter);
        counter++;
      }
    });
    removeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (model.getSize() > 0)
          model.removeElementAt(0);
      }
    });

 /*   print.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
       
      int selected[] = list.getSelectedIndices();
      System.out.println("Selected Elements:  ");

      for (int i = 0; i < selected.length; i++) {
        EmailInfo element = (EmailInfo) list.getModel()
            .getElementAt(selected[i]);
        System.out.println("  " + element.getFilename());
      }
       
      
      
      
      
      }
    });
   */ 
        
  list.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                
                if (!evt.getValueIsAdjusting()){
                      
                int selected[] = list.getSelectedIndices();
                System.out.println("Selected Elements:  ");

                for (int i = 0; i < selected.length; i++) {
                  EmailInfo element = (EmailInfo) list.getModel()
                      .getElementAt(selected[i]);
                  System.out.println("  " + element.getFilename());
                  
                }
                }   
            }
        });
    
    
    
   // add(pane, BorderLayout.NORTH);
   
   add(pane, BorderLayout.NORTH);
    add(addButton, BorderLayout.WEST);
    add(removeButton, BorderLayout.EAST);
     add(print, BorderLayout.SOUTH);
    
  }

  public static void main(String s[]) {
    JFrame frame = new JFrame("List Model Example");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(new ListModelExample());
    frame.setSize(260, 200);
    frame.setVisible(true);
  }
}




