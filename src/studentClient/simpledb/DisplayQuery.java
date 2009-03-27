/**
 * 
 */
package studentClient.simpledb;

/**
 * @author Moheb
 *
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.*;

public class DisplayQuery extends JFrame {

	/**
	 * @param args
	 */
	ResultSet rs;
	Driver d;
	Statement stmt;
	Connection conn;
	JTable outputTable;
	JScrollPane pane;
	DefaultTableModel model;
	
	public DisplayQuery(Driver d,Connection conn,Statement stmt,String qry)
	{
		this.d = d;
		this.conn = conn;
		this.stmt = stmt;
		
		try
		{
			rs = stmt.executeQuery(qry);
		}
		
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		pane = new JScrollPane();
		
		outputTable = new JTable(new DefaultTableModel());
		
		pane.setViewportView(outputTable);
		
		setLayout(null);
		setBounds(30,30,400,400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		add(pane);
		
		generateRecords();
		
		setVisible(true);
	}
	
	public void generateRecords()
	{
		try
		{
			ResultSetMetaData rsm = rs.getMetaData();
			
			int columns = rsm.getColumnCount();
			
			model = new DefaultTableModel();
			
			for(int i=0;i<columns;i++)
				model.addColumn(rsm.getColumnName(i+1));
			
			while(rs.next())
			{
				Vector newRow = new Vector();
				
				for(int i=1;i<=columns;i++)
					newRow.addElement(rs.getObject(i));
				
				model.addRow(newRow);
			}
			
			outputTable.setModel(model);
		}
		
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
}
