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
		public static void createJTreeFrame(final String root)
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
					//System.out.println(fullpath+"+++"+fullpath.getPathCount()+"==="+fullpath.toString());
					System.out.println("you selected");
					selectedFolder="";
					for (int i=0; i<fullpath.getPathCount()-1; i++)
					{						
							//System.out.println(fullpath.getPathComponent(i).toString());
							//System.out.print(fullpath.getPathComponent(i).toString()+"\\");
							selectedFolder+=fullpath.getPathComponent(i).toString()+"\\";
					}
					selectedFolder+=fullpath.getLastPathComponent().toString();
					//System.out.println(fullpath.getLastPathComponent().toString());
					//selectedFolder= fullpath.getLastPathComponent().toString();
					System.out.println("YOU SELECTED THIS  -"+selectedFolder);				
					
					
					
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
					        	//String folderName = "NEWfOLDER";//System.console().readLine();
					        	//String folderName = JOptionPane.showInputDialog(null, "Folder Name","",  1);
					        			 
					        	JOptionPane.showMessageDialog(null, "COMING SOON");
					        	//btnCreate(root,folderName);					        	
					        	//btnCreate(root,selectedFolder);
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
				        	//String folderName = "this";//"NEWfOLDER";
				        	//String folderName = JOptionPane.showInputDialog(null, "Folder Name","",  1);
				        	
				        	JOptionPane.showMessageDialog(null, "COMING SOON");
				        	//btnDelete(root,folderName);				        	
				        	//btnDelete(root,selectedFolder);
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
	
		public static void btnDelete(String pathName,String folderName) 
		{
			File direct=new File("this");//(pathName+"/"+folderName);
			//File direct=new File(pathName+"/"+folderName);
			String tmp = direct.getPath();
			String atmp = direct.getAbsolutePath();
			if(!direct.exists()){			
				
		           System.out.println("PATH = :"+tmp+"-This folder does not exist...");		           
		       }else{		    	  
		    	   System.out.println("D A PATH = :"+atmp+"-");
		    	   //direct.delete();//seems that can delete only if folder is empty.
		       }
			
			
		}
		public static void btnCreate(String pathName,String folderName)//,DefaultMutableTreeNode node) 
		{			
				    
		       File direct=new File(pathName+"/"+folderName);
		       String tmp = direct.getPath();
		       String atmp = direct.getAbsolutePath();
		       if(direct.exists()){
		           System.out.println("PATH = :"+tmp+"-This folder already exists...");
		           
		       }else{
		    	 
		    	  System.out.println("C A PATH = :"+atmp+"-");
		    	   direct.mkdir();
		       }
		}
		
		
		
			
	
			
	public static void main(String[] args) 
	{
		//JFrame theTree = new JFrame();
		
		
		System.out.println("Start");
		//final String pat = "emails";//"G:\\SUMMER2013";
		String pat ="emails";
		createJTreeFrame(pat);                
		System.out.println("End");

	}
}