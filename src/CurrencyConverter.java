import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class CurrencyConverter extends JFrame {
	
	CurrencyConverter() {
		//Frame properties
		this.setSize(300, 385);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);
		this.setTitle("Currency Converter");
		
		//Center the Frame
		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		setLocation(size.width/2 - getWidth()/2, size.height/2 - getHeight()/2);
		
		//Icon
		ImageIcon icon = new ImageIcon(getClass().getResource("/resources/icon.png"));
		setIconImage(icon.getImage());
		
		//Default System Look & Feel
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.exit(0);
        }
		
		//Header
		ImageIcon headerImage = new ImageIcon(getClass().getResource("/resources/header.png"));
		JLabel header = new JLabel(headerImage);
		header.setBounds(0, 0, 300, 200);
		add(header);
		header.setLayout(null);
		
		//Font and border
		Font font = new Font("SansSerif", Font.BOLD, 14);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		
		//Amount Label
		JLabel amountLabel = new JLabel("Amount :");
		amountLabel.setFont(font);
		amountLabel.setBounds(30, 200, 70, 24);
		this.add(amountLabel);
		
		//Amount box
		JFormattedTextField amountBox = new JFormattedTextField();
		amountBox.setFont(font);
		amountBox.setBorder(border);
		amountBox.setHorizontalAlignment(JTextField.CENTER);
		amountBox.setBounds(100, 200, 170, 24);
		this.add(amountBox);
		
		//From Label
		JLabel fromLabel = new JLabel("From :");
		fromLabel.setFont(font);
		fromLabel.setBounds(30, 230, 70, 24);
		this.add(fromLabel);
		
		//Currencies
		String[] currencies = new String[] {"AED", "BRL", "CNY", "EUR", "GBP", "INR", "JPY", "OMR", "SAR", "USD"};
		String[] crrencyNames = new String[] {"UAE Dirham (AED)", "Brazil Real (BRL)", "Chinese Yuan (CNY)", "Euro (EUR)", "British Pound (GBP)", "Indian Rupee (INR)", "Japanese Yen (JPY)", "Omani Riyal (OMR)", "Saudi Riyal (SAR)", "US Dollar (USD)"};
		
		//From Combo
		JComboBox<String> fromCombo = new JComboBox<String>(crrencyNames);
		fromCombo.setFont(font);
		fromCombo.setBorder(border);
		fromCombo.setBounds(100, 230, 170, 24);
		fromCombo.setSelectedIndex(9);
		this.add(fromCombo);
		
		//To Label
		JLabel toLabel = new JLabel("To :");
		toLabel.setFont(font);
		toLabel.setBounds(30, 260, 70, 24);
		this.add(toLabel);
		
		//To Combo
		JComboBox<String> toCombo = new JComboBox<String>(crrencyNames);
		toCombo.setFont(font);
		toCombo.setBorder(border);
		toCombo.setBounds(100, 260, 170, 24);
		toCombo.setSelectedIndex(5);
		this.add(toCombo);
		
		//Convert button
		JButton convertBtn = new JButton("Convert");
		convertBtn.setFont(font);
		convertBtn.setBounds(100, 290, 100, 25);
		this.add(convertBtn);
		
		//ResultLabel
		JLabel resLabel = new JLabel();
		resLabel.setFont(font);
		resLabel.setBorder(border);
		resLabel.setOpaque(true);
		resLabel.setBackground(Color.WHITE);
		resLabel.setHorizontalAlignment(JLabel.CENTER);
		resLabel.setBounds(25, 320, 250, 24);
		this.add(resLabel);
		
		//Click Event for convertBtn
		convertBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//Disable convertBtn temporarily
				convertBtn.setEnabled(false);
				
				SwingWorker<Void,Void> sw = new SwingWorker<Void,Void>() {
					
					//Create an object and pass the parameters
					ExchangeRate er = new ExchangeRate(currencies[fromCombo.getSelectedIndex()], currencies[toCombo.getSelectedIndex()], amountBox.getText());
					
					@Override
					protected Void doInBackground() throws Exception {
						//If value is empty or invalid
						if(!er.checkValue()) {
							JOptionPane.showMessageDialog (null, "Please Enter a valid amount.\nEx: 1234567890.12345", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
							return null;
						}
						
						//Use the API to fetch the data
						er.serverProcess();
						return null;
					}
					
					@Override
					protected void done() {
						//If there is a returned value then return it
						String res = er.getExhangeRate();
						if(res!=null) {
							resLabel.setText(res);
						}else {
							resLabel.setText("");
						}
						
						//Re-enable convertBtn again
						convertBtn.setEnabled(true);
					}
					
				};
				sw.execute();

			}
			
		});
		
		//Dispaly the frame
		this.setVisible(true);
		
	}
	
	public static void main(String[] args) {
		
		//Display the Frame
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new CurrencyConverter();
			}
		});
		
	}

}