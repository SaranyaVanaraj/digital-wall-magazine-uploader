package server;
import model.Magazine;
import database.DBConnection;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
public class ServerApp {
    public static void main(String[] args) {
        int port = 5000;
        System.out.println("🚀 Server started on port " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("📩 Client connected.");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Magazine magazine = (Magazine) ois.readObject();
                File file = new File(magazine.getFilePath());
                if (!file.exists()) {
                    System.out.println("❌ File not found: " + file.getAbsolutePath());
                    continue;
                }
                File uploadDir = new File("uploads");
                if (!uploadDir.exists()) uploadDir.mkdir();

                Path destination = Paths.get(uploadDir.getAbsolutePath(), file.getName());
                Files.copy(file.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

                try (Connection conn = DBConnection.getConnection()) {
                    String query = "INSERT INTO magazines (title, description, file_path, upload_date) VALUES (?, ?, ?, NOW())";
                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setString(1, magazine.getTitle());
                    ps.setString(2, magazine.getDescription());
                    ps.setString(3, destination.toString());
                    ps.executeUpdate();
                    System.out.println("✅ Magazine saved to database.");
                }
                ois.close();
                socket.close();
                System.out.println("✅ File uploaded successfully!\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
