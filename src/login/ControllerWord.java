package login;

import com.opencsv.CSVReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import java.io.Writer;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ControllerWord implements Initializable {

    @FXML
    private TextField inputText;
    @FXML
    private TextField inputText2;
    @FXML
    private TextField inputText3;
    @FXML
    private TextArea inputArea;
    @FXML
    private TextArea inputArea2;
    @FXML
    public TextField usernameTextField;
    @FXML
    public TextField passwordTextField;
    @FXML
    public TextField errorField;
    @FXML
    public TextField CSV;
    @FXML
    private PasswordField hiddenPasswordTextField;
    @FXML
    private CheckBox showPassword;
    @FXML
    private ListView listB;
    @FXML
    private ListView listB2;
    @FXML
    private ListView listFile;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button searchBtn;
    @FXML
    private Button readBtn;
    @FXML
    private Button writeBtn;
    @FXML
    private Button backBtn;
    @FXML
    private Button clearBtn;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private AnchorPane home;

    @FXML
    private AnchorPane word;

    @FXML
    private void homeShow() {
        home.setVisible(true);
        word.setVisible(false);
    }

    @FXML
    private void wordShow() {
        home.setVisible(false);
        word.setVisible(true);
    }

    //Creating object of AVLTree 
    AVLTree tree = new AVLTree();

    //stored data in csv (username, password, and commonplace words)
    static String csv;
    static File file = new File("/Users/zyoco/Java/NetBeansProjects/JavaFX_words/csv/" + csv);
//    static final String DATA = "csv//words.csv";

    private int selectedIndex = -1;

    //Map containing <Word, Meaning>
    TreeMap<String, String> wordInfo = new TreeMap<>();

    //Tooltips for add, delete, and search
    Tooltip tooltip1 = new Tooltip("Add wordss");
    Tooltip tooltip2 = new Tooltip("Delete word");
    Tooltip tooltip3 = new Tooltip("Search words");
    Tooltip tooltip4 = new Tooltip("Back to the previous list");
    Tooltip tooltip5 = new Tooltip("Clear the inputText box");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File file, String str) {

                if (str.indexOf("csv") != -1) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        File[] list = new File("/Users/zyoco/Java/NetBeansProjects/JavaFX_words/csv").listFiles(filter);

        if (list != null) {

            for (int i = 0; i < list.length; i++) {

                if (list[i].isFile()) {
                    listFile.getItems().add(list[i].getName());
                } else if (list[i].isDirectory()) {
                    listFile.getItems().add(list[i].getName());
                }
            }
        } else {
            System.out.println("null");
        }

    }

    //Set variables for Nodes
    class Node {

        Node left;
        Node right;
        String data;
        int height;

        public Node() {
            left = null;
            right = null;
            height = 0;
        }

        public Node(String v) {
            left = null;
            right = null;
            data = v;
            height = 0;
        }

    }

    @FXML
    private String getCSVName() {
        String csv = CSV.getText() + ".csv";
        return csv;
    }

    @FXML
    public void recursiveOpen() throws IOException {
        listFile.getItems().clear();
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File file, String str) {

                if (str.indexOf("csv") != -1) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        File[] list = new File("/Users/zyoco/Java/NetBeansProjects/JavaFX_words/csv").listFiles(filter);

        if (list != null) {

            for (int i = 0; i < list.length; i++) {

                if (list[i].isFile()) {
                    listFile.getItems().add(list[i].getName());
                } else if (list[i].isDirectory()) {
                    listFile.getItems().add(list[i].getName());
                }
            }
        } else {
            System.out.println("null");
        }
    }

    @FXML
    public void clearAllFile() throws IOException {
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File file, String str) {

                if (str.indexOf("csv") != -1) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        File[] list = new File("/Users/zyoco/Java/NetBeansProjects/JavaFX_words/csv").listFiles(filter);

        if (list != null) {

            for (int i = 0; i < list.length; i++) {

                if (list[i].isFile()) {
                    list[i].delete();
                } else if (list[i].isDirectory()) {
                    list[i].delete();
                }
            }
        } else {
            System.out.println("null");
        }
    }

    @FXML
    public void openFile() throws IOException {

    }

    @FXML
    public void createFile() throws IOException {
        if (!CSV.getText().isEmpty()) {
            csv = CSV.getText() + ".csv";
            file = new File("/Users/zyoco/Java/NetBeansProjects/JavaFX_words/csv/" + csv);

            if (file.createNewFile()) {
                listFile.getItems().sorted();
                System.out.println("succeeded");
            } else {
                System.out.println("failed");
            }
            CSV.clear();

//        List sortedListFile = Arrays.asList(file.list());
//        Collections.sort(sortedListFile);
            recursiveOpen();
        } else {
            System.out.println("failed");
        }
    }

    @FXML
    public void deleteFile() throws IOException {
        csv = CSV.getText() + ".csv";
        File file = new File("/Users/zyoco/Java/NetBeansProjects/JavaFX_words/csv/" + csv);

        if (file.exists()) {
            if (file.delete()) {
                System.out.println("succeeded");
            } else {
                System.out.println("failed");
            }
        } else {
            System.out.println("cant find a file");
        }
        CSV.clear();
        recursiveOpen();
    }

    //Get all words information to store them in words.csv
    @FXML
    private void read() throws IOException, CsvValidationException {
        String csvRead = getCSVName();
        file = new File("/Users/zyoco/Java/NetBeansProjects/JavaFX_words/csv/" + csvRead);
        Scanner scanner = new Scanner(file);
        wordInfo.clear();
        wordInfo = new TreeMap<>();
        while (scanner.hasNext()) {
            String[] wordAndMeaning = scanner.nextLine().split(",");
            wordInfo.put(wordAndMeaning[0], wordAndMeaning[1]);
        }
        for (String str : wordInfo.keySet()) {
            listB.getItems().add(str);
            listB2.getItems().add(wordInfo.get(str));
        }
        listB2.getItems().clear();
    }

    //Get all words information to store them in words.csv
    @FXML
    private void write() throws IOException {

        String csvWrite = getCSVName();
        Writer writer = Files.newBufferedWriter(Paths.get("csv//" + csvWrite));
        CSVWriter csvWriter = new CSVWriter(writer,
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);

        for (String str : wordInfo.keySet()) {
            writer.write(str + "," + wordInfo.get(str) + "\n");
        }
        writer.close();

//        String[] words = tree.Display().split(",");
//        csvWriter.writeNext(words);
//        writer.close();
    }

    //
//    //Get all words information to store them in words.csv
//    @FXML
//    private void write() throws IOException {
//        Writer writer = Files.newBufferedWriter(Paths.get(DATA));
//        CSVWriter csvWriter = new CSVWriter(writer,
//                CSVWriter.DEFAULT_SEPARATOR,
//                CSVWriter.NO_QUOTE_CHARACTER,
//                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
//                CSVWriter.DEFAULT_LINE_END);
//
//        for (String str : wordInfo.keySet()) {
//            writer.write(str + "," + wordInfo.get(str) + "\n");
//        }
//        String[] words = wordInfo.get(str).split(",");
//        csvWriter.writeNext(words);
//        writer.close();
//
////        String[] words = tree.Display().split(",");
////        csvWriter.writeNext(words);
////        writer.close();
//    }
    //When you select items in listB, that words will be displayed in inputText box
    @FXML
    private void setOnMouseClicked1() {

        listB2.getItems().clear();
        String selectedItem = listB.getSelectionModel().getSelectedItem().toString();
        selectedIndex = listB.getSelectionModel().getSelectedIndex();
        inputText.setText(selectedItem);

//                    String[] usernameAndPassword = scanner.nextLine().split(",");
//            loginInfo.put(usernameAndPassword[0], usernameAndPassword[1]);
//            
        inputText2.setText(wordInfo.get(selectedItem));
        listB2.getItems().add(wordInfo.get(selectedItem));

    }

    public static <T, E> T getKeyByValue(TreeMap<T, E> map, E value) {
        for (Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    //When you select items in listB, that words will be displayed in inputText box
    @FXML
    private void setOnMouseClicked2() {
        String selectedItem = listB2.getSelectionModel().getSelectedItem().toString();
        selectedIndex = listB2.getSelectionModel().getSelectedIndex();
        inputText2.setText(selectedItem);
//        inputText.setText(wordInfo.get(selectedItem2));
        inputText.setText(getKeyByValue(wordInfo, selectedItem));
    }

    //When you select items in listB, that words will be displayed in inputText box
    @FXML
    private void setOnMouseClicked3() {
        String selectedItem = listFile.getSelectionModel().getSelectedItem().toString().replace(".csv", "");
        selectedIndex = listFile.getSelectionModel().getSelectedIndex();
        CSV.setText(selectedItem);
    }

    //When you select items in listB, that words will be displayed in inputText box
    @FXML
    private void pressBold() {
        inputText3.setFont(Font.font(null, FontWeight.BOLD, 36));
    }

    //OnAction for adding words
    @FXML
    private void add() throws IOException {

        if (!"".equals(inputArea.getText())) {
            String real = inputArea.getText().concat("\n\n" + "挾間");
//
//            if ("(".equals(real) || ")".equals(real)) {
//                real = real.replaceAll("(" + ")", "-");
//            }
            String[] area = real.split("\n");
            String newArea = real.replaceAll(area[0] + "\n", "");
            while (!"挾間".equals(area[0])) {
                String[] area2 = newArea.split("\n\n");

                listB.getItems().clear();
                listB2.getItems().clear();
                wordInfo.put(area[0], area2[0]);

                String newArea2 = newArea.replaceAll(area2[0] + "\n\n", "");
                area = newArea2.split("\n");
                newArea = newArea2.replaceAll(area[0] + "\n", "");
            }
            for (String str : wordInfo.keySet()) {
                listB.getItems().add(str);
//                inputArea2.setText(str);
                listB2.getItems().add(wordInfo.get(str));
            }

//            String[] area = inputArea.getText().split("\n");
//            String newArea = inputArea.getText().replaceAll(area[0] + "\n", "");
////            String[] area2 = newArea.split("\n");
//            String[] area2 = newArea.split("\n" + "\n");
//            //////////////Area[0]の特は？？
////            StringBuilder sb = null;
////            String a = null;
////            for (int i = 0; i < area2.length; i++) {
////                sb = new StringBuilder(area2[i]);
////                a = sb.append(area2[i + 1]).toString();
////            }
//
//            listB.getItems().clear();
//            listB2.getItems().clear();
//            wordInfo.put(area[0], area2[0]);
//
//            for (String str : wordInfo.keySet()) {
//                listB.getItems().add(str);
//                System.out.print(wordInfo.get(str));
////                String[] newlistB = wordInfo.get(str).split("。");
////                listB2.getItems().add(wordInfo.get(str).split("。"));
//            }
        } else {

            if ("".equals(inputText.getText()) || "".equals(inputText2.getText())) {
                addBtn.setTooltip(tooltip1);

            } else {
                listB.getItems().clear();
                listB2.getItems().clear();
                wordInfo.put(inputText.getText(), inputText2.getText());
                for (String str : wordInfo.keySet()) {
                    listB.getItems().add(str);
                    listB2.getItems().add(wordInfo.get(str));
                }
            }
        }
        inputText.clear();
        inputText2.clear();
        inputArea.clear();
    }

    //OnAction for deleting words
    @FXML
    private void delete() throws IOException {

        String selectedItem = listB.getSelectionModel().getSelectedItem().toString();
        selectedIndex = listB.getSelectionModel().getSelectedIndex();
        inputText.setText(selectedItem);
        wordInfo.remove(selectedItem);

        listB.getItems().clear();
        listB2.getItems().clear();
        for (String str : wordInfo.keySet()) {
            listB.getItems().add(str);
            listB2.getItems().add(wordInfo.get(str) + "\n");
        }
        inputText.clear();
        inputText2.clear();

        deleteBtn.setTooltip(tooltip2);
    }

    //OnAction for searching words
    @FXML
    private void search() {
        tree.search(inputText.getText());
        searchBtn.setTooltip(tooltip3);
    }

    //OnAction back to the previous 
    @FXML
    private void back() throws IOException {
        listB.getItems().clear();
        listB2.getItems().clear();
//        tree.display();
        inputText.clear();
        inputText2.clear();
        backBtn.setTooltip(tooltip4);
                backBtn.getScene().getWindow().hide();
                    Parent root
                            = FXMLLoader.load(getClass().getResource("CommonplaceBook.fxml"));
                    Stage secondStage = new Stage();
                    secondStage.setTitle("My Word Book");
                    Scene scene = new Scene(root);
                    secondStage.setScene(scene);
                    secondStage.show();
    }

    //OnAction for clearing inputText
    @FXML
    private void clear() throws IOException {
        inputText.clear();
        inputText2.clear();
        backBtn.setTooltip(tooltip5);
    }

    //Class AVLTree
    class AVLTree {

        Node root;

        //Constructor 
        public AVLTree() {
            root = null;
        }

        //Function to get height of node 
        private int height(Node t) {
            return t == null ? -1 : t.height;
        }

        //Function to max of left/right node 
        private int max(int lHeight, int rHeight) {
            return lHeight > rHeight ? lHeight : rHeight;
        }

        private String findSmallestValue(Node root) {
            return root.left == null ? root.data : findSmallestValue(root.left);
        }

        //Rotate binary tree node with right child 
        private Node rightRotate(Node n1) {
            Node n2 = n1.right;
            n1.right = n2.left;
            n2.left = n1;
            n1.height = max(height(n1.left), height(n1.right)) + 1;
            n2.height = max(height(n2.right), n1.height) + 1;
            return n2;
        }

        //Rotate binary tree node with left child 
        private Node leftRotate(Node n2) {
            Node n1 = n2.left;
            n2.left = n1.right;
            n1.right = n2;
            n2.height = max(height(n2.left), height(n2.right)) + 1;
            n1.height = max(height(n1.left), n2.height) + 1;
            return n1;
        }

        /**
         * Double rotate binary tree node: first left child with its right
         * child; then node n3 with new left child
         */
        private Node doubleWithLeftChild(Node n3) {
            n3.left = rightRotate(n3.left);
            return leftRotate(n3);
        }

        /**
         * Double rotate binary tree node: first right child with its left
         * child; then node n1 with new right child
         */
        private Node doubleWithRightChild(Node n4) {
            n4.right = leftRotate(n4.right);
            return rightRotate(n4);
        }

        //AVL tree is a self-balancing Binary Search Tree (BST) 
        //where the difference between heights of left and right subtrees cannot be more than one for all nodes.
        //insertion (Insert words)
        public void insert(String data) {
            root = insert(data, root);
        }

        private Node insert(String newValue, Node n) {
            if (n == null) {
                n = new Node(newValue);

            } else if (newValue.compareTo(n.data) < 0) {
                n.left = insert(newValue, n.left);
                if (height(n.left) - height(n.right) == 2) {
                    if (newValue.compareTo(n.left.data) < 0) {
                        n = leftRotate(n);
                    } else {
                        n = doubleWithLeftChild(n);
                    }
                }
            } else if (newValue.compareTo(n.data) > 0) {
                n.right = insert(newValue, n.right);
                if (height(n.right) - height(n.left) == 2) {
                    if (newValue.compareTo(n.right.data) > 0) {
                        n = rightRotate(n);
                    } else {
                        n = doubleWithRightChild(n);
                    }
                }
            } else
           ;  // Duplicate; do nothing
            n.height = max(height(n.left), height(n.right)) + 1;
            return n;
        }

        //AVL tree is a self-balancing Binary Search Tree (BST) 
        //where the difference between heights of left and right subtrees cannot be more than one for all nodes.
        //Delete words
        public void delete(String data) {
            root = delete(data, root);
        }

        private Node delete(String value, Node dn) {
            if (dn == null) {

                return null;
            }
            if (value == null ? dn.data == null : value.equals(dn.data)) {
                // Case 1: no children
                if (dn.left == null && dn.right == null) {
                    return null;
                }
                // Case 2: only 1 child
                if (dn.right == null) {
                    return dn.left;
                }
                if (dn.left == null) {
                    return dn.right;
                }
                // Case 3: 2 children
                String smallestValue = findSmallestValue(dn.right);
                dn.data = smallestValue;
                dn.right = delete(smallestValue, dn.right);
                return dn;
            }
            if (value.compareTo(dn.data) < 0) {
                dn.left = delete(value, dn.left);
                return dn;
            } else {
                dn.right = delete(value, dn.right);
                return dn;

            }
        }

        //AVL tree is a self-balancing Binary Search Tree (BST) 
        //where the difference between heights of left and right subtrees cannot be more than one for all nodes.
        //Binary search
        public boolean search(String val) {
            return search(root, val);
        }

        private boolean search(Node sn, String val) {
            boolean found = false;
            while ((sn != null) && !found) {
                String rval = sn.data;
                if (val.compareTo(rval) < 0) {
                    sn = sn.left;
                } else if (val.compareTo(rval) > 0) {
                    sn = sn.right;
                } else {
                    found = true;
                    listB.getItems().clear();
                    listB.getItems().add(val);
                    break;
                }
                found = search(sn, val);
            }
            if (!found) {
                listB.getItems().clear();
                listB.getItems().add("Not Found");
            }
            return found;
        }

        //// A custom method to display all stuff in an ascending order. Each stuff is splited with ",".
        private String Display() {
            if (root == null) {
                inputText.setText("");
            }
            return DisplayInOrder(root);
        }

        // A custom method to display all stuff in an ascending order. Each stuff is splited with ",".
        private String DisplayInOrder(Node current) {
            String s = "";
            if (current != null) {
                s = DisplayInOrder(current.left);
                s += current.data + ",";
                s += DisplayInOrder(current.right);
            }
            return s;
        }

        //// A custom method to display all stuff in an ascending order. Each stuff is splited with ",".
        private void display() throws IOException {
            String[] words = tree.Display().split(",");
            for (String s : words) {
                listB.getItems().add(s);
            }
        }
    }
}
