package studentClient.simpledb;

import java.sql.*;
import simpledb.remote.SimpleDriver;
import java.io.*;

public class SQLInterpreter {
    private static Connection conn = null;

    public static void main(String[] args) {
	   try {
			Driver d = new SimpleDriver();
			conn = d.connect("jdbc:simpledb://localhost", null);

			Reader rdr = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(rdr);

			boolean done;
			
			 do{
				// process one line of input
				System.out.print("\nSQL> ");
				String cmd = br.readLine().trim();
				System.out.println();
				done = ExecuteCommand(cmd);
				
		    }while (!done);
	    }
	    catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (conn != null)
					conn.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
    
    private static boolean ExecuteCommand(String cmd)
    {
    	if (cmd==null || cmd.startsWith("exit"))
			return true;
    	
		else if (cmd.startsWith("select"))
			doQuery(cmd);
		
		else if(cmd.startsWith("load"))
			LoadFile(cmd.substring(5));
			
		else
			doUpdate(cmd);
    	
    	return false;
    }
    
    public static void LoadFile(String fileName)
    {
    	try
    	{
    		System.out.println("File name = "+fileName);
    		
	    	FileReader frdr = new FileReader(fileName);
	    	BufferedReader br = new BufferedReader(frdr);
	    	
	    	String query;
	    	boolean done = false;
	    	
	    	do
	    	{
	    		query = br.readLine().trim();
	    		
	    		System.out.println("Query sent = "+query);
	    		
	    		if(query.equals(""))
	    			continue;
	    		
	    		done = ExecuteCommand(query);
	    		
	    	}while(!done || query!=null);
    	}
    	
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
	private static void doQuery(String cmd) {
		try {
		    Statement stmt = conn.createStatement();
		    ResultSet rs = stmt.executeQuery(cmd);
		    ResultSetMetaData md = rs.getMetaData();
		    int numcols = md.getColumnCount();
		    int totalwidth = 0;

		    // print header
		    for(int i=1; i<=numcols; i++) {
				int width = md.getColumnDisplaySize(i);
				totalwidth += width;
				String fmt = "%" + width + "s";
				System.out.format(fmt, md.getColumnName(i));
			}
			System.out.println();
			for(int i=0; i<totalwidth; i++)
			    System.out.print("-");
		    System.out.println();

		    // print records
		    while(rs.next()) {
				for (int i=1; i<=numcols; i++) {
					String fldname = md.getColumnName(i);
					int fldtype = md.getColumnType(i);
					String fmt = "%" + md.getColumnDisplaySize(i);
					if (fldtype == Types.INTEGER)
						System.out.format(fmt + "d", rs.getInt(fldname));
					else
						System.out.format(fmt + "s", rs.getString(fldname));
				}
				System.out.println();
			}
			rs.close();
		}
		catch (SQLException e) {
			System.out.println("SQL Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void doUpdate(String cmd) {
		try {
		    Statement stmt = conn.createStatement();
		    int howmany = stmt.executeUpdate(cmd);
		    System.out.println(howmany + " records processed");
		}
		catch (SQLException e) {
			System.out.println("SQL Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
}