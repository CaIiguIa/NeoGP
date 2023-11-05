grammar newgp;

program : statement*;

statement
    : loop
    | if
    | ifElse
    | in
    | print
    | var
    | varAssign
    | const
    ;

block: statement*;

loop: 'while' '(' expression ')' '{' block '}';
if: 'if' '('  expression ')' '{' block '}';
ifElse: 'if' '('  expression ')' '{' block '}' 'else' '{' block '}';
in: 'in' ID LEND;
print: 'print' '('  expression ')' LEND;
var: 'var' ID ('=' expression)? LEND;
varAssign: ID '=' expression LEND;
const: 'const' ID '=' expression LEND;

expression
    : '(' expression ')' # ParenthesizedExpression
    | '-' expression # UnaryMinus
    | expression op=('*'|'/') expression # Multiplication
    | expression op=('+'|'-') expression # Addition
    | expression op=('<'|'>'|'<='|'>=') expression # Comparison
    | '!' expression # Negation
    | expression op=('=='|'!=') expression # Equality
    | expression op='&&' expression # LogicAnd
    | expression op='||' expression # LogicOr
    | primary # PrimaryExpression
    ;

primary
    : ID # Identifier
    | NUMBER # NumberLiteral
    | BOOL # BooleanLiteral
    | STRING # StringLiteral
    ;

ID: [a-zA-Z_][a-zA-Z_0-9]*;
NUMBER: [0-9]+;
BOOL: 'true' | 'false';
STRING: '"'  ~('\r' | '\n')* '"';


LEND: ';';

WHITESPACE : [ \t]+ -> skip;
NEWLINE: '\r'? '\n' -> skip;

