import javax.swing.*;

public class Main extends JFrame {

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        this.setTitle("Chess (:!");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.add(new Board());
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}