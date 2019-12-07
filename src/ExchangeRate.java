import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class ExchangeRate {
	
	private String from;
	private String to;
	private String amount;
	private String exchangeRate;
	
	public ExchangeRate(String from, String to, String amount) {
		this.from = from;
		this.to = to;
		this.amount = amount;
	}
	
	public boolean checkValue() {
		if(!this.amount.matches("^[0-9]{0,10}(\\.[0-9]{1,5})?$") || this.amount.equalsIgnoreCase("")) {
			return false;
		}else {
			return true;
		}
	}
	
	public void serverProcess() {
		try {
			StringBuffer htmlResponse = new StringBuffer();
			URL url = new URL("http://rate-exchange-1.appspot.com/currency?from=" + this.from + "&to=" + this.to + "&q=" + this.amount);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				htmlResponse.append(inputLine);
			}
			in.close();
			con.disconnect();
			
			Pattern p = Pattern.compile("^\\{\\\"to\\\": \\\"[A-Z]{3}\\\", \\\"rate\\\": [0-9\\\\.]{1,20}, \\\"from\\\": \\\"[A-Z]{3}\\\", \\\"v\\\": ([0-9\\\\.]{1,30})\\}$");
			Matcher m = p.matcher(htmlResponse.toString());
			m.find();
			this.exchangeRate = m.group(1);
			
			
		} catch (Exception ex) {
			JOptionPane.showMessageDialog (null, "Some error occured while trying to connect to the server. Please try again after some time.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public String getExhangeRate() {
		return this.exchangeRate;
	}

}