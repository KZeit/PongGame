package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PongGame extends Application 
{
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int PADDLE_WIDTH = 15;
    private static final int PADDLE_HEIGHT = 100;
    private static final int BALL_RADIUS = 10;

    private double paddle1Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
    private double paddle2Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
    private double ballX = WIDTH / 2;
    private double ballY = HEIGHT / 2;
    private double ballSpeedX = 3.5;
    private double ballSpeedY = 3.5;

    private int player1Score = 0;
    private int player2Score = 0;
    private String player1Name = "Player 1";
    private String player2Name = "Player 2";

    @Override
    public void start(Stage primaryStage) 
    {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), event -> 
        {
            update();
            draw(gc);
        }));
        
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        StackPane root = new StackPane(canvas);
        root.setStyle("-fx-background-color: black;"); 

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setOnKeyPressed(event -> handleKeyPress(event.getCode()));

        primaryStage.setTitle("Pong Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void update() 
    {
        movePaddle1();
        movePaddle2();
        
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        if (ballX - BALL_RADIUS < PADDLE_WIDTH && ballY > paddle1Y && ballY < paddle1Y + PADDLE_HEIGHT) 
        {
            ballSpeedX = Math.abs(ballSpeedX);
        } else if (ballX + BALL_RADIUS > WIDTH - PADDLE_WIDTH && ballY > paddle2Y && ballY < paddle2Y + PADDLE_HEIGHT) 
        {
            ballSpeedX = -Math.abs(ballSpeedX);
        }

        if (ballY - BALL_RADIUS < 0 || ballY + BALL_RADIUS > HEIGHT) 
        {
            ballSpeedY = -ballSpeedY;
        }

        if (ballX - BALL_RADIUS < 0) 
        {
            player2Score++;
            checkGameEnd();
            resetBall();
        } else if (ballX + BALL_RADIUS > WIDTH) 
        {
            player1Score++;
            checkGameEnd();
            resetBall();
        }
    }

    private void draw(GraphicsContext gc) 
    {
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        drawPaddle(gc, 0, paddle1Y, PADDLE_WIDTH, PADDLE_HEIGHT, Color.BLUE, Color.CYAN);
        drawPaddle(gc, WIDTH - PADDLE_WIDTH, paddle2Y, PADDLE_WIDTH, PADDLE_HEIGHT, Color.RED, Color.ORANGE);

        gc.setFill(Color.WHITE);
        gc.fillOval(ballX - BALL_RADIUS, ballY - BALL_RADIUS, BALL_RADIUS * 2, BALL_RADIUS * 2);

        gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        gc.setFill(Color.WHITE);
        gc.fillText(player1Name + ": " + player1Score, WIDTH / 4 - 50, 30);
        gc.fillText(player2Name + ": " + player2Score, WIDTH * 3 / 4 - 120, 30);
    }

    private void drawPaddle(GraphicsContext gc, double x, double y, double width, double height, Color startColor, Color endColor) 
    {
        LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, null,
                new Stop(0, startColor),
                new Stop(1, endColor));
        gc.setFill(gradient);
        gc.fillRect(x, y, width, height);
    }

    private void handleKeyPress(KeyCode code) 
    {
        switch (code) 
        {
            case W:
                movePaddle1Up();
                break;
            case S:
                movePaddle1Down();
                break;
            case UP:
                movePaddle2Up();
                break;
            case DOWN:
                movePaddle2Down();
                break;
        }
    }

    private void movePaddle1Up() 
    {
        if (paddle1Y > 0) 
        {
            paddle1Y -= 20;
        }
    }

    private void movePaddle1Down() 
    {
        if (paddle1Y + PADDLE_HEIGHT < HEIGHT) 
        {
            paddle1Y += 20;
        }
    }

    private void movePaddle2Up() 
    {
        if (paddle2Y > 0) 
        {
            paddle2Y -= 20;
        }
    }

    private void movePaddle2Down() 
    {
        if (paddle2Y + PADDLE_HEIGHT < HEIGHT) 
        {
            paddle2Y += 20;
        }
    }

    private void resetBall() 
    {
        ballX = WIDTH / 2;
        ballY = HEIGHT / 2;
        ballSpeedX = 3.5;
        ballSpeedY = 3.5;
    }

    private void movePaddle1() 
    {
    }

    private void movePaddle2() {
    }

    private void checkGameEnd() 
    {
        if (player1Score == 5 || player2Score == 5) 
        {
            displayWinner();
        }
    }

    private void displayWinner() 
    {
        String winner = (player1Score == 5) ? player1Name : player2Name;
        System.out.println("Game over! " + winner + " wins!");
        System.exit(0);
    }

    public static void main(String[] args) 
    {
        launch(args);
    }
}
