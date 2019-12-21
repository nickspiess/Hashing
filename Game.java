import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game{

    /**
     * File name: Game.java
     * @author SPIESSNA
     * description: This program is a game that will read in a dictionary of text files, will compile them into a
     * HashTable and then will allow you to input two words and how many hops you want them to try and get them by,
     * where the hop is how many permutations and depth it took to get to the word
     * Version: 4
     * @since 12/09/19
     */

    // Create our HashChain
    private static HashChain<String, String> hash;
    private static HashChain<String, String> words;
    // Create our queue
    private static QueueList<TNode> queue;
    // Create our ArrayList
    private static ArrayList<String> permutations;

    /**
     * Our main method to start inputting the text file and to create the HashTables and add the words
     * @param args - Any command line arguments
     * @throws - FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        // Create our HashChain
        hash = new HashChain<String, String>(79765);
        words = new HashChain<String, String>(17);
        // Create our queue
        queue = new QueueList<TNode>();
        // Instantiate our words and hops needed
        String word1 = "";
        String word2 = "";
        String stringHops;
        int numOfHops;
        // If valid number of arguments are presented
        if (args.length == 3) {
            // Grab words
            word1 = args[0];
            word2 = args[1];
            // Grab hop count and turn into integer
            stringHops = args[2];
            numOfHops = Integer.parseInt(stringHops);
            // If the length of the words are equal
            if (args[0].length() == args[1].length()) {
                // Pass the words and file name to our linePass Function
                linePass("dictionary.txt", hash, word1, word2);
                // Take our words and begin our permutation/hop checking
                checkHops(word1, word2, numOfHops);
            }
            // Inform user of non-same length, end program
            else {
                System.out.println("Words are not the same length.");
                System.out.print("There is no solution.");
                System.out.println();
                System.exit(0);
            }
        }
        // If invalid number of arguments, inform user and exit program
        else {
            System.out.println("Invalid amount of arguments.. Exiting.");
            System.out.println();
            System.exit(0);
        }
    }

    /**
     * A method that will pass each line to our fileReader to process and add the data.  Will call method to find
     * the heights of each tree at the end.
     * @param file - The file we are reading from
     * @throws - FileNotFoundException
     */
    public static void linePass(String file, HashChain hash, String word1, String word2) throws FileNotFoundException {

        // Check for valid words
        boolean wordCheck1 = false;
        boolean wordCheck2 = false;
        String line;
        // Create scanner to read through file
        Scanner inputFile = new Scanner(new File(file));
        while (inputFile.hasNextLine()) {// While file contains other lines
            while (inputFile.hasNext()) { // While there are still words
                line = inputFile.nextLine(); // Grab line
                // Checking to see if word is being added to dictionary at all
                if (line.equalsIgnoreCase(word1)) {
                    wordCheck1 = true;
                }
                // Checking to see if word2 is being added to dictionary at all
                if (line.equalsIgnoreCase(word2)) {
                    wordCheck2 = true;
                }
                // Pass the line to our file reader
                fileReader(line, hash); // Pass line to be read
            }
        }
        // If we did not trigger our words being added, inform user and exit program
        if (!wordCheck1 || !wordCheck2) {
            System.out.println("One of your words are not in the dictionary.");
            System.out.print("There is no solution.");
            System.out.println();
            System.exit(0);
        }
    }


    /**
     * This will take a line from a file and input it into our HashTable
     * @param line the line from the file
     * @param hash the HashChain we are inputting it into
     */
    public static void fileReader(String line, HashChain hash) {
        // Read the line we pass
        Scanner scan = new Scanner(line);
        // Our word is the only value in the line
        String word = scan.next();
        // Create a new HashNode and insert it into the HashTable🛸
        hash.insert(new HashNode(word, word));
    }

    /**
     * This method will return an ArrayList of strings that contain the level of permutated words
     * @param word The word we are permutating
     * @return The ArrayList of words
     */
    private static ArrayList<String> getPermutations(String word) {
        permutations = new ArrayList<>();
        char[] wordChar = word.toCharArray();

        // Iterate through each letter and see if it is a valid word
        for (int i = 0; i < wordChar.length; i++) {
            for (char ch = 'a'; ch <= 'z'; ch++) {
                wordChar[i] = ch;
                String addedWord = new String(wordChar);
                if (isWord(addedWord) && !(secondHash(addedWord))) {
                    permutations.add(addedWord);
                }
            }
            wordChar = word.toCharArray();
        }
        return permutations;
    }


    /**
     * This method will check for the hops by doing a breadth-first search based off of the words the user inputs
     * @param word1 The starting word
     * @param word2 The word we want to get to
     * @param numOfHops The number of hops the user wants to beat
     */
    public static void checkHops(String word1, String word2, int numOfHops) {
        words.insert(new HashNode<>(word1, word1));
        // Create the parent
        TNode parent = new TNode(word1, null, 0);
        queue.enqueue(parent);


        // This is the breadth first search
        while (!queue.isEmpty()) {
            ArrayList<String> knownWords = getPermutations(parent.getItem());
            for (int i = 0; i <= knownWords.size() - 1; i++) {
                queue.enqueue(new TNode(knownWords.get(i), parent, parent.getDepth() + 1));
            }
            //System.out.println("while");
            // Begin the dequeue
            TNode variationOfWord = queue.dequeue();

            if (variationOfWord.getItem().equalsIgnoreCase(word2)) {
                ArrayList<String> s = new ArrayList<>();
                System.out.println("Can make it in " + variationOfWord.getDepth() + " hops.");
                // While loop to create a string of all of our words
                while (variationOfWord != null) {
                    s.add(variationOfWord.getItem());
                    variationOfWord = variationOfWord.getParent();
                }
                for (int i = s.size() -1; i > -1; i--) {
                    System.out.print(s.get(i) + "   ");
                }
                System.out.println();
                System.exit(0);
            }
            // We have exceeded the hops.  Inform user
            else {
                if (variationOfWord.getDepth() > numOfHops) {
                    System.out.println("Solution may be beyond given depth.");
                    System.out.println("There is no solution.");
                    System.exit(0);
                }
                words.insert(new HashNode<>(variationOfWord.getItem(), variationOfWord.getItem()));
                parent = variationOfWord;
            }
        }
    }


    /**
     * This will check if our HashChain contains the word
     * @param word The word we are checking
     * @return True if it is contained, false if it is not
     */
    public static boolean isWord(String word) {
        return hash.search(word, word) != null;
    }

    /**
     * This will check if our HashChain of known words contains the word
     * @param word The word we are checking
     * @return True if it is contained, false if it is not
     */
    public static boolean secondHash(String word) {
        return words.search(word, word) != null;
    }


}
