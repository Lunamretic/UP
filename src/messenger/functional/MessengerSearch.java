package messenger.functional;

import messenger.Message;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessengerSearch {
    int showAllMessagesAuthor(String author, MessengerHistory messengerHistory) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        int msgQuantityAuthor = 0;

        for (Message msg : messengerHistory.getHistoryList()) {
            if (author.compareToIgnoreCase(msg.getAuthor()) == 0) {
                msgQuantityAuthor++;
                System.out.println(msg.getAuthor() + "  " + dateFormat.format(msg.getTimestamp()));
                System.out.println(msg.getMessage());
                System.out.println();
            }
        }

        if (msgQuantityAuthor != 0) {
            return msgQuantityAuthor;
        } else {
            System.out.println("Author dosn't exist!");
            return 0;
        }
    }

    int showAllMessageKeyWord(String keyWord, MessengerHistory messengerHistory) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        int msgQuantityKeyWord = 0;

        for (Message msg : messengerHistory.getHistoryList()) {
            if (msg.getMessage().contains(keyWord)) {
                msgQuantityKeyWord++;
                System.out.println(msg.getAuthor() + "  " + dateFormat.format(msg.getTimestamp()));
                System.out.println(msg.getMessage());
                System.out.println();
            }
        }

        if (msgQuantityKeyWord != 0) {
            return msgQuantityKeyWord;
        } else {
            System.out.println("Key word dosn't exist in message history!");
            return 0;
        }
    }

    public boolean isRegularExpressionExist(String rExspression, String str) {
        Pattern p = Pattern.compile(".*" + rExspression + ".*");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    int showAllMessagesRegularExpression(String rExpression, MessengerHistory messengerHistory) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        int msgQuantityRExpression = 0;

        for (Message msg : messengerHistory.getHistoryList()) {
            if (isRegularExpressionExist(rExpression, msg.getMessage())) {
                msgQuantityRExpression++;
                System.out.println(msg.getAuthor() + "  " + dateFormat.format(msg.getTimestamp()));
                System.out.println(msg.getMessage());
                System.out.println();
            }
        }

        if (msgQuantityRExpression != 0) {
            return msgQuantityRExpression;
        } else {
            System.out.println("There is no such regular expression in message history!");
            return 0;
        }
    }

    int showAllMessagesPeriod(long timeFrom, long timeTo, MessengerHistory messengerHistory) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        int msgQuantityPeriod = 0;

        for (Message msg : messengerHistory.getHistoryList()) {
            if (msg.getTimestamp() >= timeFrom && msg.getTimestamp() <= timeTo) {
                msgQuantityPeriod++;
                System.out.println(msg.getAuthor() + "  " + dateFormat.format(msg.getTimestamp()));
                System.out.println(msg.getMessage());
                System.out.println();
            }
        }

        if (msgQuantityPeriod != 0) {
            return msgQuantityPeriod;
        } else {
            System.out.println("There are no messages in this period!");
            return 0;
        }
    }

}
