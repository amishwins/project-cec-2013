import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.Console;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.TreeSelectionEvent; 
import javax.swing.event.TreeSelectionListener;

	class FoldMgmt
	{							
		static String selectedFolder;
		public static void createJTreeFrame(final String parent,final String root)
		{
                    
                    JFrame theTreeMgt = new JFrame();
                    theTreeMgt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    theTreeMgt.setLayout(new GridLayout(1,1));
                    //JTree tree = new JTree();
                    JTree tree = new JTree(addTree(root));	

                    tree.addTreeSelectionListener(
                                    new TreeSelectionListener() {
                            public void valueChanged(TreeSelectionEvent select) {

                                    TreePath fullpath = select.getPath();
                                    System.out.println("you selected");
                                    selectedFolder="";
                                    
                                    for (int i=0; i<fullpath.getPathCount()-1; i++)
                                    {						
                                                    selectedFolder+=fullpath.getPathComponent(i).toString()+"\\";
                                    }
                                    selectedFolder+=fullpath.getLastPathComponent().toString();
                                    selectedFolder=parent+selectedFolder;
                            }
                    });			
                    tree.setSize(100, 400);//.setSize(200.200);
                    tree.setVisible(true);			
                    JScrollPane scrollpane = new JScrollPane(tree);







                    JButton Bcreate = new JButton("CREATE");
                    Bcreate.addActionListener
                    ( 
                        new ActionListener() 
                        {				
                            public void actionPerformed(ActionEvent click)
                            {
                                if(selectedFolder!=null)
                                {
                                        String folderName = JOptionPane.showInputDialog(null, "Folder Name","",  1);
                                        btnCreate(selectedFolder,folderName);		        	
                                }
                                else
                                        JOptionPane.showMessageDialog(null, "SELECT LOCATION");
                            }
                        }
                    );

			
                JButton Bdelete = new JButton("DELETTE");
                Bdelete.addActionListener
                ( 
                                new ActionListener()
                                {						
                                    public void actionPerformed(ActionEvent click)
                                    {        	                                       
                                        if(selectedFolder!=null)			        		
                                                btnDelete(selectedFolder);
                                        else
                                                JOptionPane.showMessageDialog(null, "SELECT FOLDER TO DELETE");				        	
                                    }
                                }
                );



                JPanel Buttons = new JPanel();
                Buttons.add(Bcreate,BorderLayout.WEST);Buttons.add(Bdelete,BorderLayout.EAST);


                theTreeMgt.add(scrollpane);		
                theTreeMgt.add(Buttons);

                theTreeMgt.setSize(400, 500);
                theTreeMgt.setVisible(true);		

        }	
        public static String Selection()
        {			
                String selection="SELECTION";
                return selection;
        }
            public static DefaultMutableTreeNode addTree(String dirname) 
            {
                    File file = new File(dirname);
                    DefaultMutableTreeNode root = new DefaultMutableTreeNode();

                    root.setUserObject(file.getName());
                    if (file.isDirectory())
                    {
                            File files[] = file.listFiles();
                            for (int i = 0; i < files.length; i++) 
                            {
                                    root.insert(addTree(files[i].getPath()), i);
                            }
                    }
                    return (root);
            }

            public static void btnDelete(String folderName)//,String folderName) 
            {

                    File direct=new File(folderName);
                    if(!direct.exists())                 			           
                       JOptionPane.showMessageDialog(null, " This folder does not exist...");                 
                    else               	    	  
                       direct.delete();//seems that can delete only if folder is empty.        
            }
            public static void btnCreate(String pathName,String folderName)//,DefaultMutableTreeNode node) 
            {					   
                   File direct=new File(pathName+"/"+folderName);                   
                   if(direct.exists())                 
                       JOptionPane.showMessageDialog(null, " This folder already exists");                  
                   else           		    	             
                      direct.mkdir();		    	  
                   
            }					
	public static void main(String[] args) 
	{
		//JFrame theTree = new JFrame();
		
		
		System.out.println("Start");
		//final String pat = "emails";//"G:\\SUMMER2013";
		String par ="";//G:\\";
		String pat ="emails";//"G:\\SUMMER2013";//"emails";
		createJTreeFrame(par,pat);                
		System.out.println("End");

	}
}