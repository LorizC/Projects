//deposit.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.ResultSet;
import javax.swing.text.PlainDocument;
public class Deposit extends JFrame implements ActionListener {
JTextField amount;
JButton deposit, back;
String cardnumber;
Deposit(String cardnumber) {
this.cardnumber = cardnumber;
setTitle("Deposit");
setSize(500,300);
setLocation(300,0);
setLayout(null);
JLabel text1 = new JLabel("Enter amount you want to Deposit(₱100 minimum)");
text1.setBounds(100,30,350,30);
text1.setFont(new Font("Arial", Font.BOLD, 14));
add(text1);
amount = new JTextField();
amount.setBounds(125,70,250,30);
amount.setHorizontalAlignment(JTextField.CENTER);
((PlainDocument)amount.getDocument()).setDocumentFilter(new NumericDocument(5));
add(amount);
deposit = new JButton("ENTER");
deposit.setBounds(275,130,100,40);
deposit.addActionListener(this);
deposit.setBackground(Color.GREEN);
deposit.setForeground(Color.BLACK);
add(deposit);
back = new JButton("CANCEL");
back.setBounds(125,130,100,40);
back.addActionListener(this);
back.setBackground(Color.RED);
back.setForeground(Color.BLACK);
add(back);
setLocationRelativeTo(null);
setVisible(true);
getContentPane().setBackground(Color.WHITE);
}
public void actionPerformed(ActionEvent e) {
if (e.getSource() == deposit) {
String number = amount.getText();
Date date = new Date();
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String formattedDate = formatter.format(date);
if (number.equals("")) {
JOptionPane.showMessageDialog(null, "Please enter the amount you want to Deposit");
} else {
try {
int depositAmount = Integer.parseInt(number) * 100; // Convert to cents
if (depositAmount < 10000) { // ₱100 minimum
JOptionPane.showMessageDialog(null, "Minimum deposit is ₱100");
return;
}
Conn conn = new Conn();
String balanceQuery = "SELECT balance FROM TransactionHistory WHERE cardnumber = '" +
cardnumber + "' ORDER BY date DESC LIMIT 1";
ResultSet rs = conn.s.executeQuery(balanceQuery);
int currentBalance = rs.next() ? rs.getInt("balance") : 0;
//int depositAmount = Integer.parseInt(number);
if (depositAmount <= 0) {
JOptionPane.showMessageDialog(null, "Amount must be greater than 0");
return;
}
int newBalance = currentBalance + depositAmount;
String TransactionQuery = "INSERT INTO TransactionHistory (cardnumber, date, amount, type, balance) " +
"VALUES ('"+ cardnumber +"', '"+formattedDate+"', "+ depositAmount + ", 'Deposit', " + newBalance + ")";
conn.s.executeUpdate(TransactionQuery);
JOptionPane.showMessageDialog(null, "₱" + number + " Deposited Successfully");
setVisible(false);
new Transactions(cardnumber).setVisible(true);
} catch(Exception ae) {
System.out.println(ae);
}
}
} else if (e.getSource() == back) {
setVisible(false);
new Transactions(cardnumber).setVisible(true);
}
}
public static void main(String [] args) {
new Deposit("");
}
}