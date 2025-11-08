package client;

import model.Magazine;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.File;

public class ClientUI extends JFrame {
    private JTextField titleField;
    private JTextArea descArea;
    private JTextField fileField;
    private JButton uploadButton;
    private JLabel previewLabel;

    public ClientUI() {
        setTitle("📰 Digital Wall Magazine Uploader");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setBounds(40, 30, 100, 25);
        titleField = new JTextField();
        titleField.setBounds(150, 30, 250, 25);

        JLabel descLabel = new JLabel("Description:");
        descLabel.setBounds(40, 70, 100, 25);
        descArea = new JTextArea();
        descArea.setBounds(150, 70, 250, 80);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);

        JLabel fileLabel = new JLabel("File Path:");
        fileLabel.setBounds(40, 170, 100, 25);
        fileField = new JTextField();
        fileField.setBounds(150, 170, 250, 25);
        JButton browseButton = new JButton("Browse");
        browseButton.setBounds(150, 200, 100, 25);

        uploadButton = new JButton("Upload");
        uploadButton.setBounds(200, 240, 120, 30);

        previewLabel = new JLabel();
        previewLabel.setBounds(150, 290, 200, 150);
        previewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        add(titleLabel);
        add(titleField);
        add(descLabel);
        add(descArea);
        add(fileLabel);
        add(fileField);
        add(browseButton);
        add(uploadButton);
        add(previewLabel);

        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(ClientUI.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                fileField.setText(selectedFile.getAbsolutePath());
                ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
                Image image = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
                previewLabel.setIcon(new ImageIcon(image));
            }
        });

        uploadButton.addActionListener(e -> {
            String title = titleField.getText();
            String desc = descArea.getText();
            String filePath = fileField.getText();

            if (title.isEmpty() || desc.isEmpty() || filePath.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!");
                return;
            }

            try {
                Socket socket = new Socket("localhost", 5000);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(new Magazine(title, desc, filePath));
                oos.flush();
                oos.close();
                socket.close();

                JOptionPane.showMessageDialog(this, "✅ Uploaded successfully!");
                titleField.setText("");
                descArea.setText("");
                fileField.setText("");
                previewLabel.setIcon(null);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "❌ Upload failed.");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientUI().setVisible(true));
    }
}
