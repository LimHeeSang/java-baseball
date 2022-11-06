package baseball.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Player {

    public static final String NUMBER_SIZE_EX_MESSAGE = "입력값은 3개의 숫자여야 합니다.";
    public static final int GAME_ROUND = 3;
    public static final String NUMBER_DUPLICATE_EX_MESSAGE = "입력값은 같은 수가 하나라도 있으면 안됩니다.";
    public static final int START_POSITION_NUMBER = 1;

    private List<Ball> balls;

    public Player() {
    }

    public Player(List<Integer> numbers) {
        validate(numbers);
        balls = mapToBall(numbers);
    }

    private void validate(List<Integer> numbers) {
        validateSize(numbers);
        validateDuplicate(numbers);
    }

    private void validateSize(List<Integer> numbers) {
        if (numbers.size() != GAME_ROUND) {
            throw new IllegalArgumentException(NUMBER_SIZE_EX_MESSAGE);
        }
    }

    private void validateDuplicate(List<Integer> numbers) {
        Set<Integer> checkDuplicateNumbers = new HashSet<>(numbers);
        if (checkDuplicateNumbers.size() != GAME_ROUND) {
            throw new IllegalArgumentException(NUMBER_DUPLICATE_EX_MESSAGE);
        }
    }

    private List<Ball> mapToBall(List<Integer> numbers) {
        AtomicInteger position = new AtomicInteger(START_POSITION_NUMBER);
        return numbers.stream()
                .map(number -> new Ball(position.getAndIncrement(), number))
                .collect(Collectors.toList());
    }

    public GameResult playGame(Player otherPlayer) {
        GameResult gameResult = new GameResult();

        for (Ball ball : balls) {
            BallStatus ballStatus = otherPlayer.playRound(ball);
            gameResult.addScore(ballStatus);
        }
        return gameResult;
    }

    private BallStatus playRound(Ball otherBall) {
        return balls.stream()
                .map(ball -> ball.play(otherBall))
                .filter(ballStatus -> !ballStatus.isNothing())
                .findFirst()
                .orElse(BallStatus.NOTHING);
    }

    public void changeBalls(List<Integer> newBallNumbers) {
        validate(newBallNumbers);
        balls = mapToBall(newBallNumbers);
    }
}