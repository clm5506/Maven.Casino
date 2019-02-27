package io.zipcoder.casino.Games;

import io.zipcoder.casino.CardsAndDice.Dice;
import io.zipcoder.casino.Casino.Casino;
import io.zipcoder.casino.Players.CrapsPlayer;
import io.zipcoder.casino.utilities.Console;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Craps implements Game {
    private CrapsPlayer user = new CrapsPlayer(Casino.getProfile());
    private int initialBalance = user.getProfile().getBalance();
    Console console = Console.getConsole();
    private int point = 0;
    private boolean toWinPassBet = false;
    private boolean toWinLayBet = false;
    private boolean isFirstRoll = true;
    private boolean isOver = false;
    private int placeBetChoice;
    private final int[] anyCraps = {2, 3, 12};
    private final int[] hornBetRolls = {2, 3, 11, 12};
    private final int[] hardwaysRolls = {4, 6, 8, 10};
    private final int[] placeNumberRolls = {4, 5, 6, 8, 9, 10};
    private final int[] fieldNumberRolls = {2, 3, 4, 9, 10, 11, 12};
    private int adjustedBalance = initialBalance;
    private int roll1;
    private int roll2;
    private int rollSum;
    private int hardwaysRoll;
    private boolean isAnyCrapsBet;
    private boolean isFieldBet;
    private boolean isHornBet;
    private boolean isPlaceBet;
    private boolean isLayBet;
    private int firstRollSum;
    Dice die1 = new Dice();
    Dice die2 = new Dice();
    private int betAmount = 0;
    private int hardwaysBet = 0;
    private boolean rollSumHardways = false;
    private GameStatus gameState;
    private boolean isHardwaysBet = false;
    private boolean isCrappedRolls = false;
    private boolean isNatural = false;
    private boolean isEleven = false;
    private boolean isPoint = false;
    private boolean isPlaying = true;

    public boolean getToWinPassBet() {
        return toWinPassBet;
    }

    public void setIsFirstRoll(boolean isFirstRoll) {
        this.isFirstRoll = isFirstRoll;
    }

    public void setRollSum(int rollSum) {
        this.rollSum = rollSum;
    }

    public Map<String, Boolean> getBetMap() {
        return betMap;
    }

    public void setToWinPassBet(boolean toWinPassBet) {
        this.toWinPassBet = toWinPassBet;
    }

    public void setBetAmount(int betAmount) {
        this.betAmount = betAmount;
    }

    public int getAdjustedBalance() {
        return adjustedBalance;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getPoint() {
        return point;
    }

    public boolean getIsFirstRoll() {
        return isFirstRoll;
    }

    public void setBetToTrueOnBetMap(String hardways) {
        betMap.put(hardways, true);
    }

    public void clearBets() {
        betMap.put("Pass Bet", false);
        betMap.put("Place Bet", false);
        betMap.put("Lay Bet", false);
        betMap.put("Any Craps", false);
        betMap.put("The Field", false);
        betMap.put("The Horn", false);
        betMap.put("Hardways", false);
        betMap.put("Hop Bet", false);
    }

    public void setIsPlaceBet(boolean isPlaceBet) {
        this.isPlaceBet = isPlaceBet;
    }

    public boolean getIsPlaceBet() {
        return isPlaceBet;
    }

    public void setIsLayBet(boolean isLayBet) {
        this.isLayBet = isLayBet;
    }

    public boolean getIsLayBet() {
        return isLayBet;
    }

    public void setIsAnyCrapsBet(boolean isAnyCrapsBet) {
        this.isAnyCrapsBet = isAnyCrapsBet;
    }

    public boolean getIsAnyCrapsBet() {
        return isAnyCrapsBet;
    }

    public boolean getIsHardwaysBet() {
        return isHardwaysBet;
    }

    public void setIsHardwaysBet(boolean isHardwaysBet) {
        this.isHardwaysBet = isHardwaysBet;
    }

    public void setDie1(Dice dice) {
        die1 = dice;
    }

    public void setDie2(Dice dice) {
        die2 = dice;
    }

    public int getRollSum() {
        return rollSum;
    }

    public void setRollSumHardways(boolean rollSumHardways) {
        this.rollSumHardways = rollSumHardways;
    }

    public boolean getRollSumHardways() {
        return rollSumHardways;
    }

    public void setIsNatural(boolean isNatural) {
        this.isNatural = isNatural;
    }

    public boolean getIsNatural() {
        return isNatural;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public boolean getIsPlaying() {
        return this.isPlaying;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public void setAdjustedBalance(int adjustedBalance) {
        this.adjustedBalance = adjustedBalance;
    }

    public void setToWinLayBet(boolean toWinLayBet) {
        this.toWinLayBet = toWinLayBet;
    }

    public void setIsOver(boolean isOver) {
        this.isOver = isOver;
    }

    public void setIsCrappedRolls(boolean isCrappedRolls) {
        this.isCrappedRolls = isCrappedRolls;
    }

    public void setIsPoint(boolean isPoint) {
        this.isPoint = isPoint;
    }

    public void setIsFieldBet(boolean isFieldBet) {
        this.isFieldBet = isFieldBet;
    }

    public void setIsHornBet(boolean isHornBet) {
        this.isHornBet = isHornBet;
    }

    public void setHardwaysRoll(int hardwaysRoll) {
        this.hardwaysRoll = hardwaysRoll;
    }

    public void setPlaceBetChoice(int placeBetChoice) {
        this.placeBetChoice = placeBetChoice;
    }

    public boolean getIsOver() {
        return this.isOver;
    }

    public void setInitialBalance(int initialBalance) {
        this.initialBalance = initialBalance;
    }

    public CrapsPlayer getUser() {
        return user;
    }

    private enum GameStatus {UNRESOLVED, WON, LOST}

    public enum BetList {
        PLACE("always"),
        LAY("always"),
        ANYCRAPS("oneOff"),
        FIELD("oneOff"),
        HORN("oneOff"),
        HARDWAYS("always");
        private String typeOfBet;

        BetList(String typeOfBet) {
            this.typeOfBet = typeOfBet;
        }

        public static Stream<BetList> stream() {
            return Stream.of(BetList.values());
        }
    }

    private static Map<String, Boolean> betMap = new HashMap<>();

    static {
        betMap.put("Pass Bet", false);
        betMap.put("Place Bet", false);
        betMap.put("Lay Bet", false);
        betMap.put("Any Craps", false);
        betMap.put("The Field", false);
        betMap.put("The Horn", false);
        betMap.put("Hardways", false);
        betMap.put("Hop Bet", false);
    }

    private enum BetExplanations {
        PLACE("The Place bet wins if the chosen number is rolled before a 7"),
        LAY("The Lay bet wins if a 7 is rolled before the point"),
        ANYCRAPS("Any Craps pays off on 2, 3 or 12 before rolling a 7"),
        FIELD("The Field wins if 4, 5, 6, 8, 9 or 10 hits before 7"),
        HORN("The Horn wins on 2, 3, 11 or 12 before rolling a 7"),
        HARDWAYS("Hardways involves two dice of the same face value, which must be hit before 7 or the corresponding sum thrown 'easy'\n");
        private String betRules;

        BetExplanations(String betRules) {
            this.betRules = betRules;
        }

        public String getExplanations() {
            return betRules;
        }

        public static Stream<BetExplanations> stream() {
            return Stream.of(BetExplanations.values());
        }
    }

    public Craps() {
    }

    // Purely for testing
    public Craps(Console console) {
        this.console = console;
    }

    public static void main(String[] args) {
        Craps craps = new Craps();
        craps.play();
    }

    public void play() {
        console.println("Welcome to the craps table!");
        while (!isOver) {
            while (isFirstRoll) {
                promptBet();
                roll();
                evaluateFirstRoll();
            }
            while (isPlaying && gameState == GameStatus.UNRESOLVED) {
                promptBet();
                roll();
                evaluate();
            }
        }
    }

    public void evaluateFirstRoll() {
        if (Arrays.stream(anyCraps).anyMatch(i -> i == rollSum) && toWinPassBet) {
            console.println("Whomp, whomp, you crapped out\n");
            adjustBalance(-betAmount);
            resetPointAndRoll();
        } else if (rollSum == 7 && toWinPassBet || rollSum == 11 && toWinPassBet) {
            console.println("You won the Pass bet, keep it up!\n");
            gameState = GameStatus.WON;
            adjustBalance(betAmount);
            resetPointAndRoll();
        } else if (rollSum == 7 && !toWinPassBet || rollSum == 11 && !toWinPassBet) {
            console.println("You lost the Pass bet!\n");
            gameState = GameStatus.UNRESOLVED;
            adjustBalance(-betAmount);
            resetPointAndRoll();
        } else if (Arrays.stream(anyCraps).anyMatch(i -> i == rollSum) && !toWinPassBet) {
            console.println("You won the Don't Pass bet, keep it up!\n");
            gameState = GameStatus.UNRESOLVED;
            adjustBalance(betAmount);
            resetPointAndRoll();
        } else {
            point = rollSum;
            console.println("The point is now " + point + ".\nKeep on rollin', shooter!\n");
            gameState = GameStatus.UNRESOLVED;
            isFirstRoll = false;
        }
    }

    public void resetPointAndRoll() {
        point = 0;
        isFirstRoll = true;
        isPlaying = false;
    }

    public void promptBet() {
        if (isFirstRoll) {
            int betReturn = console.getIntegerInput("How much would you like to bet?");
            betAmount = betReturn;
            String passChoice = console.getStandardInput("Please choose 'Pass' or 'Don't Pass'");
            if (passChoice.equals("pass")) {
                toWinPassBet = true;
                betMap.put("Pass Bet", true);
            } else if (passChoice.equals("don't pass")) {
                toWinPassBet = false;
                betMap.put("Pass Bet", false);
            }
        } else if (!isFirstRoll) {
            int betChoice = console.getIntegerInput("You have five choices:\n1) Explain Possible Bets\n2) List Current Bets\n3) List Available Bets\n4) Make a Bet\n5) Continue Rolling");
            switch (betChoice) {
                case 1:
                    betRulesListed();
                    promptBet();
                    break;
                case 2:
                    console.println("You have the current bets: " + currentBetList(true) + "\n");
                    promptBet();
                    break;
                case 3:
                    listBets();
                    promptBet();
                    break;
                case 4:
                    makeBet();
                    break;
                case 5:
                    break;
            }
        }
    }

    public void makeBet() {
        String betName = console.getStandardInput("What bet would you like to place?");
        switch (betName) {
            case "place":
                isPlaceBet = true;
                placeBetChoice = console.getIntegerInput("What number do you want to make a Place Bet for?");
                if (Arrays.stream(placeNumberRolls).anyMatch(i -> i == placeBetChoice)) {
                    console.println("Excellent choice!");
                } else {
                    placeBetChoice = console.getIntegerInput("Stick to the Place numbers, buddy! Pick from 4, 5, 6, 8, 9 or 10");
                }
                break;
            case "lay":
                isLayBet = true;
                break;
            case "anycraps":
                isAnyCrapsBet = true;
                break;
            case "field":
                isFieldBet = true;
                break;
            case "horn":
                isHornBet = true;
                break;
            case "hardways":
                isHardwaysBet = true;
                hardwaysRoll = console.getIntegerInput("What number do you want to place a Hardways Bet on?");
                if (Arrays.stream(hardwaysRolls).anyMatch(i -> i == hardwaysRoll)) {
                    console.println("Excellent choice!\n");
                } else {
                    hardwaysRoll = console.getIntegerInput("Stick to the Place numbers, buddy! Pick from 4, 6, 8, or 10");
                }
                break;
            default:
                console.println("Please enter an actual bet, pal");
                makeBet();
        }
    }

    public void betRulesListed() {
        for (BetExplanations bet : BetExplanations.values()) {
            console.println(bet.getExplanations());
        }
    }

    public void filteredBetList(String betOptions) {
        BetList.stream()
                .filter(e -> e.typeOfBet.equals(betOptions))
                .forEach(console::println);
    }

    public List<String> currentBetList(boolean value) {
        return betMap
                .entrySet()
                .stream()
                .filter(e -> Objects.equals(e.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public void listBets() {
        int betList = console.getIntegerInput("Choose one of the follow:\n1) List Bets always available\n2) List one-off Bets\n3) Go to previous Menu");
        switch (betList) {
            case 1:
                filteredBetList("always");
                break;
            case 2:
                filteredBetList("oneOff");
                break;
            case 3:
                promptBet();
                break;
        }
    }

    public int roll() {
        roll1 = die1.roll();
        roll2 = die2.roll();
        rollSum = roll1 + roll2;
        if (roll1 == roll2) {
            rollSumHardways = true;
        } else if (roll1 != roll2) {
            rollSumHardways = false;
        }
        switch (rollSum) {
            case 2:
            case 3:
            case 12:
                isCrappedRolls = true;
            case 7:
                isNatural = true;
            case 11:
                isEleven = true;
            default:
                isNatural = false;
                isPoint = true;
        }
        console.println("\nHERE ARE YOUR DIE:\n" + Dice.getDiceString(roll1, roll2));
        isPlaying = true;
        return rollSum;
    }

    public void evaluate() {
        if (isNatural) {
            if (toWinLayBet)  {
                gameState = GameStatus.WON;
                console.println("Your Lay bet paid off!\n");
                adjustBalance(betAmount);
                promptQuit();
            }
            if (!toWinPassBet && gameState != GameStatus.WON) {
                gameState = GameStatus.WON;
                console.println("Your Don't Pass bet paid off!\n");
                adjustBalance(betAmount);
                promptQuit();
            } else if (!isOver && gameState != GameStatus.WON) {
                console.println("Sorry shooter, it looks like your hot streak has come to an end!\n");
                gameState = GameStatus.LOST;
                adjustBalance(-betAmount);
                resetPointAndRoll();
                promptQuit();
            }
        }
        if (isCrappedRolls && !isFirstRoll && gameState != GameStatus.WON && gameState != GameStatus.LOST) {
            if (isAnyCrapsBet && Arrays.stream(anyCraps).anyMatch(i -> i == rollSum)) {
                gameState = GameStatus.WON;
                console.println("Your Any Craps bet paid off!\n");
                adjustBalance(betAmount);
                promptQuit();
            }
            if (isAnyCrapsBet && Arrays.stream(anyCraps).noneMatch(i -> i == rollSum) && gameState != GameStatus.WON && gameState != GameStatus.LOST) {
                gameState = GameStatus.LOST;
                console.println("You lost your Any Craps bet!\n");
                adjustBalance(-betAmount);
                promptQuit();
            }
        }
        if (isPoint && gameState != GameStatus.WON && gameState != GameStatus.LOST) {
            if (isFieldBet && Arrays.stream(fieldNumberRolls).anyMatch(i -> i == rollSum) && gameState != GameStatus.WON && gameState != GameStatus.LOST) {
                gameState = GameStatus.WON;
                console.println("You won your Field bet!\n");
                adjustBalance(betAmount);
                promptQuit();
            }
            if (isHornBet && Arrays.stream(hornBetRolls).anyMatch(i -> i == rollSum) && gameState != GameStatus.WON && gameState != GameStatus.LOST) {
                gameState = GameStatus.WON;
                console.println("You won your Horn bet!\n");
                adjustBalance(betAmount);
                promptQuit();
            }
            if (isHardwaysBet && (roll1 == roll2) && hardwaysRoll == rollSum && gameState != GameStatus.WON && gameState != GameStatus.LOST) {
                gameState = GameStatus.WON;
                console.println("You won your Hardways bet!\n");
                adjustBalance(betAmount);
                promptQuit();
            }
            if (isLayBet && rollSum == 7 && gameState != GameStatus.WON && gameState != GameStatus.LOST) {
                gameState = GameStatus.WON;
                console.println("You won your Lay Bet!\n");
                adjustBalance(betAmount);
                promptQuit();
            }
            if (rollSum == point && toWinPassBet && gameState != GameStatus.WON && gameState != GameStatus.LOST) {
                console.println("Lucky number " + point + "! You hit the point!\n");
                adjustBalance(betAmount);
                promptQuit();
            } else {
                gameState = GameStatus.UNRESOLVED;
                isPlaying = true;
                console.println("Nothing won yet...\n");
            }
        }
        if (rollSum == placeBetChoice && gameState != GameStatus.WON && gameState != GameStatus.LOST) {
            console.println("Your Place bet paid off!\n");
            adjustBalance(betAmount);
            isPlaying = false;
            promptQuit();
        }
    }

    public void promptQuit() {
        String quitPrompt = console.getStandardInput("Would you like to quit while you're ahead?");
        if (quitPrompt.equals("yes")) {
            cashOut();
        } else if (quitPrompt.equals("no")) {
            console.println("Okay, you're a responsible adult, and you know your limits\n");
            isPlaying = true;
            resetPointAndRoll();
        }
    }

    public int adjustBalance(int profitOrLoss) {
        if (profitOrLoss > 0) {
            console.println(String.format("You're on a roll and %d NUCs richer!\n", profitOrLoss));
        } else if (profitOrLoss < 0) {
            console.println(String.format("%d NUCs gone already? Play again to win that back and more!\n", profitOrLoss));
        }
        adjustedBalance += profitOrLoss;
        return adjustedBalance;
    }

    public void compareBalance() {
        if (adjustedBalance > initialBalance) {
            console.println(String.format("You won %d NUCs!\n", (adjustedBalance - initialBalance)));
        } else if (adjustedBalance < initialBalance) {
            console.println(String.format("You're %d NUCs poorer!\n", (initialBalance - adjustedBalance)));
        } else if (adjustedBalance == initialBalance) {
            console.println("You broke even!\n");
        }
    }

    public void cashOut() {
        String continuePlaying = console.getStandardInput("Wouldn't you rather continue playing?");
        isPlaying = false;
        if (continuePlaying.equals("no")) {
            compareBalance();
            user.getProfile().setBalance(adjustedBalance);
            isOver = true;
        } else if (continuePlaying.equals("yes")) {
            isPlaying = true;
        }
    }
}
