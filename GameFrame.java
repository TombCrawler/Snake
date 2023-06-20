import javax.swing.JFrame;

public class GameFrame extends JFrame{
    
    GameFrame(){
        // this is a conventional way to inherit a class
        // GamePanel panel = new GamePanel();
        // this.add(panel);

        this.add(new GamePanel()); // but you can do so in one line
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null); // to make the game screen appear in the middle
    }
}