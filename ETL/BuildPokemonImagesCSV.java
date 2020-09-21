//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.apache.commons:commons-csv:1.8
//JAVA 11

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;

public class BuildPokemonImagesCSV {

    // CSV Configuration
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final CSVFormat CSV_TYPE = CSVFormat.DEFAULT.withFirstRecordAsHeader();
    private static final CSVFormat OUT_CSV_TYPE = CSVFormat.DEFAULT.withFirstRecordAsHeader()
            .withQuoteMode(QuoteMode.NON_NUMERIC);

    // Files Location
    private static final String POKEMON_CSV_FILE = "../pokemon/pokemon.csv";
    private static final String POKEMON_IMG_DIR = "../pokemon/images";
    private static final String POKEMON_IMGS_CSV = "../pokemon/pokemon_images.csv";

    private static final String BASE64_PREFIX = "data:image/png;base64,";

    // Columns
    private static final String CL_NUMBER = "pokedex_number";
    private static final String CL_NAME = "name";
    private static final String CL_SVG = "pokemon_img";
    private static final String CL_CLASSFICATION = "classfication";
    private static final List<String> OUT_COLUMNS = List.of(CL_NUMBER, CL_NAME, CL_SVG, CL_CLASSFICATION);

    public static void main(String... args) throws Exception {
        System.out.println("Starting generating CSV with Pokemon images");
        var pokemonCSVFile = Paths.get(POKEMON_CSV_FILE);
        var parser = CSVParser.parse(pokemonCSVFile, CHARSET, CSV_TYPE);
        var records = parser.getRecords();
        var pokemonImagesCSV = Paths.get(POKEMON_IMGS_CSV);

        Files.delete(pokemonImagesCSV);
        Files.createFile(pokemonImagesCSV);

        var outPrinter = OUT_CSV_TYPE.print(pokemonImagesCSV, CHARSET);

        printTo(outPrinter, OUT_COLUMNS);

        for (CSVRecord pkRecord : records) {
            var number = pkRecord.get(CL_NUMBER);
            var name = pkRecord.get(CL_NAME);
            var classification = pkRecord.get(CL_CLASSFICATION);
            var imgFile = Paths.get(POKEMON_IMG_DIR, number + ".png");
            var imgData = "";
            if (imgFile.toFile().exists()) {
                var imgContent = Files.readAllBytes(imgFile);
                imgData = BASE64_PREFIX.concat(Base64.getEncoder().encodeToString(imgContent));

            }
            printTo(outPrinter, List.of(number, name, imgData, classification));
        }
        outPrinter.close();
        System.out.println("Finishing");
    }

    private static void printTo(CSVPrinter printer, List<String> values) {
        try {
            printer.printRecord(values);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
