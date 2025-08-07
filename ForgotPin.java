import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.PlainDocument;
import java.sql.*;
public class ForgotPin extends JFrame implements ActionListener {
JTextField cardField, securityAnswerField;
JPasswordField newPinField, confirmPinField;
JButton enter, cancel, nextButton, backButton;
private int step = 0;
String cardNumber;
String pinnumber;
// Forgot PIN lock variables
private static int forgotPinAttempts = 0;
private static boolean forgotPinLocked = false;
private static long forgotPinLockTime = 0;
private static final int MAX_ATTEMPTS = 3;
private static final long LOCKOUT_DURATION = 5 * 60 * 1000;
ForgotPin() {
setTitle("PIN Recovery");
setSize(500, 300);
setLayout(null);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
getContentPane().setBackground(Color.WHITE);
setLocationRelativeTo(null);
// Title
JLabel title = new JLabel("Forgot PIN Recovery");
title.setFont(new Font("Arial", Font.BOLD, 20));
title.setBounds(120, 20, 250, 30);
add(title);
// Card Number
JLabel cardLabel = new JLabel("Card Number:");
cardLabel.setBounds(150, 80, 100, 20);
add(cardLabel);
cardField = new JTextField();
cardField.setBounds(150, 100, 200, 25);
((PlainDocument) cardField.getDocument()).setDocumentFilter(new NumericDocument(8));
add(cardField);
enter = new JButton("NEXT");
enter.setBounds(270, 190, 100, 30);
enter.setBackground(Color.GREEN);
enter.setForeground(Color.BLACK);
enter.addActionListener(this);
add(enter);
cancel = new JButton("CANCEL");
cancel.setBounds(150, 190, 100, 30);
cancel.setBackground(Color.RED);
cancel.setForeground(Color.BLACK);
cancel.addActionListener(this);
add(cancel);
setVisible(true);
}
private void showSecurityScreen() {
getContentPane().removeAll();
JLabel securityLabel = new JLabel("SECURITY VERIFICATION");
securityLabel.setFont(new Font("Arial", Font.BOLD, 20));
securityLabel.setBounds(150, 30, 250, 30);
add(securityLabel);
try {
Conn conn = new Conn();
ResultSet rs = conn.s.executeQuery("SELECT security_question FROM signup WHERE
cardnumber = '"+cardNumber+"'");
if(rs.next()) {
JLabel questionLabel = new JLabel(rs.getString("security_question"));
questionLabel.setBounds(150, 70, 250, 30);
add(questionLabel);
JTextArea questionArea = new JTextArea(rs.getString("security_question"));
questionArea.setBounds(150, 90, 250, 40);
questionArea.setEditable(false);
questionArea.setLineWrap(true);
questionArea.setWrapStyleWord(true);
questionArea.setBackground(getBackground());
add(questionArea);
}
} catch(Exception e) {
System.out.println(e);
}
JLabel answerLabel = new JLabel("Your Answer:");
answerLabel.setBounds(150, 110, 100, 20);
add(answerLabel);
securityAnswerField = new JTextField();
securityAnswerField.setBounds(150, 130, 200, 25);
add(securityAnswerField);
nextButton = new JButton("Verify");
nextButton.setBounds(270, 190, 100, 30);
nextButton.setBackground(Color.GREEN);
nextButton.setForeground(Color.BLACK);
nextButton.addActionListener(this);
add(nextButton);
backButton = new JButton("Back");
backButton.setBounds(150, 190, 100, 30);
backButton.setBackground(Color.BLACK);
backButton.setForeground(Color.WHITE);
backButton.addActionListener(this);
add(backButton);
revalidate();
repaint();
}
private void showNewPinScreen() {
getContentPane().removeAll();
JLabel text = new JLabel("SET NEW PIN");
text.setFont(new Font("Arial", Font.BOLD, 20));
text.setBounds(150, 30, 250, 30);
add(text);
JLabel newPinLabel = new JLabel("New PIN:");
newPinLabel.setBounds(150, 80, 100, 20);
add(newPinLabel);
newPinField = new JPasswordField();
newPinField.setBounds(150, 100, 200, 25);
((PlainDocument)newPinField.getDocument()).setDocumentFilter(new NumericDocument(4));
newPinField.setEchoChar('•');
add(newPinField);
JLabel confirmLabel = new JLabel("Confirm PIN:");
confirmLabel.setBounds(150, 130, 100, 20);
add(confirmLabel);
confirmPinField = new JPasswordField();
confirmPinField.setBounds(150, 150, 200, 25);
((PlainDocument)confirmPinField.getDocument()).setDocumentFilter(new NumericDocument(4));
confirmPinField.setEchoChar('•');
add(confirmPinField);
nextButton = new JButton("Change");
nextButton.setBounds(270, 190, 100, 30);
nextButton.setBackground(Color.GREEN);
nextButton.setForeground(Color.BLACK);
nextButton.addActionListener(this);
add(nextButton);
backButton = new JButton("Back");
backButton.setBounds(150, 190, 100, 30);
backButton.setBackground(Color.BLACK);
backButton.setForeground(Color.WHITE);
backButton.addActionListener(this);
add(backButton);
revalidate();
repaint();
}
@Override
public void actionPerformed(ActionEvent e) {
if (e.getSource() == enter || e.getSource() == nextButton) {
try {
Conn conn = new Conn();
if (step == 0) { // First step: Verify Card Number
cardNumber = cardField.getText();
if (cardNumber.isEmpty()) {
JOptionPane.showMessageDialog(null, " Card Number is Required.");
return;
} else if (!cardNumber.matches("\\d{8}")) {
JOptionPane.showMessageDialog(null, "Card number must be 8 digits");
return;
}
ResultSet rs = conn.s.executeQuery("SELECT pinnumber FROM login WHERE cardnumber = '"+cardNumber+"'");
if (rs.next()) {
pinnumber = rs.getString("pinnumber");
step = 1;
showSecurityScreen();
} else {
JOptionPane.showMessageDialog(null, "Card number not found");
}
}
else if (step == 1) { // Second step: Verify Security Answer
if (forgotPinLocked) {
long currentTime = System.currentTimeMillis();
if (currentTime - forgotPinLockTime < LOCKOUT_DURATION) {
long remainingTime = (LOCKOUT_DURATION - (currentTime - forgotPinLockTime)) / 1000;
JOptionPane.showMessageDialog(null, "Forgot PIN temporarily locked. Please try again in " +
remainingTime + " seconds.");
return;
} else {
forgotPinLocked = false;
forgotPinAttempts = 0;
}
}
String answer = securityAnswerField.getText();
ResultSet rs = conn.s.executeQuery("SELECT security_answer FROM signup WHERE cardnumber = '"+cardNumber+"'");
if (answer.isEmpty()) {
JOptionPane.showMessageDialog(null, "Security answer is required");
return;
}
if (rs.next() && rs.getString("security_answer").equals(answer)) {
forgotPinAttempts = 0;
step = 2;
showNewPinScreen();
} else {
forgotPinAttempts++;
if (forgotPinAttempts >= MAX_ATTEMPTS) {
forgotPinLocked = true;
forgotPinLockTime = System.currentTimeMillis();
JOptionPane.showMessageDialog(null, "Too many incorrect attempts. Security verification is locked for 5 minutes.");
} else {
JOptionPane.showMessageDialog(null, "Incorrect Security Answer. Attempts remaining: " +
(MAX_ATTEMPTS - forgotPinAttempts));
}
}
}
else if (step == 2) { // Final step: Change PIN
String npin = new String(newPinField.getPassword());
String rpin = new String(confirmPinField.getPassword());
if (!npin.equals(rpin)) {
JOptionPane.showMessageDialog(null, "PINs do not match");
return;
}
if (!npin.matches("\\d{4}")) {
JOptionPane.showMessageDialog(null, "PIN must be 4 digits");
return;
}
// Update all tables
conn.s.executeUpdate("UPDATE login SET pinnumber = '"+npin+"' WHERE cardnumber = '"+cardNumber+"'");
conn.s.executeUpdate("UPDATE signup SET pinnumber = '"+npin+"' WHERE cardnumber = '"+cardNumber+"'");
JOptionPane.showMessageDialog(null, "PIN Changed Successfully");
setVisible(false);
new Login().setVisible(true);
}
} catch (Exception ex) {
JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
}
}
else if (e.getSource() == cancel || e.getSource() == backButton) {
// Handle back/cancel with step tracking
if (step > 0) {
step--;
if (step == 0) {
new ForgotPin().setVisible(true);
dispose();
} else if (step == 1) {
showSecurityScreen();
}
} else {
setVisible(false);
new Login().setVisible(true);
}
}
}
public static void main(String[] args) {
new ForgotPin();
}
}