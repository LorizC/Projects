import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.sql.*; //Imports JDBC (Java Database Connectivity) classes
import javax.swing.text.PlainDocument;
// ung line na to is for class name, and extends JFrame is a class to create gui window,
// by using extends Jframe ung Login class makukuha nya ung lahat ng features ng isang
JFrame.
// based kay chatgpt ung implements ActionListener is para sa buong class na para i handle nya
ung button clicks.
// This means that whenever a button is clicked, the actionPerformed(ActionEvent e) method will
be triggered.
public class Login extends JFrame implements ActionListener{
// This line of code naman is I declared the JButton, JTextField and JPassword para ma run sya
ng multiple methods tulad ng pag gamit sa actionperformed
// kasi pag di ko ginamit or diniclare na instance variable sya limited ko lang magagamit ung
variable name.
// Instance variables is they can access sa buong class
JButton login, signup, clear, forgotpin;
JTextField cardTextField;
JPasswordField pinTextField;
// Placeholder instance variables labels that overlay the text fields
private JLabel cardPlaceholderLabel;
private JLabel pinPlaceholderLabel;
// for clearing fields.
private void clearFields() {
cardTextField.setText("");
pinTextField.setText("");
// for placeholder
cardPlaceholderLabel.setVisible(true);
pinPlaceholderLabel.setVisible(true);
}
// instant variable for limitation for sign in/ para ma disabled ng 5 mins ung sign in if 3 attemps
failed.
private static int loginAttempts = 0;
private static boolean signInLocked = false;
private static long signInlockoutTime = 0;
private static final int MAX_ATTEMPTS = 3;
private static final long LOCKOUT_DURATION = 5 * 60 * 1000;
Login() {
// frame attributes
setTitle("Login");
setSize(500,300);
setLayout(null);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//content title
JLabel text = new JLabel("WELCOME TO ATM");
text.setFont(new Font("Arial", Font.BOLD, 20));
text.setBounds(150,30,200,30);
add(text);
//content text
JLabel cardnum = new JLabel("Card No: ");
cardnum.setBounds(150,80,100,20);
add(cardnum);
JLabel pin = new JLabel("PIN: ");
pin.setBounds(150,135,100,20);
add(pin);
// Card Number Placeholder
cardPlaceholderLabel = new JLabel("Enter 8-digit Card No.");
cardPlaceholderLabel.setBounds(150, 100, 200, 25);
cardPlaceholderLabel.setForeground(Color.GRAY);
cardPlaceholderLabel.setFont(new Font("Arial", Font.ITALIC, 12));
cardPlaceholderLabel.setVisible(true);
add(cardPlaceholderLabel);
// PIN Placeholder
pinPlaceholderLabel = new JLabel("Enter 4-digit PIN");
pinPlaceholderLabel.setBounds(150, 155, 200, 25);
pinPlaceholderLabel.setForeground(Color.GRAY);
pinPlaceholderLabel.setFont(new Font("Arial", Font.ITALIC, 12));
pinPlaceholderLabel.setVisible(true);
add(pinPlaceholderLabel);
//input ng user or ung box na white
cardTextField = new JTextField(); // for card number
cardTextField.setBounds(150,100,200,25);
// finifilter or nirerestrict nya na numeric/numbers only ung pd itype
((PlainDocument)cardTextField.getDocument()).setDocumentFilter(new NumericDocument(8));
add(cardTextField);
pinTextField = new JPasswordField(); // for pin
pinTextField.setBounds(150, 155, 200, 25);
// finifilter or nirerestrict nya na numeric/numbers only ung pd itype
((PlainDocument)pinTextField.getDocument()).setDocumentFilter(new NumericDocument(4));
pinTextField.setEchoChar((char) 0); // Set a bullet character for hiding PIN
add(pinTextField);
// Create an eye icon button
JButton toggleButton = new JButton("👁"); // Unicode eye symbol
toggleButton.setFocusable(false);
toggleButton.setBorder(BorderFactory.createEmptyBorder());
add(toggleButton);
toggleButton.setBounds(360, 155, 40, 25);
// Toggle action to show/hide PIN
toggleButton.addActionListener(new ActionListener() {
private boolean isVisible = false;
@Override
public void actionPerformed(ActionEvent e) {
isVisible = !isVisible; // Toggle visibility
if (isVisible) {
pinTextField.setEchoChar((char) 0); // Show PIN as text
toggleButton.setText("🙈"); // Change icon
} else {
pinTextField.setEchoChar('•'); // Hide PIN
toggleButton.setText("👁");
}
}
});
// Buttons
login = new JButton("Sign in");
login.setBounds(150,190,100,30);
login.setBackground(Color.BLACK);
login.setForeground(Color.WHITE);
login.addActionListener(this);
add(login);
clear = new JButton("Clear");
clear.setBounds(270,190,100,30);
clear.setBackground(Color.BLACK);
clear.setForeground(Color.WHITE);
clear.addActionListener(this);
add(clear);
signup = new JButton("Sign up");
signup.setBounds(150,230,100,30);
signup.setBackground(Color.BLACK);
signup.setForeground(Color.WHITE);
signup.addActionListener(this);
add(signup);
forgotpin = new JButton("Forgot PIN");
forgotpin.setBounds(270, 230, 100, 30);
forgotpin.setBackground(Color.BLACK);
forgotpin.setForeground(Color.WHITE);
forgotpin.addActionListener(this);
add(forgotpin);
cardTextField.addFocusListener(new FocusAdapter() {
@Override
public void focusGained(FocusEvent e) {
cardPlaceholderLabel.setVisible(false);// Hide placeholder
}
@Override
public void focusLost(FocusEvent e) {
// Show placeholder if field is empty
cardPlaceholderLabel.setVisible(cardTextField.getText().isEmpty());
}
});
pinTextField.addFocusListener(new FocusAdapter() {
@Override
public void focusGained(FocusEvent e) {
pinPlaceholderLabel.setVisible(false); // Hide placeholder
}
@Override
public void focusLost(FocusEvent e) {
// Show placeholder if field is empty
pinPlaceholderLabel.setVisible(pinTextField.getPassword().length == 0);
}
});
getContentPane().setComponentZOrder(cardPlaceholderLabel, 0);
getContentPane().setComponentZOrder(cardTextField, 1);
getContentPane().setComponentZOrder(pinPlaceholderLabel, 0);
getContentPane().setComponentZOrder(pinTextField, 1);
//pinapalitan nya ung color ng JFrame bg or sineset nya, pag wala to di lumalabas ung ibang
components kasi nag bleblend sa color default.
getContentPane().setBackground(Color.WHITE);
setLocationRelativeTo(null);
setVisible(true);
}
//when any button is clicked, ang tawag don is actionPerformed(ActionEvent e)
// e.getSource() kinukuha nya anong button ung pinindot mo and it would trigger the action for
that button
@Override
public void actionPerformed(ActionEvent e){
if (e.getSource() == clear) { // clears both input fields
// iche-check nya kung may text ba sa field then lalabas confirmation button.
boolean hasTextToClear = !cardTextField.getText().isEmpty() || pinTextField.getPassword().length
> 0;
if (hasTextToClear) { // Show confirmation dialog only if there's text to clear
int response = JOptionPane.showConfirmDialog
(this, "Are you sure you want to clear all fields?", "Confirmation",
JOptionPane.YES_NO_OPTION
);
// clears the text if yes.
if (response == JOptionPane.YES_OPTION) {
clearFields();
}
} else {
clearFields();
}
} else if (e.getSource() == login){
if (signInLocked) {
long currentTime = System.currentTimeMillis();
if (currentTime - signInlockoutTime < LOCKOUT_DURATION) {
long remainingTime = (LOCKOUT_DURATION - (currentTime - signInlockoutTime)) / 1000;
JOptionPane.showMessageDialog(null, "Sign in temporarily locked. Please try again in " +
remainingTime + " seconds.");
return;
} else {
// Lockout period has expired
signInLocked = false;
loginAttempts = 0;
}
}
//connector ng database
Conn conn = new Conn();
//Gets the input values from text fields
String cardnumber = cardTextField.getText();
String pinnumber = pinTextField.getText();
// this line of code is ichecheck nya kung walang input
if (cardnumber.isEmpty() || pinnumber.isEmpty()) {
JOptionPane.showMessageDialog(null,"Please enter both Card Number and PIN.");
return; // stop execution if field are empty
}
// this line of code is ichecheck nya kung walang input/kulang
if (!cardnumber.matches("\\d{8}") || !pinnumber.matches("\\d{4}")) {
if (!cardnumber.matches("\\d{8}") && !pinnumber.matches("\\d{4}")) {
JOptionPane.showMessageDialog(null,"Card Number must be 8 digits and Pin must be 4
digits");
} else if (!cardnumber.matches("\\d{8}")) {
JOptionPane.showMessageDialog(null, "Card Number must be exactly 8 digits");
}
else {
JOptionPane.showMessageDialog(null,"Pin must be exactly 4 digits");
}
return;
}
// checking credentials from the database
// i verify sya sa database if the cardnumber and pin is nag eexist ba sya sa db. before ma
access ng user ung transaction class/features.
String query = "select * from login where cardnumber = '"+cardnumber+"' and pinnumber = '"+
pinnumber+"'";
// execution ng sql querry
try {
ResultSet rs = conn.s.executeQuery(query); // mapupunta sya sa db
//if records is match, login success.
if (rs.next()) {
loginAttempts = 0; // Reset attempts on successful login
setVisible(false); //hides the login window
new Transactions(cardnumber).setVisible(true);
} else {
loginAttempts++;
if (loginAttempts >= MAX_ATTEMPTS) {
signInLocked = true;
signInlockoutTime = System.currentTimeMillis();
JOptionPane.showMessageDialog(null, "Too many failed attempts. Sign in locked for 5
minutes.");
} else {
JOptionPane.showMessageDialog(null, "Incorrect Card number or PIN, Attempts remaining: " +
(MAX_ATTEMPTS - loginAttempts)); // invalid credentials and
}
}
} catch (Exception ae) {
System.out.println(ae);
}
// opens the signupone for new users
} else if (e.getSource() == signup){
setVisible(false); // hides the current login window
new SignupOne().setVisible(true); //Opens the SignupOne form to register a new account.
} else if (e.getSource() == forgotpin) {
setVisible(false);
new ForgotPin().setVisible(true);
}
}
public static void main(String[] args){
new Login();
}
}