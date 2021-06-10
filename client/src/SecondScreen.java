import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondScreen {


    public static JFrame createFrame(int fileId,String fileName, byte[] fileData, String fileExtension) {

        // Frame to hold everything.
        JFrame jFrame = new JFrame("File System Server");
        // Set the size of the frame.
        jFrame.setSize(500, 300);

        // Panel to hold everything.
        JPanel jPanel = new JPanel();
        // Make the layout a box layout with child elements stacked on top of each other.
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        // Title above panel.
        JLabel jlTitle = new JLabel("What do you want?");
        // Center the label title horizontally.
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Change the font family, size, and style.
        jlTitle.setFont(new Font("Arial", Font.BOLD, 25));
        // Add spacing on the top and bottom of the element.
        jlTitle.setBorder(new EmptyBorder(20,0,10,0));

        // Label to prompt the user if they are sure they want to download the file.
        JLabel jlPrompt = new JLabel("Delete this file or download?");
        // Change the font style, size, and family of the label.
        jlPrompt.setFont(new Font("Arial", Font.PLAIN, 20));
        // Add spacing on the top and bottom of the label.
        jlPrompt.setBorder(new EmptyBorder(20,0,10,0));
        // Center the label horizontally.
        jlPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create the yes for accepting the download.
        JButton downloadButon = new JButton("Download");
        downloadButon.setPreferredSize(new Dimension(170, 50));
        downloadButon.setBackground(Color.black);
        downloadButon.setForeground(Color.green);
        downloadButon.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.gray),
                new EmptyBorder(new Insets(15, 25, 15, 25))));
        // Set the font for the button.
        downloadButon.setFont(new Font("Arial", Font.BOLD, 20));

        // No button for rejecting the download.
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(Color.black);
        deleteButton.setForeground(Color.red);
        deleteButton.setBorder(BorderFactory.createCompoundBorder(
                new CustomeBorder(Color.gray),
                new EmptyBorder(new Insets(15, 25, 15, 25))));
        // Change the size of the button must be preferred because if not the layout will ignore it.
        deleteButton.setPreferredSize(new Dimension(150, 50));
        // Set the font for the button.
        deleteButton.setFont(new Font("Arial", Font.BOLD, 20));


        // Panel to hold the yes and no buttons and make the next to each other left and right.
        JPanel jpButtons = new JPanel();
        // Add spacing around the panel.
        jpButtons.setBorder(new EmptyBorder(20, 0, 10, 0));
        // Add the yes and no buttons.
        jpButtons.add(downloadButon);
        jpButtons.add(deleteButton);


        // Yes so download file.
        downloadButon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create the file with its name.
//                File fileToDownload = new File(fileName);
//                try {
//                    // Create a stream to write data to the file.
//                    FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);
//                    // Write the actual file data to the file.
//                    fileOutputStream.write(fileData);
//                    // Close the stream.
//                    fileOutputStream.close();
//                    // Get rid of the jFrame. after the user clicked yes.
//                    jFrame.dispose();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
                Client.downloadFile(fileId);

            }
        });

        // No so close window.
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // User clicked no so don't download the file but close the jframe.
                Client.deleteFile(fileId);
                jFrame.dispose();
            }
        });

        // Add everything to the panel before adding to the frame.
        jPanel.add(jlTitle);
        jPanel.add(jlPrompt);
        jPanel.add(jpButtons);

        // Add panel to the frame.
        jFrame.add(jPanel);

        // Return the jFrame so it can be passed the right data and then shown.
        return jFrame;

    }
}
