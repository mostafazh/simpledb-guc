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
import simpledb.remote.SimpleDriver;
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
	
	public DisplayQuery(String message,String qry)
	{
		super(message);
		
		try{
			
			this.d = new SimpleDriver();
			this.conn = d.connect("jdbc:simpledb://localhost", null);
			this.stmt = conn.createStatement();
		
			rs = stmt.executeQuery(qry);
		}
		
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		pane = new JScrollPane();
		pane.setBounds(30,30,300,300);
		
		outputTable = new JTable();
		
		pane.setViewportView(outputTable);
		outputTable.setFillsViewportHeight(true);
		
		setLayout(null);
		setBounds(30,30,400,400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		add(pane);
		
		generateRecords();
		
		setVisible(true);
		repaint();
		validate();
	}
	
	public void generateRecords()
	{
		try
		{
			ResultSetMetaData rsm = rs.getMetaData();
			
			int columns = rsm.getColumnCount();
			
			model = new DefaultTableModel();
			
			for(int i=columns;i>0;i--)
				model.addColumn(rsm.getColumnName(i));
			
			while(rs.next())
			{
				Vector<Object> newRow = new Vector<Object>();
				
			//	for(int i=1;i<=columns;i++)
					newRow.addElement(rs.getString("sname"));
					newRow.addElement(rs.getInt("gradyear"));
				
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
