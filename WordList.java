# HangmanComplete
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class WordList {
    private ArrayList<String> words = new ArrayList();

    public WordList(String wordlistFileName) {
        try {
            Scanner in = new Scanner(new File(wordlistFileName));  
            while (in.hasNextLine()) {
                String line = in.nextLine().trim();
                if (line.length() <= 0) continue;
                this.words.add(line);
            }
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Can't read list of words from " + wordlistFileName);
        }
    }

    public int getWordCount() {
        return this.words.size();
    }

    public String getWord(int index) {
        return this.words.get(index);
    }

    public String removeWord(int index) {
        return this.words.remove(index);
    }
}
