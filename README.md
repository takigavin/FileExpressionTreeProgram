# README
FileExpressionTreeProgram: Builds expression trees from user input or a given file, read line by line. The expression tree is then evaluated and if valid, it is written to a default/given file name in infix expression format with the correct final value.

## AUTHOR
Gavin Horner

gavinhorner0@gmail.com

## USAGE
Once the program is run, you will be prompted to enter where you would like your infix expressions written to? The present working directory or
your choice. If present is chosen, it will continue. Otherwise, you will be asked to input a valid path and then continue.

Next, you will be asked whether you would like to enter expressions from your keyboard or input with a file. If keyboard is chosen, it will continue.
Otherwise, you will be asked to enter a valid file location.

Finally, you will be asked to enter a postfix expression of your choice, if keyboard was chosen. Once entered, if valid, it will return the postfix expression entered, and the value of evaluating the expression tree. Then it will ask if you would like to input another, prompting to answer yes or no. Yes, allowing you to continue and no, ending the program.

If file was chosen, and a valid file location was entered, it will output similarly to below, but for every postfix expression in the file. Writing every infix expression to the Answers.txt.

Examples:
 - present keyboard 8 4 + 

Outputs:
 - Postorder: 8 4 +
 - Final value: 12
 - Would you like to input another postfix expression? Please answer "yes" or "no":

## ABOUT
This program is useful for building an expression tree quickly in the correct way without needing any thinking from the user. The same can also be said about the quick calculation and result of evaluating any given expression tree. In addition, it prints the infix expression of the given tree which makes it easy to understand just how it was evaluated.
It is especially helpful when providing a file of post-fix expressions as it will build and evaluate all of them extremely quickly and efficiently.

Classes consist of:
- FileExpressionTreeProgram

Sidenote: I would definitely split this program into more classes if given the time to, however I am focused more on continuing to learn and hopefully create more valuable projects!
