package messenger.functional;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.UUID;

public class MessengerController {
    private String author;
    private MessengerHistory msgHistory;
    private MessengerSearch msgSearch;

    public MessengerController() {
        msgHistory = new MessengerHistory();
        msgSearch = new MessengerSearch();
    }

    public boolean isBackToMenu(String str) {
        Pattern p = Pattern.compile("@menu");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public boolean isCorrectChoice(String str) {
        Pattern p = Pattern.compile("\\d");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public boolean isMillisecond(String str) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public void runMainController() {
        File logFile = new File("logFile.txt");
        try (PrintWriter pw = new PrintWriter(logFile.getAbsoluteFile())) {
            menuController(pw);
        } catch (IOException e) {
            System.out.println("Error!");
        }
    }

    void menuController(PrintWriter pw) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM - HH:mm:ss");

        try (Scanner scanner = new Scanner(System.in)) {
            if (author == null) {
                System.out.print("Type your name: ");
                author = scanner.nextLine();

                pw.print(dateFormat.format(date) + "(" + author + "): ");
                pw.println("Changed nickname;");
            }

            System.out.println("----------------");
            System.out.println("1. add message");
            System.out.println("2. load messages");
            System.out.println("3. backup messages");
            System.out.println("4. show history");
            System.out.println("5. search message");
            System.out.println("6. remove message");
            System.out.println("7. change nickname");
            System.out.println("0. exit");

            int choice;
            String choiceStr;
            do {
                System.out.print("Your choice: ");
                choiceStr = scanner.nextLine();
            } while (!isCorrectChoice(choiceStr));
            choice = Integer.parseInt(choiceStr);

            switch (choice) {
                case 1:
                    System.out.println("----------------");
                    System.out.println("Input \"@menu\" to return to main menu.");

                    int historySize = msgHistory.historySize();
                    addMessage(scanner);

                    pw.print(dateFormat.format(date) + "(" + author + "): ");
                    pw.println("Added " + (msgHistory.historySize() - historySize) + " messages;");

                    menuController(pw);
                    break;
                case 2:
                    System.out.println("----------------");

                    int msgQuantity;
                    if ((msgQuantity = msgHistory.loadMessages()) != 0) {
                        System.out.println("Done!");
                    }

                    pw.print(dateFormat.format(date) + "(" + author + "): ");
                    pw.println("Loaded " + msgQuantity + " messages from history.json;");

                    menuController(pw);
                    break;
                case 3:
                    System.out.println("----------------");

                    int msgQuantityBackUpped;
                    if ((msgQuantityBackUpped = msgHistory.backupMessages()) != 0) {
                        System.out.println("Done!");
                    }

                    pw.print(dateFormat.format(date) + "(" + author + "): ");
                    pw.println("Back upped " + msgQuantityBackUpped + " messages to history.json;");

                    menuController(pw);
                    break;
                case 4:
                    System.out.println("----------------");

                    int msgQuantityHistory = msgHistory.showHistory();
                    pw.print(dateFormat.format(date) + "(" + author + "): ");
                    pw.println("Showed " + msgQuantityHistory + " messages from history;");

                    System.out.print("Press any key to continue...");
                    scanner.next();

                    menuController(pw);
                    break;
                case 5:
                    System.out.println("----------------");
                    searchController(scanner, pw);
                    menuController(pw);
                    break;
                case 6:
                    System.out.println("----------------");

                    String removeId;
                    System.out.print("Input id of the message: ");
                    removeId = scanner.nextLine();

                    if (msgHistory.removeMessageById(removeId)) {
                        pw.print(dateFormat.format(date) + "(" + author + "): ");
                        pw.println("Removed message with id " + removeId + ";");
                    } else {
                        pw.print(dateFormat.format(date) + "(" + author + "): ");
                        pw.println("Could not remove message with id " + removeId + ";");
                    }
                    menuController(pw);
                    break;
                case 7:
                    this.author = null;
                    menuController(pw);
                    break;
                default:
                    System.out.println("Incorrect input. Try again!");
                    menuController(pw);

                    pw.print(dateFormat.format(date) + "(" + author + "): ");
                    pw.println("Incorrect input in menu;");
                    break;
                case 0:
                    pw.print(dateFormat.format(date) + "(" + author + "): ");
                    pw.println("Finished the program;");
            }
        }
    }

    int addMessage(Scanner scanner) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("(dd:MM - HH:mm:ss)");
        UUID uniqueID = UUID.randomUUID();

        String message;
        message = scanner.nextLine();
        if (isBackToMenu(message)) {
            return 0;
        } else {

            System.out.println(this.author + "   " + dateFormat.format(date));
            System.out.println();

            msgHistory.addMessageToHistory(uniqueID.toString(), message, this.author, date.getTime());
            addMessage(scanner);
        }
        return 0;
    }

    void searchController(Scanner scanner, PrintWriter pw) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM - HH:mm:ss");

        System.out.println("1. Search by author");
        System.out.println("2. Search by key word");
        System.out.println("3. Search by regular expression");
        System.out.println("4. Search by period");
        System.out.println("0. Back to main menu");

        int choice;
        String choiceStr;
        do {
            System.out.print("Your choice: ");
            choiceStr = scanner.nextLine();
        } while (!isCorrectChoice(choiceStr));
        choice = Integer.parseInt(choiceStr);

        switch (choice) {
            case 1:
                System.out.println("----------------");
                System.out.print("Input author: ");

                String authorStr;
                authorStr = scanner.nextLine();

                System.out.println();
                int msgQuantityAuthor = msgSearch.showAllMessagesAuthor(authorStr, msgHistory);

                pw.print(dateFormat.format(date) + "(" + author + "): ");
                pw.println("Founded " + msgQuantityAuthor + " messages by " + authorStr + ";");

                System.out.print("Press any key to continue...");
                scanner.next();
                break;
            case 2:
                System.out.println("----------------");
                System.out.print("Input key word: ");

                String keyWordStr;
                keyWordStr = scanner.nextLine();

                System.out.println();
                int msgQuantityKeyWord = msgSearch.showAllMessageKeyWord(keyWordStr, msgHistory);

                pw.print(dateFormat.format(date) + "(" + author + "): ");
                pw.println("Founded " + msgQuantityKeyWord + " messages by key word \"" + keyWordStr + "\";");

                System.out.print("Press any key to continue...");
                scanner.next();
                break;
            case 3:
                System.out.println("----------------");
                System.out.print("Input regular expression: ");

                String rExpression;
                rExpression = scanner.nextLine();

                System.out.println();
                int msgQuantityRExpression = msgSearch.showAllMessagesRegularExpression(rExpression, msgHistory);

                pw.print(dateFormat.format(date) + "(" + author + "): ");
                pw.println("Founded " + msgQuantityRExpression + " messages by regular expression \"" + rExpression + "\";");

                System.out.print("Press any key to continue...");
                scanner.next();
                break;
            case 4:
                System.out.println("----------------");

                String timeFrom;
                do {
                    System.out.print("Input period from (in millisecond): ");
                    timeFrom = scanner.nextLine();
                } while (!isMillisecond(timeFrom));

                String timeTo;
                do {
                    System.out.print("Input period to (in millisecond): ");
                    timeTo = scanner.nextLine();
                } while (!isMillisecond(timeTo));

                System.out.println();
                int msgQuantityPeriod = msgSearch.showAllMessagesPeriod(Long.parseLong(timeFrom), Long.parseLong(timeTo), msgHistory);

                pw.print(dateFormat.format(date) + "(" + author + "): ");
                pw.println("Founded " + msgQuantityPeriod + " messages in period \"" + dateFormat.format(timeFrom) +
                        " - " + dateFormat.format(timeTo) + "\";");

                System.out.print("Press any key to continue...");
                scanner.next();
                break;
            default:
                System.out.println("Incorrect input. Try again!");
                pw.print(dateFormat.format(date) + "(" + author + "): ");
                pw.println("Incorrect input in search menu;");
                searchController(scanner, pw);
                break;
            case 0:
        }
    }

}
