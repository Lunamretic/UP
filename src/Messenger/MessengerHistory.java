package Messenger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MessengerHistory {
    private List<Message> historyList;

    public static class MyComparator implements Comparator<Message> {
        @Override
        public int compare(Message m1, Message m2) {
            return (int) (m1.getTimestamp() - m2.getTimestamp());
        }
    }


    public MessengerHistory() {
        historyList = new LinkedList<>();
    }

    public List<Message> getHistoryList() {
        return this.historyList;
    }

    boolean addMessageToHistory(String id, String message, String author, long timestamp) {
        for (Message msg : historyList) {
            if (id.compareToIgnoreCase(msg.getId()) == 0) {
                System.out.println("Id \"" + id + "\" is already used!");
                return false;
            }
        }
        this.historyList.add(new Message(id, message, author, timestamp));
        Collections.sort(historyList, new MyComparator());
        return true;
    }

    int backupMessages() {
        Gson gson = new GsonBuilder().create();
        File file = new File("history.json");
        try (PrintStream printStream = new PrintStream(file.getAbsoluteFile())) {
            gson.toJson(historyList, printStream);
        } catch (IOException e) {
            System.out.println("Error!");
            return 0;
        }
        return historyList.size();
    }

    int loadMessages() {
        Gson gson = new GsonBuilder().create();

        List<Message> msgList;
        try (BufferedReader br = new BufferedReader(new FileReader("history.json"))) {
            msgList = gson.fromJson(br, new TypeToken<List<Message>>() {
            }.getType());
            historyList.addAll(msgList);
        } catch (IOException e) {
            System.out.println("Error!");
            return 0;
        }
        Collections.sort(historyList, new MyComparator());
        return msgList.size();
    }

    int showHistory() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("(dd:MM - HH:mm:ss)");
        if (historyList.isEmpty()) {
            System.out.println("The history is empty!");
            return 0;
        } else {
            for (Message msg : historyList) {
                System.out.println(msg.getAuthor() + "   " +
                        dateFormat.format(msg.getTimestamp()) + " (" + msg.getId() + ")");
                System.out.println(msg.getMessage());
                System.out.println();
            }
        }
        return historyList.size();
    }

    boolean removeMessageById(String id) {
        for (Message msg : historyList) {
            if (id.compareToIgnoreCase(msg.getId()) == 0) {
                historyList.remove(msg);
                System.out.println("Removed!");
                return true;
            }
        }
        System.out.println("There is no message with id \"" + id + "\"");
        return false;
    }

    int historySize() {
        return historyList.size();
    }
}
