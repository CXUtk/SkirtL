package Utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;


public class ASTGenerator {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(1);
        }
        String outputDir = args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
                "Binary   : Expr left, Token operator, Expr right",
                "Grouping : Expr expression",
                "Literal  : Object value",
                "Unary    : Token operator, Expr right",
                "Variable : Token name",
                "Assign   : Token name, Expr value",
                "Logical  : Expr left, Token operator, Expr right",
                "Call     : Expr callee, Token paren, List<Expr> arguments"
        ));
        defineAst(outputDir, "Stmt", Arrays.asList(
                "Block      : ArrayList<Stmt> statements",
                "Expression : Expr expression",
                "Print      : Expr expression",
                "Var        : Token name, Expr initializer",
                "While      : Expr condition, Stmt body",
                "For        : Stmt decl, Expression condition, Stmt upd"
        ));
    }

    private static void defineAst(String outputDir, String base, List<String> types) throws IOException {
        String path = outputDir + "/" + base + ".java";
        File tmpDir = new File(path);
        if(tmpDir.exists()) {
            System.out.println(path + " Already exits.");
            path += "1";
        }
        PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);
        writer.println("package Parsing.AST;");
        writer.println();
        writer.println("import Lexical.Token;");
        writer.println();
        writer.println("public abstract class " + base + " {");
        defineVisitor(writer, base, types);
        // The AST classes.
        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, base, className, fields);
        }
        // The base accept() method.
        writer.println();
        writer.println("  public abstract <R> R accept(Visitor<R> visitor);");

        writer.println("}");
        writer.close();
    }

    private static void defineVisitor(PrintWriter writer, String base, List<String> types) {
        writer.println("  public interface Visitor<R> {");

        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println("    R visit" + typeName + base + "(" +
                    typeName + " " + base.toLowerCase() + ");");
        }

        writer.println("  }");
    }

    private static void defineType(PrintWriter writer, String base, String className, String fieldList) {
        writer.println("  public static class " + className + " extends " +
                base + " {");

        // Constructor.
        writer.println("    public " + className + "(" + fieldList + ") {");

        // Store parameters in fields.
        String[] fields = fieldList.split(", ");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println("      this." + name + " = " + name + ";");
        }

        writer.println("    }");

        // Visitor pattern.
        writer.println();
        writer.println("    public <R> R accept(Visitor<R> visitor) {");
        writer.println("      return visitor.visit" +
                className + base + "(this);");
        writer.println("    }");

        // Fields.
        writer.println();
        for (String field : fields) {
            writer.println("    private final " + field + ";");
        }
        for (String field : fields) {
            writer.println("    private final " + field + ";");
        }

        writer.println("  }");
    }
}
