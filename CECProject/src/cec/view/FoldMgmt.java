package cec.view;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
                    final JTree tree = new JTree(addTree(root));	

                    tree.addTreeSelectionListener(
                                    new TreeSelectionListener() {
                            public void valueChanged(TreeSelectionEvent select) {

                                    TreePath fullpath = select.getPath();
                                   // System.out.println("you selected");
                                    selectedFolder="";
                                    
                                    for (int i=0; i<fullpath.getPathCount()-1; i++)
                                    {						
                                                    selectedFolder+=fullpath.getPathComponent(i).toString()+"\\";
                                    }
                                    selectedFolder+=fullpath.getLastPathComponent().toString();
                                    selectedFolder=parent+selectedFolder;
                                    System.out.println("you selected----   "+selectedFolder);
                            }
                    });			
                    tree.setSize(100, 400);//.setSize(200.200);
                    tree.setVisible(true);			
                    JScrollPane scrollpane = new JScrollPane(tree);

                    JButton Bcreate = new JButton("CREATE");
                    Bcreate.addActionListener
                    (new ActionListener(){public void actionPerformed(ActionEvent click) {create_click();}});
                        			                           
                    JButton Bdelete = new JButton("DELETTE");
                    Bdelete.addActionListener
                    (new ActionListener(){public void actionPerformed(ActionEvent click) {delete_click();}});

                    JPanel Buttons = new JPanel();
                    Buttons.add(Bcreate,BorderLayout.WEST);Buttons.add(Bdelete,BorderLayout.EAST);           				

                                    

                    tree.addKeyListener(KenterL);



                    final JPopupMenu Pmenu=new JPopupMenu();      
                    JMenuItem menuItem;
                    menuItem = new JMenuItem("Delete Ctrl+D");
                    Pmenu.add(menuItem);
                    menuItem = new JMenuItem("Create Ctrl+C");
                    Pmenu.add(menuItem);						

                    menuItem.addActionListener(new ActionListener()
                        {
                           public void actionPerformed(ActionEvent e){}
                           });
                                tree.addMouseListener(new MouseAdapter()
                                {
                                   public void mouseReleased(MouseEvent Me)
                                   {
                                        int selFold = tree.getRowForLocation(Me.getX(), Me.getY());
                                        TreePath selection = tree.getPathForLocation(Me.getX(), Me.getY());
                                        if(selFold != -1)                                     
                                             Pmenu.show(Me.getComponent(), Me.getX(), Me.getY());	
                                   }
                        }
                    );

                theTreeMgt.add(scrollpane);		
                theTreeMgt.add(Buttons);

                theTreeMgt.setSize(400, 500);
                theTreeMgt.setVisible(true);		

        }	
		
		
		
		static KeyListener KenterL = new KeyListener()
		{
			
			//Perform action Cut when you press ctrl+X keys to cut the selected cell
			public void keyPressed(KeyEvent ke) 
			{
				
				if(ke.isControlDown())
				{	
					if(ke.getKeyCode()==ke.VK_D)
					{ 
						delete_click();
					}
					
					else if (ke.getKeyCode()==ke.VK_C)
					{						
						create_click();
					}					
					
				}
				
				if(ke.getKeyCode()==ke.VK_ESCAPE)
				{
					// JOptionPane.showMessageDialog(null, "ESCAPE");
				}
				
			}	
			
			
			@Override
			public void keyReleased(KeyEvent ke) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent ke) {
				// TODO Auto-generated method stub
				
			}				
		};

		
		
        
        
        
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
            
            
            
            
            
            
            
            
            public static void create_click()
            {
        		if(selectedFolder!=null)
                {
                        String folderName = JOptionPane.showInputDialog(null, "Folder Name","",  1);
                        btnCreate(selectedFolder,folderName);		        	
                }
                else
                        JOptionPane.showMessageDialog(null, "SELECT LOCATION");
        	}
            public static void delete_click()
            {
            if(selectedFolder!=null)			        		
                    btnDelete(selectedFolder);
            else
                    JOptionPane.showMessageDialog(null, "SELECT FOLDER TO DELETE");	
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
		String par ="G:\\";//"";//
		String pat ="G:\\SUMMER2013";//"emails";
		createJTreeFrame(par,pat);                
		System.out.println("End");

	}
}