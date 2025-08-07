import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.text.PlainDocument;
public class FundTransfer extends JFrame implements ActionListener {
private String cardnumber;
private JTextField cardnumberField, amountField;
private JButton transferButton, backButton;
private JLabel messageLabel;
public FundTransfer(String cardnumber) {
this.cardnumber = cardnumber;
//Frame attributes
setTitle("Fund Transfer");
setLayout(null);
// title
JLabel title = new JLabel("Fund Transfer");
title.setBounds(150,20,200,30);
title.setFont(new Font("Arial", Font.BOLD, 14));
add(title);
//txt display
JLabel accountLabel = new JLabel("Recipient Card NO: ");
accountLabel.setBounds(125,70,120,20);
add(accountLabel);
//txt input for card no.
cardnumberField = new JTextField();
cardnumberField.setBounds(250,70,125,25);
cardnumberField.setHorizontalAlignment(JTextField.CENTER);
((PlainDocument)cardnumberField.getDocument()).setDocumentFilter(new
NumericDocument(8));
add(cardnumberField);
//txt display
JLabel amountLabel = new JLabel("Amount to Transfer: ");
amountLabel.setBounds(125,110,120,20);
add(amountLabel);
//txt input for transfer.
amountField = new JTextField();
amountField.setBounds(250,110,125,25);
amountField.setHorizontalAlignment(JTextField.CENTER);
((PlainDocument)amountField.getDocument()).setDocumentFilter(new NumericDocument(5));
add(amountField);
// Transfer Button
transferButton = new JButton("TRANSFER");
transferButton.setBounds(275, 160, 100, 40);
transferButton.addActionListener(this);
transferButton.setBackground(Color.GREEN);
transferButton.setForeground(Color.BLACK);
add(transferButton);
// Back Button
backButton = new JButton("CANCEL");
backButton.setBounds(125, 160, 100, 40);
backButton.addActionListener(this);
backButton.setBackground(Color.RED);
backButton.setForeground(Color.BLACK);
add(backButton);
// Message Label
messageLabel = new JLabel("");
messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
messageLabel.setForeground(Color.RED);
messageLabel.setBounds(125, 210, 250, 20);
add(messageLabel);
setSize(500,300);
setLocationRelativeTo(null);
setVisible(true);
}
@Override
public void actionPerformed(ActionEvent e) {
if (e.getSource() == transferButton) {
String Cardnumber = cardnumberField.getText();
String amountStr = amountField.getText();
if (Cardnumber.isEmpty() || amountStr.isEmpty()) {
messageLabel.setText("Please fill all fields");
return;
}
Conn conn = null;
try {
conn = new Conn();
// Start transaction
conn.s.execute("START TRANSACTION");
//String selfCheckQuery = "SELECT cardnumber FROM login WHERE cardnumber = '" +
cardnumber + "'";
//ResultSet selfRs = conn.s.executeQuery(selfCheckQuery);
//if (selfRs.next()) {
//String selfCardnumber = selfRs.getString("cardnumber");
if (Cardnumber.equals(this.cardnumber)) {
messageLabel.setText("Cannot transfer to your own account");
conn.s.execute("ROLLBACK");
return;
}
double amount = Double.parseDouble(amountStr);
int amountInCents = (int) Math.round(amount * 100);
if (amountInCents <= 0) {
messageLabel.setText("Amount must be positive");
return;
}
//int amount = Integer.parseInt(amountStr);
//if (amount <= 0) {
//messageLabel.setText("Amount must be positive");
//conn.s.execute("ROLLBACK");
//return;
//}
String fundquery = "SELECT cardnumber FROM login WHERE cardnumber = '" + Cardnumber+
"'";
ResultSet rs = conn.s.executeQuery(fundquery);
if (!rs.next()) {
messageLabel.setText("Recipient account not found");
conn.s.execute("ROLLBACK");
return;
}
//String balanceQuery = "SELECT " +
//"COALESCE(SUM(CASE WHEN type = 'Deposit' OR type = 'Transfer In' THEN amount ELSE
-amount END), 0) " +
//"FROM TransactionHistory WHERE pinnumber = '" + pinnumber + "'";
//String balanceQuery = "SELECT COALESCE(SUM(amount), 0) FROM TransactionHistory
WHERE pinnumber = '" + pinnumber + "'";
String senderbalanceQuery = "SELECT balance FROM TransactionHistory WHERE cardnumber
= '" + cardnumber + "' ORDER BY date DESC LIMIT 1";
rs = conn.s.executeQuery(senderbalanceQuery);
int currentBalance = rs.next() ? rs.getInt("balance") : 0;
if (currentBalance < amountInCents ) {
messageLabel.setText("Insufficient balance");
conn.s.execute("ROLLBACK");
return;
}
int newSenderBalance = currentBalance - amountInCents;
// Get recipient's pinnumber from their cardnumber
String recipientCardQuery = "SELECT cardnumber FROM login WHERE cardnumber = '" +
Cardnumber + "'";
rs = conn.s.executeQuery(recipientCardQuery);
String recipientCardnumber = "";
if (rs.next()) {
recipientCardnumber = rs.getString("cardnumber");
} else {
messageLabel.setText("Recipient account not found");
conn.s.execute("ROLLBACK");
return;
}
String recipientBalanceQuery = "SELECT balance FROM TransactionHistory WHERE
cardnumber = '" + recipientCardnumber + "' ORDER BY date DESC LIMIT 1";
rs = conn.s.executeQuery(recipientBalanceQuery);
int recipientBalance = rs.next() ? rs.getInt("balance") : 0;
int newRecipientBalance = recipientBalance + amountInCents;
// Record sender's transfer (Transfer Out)
String senderQuery = "INSERT INTO TransactionHistory(cardnumber, date, amount, type,
balance) " +
"VALUES('" + cardnumber + "', NOW(), " + (-amountInCents) + ", 'Transfer Out', "+
newSenderBalance + ")";
conn.s.executeUpdate(senderQuery);
// Record recipient's transfer (Transfer In)
String recipientQuery = "INSERT INTO TransactionHistory(cardnumber, date, amount, type,
balance) " +
"VALUES('" + recipientCardnumber + "', NOW(), " + amountInCents + ", 'Transfer In',"
+ newRecipientBalance + ")";
conn.s.executeUpdate(recipientQuery);
// Commit transaction
conn.s.execute("COMMIT");
JOptionPane.showMessageDialog(null, "Transfer successful!");
cardnumberField.setText("");
amountField.setText("");
messageLabel.setText("");
} catch (NumberFormatException ae) {
messageLabel.setText("Invalid amount format");
try {
if (conn != null) conn.s.execute("ROLLBACK");
} catch (SQLException ex) {
ex.printStackTrace();
}
} catch (SQLException ae) {
try {
if (conn != null) conn.s.execute("ROLLBACK");
} catch (SQLException ex) {
ex.printStackTrace();
}
messageLabel.setText("Database error: " + ae.getMessage());
} finally {
try {
if (conn != null) conn.c.close();
} catch (SQLException ex) {
ex.printStackTrace();
}
}
} else if (e.getSource() == backButton) {
setVisible(false);
new Transactions(cardnumber).setVisible(true);
}
}
public static void main(String [] args) {
new FundTransfer("");
}
}
