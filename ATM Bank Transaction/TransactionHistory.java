import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import java.text.NumberFormat;
public class TransactionHistory extends JFrame implements ActionListener {
private JTextArea TransacArea;
private String cardnumber;
JButton back;
public TransactionHistory(String cardnumber) {
this.cardnumber = cardnumber;
TransacArea = new JTextArea();
TransacArea.setEditable(false); // prevents the user to edit the text area
add(new JScrollPane(TransacArea), BorderLayout.CENTER);
TransacArea.setFont(new Font("Arial", Font.PLAIN, 14));
JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
back = new JButton("Back");
back.setBackground(Color.BLACK);
back.setForeground(Color.WHITE);
back.addActionListener(this);
buttonPanel.add(back);
add(buttonPanel, BorderLayout.SOUTH);
fetchTransactionHistory();
setSize(500,300);
setTitle("Transaction History");
setLocationRelativeTo(null);
setVisible(true);
}
private void fetchTransactionHistory() {
try {
Conn conn = new Conn();
String query = "SELECT date, type, amount, balance FROM TransactionHistory " +
"WHERE cardnumber = '" + cardnumber + "' " + "ORDER BY date DESC";
ResultSet rs = conn.s.executeQuery(query);
StringBuilder history = new StringBuilder();
//number formatter for currency display
NumberFormat pesoFormat = NumberFormat.getNumberInstance();
pesoFormat.setMinimumFractionDigits(2);
pesoFormat.setMaximumFractionDigits(2);
//column headers with formatting
history.append(String.format("%-20s %-12s %15s %15s%n%n", "DATE", "TYPE", "AMOUNT",
"BALANCE"));
boolean hasTransactions = false;
while (rs.next()) {
hasTransactions = true;
String date = rs.getString("date");
String type = rs.getString("type");
int amountInCents = rs.getInt("amount"); // whole number
int balanceInCents = rs.getInt("balance");
// Convert cents to pesos for display
double amountInPesos = amountInCents / 100.0;
double balanceInPesos = balanceInCents / 100.0;
String amountDisplay = amountInCents < 0 ?
"(" + pesoFormat.format(Math.abs(amountInPesos)) + ")" :
pesoFormat.format(amountInPesos);
history.append(String.format("%-20s %-12s %15s %15s%n",
date,
type,
"₱" + amountDisplay,
"₱" + pesoFormat.format(balanceInPesos)));
// Format transaction history
//history.append(date).append("\t")
//.append(type).append("\t")
//.append(amount).append("\t")
//.append(balance).append("\n");
}
// If no transactions exist, display message
if (!hasTransactions) {
TransacArea.setText("No transactions found.");
} else {
TransacArea.setText(history.toString());
}
} catch (Exception e) {
JOptionPane.showMessageDialog(null, "Error retrieving transaction history: " + e.getMessage());
e.printStackTrace();
}
}
public void actionPerformed(ActionEvent e) {
if (e.getSource() == back) {
setVisible(false);
new Transactions(cardnumber).setVisible(true);
}
}
public static void main(String [] args) {
new TransactionHistory("");
}
}
