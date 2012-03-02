import javax.swing.SwingUtilities;
import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.ComponentOrientation;
import java.sql.*;





public class Query3 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JScrollPane scrollPane = null;
	private static JTextArea resultTextArea = null;
	private JLabel jLabel = null;
	private static JTextField tableTextField = null;
	private JButton submittButton = null;
	static int table_id;

	
	

	
	/**
	 * This method initializes scrollPane	
	 * 	
	 * @return java.awt.ScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setMinimumSize(new Dimension(485, 100));
			scrollPane.setPreferredSize(new Dimension(485, 200));
			scrollPane.setViewportView(getResultTextArea());
		}
		return scrollPane;
	}

	/**
	 * This method initializes resultTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getResultTextArea() {
		if (resultTextArea == null) {
			resultTextArea = new JTextArea();
		}
		return resultTextArea;
	}

	/**
	 * This method initializes tableTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTableTextField() {
		if (tableTextField == null) {
			tableTextField = new JTextField();
			tableTextField.setPreferredSize(new Dimension(375, 4));
		}
		return tableTextField;
	}

	/**
	 * This method initializes submittButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSubmittButton() {
		if (submittButton == null) {
			submittButton = new JButton();
			submittButton.setHorizontalAlignment(SwingConstants.CENTER);
			submittButton.setVerticalAlignment(SwingConstants.CENTER);
			submittButton.setPreferredSize(new Dimension(4, 4));
			submittButton.setName("");
			submittButton.setText("Submit");
			submittButton.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		}
		submittButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (tableTextField.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "Please enter value");
					return;
				}
				//gets the table_id from the textfield
				try
				{
					table_id=Integer.parseInt(tableTextField.getText().trim());
				}
				catch(NumberFormatException ex)
				{
					resultTextArea.append("Please enter a valid table #\n");
					return;
				}
				try {
					//calls the sql routine
					getResults(table_id);
				}
				catch(java.sql.SQLException ex)
				{
					System.out.println(ex.getMessage());
					resultTextArea.append("Table " + table_id + " doesn't have any open orders\n");
				}
				//add code to clear variable for next run??
				
			}
		});
		return submittButton;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Query3 thisClass = new Query3();
				thisClass.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				thisClass.setVisible(true);
				tableTextField.setText("");
				resultTextArea.setText("");
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public Query3() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(501, 282);
		this.setContentPane(getJContentPane());
		this.setTitle("Query3");
	}

	private static void getResults(int id) throws SQLException
	{
		
		
		//Setup database connection
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		Connection conn1 = 
			DriverManager.getConnection("jdbc:oracle:thin:@babbage2.cs.iit.edu:1521:dev101ee", "poshea","*******");
		
		//setup sql statement
		Statement stmt1 = conn1.createStatement(); // 1st Statement off conn1
		String ids = "Select count(id) from tables";
		ResultSet rset2 = stmt1.executeQuery(ids);
		rset2.next();
		String sql = "select name from Employee where id in (select emp_id from Orders where closed is null and table_id = '" + id + "')";
		
	
		
		if (id > rset2.getInt(1))
		{
			resultTextArea.setText("That table does not exist");
			return;
		}
		//acquire results
		ResultSet rset1 = stmt1.executeQuery(sql);
		
	
		
		//Print results to textarea
		//rset1.last();
		
		//System.out.println(rset1.getRow());
		rset1.next();
		String result = "Table " + id + "'s open order is " + rset1.getString(1) + "\n";
		resultTextArea.append(result);
		
		rset1.close();
		conn1.close();
		
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabel = new JLabel();
			jLabel.setText("Enter Table #");
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getTableTextField(), BorderLayout.WEST);
			jContentPane.add(jLabel, BorderLayout.NORTH);
			jContentPane.add(getSubmittButton(), BorderLayout.CENTER);
			jContentPane.add(getJScrollPane(), java.awt.BorderLayout.SOUTH);
		}
		return jContentPane;
	}

}  //  @jve:decl-index=0:visual-constraint="119,61"
