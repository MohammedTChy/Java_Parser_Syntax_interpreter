//Mohammed Tahmid Chowdhury moch8386

package prop.assignment0;

import java.io.IOException;

public class Parser implements IParser {
    private ITokenizer iTokenizer = null;

    


    @Override
    public void open(String fileName) throws IOException, TokenizerException {
        iTokenizer = new Tokenizer();
        iTokenizer.open(fileName);
        iTokenizer.moveNext();

    }

    @Override
    public INode parse() throws IOException, TokenizerException, ParserException {
        if (iTokenizer == null) {
            throw new IOException("Ingen fil");
        }
        return new AssignmentNode(iTokenizer);
    }

   


    @Override
    public void close() throws IOException {
        if (iTokenizer != null)
            iTokenizer.close();

    }

    
    private class AssignmentNode implements INode {

        private Lexeme firstLexeme = null, secondLexeme = null, thirdLexeme = null;
        private ExpressionNode expr = null;


        public AssignmentNode(ITokenizer iTokenizer) throws ParserException, IOException, TokenizerException {
          
            Lexeme lexemeCurrent;

            lexemeCurrent = iTokenizer.current();


            if (lexemeCurrent.token() == Token.IDENT) {
                this.firstLexeme = lexemeCurrent; 
                iTokenizer.moveNext();
                lexemeCurrent = iTokenizer.current();
            } else {
                throw new ParserException("Något gick fel, kan inte känna igen detta som en id ---> " + iTokenizer.current().toString());
            }

            if (lexemeCurrent.token() == Token.ASSIGN_OP) {
                this.secondLexeme = lexemeCurrent;
                iTokenizer.moveNext();

                this.expr = new ExpressionNode(iTokenizer);
            } else {
                throw new ParserException("Något gick fel, kan inte känna igen detta som en lika med = tecken ---> " + iTokenizer.current().toString());
            }


            lexemeCurrent = iTokenizer.current();

            if (lexemeCurrent.token() == Token.SEMICOLON) {
                this.thirdLexeme = lexemeCurrent; 

            } else {
                throw new ParserException("Något gick fel, känner inte igen tecknet som semiokolon ---> " + iTokenizer.current().toString());
            }


        }//Asign Parse


        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        
        @Override
        public void buildString(StringBuilder b, int t) {
            b.append("\t".repeat(t) + "AssignmentNode\n");
            b.append("\t".repeat(t + 1) + firstLexeme.toString() + "\n");
            b.append("\t".repeat(t + 1) + secondLexeme.toString() + "\n");
            expr.buildString(b, (t + 1));
            b.append("\t".repeat(t + 1) + thirdLexeme.toString() + "\n");
        }

    }//assign node


    
    private class ExpressionNode implements INode {
        private ExpressionNode expressionNode = null;
        private Lexeme firstLexeme = null; 
        private TermNode term = null;


        //Expression parse
        public ExpressionNode(ITokenizer iTokenizer) throws ParserException, IOException, TokenizerException {
            
            Lexeme lexemeCurrent;
            this.term = new TermNode(iTokenizer);
            lexemeCurrent = iTokenizer.current();


           
            if (lexemeCurrent.token() == Token.ADD_OP || lexemeCurrent.token() == Token.SUB_OP) {
                this.firstLexeme = lexemeCurrent;
                iTokenizer.moveNext();
                
                this.expressionNode = new ExpressionNode(iTokenizer);

            }

        }//expr parse

        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        //hur utskriften ska se ut för ExpressionNode
        @Override
        public void buildString(StringBuilder b, int t) {
            b.append("\t".repeat(t) + "ExpressionNode\n");
            term.buildString(b, (t + 1));
            if (firstLexeme != null) {
                b.append("\t".repeat(t + 1) + firstLexeme.toString() + "\n");
                expressionNode.buildString(b, (t + 1));
            }
        }
    }//expr node


    //Term node
    private class TermNode implements INode {
        private TermNode termNode = null;
        private Lexeme firstLexeme = null; 
        private FactorNode factor = null;


        //Term parse
        public TermNode(ITokenizer iTokenizer) throws ParserException, IOException, TokenizerException {
            
            Lexeme lexemeCurrent;
            this.factor = new FactorNode(iTokenizer);
            lexemeCurrent = iTokenizer.current();

           
            if (lexemeCurrent.token() == Token.MULT_OP || lexemeCurrent.token() == Token.DIV_OP) {
                this.firstLexeme = lexemeCurrent;
                iTokenizer.moveNext();
                this.termNode = new TermNode(iTokenizer);

            }


        }//Term parse


        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        //Hur utskriften för TermNode ska se ut
        @Override
        public void buildString(StringBuilder b, int t) {
            b.append("\t".repeat(t) + "TermNode\n");
            factor.buildString(b, (t + 1));
            if (firstLexeme != null) {
                b.append("\t".repeat(t + 1) + firstLexeme.toString() + "\n");
                termNode.buildString(b, (t + 1));
            }

        }
    }//Term node


    //Factor node
    private class FactorNode implements INode {
        private ExpressionNode expressionNode = null;
        private Lexeme firstLexeme = null, secondLexeme = null, thirdLexeme = null; 


        //Factor parse
        public FactorNode(ITokenizer iTokenizer) throws ParserException, IOException, TokenizerException {
            Lexeme lexemeCurrent; 
            lexemeCurrent = iTokenizer.current();

           
            if (lexemeCurrent.token() == Token.INT_LIT) {
                this.thirdLexeme = lexemeCurrent;
                iTokenizer.moveNext();
            } else if (lexemeCurrent.token() == Token.LEFT_PAREN) {
                this.firstLexeme = lexemeCurrent;
                iTokenizer.moveNext();
                this.expressionNode = new ExpressionNode(iTokenizer);
                lexemeCurrent = iTokenizer.current();

                
                if (lexemeCurrent.token() == Token.RIGHT_PAREN) {
                    this.secondLexeme = lexemeCurrent;
                    iTokenizer.moveNext();
                } else {
                    throw new ParserException("Något gick fel, saknar en höger parantes");
                }


            } else {
                throw new ParserException("Något gick fel, kolla om detta tecken är en int ---> " + lexemeCurrent.toString());

            }

        }//Factor parse


        @Override
        public Object evaluate(Object[] args) throws Exception {

            return null;
        }


        
        @Override
        public void buildString(StringBuilder b, int t) {
            b.append("\t".repeat(t) + "FactorNode" + "\n");
            if (expressionNode != null) {
                b.append("\t".repeat(t + 1) + firstLexeme.toString() + "\n");
                expressionNode.buildString(b, (t + 1));
                b.append("\t".repeat(t + 1) + secondLexeme.toString() + "\n");
            } else {
                b.append("\t".repeat(t + 1) + thirdLexeme.toString() + "\n");
            }

        }
    }//factor node


}