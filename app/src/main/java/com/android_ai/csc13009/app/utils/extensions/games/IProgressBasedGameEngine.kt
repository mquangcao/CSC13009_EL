package com.android_ai.csc13009.app.utils.extensions.games;

public interface IProgressBasedGameEngine : IGameEngine {
    val maxRound: Int
    var currentRound: Int;

    override suspend fun submitAnswer(answer: String) {
        if (answer.uppercase() == currentWord?.word!!.uppercase()) {
            score += 1000;
            score += streak * 100;
            streak++;
            nextRound();
        } else {
            // neu dung 80% van tinh diem
            score += (1000 * gaugeCorrectness(answer)).toInt();
            streak = 0;
            nextRound();
        }
    }

    private fun gaugeCorrectness(answer: String): Double {
        val correctAnswer = currentWord?.word!!.uppercase()
        val answerUppercase = answer.uppercase()

        val length = correctAnswer.length
        var correctLetterCount = 0

        for (i in 0 until answerUppercase.length) {
            if (answerUppercase[i] == correctAnswer[i]) {
                correctLetterCount++
            }
        }

        var result = correctLetterCount.toDouble() / length
        if (result < 0.8) {
            result = 0.0
        }
        return result
    }

    override fun nextRound() {
        currentRound++;
        if (currentRound >= maxRound) {
            endGame();
            return;
        }

        currentWord = words[currentRound];
    }

    override suspend fun startGame() {
        for (i in 1..maxRound) {
            fetchWord()
        }
        nextRound()
        super.startGame()
    }
}
