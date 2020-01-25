import net.datastructures.LinkedBinaryTree;
import net.datastructures.Position;
import net.datastructures.LinkedStack;
import net.datastructures.SinglyLinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
 /* Author: Gavin Horner
            Western Illinois University
            CS351 : Data Structures II

    Program Overview: This program was made during my Data Structures II course. It is used primarily for building and evaluating expression trees. 
    You are asked to insert a post-order expression tree either from your keyboard or a provided file read line by line. It will then
    create the expression tree granted it is valid, or print out what went wrong and where. Next, the expression tree will be evaluated
    printing you the results. Finally, it will write the results of the valid expression trees using infix expression to the given file location.
 */
public class FileExpressionTreeProgram {

    /* This method builds an expression tree from a given string,
       catching any invalid operands or operators that would result
       in an invalid tree. */

    public static LinkedBinaryTree<String> buildTreeFromString(String build) {
        LinkedBinaryTree<String> exprTree = new LinkedBinaryTree<>();
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        StringTokenizer st = new StringTokenizer(build, " ");
        Position<String> p = null;
        String remove = "null";
        while (st.hasMoreTokens()) {
            list.addFirst(st.nextToken());
        }


        while (!list.isEmpty()) { /* Removes from the list from the beginning, determining whether it is an operator
                                  or an operand and thus determining where to insert the given string. */
            remove = list.removeFirst();
            try {
                if (IsOperator(remove)) {
                    if (exprTree.isEmpty()) {
                        p = exprTree.addRoot(remove); // If string is an operator and an empty tree, it will be made the root.
                    } else if (exprTree.numChildren(p) == 2) {
                        for (Position<String> pre : exprTree.preorder()) {
                            if (exprTree.isInternal(pre) && exprTree.numChildren(pre) < 2) { /* If the given position p has two children. A pre-order traversal will start using position "pre"
                                                                                               pre will be checked if it is internal and has less than two children. If so, the program will then
                                                                                               check to see whether the left or right child is missing, inserting the string on confirmation. */
                                if (exprTree.left(pre) == null) {
                                    p = exprTree.addLeft(pre, remove);
                                }
                                if (exprTree.right(pre) == null) {
                                    p = exprTree.addRight(pre, remove);
                                }
                            } else { // If "pre" is not internal and doesn't have less than 2 children, then our loop will break.
                                break;
                            }
                        }

                    } else if (IsOperator(p.getElement())) {
                        while (IsOperator(remove)) {
                            if (exprTree.numChildren(p) == 2) { /* If the element of position p is an operator and has two children,
                                                                  then position p will be set to a new child added left of the root. */
                                p = exprTree.addLeft(exprTree.root(), remove);
                                break;
                            } else {                            /* However, if position p does not have two children, the program will
                                                                   check to determine which child is missing and insert the child there,
                                                                   making it the new position p. */
                                if (exprTree.right(p) == null) {
                                    p = exprTree.addRight(p, remove);
                                    break;
                                } else if (exprTree.left(p) == null) {
                                    p = exprTree.addLeft(p, remove);
                                    break;
                                }
                            }
                            remove = list.removeFirst();
                        }
                    }
                } else { /* If the string is not an operator and the expression tree is empty, then it wouldn't be a valid root, so we exit the loop.
                        otherwise we continue for the operand cases. */
                    if (exprTree.isEmpty()) {
                        System.out.println("Not valid root, exiting.");
                        break;
                    }

                    try {
                        int test = Integer.parseInt(remove); // Converts the given string to an integer for our valid operands.
                        if (exprTree.numChildren(p) < 2) {  /* If position p has less than two children. Check to see which
                                                               child is missing and add it accordingly, position p remaining the same. */
                            if (exprTree.right(p) == null) {
                                exprTree.addRight(p, remove);
                            } else if (exprTree.left(p) == null) {
                                exprTree.addLeft(p, remove);
                            }
                        } else if (exprTree.numChildren(p) == 2) {
                            while (exprTree.numChildren(p) == 2) { /* If position p has two children, enter a while loop.
                                                                      while position p has two children, set p equal to the parent of p.
                                                                      determine whether the left or right child is missing and add them accordingly,
                                                                      otherwise we break from the while loop. */
                                p = exprTree.parent(p);
                                if (exprTree.left(p) == null) {
                                    exprTree.addLeft(p, remove);
                                }
                                if (exprTree.right(p) == null) {
                                    exprTree.addRight(p, remove);
                                } else {
                                    break;
                                }

                            }
                        } else { /* If position p does not have 2 children or less than 2 children.
                                    We break from the loop. */
                            System.out.println("Invalid position for " + remove);
                            break;
                        }


                    } catch (NumberFormatException e) { /* Catches NumberFormatExceptions from trying to convert the given string to
                                                           an integer. Prints that it is not a valid operand, and where exactly the error took place. */
                        System.out.println("Not valid operand found under: " + p.getElement() + ": " + remove + " Exiting.");
                        break;
                    }


                }
            } catch (IllegalArgumentException e) { /* Catches anything that may result in an IllegalArgumentException, as that would mean we have
                                                      too few or too many operators. */
                System.out.println("Too few or too many operators.");
                break;
            }
        }
        System.out.print("Postorder: "); // Finally prints out the expression tree in post-order traversal.
        for (Position<String> post : exprTree.postorder())
            System.out.print(post.getElement() + " ");

        System.out.println();
        System.out.println("Final value: " + evaluateTree(exprTree)); // And evaluates the expression tree granted it is valid.

        return exprTree;

    }

    /* This method is used to check whether the given string is an operator or not
       using a number of switch cases to determine it. */

    public static boolean IsOperator(String s) {
        switch (s) {
            case "+":
            case "/":
            case "-":
            case "*":
                return true;
            default:
                return false;
        }
    }

    /* buildTreeFromFile method takes no arguments builds an expression tree from a given file. I used a while loop to read the file line by line
       calling on my buildTreeFromString method in order to build the trees with each line that was read from the file. */

    public static SinglyLinkedList<LinkedBinaryTree<String>> buildTreeFromFile() {
        SinglyLinkedList<LinkedBinaryTree<String>> fileTree = new SinglyLinkedList<>();
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter a file name location: ");
        String dir = scan.next();
        File inputFile = new File(dir);
        Scanner fileScan = null;
        try {
            fileScan = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            System.out.println("No file found at " + dir + " exiting.");
            System.exit(1);
        }
        while (fileScan.hasNextLine()) {
            String line = fileScan.nextLine();
            System.out.println("______________");
            System.out.println(line);
            System.out.println("--------------");
            LinkedBinaryTree<String> tree = buildTreeFromString(line);
            fileTree.addLast(tree); // All of the valid expression trees are added to a SinglyLinkedList and then returned
        }
        fileScan.close();
        return fileTree;
    }

    /* This method was provided by our professor, it works with the below method to build a string recursively from a given LinkedBinaryTree. This
    is used in order to write the proper evaluation of our valid expression trees to the file. Ex. ((3/8)+3)
     */
    public static String inFixSubTree(LinkedBinaryTree<String> t, Position<String> p) {
        if (t.numChildren(p) == 2) {
            StringBuilder sb = new StringBuilder("(");
            sb.append(inFixSubTree(t, t.left(p)));
            sb.append(p.getElement());
            sb.append(inFixSubTree(t, t.right(p)));
            sb.append(")");
            return sb.toString();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(p.getElement());
            return sb.toString();
        }
    }

    /* Also provided by our professor, works with the above method in order to create the inorder expression of the provided expression tree */

    public static String inFixFromExpressionTree(LinkedBinaryTree<String> t) {
        return inFixSubTree(t, t.root());
    }

    /* This method evaluates the given tree returning an integer value, careful to catch any possible errors, stopping the evaluation process
       and pinpointing what went wrong and where.
     */
    public static int evaluateTree(LinkedBinaryTree<String> eval) {
        LinkedStack<Integer> stack = new LinkedStack<>(); // Creating a stack for holding all of the valid integer values.
        for (Position<String> post : eval.postorder()) {
            if (eval.isExternal(post)) {  /* Loops through the tree in a postorder traversal, checking to see if the position "post" is external,
                                             if it is then it will try to push the position's element as an integer. If it isn't an integer but is external
                                             then that is an error (as expression trees can't have operators as external nodes) and it is caught. */
                try {
                    stack.push(Integer.parseInt(post.getElement()));
                } catch (NumberFormatException e) {
                    System.out.println("Non-integer operand found: " + post.getElement());
                    return Integer.MIN_VALUE; // Part of the assignment requirements were to return Integer.MIN_VALUE for any of the errors that may appear.
                }

            } else {
                if (eval.numChildren(post) == 2) { /* If the position isn't external and the position has two children, the top two values on the stack are
                                                      popped off and a switch statement is started to perform the proper operation (given by the position "post"
                                                      on the two popped integers. */
                    int op2 = stack.pop();
                    int op1 = stack.pop();
                    switch (post.getElement()) {
                        case "+":
                            stack.push(op1 + op2);
                            break;
                        case "/":
                            if (op2 == 0) {
                                System.out.println("Can't divide by 0, exiting.");
                                return Integer.MIN_VALUE;
                            }

                            stack.push(op1 / op2);
                            break;
                        case "*":
                            stack.push(op1 * op2);
                            break;
                        case "-":
                            stack.push(op1 - op2);
                            break;
                        default:
                            System.out.println("Invalid Operator: " + post.getElement()); // If the element isn't one of the four valid operators, then it is invalid.
                            System.out.print("Contents of the Stack upon Error encounter: ");
                            while (!stack.isEmpty()) {
                                System.out.print(stack.pop() + " ");
                            }
                            System.out.println();
                            return Integer.MIN_VALUE;
                    }
                } else { /* If the position isn't external and it doesn't have two children, then something is wrong with our expression tree and
                            it is determined where the operand is missing and under which operator. */
                    System.out.println("Missing operand in: " + post.getElement());
                    if (eval.left(post) == null)
                        System.out.println("Left child missing.");
                    if (eval.right(post) == null)
                        System.out.println("Right child missing.");
                    System.out.print("Contents of the Stack upon Error encounter: ");
                    while (!stack.isEmpty()) {
                        System.out.print(stack.pop() + " ");
                    }
                    System.out.println();
                    return Integer.MIN_VALUE;
                }
            }
        }
        if (stack.isEmpty()) { // If our stack is empty after all of the above, then it is empty, thus returns an error.
            System.out.println("Empty Tree, exiting.");
            return Integer.MIN_VALUE;
        }
        return stack.top(); // The final value will be the only one left on the stack
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String choice = "null";
        String outPut = "invalid location";

        System.out.println("Would you like your Answers.txt file placed in the present working directory or in your choice of location? Please answer \"present\" or \"choice\":");
        while (!choice.equalsIgnoreCase("present") || !choice.equalsIgnoreCase("choice")) { // Loops until "present" or "choice" is entered, otherwise it will print out "Invalid entry, please try again."
            choice = scan.next();
            if (choice.equalsIgnoreCase("present")) {
                outPut = "./Answers.txt";
                break;
            } else if (choice.equalsIgnoreCase("choice")) {
                System.out.println("Please enter where you would like to place your evaluated trees file (Including the file name you would like).");
                outPut = scan.next();
                break;
            } else {
                System.out.println("Invalid entry, please try again.");
            }
        }

        File outputFile = new File(outPut);

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(outPut);
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't create file at that location.");
            System.exit(1);
        } catch (SecurityException e) {
            System.out.println("Do not have permission to write to that location.");
            System.exit(1);
        }
        String input = "null";
        String response = "null";
        System.out.println("Would you like to enter expressions from your keyboard, or from a file? Please answer \"keyboard\" or \"file\":");
        while (!input.equalsIgnoreCase("keyboard") || !input.equalsIgnoreCase("file")) { // Continues to loop while input is not "keyboard" or "file"
            if(response!="yes")
                input = scan.nextLine();

            if (input.equalsIgnoreCase("keyboard")) {
                while (!response.equalsIgnoreCase("no")) { // Continues to allow more postfix expression inputs as long as the response to wanting to continue is not a "no".
                    System.out.println("Enter a postfix expression: ");
                    String post = scan.nextLine();
                    LinkedBinaryTree<String> kTree = buildTreeFromString(post); // Builds tree from given string from user input
                    try {
                        writer.print(inFixFromExpressionTree(kTree));
                        writer.print("= " + evaluateTree(kTree)); // Writes the infix expression and evaluation of the tree to the specified file.
                        writer.println();
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid tree.");
                    }
                    System.out.println("Would you like to input another postfix expression? Please answer \"yes\" or \"no\":");
                    response = scan.nextLine();
                    if (response.equalsIgnoreCase("yes")) {
                        continue;
                    } else if (response.equalsIgnoreCase("no")) {
                        break;
                    } else {
                        System.out.println("Invalid entry, continuing...");
                    }
                }
                break;


            } else if (input.equalsIgnoreCase("file")) { // If the user wants to use a file, it will call the necessary methods, building the tree from the file and writing it to the specified destination.
                SinglyLinkedList<LinkedBinaryTree<String>> fTree = buildTreeFromFile();
                while (!fTree.isEmpty()) {
                    LinkedBinaryTree<String> mTree = fTree.removeFirst();
                    if (evaluateTree(mTree) == Integer.MIN_VALUE)
                        continue;
                    else {
                        writer.print(inFixFromExpressionTree(mTree));
                        writer.print("= " + evaluateTree(mTree));
                        writer.println();
                    }

                }
                break;

            }

        }
        writer.close();
    }
}
