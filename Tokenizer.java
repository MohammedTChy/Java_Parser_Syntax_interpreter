//Mohammed Tahmid Chowdhury moch8386
package prop.assignment0;

import java.io.IOException;

public class Tokenizer implements ITokenizer {

    private Scanner scanner = null; 
    private Lexeme lexemeCurrent = null; 


    @Override
    public void open(String fileName) throws IOException, TokenizerException {
        scanner = new Scanner();
        scanner.open(fileName);


    }


    @Override
    public Lexeme current() {
        return lexemeCurrent;
    }

 
    @Override
    public void moveNext() throws IOException, TokenizerException {
        if (scanner == null) {
            throw new IOException("Ingen fil");
        }

        boolean whiteSpace = false, digit = false, alphabet = false, symbol = false, eof = false; 
        char ch;
        scanner.moveNext();
        ch = scanner.current();
        if (ch == Scanner.EOF) { 
            eof = true;
            lexemeCurrent = new Lexeme(ch, Token.EOF);
        } else if (Character.isWhitespace(ch)) {
            whiteSpace = true;
            moveNext();
        } else if (Character.isDigit(ch)) {
            digit = true;
            double number = Double.parseDouble(String.valueOf(ch));
            lexemeCurrent = new Lexeme(number, Token.INT_LIT);
        } else if (String.valueOf(ch).matches("[a-z]")) { 
            alphabet = true;
            lexemeCurrent = new Lexeme(String.valueOf(ch), Token.IDENT);
        } else {
            switch (ch) {
                case '+':
                    lexemeCurrent = new Lexeme(String.valueOf(ch), Token.ADD_OP);
                    symbol = true;
                    break;
                case '-':
                    lexemeCurrent = new Lexeme(String.valueOf(ch), Token.SUB_OP);
                    symbol = true;
                    break;
                case '*':
                    lexemeCurrent = new Lexeme(String.valueOf(ch), Token.MULT_OP);
                    symbol = true;
                    break;
                case '/':
                    lexemeCurrent = new Lexeme(String.valueOf(ch), Token.DIV_OP);
                    symbol = true;
                    break;
                case '(':
                    lexemeCurrent = new Lexeme(String.valueOf(ch), Token.LEFT_PAREN);
                    symbol = true;
                    break;
                case '{':
                    lexemeCurrent = new Lexeme(String.valueOf(ch), Token.LEFT_CURLY);
                    symbol = true;
                    break;
                case ')':
                    lexemeCurrent = new Lexeme(String.valueOf(ch), Token.RIGHT_PAREN);
                    symbol = true;
                    break;
                case '}':
                    lexemeCurrent = new Lexeme(String.valueOf(ch), Token.RIGHT_CURLY);
                    symbol = true;
                    break;
                case '=':
                    lexemeCurrent = new Lexeme(String.valueOf(ch), Token.ASSIGN_OP);
                    symbol = true;
                    break;
                case ';':
                    lexemeCurrent = new Lexeme(String.valueOf(ch), Token.SEMICOLON);
                    symbol = true;
                    break;


            }
        }
        if (!digit && !alphabet && !symbol && !whiteSpace && !eof) { 
            throw new TokenizerException("Denna tecken är in accepterad i detta program -->  " + ch);
        }

    }

    
    @Override
    public void close() throws IOException {
        if(scanner!= null) {
            scanner.close();
        }
    }

}