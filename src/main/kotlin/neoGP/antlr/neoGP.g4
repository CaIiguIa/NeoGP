grammar neoGP;

program : statement*;

statement
    : ifElse
    | loop
    | if
    | in
    | print
    | var
    | varAssign
    | const
    ;

block: statement*;

ifElse: 'if' '(' expression ')' '{' block '}' 'else' '{' block '}';
loop: 'while' '(' expression ')' '{' block '}';
if: 'if' '(' expression ')' '{' block '}';
in: 'in' ID LEND;
print: 'print' '(' expression ')' LEND;
var: 'var' ID ('=' expression)? LEND;
varAssign: ID '=' expression LEND;
const: 'const' ID '=' expression LEND;

expression
    : primary # PrimaryExpression
    | '(' expression ')' # ParenthesizedExpression
    | '-' expression # UnaryMinus
    | expression op=('*'|'/') expression # Multiplication
    | expression op=('+'|'-') expression # Addition
    | expression op=('<'|'>'|'<='|'>=') expression # Comparison
    | '!' expression # Negation
    | expression op=('=='|'!=') expression # Equality
    | expression op='&&' expression # LogicAnd
    | expression op='||' expression # LogicOr
    ;

primary
    :  BOOL # BooleanLiteral
    | INT # IntLiteral
    | FPNUMBER # FPNumberLiteral
    | ID # Identifier
    ;

BOOL: 'true' | 'false';
ID: [a-zA-Z_][a-zA-Z_0-9]*;
INT: [0-9]+;
FPNUMBER: [0-9]+ '.' [0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9];


LEND: ';';

WHITESPACE : [ \t]+ -> skip;
NEWLINE: '\r'? '\n' -> skip;

