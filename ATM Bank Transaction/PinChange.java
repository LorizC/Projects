//pinchange.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import javax.swing.text.PlainDocument;
public class PinChange extends JFrame implements ActionListener {
JPasswordField pin, repin;
JButton change, back;
String cardnumber;
PinChange(String cardnumber) {
this.cardnumber = cardnumber;
setLayout(null);
JLabel text = new JLabel("Change Your PIN");
text.setFont(new Font("Arial", Font.BOLD, 14));
text.setBounds(150,30,200,30);
add(text);
JLabel pintext = new JLabel("New pin: ");
pintext.setBounds(125,70,100,30);
add(pintext);
pin= new JPasswordField();
pin.setBounds(225,70,150,30);
pin.setFont(new Font("Arial", Font.PLAIN, 14));
pin.setHorizontalAlignment(JTextField.CENTER);
((PlainDocument)pin.getDocument()).setDocumentFilter(new NumericDocument(4));
add(pin);
JLabel repintext = new JLabel("Re-enter pin: ");
repintext.setBounds(125,110,100,30);
add(repintext);
repin = new JPasswordField();
repin.setBounds(225,110,150,30);
repin.setFont(new Font("Arial", Font.PLAIN, 14));
repin.setHorizontalAlignment(JTextField.CENTER);
((PlainDocument)repin.getDocument()).setDocumentFilter(new NumericDocument(4));
add(repin);
change = new JButton("CHANGE");
change.setBounds(275,160,100,40);
change.addActionListener(this);
change.setBackground(Color.GREEN);
change.setForeground(Color.BLACK);
add(change);
back = new JButton("CANCEL");
back.setBounds(125,160,100,40);
back.addActionListener(this);
back.setBackground(Color.RED);
back.setForeground(Color.BLACK);
add(back);
setTitle("Pin Change");
setSize(500,300);
setLocationRelativeTo(null);
getContentPane().setBackground(Color.WHITE);
setVisible(true);
}
public void actionPerformed(ActionEvent e){
if (e.getSource() == change) {
try {
String npin = pin.getText();
String rpin = repin.getText();
if (!npin.equals(rpin)) {
JOptionPane.showMessageDialog(null, "Entered pin does not match");
return;
}
if (npin.equals("")) {
JOptionPane.showMessageDialog(null, "Please enter new Pin");
return;
}
if (rpin.equals("")) {
JOptionPane.showMessageDialog(null, "Please re-enter Pin");
return;
}
// Check if new PIN is already used by another user
//Conn connCheck = new Conn();
//String checkQuery = "SELECT pinnumber FROM signup WHERE pinnumber = '" + npin + "'
AND pinnumber <> '" + pinnumber + "'";
//ResultSet rs = connCheck.s.executeQuery(checkQuery);
//if (rs.next()) {
//JOptionPane.showMessageDialog(null, "This PIN is already in use. Please choose a different
PIN.");
//return;
//}
Conn conn = new Conn();
String query2 = "update login set pinnumber = '"+rpin+"' where cardnumber ='"+ cardnumber +"'";
String query3 = "update signup set pinnumber = '"+rpin+"' where cardnumber ='"+ cardnumber
+"'";
//conn.s.executeUpdate(query1);
conn.s.executeUpdate(query2);
conn.s.executeUpdate(query3);
JOptionPane.showMessageDialog(null, "PIN changed successfully");
setVisible(false);
new Transactions(cardnumber).setVisible(true);
} catch (Exception ae) {
System.out.println(ae);
}
} else {
setVisible(false);
new Transactions(cardnumber).setVisible(true);
}
}
public static void main(String[] args) {
new PinChange("").setVisible(true);
}
}