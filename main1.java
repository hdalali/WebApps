package com.webapps.proj1;

import java.util.*;
import java.sql.*;
import java.text.*;

public class Proj1 {
	public static void main(String[] args) throws Exception{
		int ab,ac;
		
		do{
		System.out.println("Welcome to DBMS system,select one of the following");
		System.out.println("1:To goto get-the-database/user/password state\n0:to exit from databse");
		System.out.println("");
		Scanner sl=new Scanner(System.in);
		
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Scanner sc = new Scanner(System.in);
		
		try{
		    ab=sl.nextInt();}catch (Exception e) {
		    	System.out.println("Invalid Input, Please enter valid input next time");
		    	System.out.println("");
		    	ab=1;continue;
		}
		
		if (ab==0) break;
		
		Scanner cs = new Scanner(System.in);
		System.out.println("Enter Database Name");
		String db=cs.nextLine();
		
		String dbs="jdbc:mysql:///"+db+"?autoReconnect=true&useSSL=false";
		try{
		Connection cnt=DriverManager.getConnection(dbs,"root","unlock.123");}
		catch(Exception e){
			System.out.println("Database does not exist,Please enter correct database name next time,restarting the system");
			main(new String[]{"aa", "bb", "cc"});
		}
		
		System.out.println("Enter User Name");
		String un=cs.nextLine();
		System.out.println("Enter Password");
		String pw=cs.nextLine();
		
		
		
		
		Connection connection=null;
		//Connection connection = DriverManager.getConnection("jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false","hdalali", "unlock.123");
		try{
		connection = DriverManager.getConnection(dbs,un, pw);}
		catch(Exception e){
			System.out.println(e.getMessage());
			System.out.println("Invalid User Name/Password: Please enter correct details next time,restarting the system");
			System.out.println("");
			main(new String[]{"aa", "bb", "cc"});
		}
		
		System.out.println("Your credentials are valid");
		System.out.println("");
		System.out.println("Enter a number based on what u wanna do");
		System.out.println("1:To print movies featuring given star\n2:insert new star\n3:add a customer\n4:Delete a customer\n5:show metadata\n6:Enter a valid SQL Command");
		Scanner sk=new Scanner(System.in);
		int j,p;
		try{
		    j=sk.nextInt();}catch (Exception e) {
			j=8;
			//System.err.println("Improper Input " + e);
		}
		    
		
		do{
		switch(j){
		case 1: System.out.println("You have chosen to print movies featuring given star");
		starInfo(connection);
		break;
		case 2: System.out.println("You have chosen to add a star");
		addStar(connection);
		break;
		case 3: System.out.println("You have chosen to add customer");
		addCustomer(connection);
		break;
		case 4: System.out.println("You have chosen to delete a customer");
		DeleteElement(connection);
		break;
		case 5: System.out.println("You have chosen to show metadata of database "+db);
		System.out.println("");
		MetData(connection);
		break;
		case 6: System.out.println("You have chosen to Enter valid SQL query, choose what kind of query you want to execute");
		MetaData(connection);
		break;
		default: System.out.println("Invalid Input: Please Enter proper option");
		break;
		}
		System.out.println("To continue, press 1 else press other key to return to main menu");
		Scanner scn=new Scanner(System.in);
		try{
		p=scn.nextInt();
		}catch(Exception e){
			p=0;
		}
		if(p==1){
			System.out.println("Enter a number based on what u wanna do");
			System.out.println("1:To print movies featuring given star\n2:insert new star\n3:add a customer\n4:Delete a customer\n5:show metadata\n6:Enter a valid SQL Command");
			Scanner scp=new Scanner(System.in);
			int q=scp.nextInt();
			j=q;
		}
		else{
			System.out.println("U have chosen to return to main menu, bye");
			System.out.println("");
			break;
			
		}
		}while(j!=0);
		
		}while(ab==1);
	}
		//Method for option 1-print star info
	    public  static void starInfo(Connection connection) throws SQLException{ 
        Scanner sc = new Scanner(System.in);
        System.out.println("Id or Name ");
        int Id ;
        
        String first="";
        String Last = "";
        String name = "";
        String name1 = "";
        
        String s = sc.nextLine();
        try{
           Id = Integer.parseInt(s);
            }
        catch(Exception e){
            Id=-1;
        }
        if(Id == -1){
            String[] sArray = s.split(" ");
            if (sArray.length==2){
                first = "\""+sArray[0]+"\"";
                Last = "\""+sArray[1]+"\"";
            
            }else {
                 name = sArray[0];
                 name1= "\""+name+"\"";
                    }   
        }
    Statement select = connection.createStatement();
    ResultSet result = null;
    if(Id!=-1){
     result = select.executeQuery("select * from movies where id in (select movie_id from stars_in_movies where star_id = "+Id+")");
    }else{
        if(!name.isEmpty()){
            
            result = select.executeQuery("select * from movies where id in (select movie_id from stars_in_movies where star_id in (select id from stars where last_name = " +name1+ "or first_name = "+name1+"))");
        }
        else {
            
            result = select.executeQuery("select * from movies where id in (select movie_id from stars_in_movies where star_id in (select id from stars where first_name = "+first+"AND last_name = "+Last +"))");
        }
            
        }
    
    
    System.out.println("The results of the query are \n");
    ResultSetMetaData metadata = null;
    try{
        metadata = result.getMetaData();
        
    }
    catch(Exception e){System.out.println("No data");}
    
    if(!result.next()){
        System.out.println(" -----  no records for your query -----");
           }    
    result.previous();
    
    for(int i=0;i<metadata.getColumnCount();i++){
        while(result.next()){
           
            System.out.println("Movie Id = " + result.getInt(1));
            System.out.println("Movie Title = " + result.getString(2) );
            System.out.println("Release Year= " + result.getString(3));
            System.out.println("Director = " + result.getString(4));
            //System.out.println("Diresctor = " + result.getString(4));
            //1System.out.println("Diresctor = " + result.getString(4));
            System.out.println();
       }
    }
            }
	    
	  //Method for option 2-Add Star 
	    public static void addStar(Connection connection)throws SQLException, ParseException {
	        Scanner sc = new Scanner(System.in);
	        System.out.println("enter name");
	            
	                String name  = sc.nextLine();
	                String[] sArray = name.split(" ");
	                
	                String first_name="";
	                String Last_name="";
	                if(sArray.length==1){
	                    first_name = "\""+" "+"\""+",";
	                    Last_name ="\""+sArray[0]+"\""+",";
	                    }else{
	                 first_name = "\""+sArray[0]+"\""+",";
	                 Last_name = "\""+sArray[1]+"\""+",";
	                }
	                System.out.println("enter DOB in yyyy-mm-dd format ");
	                //sc.nextLine();
	                
	                String DOB  = "\""+sc.nextLine()+"\""+",";
	                System.out.println("enter photo_url");
	                String photo_url  = "\""+sc.next()+"\""+")";
	                
	                //System.out.println(first_name+Last_name+DOB + photo_url);
	                String updateString =""; 
	        
	            try{
	                    
	                    updateString = "Insert  INTO moviedb.stars" + "(first_name ,last_name ,dob,photo_url)" +" VALUES (" +first_name+Last_name + DOB+photo_url ;
	                    Statement updateStars = connection.createStatement();
	                    updateStars.executeUpdate(updateString);
	                    System.out.println("--------Added Customer Successfully!---------");
	                    }
	            
	                catch(Exception e){System.out.println(e);
	                    }
	    }

	    
	    //Method for option 3-Add customer	    
	    public static void addCustomer(Connection connection)throws Exception{
	    	Scanner sc = new Scanner(System.in);
	        
	        System.out.println("enter cc_id");
	        //sc.nextLine();
	        String cc_id  = sc.nextLine();
	        String cc_id1 = "\""+cc_id+"\""+",";
	        
	        
	        String updateString ="";
	        
	        ResultSet result1=null;
	        Statement select = connection.createStatement();
	        result1 = select.executeQuery("Select * from moviedb.creditcards where id ='" + cc_id +"'");
	        ResultSetMetaData metadata = result1.getMetaData();
	        result1.previous();
	        
	        
	       
	        
	        if(result1.next()){
	            System.out.println("Credit card found !");
	            System.out.println("enter name");
	        
	            String name  = sc.nextLine();
	            String[] sArray = name.split(" ");
	            
	            String first_name="";
	            String Last_name="";
	            if(sArray.length==1){
	                first_name = "\""+" "+"\""+",";
	                Last_name ="\""+sArray[0]+"\""+",";
	                }else{
	             first_name = "\""+sArray[0]+"\""+",";
	             Last_name = "\""+sArray[1]+"\""+",";
	            }
	            System.out.println("enter address");
	            //sc.nextLine();
	            String address  = "\""+sc.nextLine()+"\""+",";
	            System.out.println("enter email");
	            String email  = "\""+sc.next()+"\""+",";
	            System.out.println("enter password");
	            String password  = "\""+sc.next()+"\""+")";
	            //System.out.println(first_name+Last_name+cc_id1 + address+email+password);
	            
	            try{
	                updateString = "Insert  INTO moviedb.customers" + "(first_name ,last_name ,cc_id,address,email,password)" +" VALUES (" +first_name+Last_name+cc_id1 + address+email+password ;
	                Statement updateStars = connection.createStatement();
	                updateStars.executeUpdate(updateString);
	                System.out.println("--------Added Customer Successfully!---------");
	                }
	        
	            catch(Exception e){System.out.println(e);
	                }
	        }else{
	            System.out.println("----Credit card does not exist in records!----");
	            addCustomer(connection);
	    }
	    

	    }

	    
	  //Method for option 4-Deleting from Database
	    public  static void DeleteElement(Connection connection) throws SQLException{
	    	Statement select = connection.createStatement();
			Scanner sc=new Scanner(System.in);
			System.out.println("Enter Customer id");
			try{
				int x=sc.nextInt();
				System.out.println(x);
			String query="DELETE FROM customers WHERE id="+x;
			System.out.println(query);// Delete this if necessary
			try{
				int k=select.executeUpdate(query);
				if(k!=0)
					System.out.println("Deleted the row");
				else
					System.out.println("Row not found.");}
			catch(Exception e)
			{
				System.out.println("error in query"+e);
			}}
			catch(Exception w){
				System.out.println("You have given incorrect input");
			}
	    }
	    
	  //Method for option 5-Meta Data
	    public  static void MetData(Connection connection) throws SQLException{
	    	Statement select = connection.createStatement();
	    	ResultSet result = select.executeQuery("Show tables");
	    	ResultSetMetaData metadata = result.getMetaData();
	    	
	    	int cnt=1;
	    	String su;
	    	System.out.println("The name of the tables are");
	    	ArrayList<String> sa=new ArrayList<String>();
	    	ArrayList<String> sb=new ArrayList<String>();
	    	while(result.next()){
	    		System.out.println(result.getString(cnt));
	    		su="Select * from "+result.getString(cnt);
	    		sb.add(result.getString(cnt));
	    		sa.add(su);
	    	}
	    	System.out.println("");
	    	System.out.println("Each Tables' attribute and its datatype is mentioned in the below format");
	    	System.out.println("Attribute(Data Type: )");
	    	System.out.println("");
	    	
	    	for(int h=0;h<sa.size();h++){
	    		ResultSet result1 = select.executeQuery(sa.get(h));
		    	ResultSetMetaData metadata1 = result1.getMetaData();
		    	System.out.println("For the table "+"'"+sb.get(h)+"'"+","+" attribute and corresponding data types are");
		    	for (int i = 1; i <= metadata1.getColumnCount(); i++)
	                System.out.print(metadata1.getColumnLabel(i)+"("+"Data Type:"+metadata1.getColumnTypeName(i)+")"+"\t");
		    	System.out.println("");
		    	System.out.println("");
	    	}
	    	
	    	
	    }
	    
	    //Method for option 6-Enter Valid SQL Query
	    public  static void MetaData(Connection connection) throws SQLException{
	    	
			Scanner sc= new Scanner(System.in);
			
			Statement select = connection.createStatement();
			System.out.println("Select one among the following");
			System.out.println( "1 : Update "
					+ " 2 : Select "
					+ " 3 : Delete "
					+ " 4 : Insert ");
			int i=0,z=0;
			int x=5;
			try{
				x=sc.nextInt();
			}
			catch(Exception e)
			{
				//System.out.println("Enter a valid integer");
				x=10;
			}
			String s=null;
			String f_name=null;
			boolean exit=true;
			ResultSet rs=null;
			String input =null;
			Scanner temp=new Scanner(System.in).useDelimiter("\\+");
			//till not zero keep on rotating the loop
			while(x!=0){
			switch(x){
			case 1:
				System.out.println("You have chosen to update the database. Enter the query and press + and carriage return to execute the query");
					String update=temp.next();
					PreparedStatement ps=connection.prepareStatement(update);
				try{	 i=ps.executeUpdate();	
				System.out.println(i+ " row updated");}
				catch(Exception e)
				{
					System.out.println("Error occured while updating "+e);
				}
			break;
			case 2:
				//Select query
				
				System.out.println("Enter query to select, make sure you end it with + ");
				try{
				s=temp.next();	
				rs =select.executeQuery(s);
				//System.out.println(rs.toString());
				while(rs.next())
				{
					int j=0;
				for(i=1;i<=rs.getMetaData().getColumnCount();i++)
				{				
					System.out.print(rs.getString(i)+ "   ");
					
				}
				System.out.println();
				
				}}
				catch(Exception e)
				{
					System.out.println("Error occured "+e);
				}
				
				break;
			case 3: //Delete- Row will be updated if any one of the parameter is met
					//It follows cascade delete pattern
				
				System.out.println("Deleting Items ");
				try{s=temp.next();
				PreparedStatement myStatement=connection.prepareStatement(s);
				int res=myStatement.executeUpdate();
				System.out.println(res+ " rows deleted");}
				catch(Exception e)
				{
					System.out.println("Error occured "+e);
				}
				break;
			case 4: //Insert
				//wont work a foreign key constraint fails
				//other than credit card you don't have to insert primary key
				//foreign key if any must match with primary key
				
				System.out.println("Insert Enter table name");
				System.out.println("Defining new delimiter to read entire statement +");
				Scanner temp1=new Scanner(System.in).useDelimiter("\\+");
				try{
				s=temp1.next();
				PreparedStatement mst=connection.prepareStatement(s);
				i=mst.executeUpdate();
				System.out.println(i);}
				catch(Exception e)
				{
					System.out.println("Error occured "+e);
				}
				break;
			default:
					System.out.println("Not valid");
					break;
			
			}
			
			System.out.println("to continue select option from the following or to exit press 0");
			System.out.println( "1 : Update "
					+ " 2 : Select "
					+ " 3 : Delete "
					+ " 4 : Insert ");
			try{x=sc.nextInt();}catch (Exception e){
				x=5;
				System.out.println("You have entered wrong input");
			}
			}
			sc.close();
			System.out.println("Come back again");
	    }
	    
	    
	
}
