package source.parser;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.ParserCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import com.github.javaparser.utils.SourceRoot;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by konstantin on 26.09.2020.
 */
public class JavaParser {
    public static void main(String[] args) throws IOException {
        Path parentRoot = Paths.get("D:\\workspace\\konstantin-examples");
        SourceRoot sourceRoot = new SourceRoot(parentRoot);

        com.github.javaparser.JavaParser javaParser = new com.github.javaparser.JavaParser();
        ParseResult<CompilationUnit> parseResult = javaParser.parse(Paths.get("D:\\workspace\\konstantin-examples\\Database\\src\\test\\java\\dao\\CsvDataProvider.java"));

        List<ParseResult<CompilationUnit>> parseResults = sourceRoot.tryToParse();
        List<CompilationUnit> compilationUnits = sourceRoot.getCompilationUnits();

        // only parsing - aka modules or highlevel packages
        final ProjectRoot projectRoot = new ParserCollectionStrategy().collect(parentRoot);
        // parsing and resolving
        final ProjectRoot projectRoot2 = new SymbolSolverCollectionStrategy().collect(parentRoot);
        int a = 1;


    }
}
