//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.apache.commons:commons-csv:1.8
//JAVA 11
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

public class COVID19DataProcessor {

    private static final int TOTAL_DAYS = 10;
    private static final CSVFormat CSV_TYPE = CSVFormat.DEFAULT.withFirstRecordAsHeader();
    private static final CSVFormat OUT_CSV_TYPE = CSVFormat.DEFAULT.withFirstRecordAsHeader()
                                                                   .withQuoteMode(QuoteMode.NON_NUMERIC);

    private static final int VALUES_STARTING_POS = 4;
    private static final int COUNTRY_POS = 1;
    public static final int LAT_POS = 2;
    public static final int LONG_POS = 3;
    public static final String LOCATION_COLUMN = "Location";
    public static final String RECENT_DATA_COLUMN = "Recent Data";

    static final Charset CHARSET = StandardCharsets.UTF_8;

    static final String ORIGIN_BASE_PATH = "/home/wsiqueir/projects/COVID-19/csse_covid_19_data/csse_covid_19_time_series";
    static final String DEST_BASE_PATH = "/home/wsiqueir/projects/dashbuilder-data/covid19/";

    static final String COVID_19_RECOVERED_FILE = "time_series_covid19_recovered_global.csv";
    static final String COVID_19_CONFIRMED_FILE = "time_series_covid19_confirmed_global.csv";
    static final String COVID_19_DEATHS_FILE = "time_series_covid19_deaths_global.csv";

    private static final String OUT_DEATHS = "covid19_deaths.csv";
    private static final String OUT_CONFIRMED = "covid19_confirmed.csv";
    private static final String OUT_RECOVERED = "covid19_recovered.csv";

    static final Map<String, String> ORIGIN_DEST = Map.of(
                                                          COVID_19_RECOVERED_FILE, OUT_RECOVERED,
                                                          COVID_19_CONFIRMED_FILE, OUT_CONFIRMED,
                                                          COVID_19_DEATHS_FILE, OUT_DEATHS);

    public static void main(String[] args) throws Exception {
        System.out.println("Starting transformation...");
        var paths = new HashMap<Path, Path>();        

        ORIGIN_DEST.forEach((o, d) -> paths.put(Paths.get(ORIGIN_BASE_PATH, o),
                                                Paths.get(DEST_BASE_PATH, d)));

        var baseDir = Paths.get(DEST_BASE_PATH);
        if (!Files.exists(baseDir)) {
            Files.createDirectories(baseDir);
        }

        Map<String, List<Long>> groupingByCountry = new HashMap<>();
        paths.forEach((o, d) -> {
            try {
                Files.deleteIfExists(d);

                var parser = CSVParser.parse(o, CHARSET, CSV_TYPE);
                var records = parser.getRecords();
                var printer = OUT_CSV_TYPE.print(d, CHARSET);
                var headerNames = parser.getHeaderNames();
                var newHeaders = produceHeaders(headerNames);
                var days = Arrays.copyOfRange(headerNames.toArray(), VALUES_STARTING_POS, headerNames.size());

                
                printer.printRecord(newHeaders);

                records.stream().map(record -> record.toMap().values())
                       .map(ArrayList::new)
                       .map(COVID19DataProcessor::keepOnlyLatestData)
                       .peek(COVID19DataProcessor::addNewColumns)
                       .forEach(values -> printTo(printer, values));

                records.stream().forEach(r -> {
                    var countryName = r.get(COUNTRY_POS);
                    var values = r.toMap().values().stream()
                                  .skip(VALUES_STARTING_POS + 1)
                                  .map(v -> {
                                      try {
                                          return Long.valueOf(v);
                                      } catch (Exception e) {
                                          return 0l;
                                      }
                                  })
                                  .collect(Collectors.toList());
                    if (groupingByCountry.get(countryName) != null) {
                        groupingByCountry.computeIfPresent(countryName,
                                                           (k, existingsValues) -> IntStream.range(0, values.size())
                                                                                            .mapToObj(i -> values.get(i) + existingsValues.get(i))
                                                                                            .collect(Collectors.toList()));
                    } else {
                        groupingByCountry.putIfAbsent(countryName, values);
                    }
                });
                printer.close();
                storeGroupingMap(d, days, groupingByCountry);
                groupingByCountry.clear();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println("Finished");
    }

    private static ArrayList<String> produceHeaders(List<String> headerNames) {
        var newHeaders = new ArrayList<String>();
        headerNames.subList(0, VALUES_STARTING_POS).forEach(newHeaders::add);
        newHeaders.add(RECENT_DATA_COLUMN);
        newHeaders.add(LOCATION_COLUMN);
        return newHeaders;
    }
    
    private static List<String> keepOnlyLatestData(List<String> columns) {
        var newList = new ArrayList<String>();
        columns.subList(0, VALUES_STARTING_POS).forEach(newList::add);
        // add only latest value
        newList.add(columns.get(columns.size() - 1));
        return newList;
    }

    private static void storeGroupingMap(Path d, Object[] days, Map<String, List<Long>> groupingByCountry) {
        var countryGroup = d.getFileName().toString().split("\\.")[0] + "_country_group.csv";
        var destination = Paths.get(DEST_BASE_PATH, countryGroup);
        try {
            Files.deleteIfExists(destination);
            var printer = OUT_CSV_TYPE.print(destination, CHARSET);
            var header = new ArrayList<String>();
            var countries = groupingByCountry.keySet();
            header.add("ID");
            header.add("Day");
            header.addAll(countries);
            printer.printRecord(header);
            var total = groupingByCountry.get(countries.iterator().next()).size();
            var records = new ArrayList<List<Object>>(total);

            for (int i = 0; i < total; i++) {
                var list = new ArrayList<Object>();
                list.add(i);
                list.add(days[i]);
                for (String country : countries) {
                    list.add(groupingByCountry.get(country).get(i));
                }
                records.add(list);
            }
            for (List<Object> record : records) {
                printer.printRecord(record);
            }
            printer.close();

        } catch (IOException e) {
            // do nothing
            e.printStackTrace();
        }

    }

    private static void printTo(CSVPrinter printer, List<String> values) {
        try {
            printer.printRecord(values);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addNewColumns(List<String> records) {
        var location = records.get(LAT_POS) + "," + records.get(LONG_POS);
        records.set(0, "-");
        records.add(location);
    }

}
