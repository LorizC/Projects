import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
public class BalanceInquiry extends JFrame implements ActionListener{
JButton back;
String cardnumber;
BalanceInquiry(String cardnumber) {
this.cardnumber = cardnumber;
back = new JButton("Back");
back.setBounds(210,130,120,40);
back.addActionListener(this);
back.setBackground(Color.BLACK);
back.setForeground(Color.WHITE);
add(back);
int balance = 0;
try {
//String balanceQuery = "SELECT " +
//"COALESCE(SUM(CASE WHEN type = 'Deposit' OR type = 'Transfer In' THEN amount ELSE
-amount END), 0) " +
//"FROM TransactionHistory WHERE pinnumber = '" + pinnumber + "'";
// Instead of summing, fetch the last (latest) recorded balance.
String balanceQuery = "SELECT balance FROM TransactionHistory " +
"WHERE cardnumber = '" + cardnumber + "' " + "ORDER BY date DESC LIMIT 1";
Conn c = new Conn();
ResultSet rs = c.s.executeQuery(balanceQuery);
if (rs.next()) {
balance = rs.getInt("balance");
} else {
balance = 0;
}
} catch (Exception ae) {
System.out.print(ae);
}
double displayBalance = balance / 100.0;
JLabel text = new JLabel("Your current account balance is ₱" + String.format("%.2f",
displayBalance));
text.setBounds(50,30,400,100);
add(text);
setTitle("Balance Inquiry");
setLayout(null);
setSize(500,300);
setLocation(300,0);
setLocationRelativeTo(null);
setVisible(true);
}
public void actionPerformed(ActionEvent e) {
setVisible(false);
new Transactions(cardnumber).setVisible(true);
}
public static void main(String [] args) {
new BalanceInquiry("");
}
}