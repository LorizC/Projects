//Signin.java
import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.awt.event.*;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.sql.*; // This imports all SQL-related classes, including Connection, Statement, and
ResultSet.
public class SignupOne extends JFrame implements ActionListener {
JTextField name1, securityAnswerField;
JDateChooser bdate;
JButton submitButton, cancelButton;
long random;
JComboBox<String> securityQuestionCombo;
SignupOne() {
setLayout(null);
setTitle("Sign Up Form For New User");
// generate random application number
Random ran = new Random();
random = Math.abs((ran.nextLong() % 9000L) + 1000L) ;
// application form number
JLabel formno = new JLabel("APPLICATION FORM NO. " + random);
formno.setFont(new Font("Arial", Font.BOLD, 14));
formno.setBounds(150,20,250,30);
add(formno);
//Text of personal details
JLabel personDetails = new JLabel("Personal Details");
personDetails.setBounds(150,60,200,20);
personDetails.setFont(new Font("Arial", Font.BOLD, 12));
add(personDetails);
// name:
JLabel name = new JLabel("Name: ");
name.setBounds(50,90,80,20);
add(name);
//JTextField
name1 = new JTextField();
name1.setBounds(150, 90, 250, 25);
add(name1);
//JDateChooser
bdate = new JDateChooser();
bdate.setBounds(150,125,250,25);
bdate.getDateEditor().getUiComponent().setEnabled(false); // Disable direct text editing
add(bdate);
// Get today's date
Calendar today = Calendar.getInstance();
today.add(Calendar.DAY_OF_MONTH, -1); //move 1 day back(yesterday)
Date yesterday = today.getTime();
// Disable today and future dates
bdate.setMaxSelectableDate(yesterday);
JTextField dateField = (JTextField) bdate.getDateEditor().getUiComponent();
dateField.setBorder(BorderFactory.createCompoundBorder(
BorderFactory.createLineBorder(Color.GRAY),
BorderFactory.createEmptyBorder(0,5,0,5)
));
dateField.setBackground(Color.WHITE);
bdate.getDateEditor().addPropertyChangeListener("date", evt -> {
Date selectedDate = bdate.getDate();
if (selectedDate != null && !selectedDate.before(yesterday)) {
dateField.setBorder(BorderFactory.createLineBorder(Color.RED));
JOptionPane.showMessageDialog(this, "Invalid date! Please select a past date.", "Warning",
JOptionPane.WARNING_MESSAGE);
bdate.setDate(null); // Reset selection
} else {
// Restore natural border
dateField.setBorder(BorderFactory.createCompoundBorder(
BorderFactory.createLineBorder(Color.GRAY),
BorderFactory.createEmptyBorder(0,5,0,5)
));
}
});
// Date of birth
JLabel bday = new JLabel("Date of Birth: ");
bday.setBounds(50,125,80,20);
add(bday);
// Security Question
JLabel secQues = new JLabel("Security Question:");
secQues.setBounds(50,160,108,20);
add(secQues);
String[] questions = {
"What was your first pet's name?",
"What city were you born in?",
"What is your mother's maiden name?",
"What was your first school?"
};
securityQuestionCombo = new JComboBox<>(questions);
securityQuestionCombo.setBounds(150, 160, 250, 25);
add(securityQuestionCombo);
JLabel answerLabel = new JLabel("Answer:");
answerLabel.setBounds(50, 195, 80, 20);
add(answerLabel);
securityAnswerField = new JTextField();
securityAnswerField.setBounds(150,195,250,25);
add(securityAnswerField);
//JButtons
submitButton = new JButton("Submit");
submitButton.setBounds(270, 240, 100, 30);
submitButton.addActionListener(this);
submitButton.setBackground(Color.BLACK);
submitButton.setForeground(Color.WHITE);
add(submitButton);
cancelButton = new JButton("Cancel");
cancelButton.setBounds(150, 240, 100, 30);
cancelButton.addActionListener(this);
cancelButton.setBackground(Color.BLACK);
cancelButton.setForeground(Color.WHITE);
add(cancelButton);
getContentPane().setBackground(Color.WHITE);
setSize(500,300);
setLocation(300,100);
setLocationRelativeTo(null);
setVisible(true);
}
@Override
public void actionPerformed(ActionEvent e) {
String formNumber = String.valueOf(random);
String name = name1.getText();
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
String birthDate = (bdate.getDate() != null)? sdf.format(bdate.getDate()) : "";
// Get security question and answer from components
String securityQuestion = (String) securityQuestionCombo.getSelectedItem();
String securityAnswer = securityAnswerField.getText(); // Get answer from text field
if (e.getSource() == submitButton) {
if (name.isEmpty()) {
JOptionPane.showMessageDialog(null, "Name is Required.");
return;
} else if (birthDate.isEmpty()) {
JOptionPane.showMessageDialog(null, "Date of Birth is Required.");
return;
} else if (securityAnswer.isEmpty()) {
JOptionPane.showMessageDialog(null, "Security answer is required");
return;
}
int confirmation = JOptionPane.showConfirmDialog(
this,
"Are you sure you want to submit this form?\n\n" +
"Name: " + name + "\n" +
"Birth Date: " + birthDate + "\n" +
"Security Question: " + securityQuestion + "\n" +
"Answer: " + securityAnswer,"Confirm Submission",
JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
);
if (confirmation == JOptionPane.YES_OPTION) {
try {
Conn c = new Conn();
Random random = new Random();
String cardnumber;
boolean isCardUnique;
do {
cardnumber = "5040" + String.format("%04d", random.nextInt(10000));
// Check database for duplicates
String checkCardQuery = "SELECT cardnumber FROM signup WHERE cardnumber = '" +
cardnumber + "'";
ResultSet rsCard = c.s.executeQuery(checkCardQuery);
//Determine uniqueness
isCardUnique = !rsCard.next();
} while (!isCardUnique);
String pinnumber;
//boolean isPinUnique;
//do {
// no duplicates pins exist in the database
pinnumber = String.valueOf(1000 + random.nextInt(9000));
//String checkPinQuery = "SELECT pinnumber FROM signup WHERE pinnumber = '" +
pinnumber + "'";
//ResultSet rs = c.s.executeQuery(checkPinQuery);
//isPinUnique = !rs.next() ;
// If the PIN is NOT found in the database, it's unique
//} while (!isPinUnique);
// Database querry, insert the data to sql column.
String query1 = "INSERT INTO signup (formno, name, bdate, cardnumber, pinnumber,
security_question, security_answer) VALUES ('" + formNumber + "', '" + name + "', '" + birthDate
+ "', '" + cardnumber + "', '" + pinnumber + "', '" + securityQuestion + "', '" + securityAnswer + "')";
String query2 = "INSERT INTO login (formNumber, cardnumber, pinnumber) VALUES ('"+
formNumber+"','"+cardnumber+"', '"+pinnumber+"')";
c.s.executeUpdate(query1);
c.s.executeUpdate(query2);
JOptionPane.showMessageDialog(this, "Sign Up Successful!\n\n" + "Card Number: " +
cardnumber + "\n" + "PIN: " + pinnumber + "\n\n" + "Please keep this information secure.",
"Success", JOptionPane.INFORMATION_MESSAGE);
setVisible(false);
new Login().setVisible(true);
} catch (Exception ae) {
JOptionPane.showMessageDialog(this,
"An error occurred during submission: " + ae.getMessage(), "Error",
JOptionPane.ERROR_MESSAGE);
System.out.println(ae);
}
}
} else if (e.getSource() == cancelButton) {
int response = JOptionPane.showConfirmDialog
(this, "Are you sure you want to cancel?", "Confirmation",
JOptionPane.YES_NO_OPTION
);
if (response == JOptionPane.YES_OPTION) {
setVisible(false);
new Login().setVisible(true);
}
}
}
public static void main(String[] args){
new SignupOne();
}
}