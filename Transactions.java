import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Transactions extends JFrame implements ActionListener{
JButton withdraw, bal, dep, funds, trans, pin, exit;
String cardnumber;
Transactions(String cardnumber) {
this.cardnumber = cardnumber;
setLayout(null);
setTitle("ATM Transactions");
setSize(500,300);
setLocationRelativeTo(null); // Center the window
setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
JLabel text = new JLabel("Please select Your Transaction");
text.setBounds(150,20,200,30);
add(text);
withdraw = new JButton("Withdrawal");
withdraw.setBounds(60,70,180,30);
withdraw.setBackground(Color.BLACK);
withdraw.setForeground(Color.WHITE);
withdraw.addActionListener(this);
add(withdraw);
bal = new JButton("Balance");
bal.setBounds(260,70,180,30);
bal.setBackground(Color.BLACK);
bal.setForeground(Color.WHITE);
bal.addActionListener(this);
add(bal);
dep = new JButton("Deposit");
dep.setBounds(60,120,180,30);
dep.setBackground(Color.BLACK);
dep.setForeground(Color.WHITE);
dep.addActionListener(this);
add(dep);
funds = new JButton("Fund Transfer");
funds.setBounds(260,120,180,30);
funds.setBackground(Color.BLACK);
funds.setForeground(Color.WHITE);
funds.addActionListener(this);
add(funds);
trans = new JButton("Transaction History");
trans.setBounds(60,170,180,30);
trans.setBackground(Color.BLACK);
trans.setForeground(Color.WHITE);
trans.addActionListener(this);
add(trans);
pin = new JButton("Pin Change");
pin.setBounds(260,170,180,30);
pin.setBackground(Color.BLACK);
pin.setForeground(Color.WHITE);
pin.addActionListener(this);
add(pin);
exit = new JButton("Exit");
exit.setBounds(160,220,180,30);
exit.setBackground(Color.BLACK);
exit.setForeground(Color.WHITE);
exit.addActionListener(this);
add(exit);
// Add window listener to handle closing
addWindowListener(new WindowAdapter() {
@Override
public void windowClosed(WindowEvent e) {
returnToLogin();
}
});
getContentPane().setBackground(Color.WHITE);
setVisible(true);
}
@Override
public void actionPerformed(ActionEvent e) {
if (e.getSource() == exit) {
int response = JOptionPane.showConfirmDialog
(this, "Are you sure you want to exit?", "Confirmation",
JOptionPane.YES_NO_OPTION
);
if (response == JOptionPane.YES_OPTION) {
returnToLogin();
}
} else if (e.getSource() == dep){
setVisible(false);
new Deposit(cardnumber).setVisible(true);
} else if (e.getSource() == withdraw) {
setVisible(false);
new Withdrawal(cardnumber).setVisible(true);
} else if (e.getSource() == bal) {
setVisible(false);
new BalanceInquiry(cardnumber).setVisible(true);
} else if (e.getSource() == pin) {
setVisible(false);
new PinChange(cardnumber).setVisible(true);
} else if (e.getSource() == trans) {
setVisible(false);
new TransactionHistory(cardnumber).setVisible(true);
} else if (e.getSource() == funds) {
setVisible(false);
new FundTransfer(cardnumber).setVisible(true);
}
}
private void returnToLogin() {
dispose(); // Close current window
new Login().setVisible(true); // Open new login window
}
public static void main(String[] args){
new Transactions("");
}
}
