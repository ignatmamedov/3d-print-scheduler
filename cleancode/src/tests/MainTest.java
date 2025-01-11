package tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import saxion.Main;
import saxion.dataprovider.DataProvider;
import saxion.facade.Facade;
import saxion.models.Print;
import saxion.printers.Printer;
import saxion.view.TerminalView;
import saxion.view.View;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.Arguments;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MainTest {

    private FakeUserInput fakeInput;
    private Facade facade;
    private View<String> terminal;
    private Main main;
    private List<Printer> printers;
    private List<Print> prints;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private Map<Print, Integer> printMap;
    private final Map<String, Map<Print, Map<Printer, Boolean>>> results = new HashMap<>();

    MainTest() throws FileNotFoundException {
        DataProvider dataProvider = new DataProvider();
        printers = dataProvider.readFromFile("", Printer.class, true);
        prints = dataProvider.readFromFile("", Print.class, true);

        printMap = new HashMap<>();
        for (int i = 0; i < prints.size(); i++) {
            printMap.put(prints.get(i), i + 1);
        }

        for (Filament filament  : Filament.values()) {
            results.put(filament.name(), new HashMap<>());
            for (Print print : prints) {
                results.get(filament.name()).put(print, new HashMap<>());
            }
        }
    }

    @BeforeEach
    void setUp() {
        fakeInput = new FakeUserInput();
        facade = new Facade();
        terminal = new TerminalView();
        main = new Main(fakeInput, facade, terminal);

        outContent.reset();
        System.setOut(new PrintStream(outContent));
        fakeInput.clearInputs();
    }

    Stream<Arguments> providePrintersAndMaterialsAndPrints() {
        return printers.stream().flatMap(
                printer -> Stream.of(Filament.values())
                        .flatMap(material -> prints.stream()
                                .map(print -> arguments(printer, material, print))
                        )
        );
    }

    @ParameterizedTest(name = "{2} {1} on printer {0}")
    @MethodSource("providePrintersAndMaterialsAndPrints")
    @DisplayName("Dynamic Test for Printers with different materials for each Print")
    void testRun_withPrinter_andMaterial(Printer printer, Filament material, Print print) {
        printer.setTask(null);
        facade.setPrinters(List.of(printer));

        fakeInput.addInput(1);
        fakeInput.addInput(printMap.get(print));
        fakeInput.addInput(material.code);

        int colors;
        switch (printMap.get(print)) {
            case 6, 7 -> colors = 4;
            case 8 -> colors = 3;
            case 9 -> colors = 2;
            default -> colors = 1;
        }
        for (int i = 0; i < colors; i++) {
            fakeInput.addInput(1);
        }

        fakeInput.addInput(5);
        fakeInput.addInput(0);

        main.run(new String[]{});

        String output = outContent.toString();
        System.out.println(output);

        boolean success = output.contains(print.getName() + " " + material.name + " on printer " + printer.getName());
        results.get(material.name()).get(print).put(printer, success);

        assertTrue(success);
    }

    @AfterAll
    void writeResultsToCsv() throws IOException {
        for (String filament : results.keySet()) {
            File file = new File("./tests/" + filament + "_results.csv");
            try (PrintWriter writer = new PrintWriter(file)) {

                writer.print("Print Name");
                for (Printer printer : printers) {
                    writer.print("," + printer.getName());
                }
                writer.println();

                for (Map.Entry<Print, Map<Printer, Boolean>> entry : results.get(filament).entrySet()) {
                    writer.print(entry.getKey().getName());
                    for (Printer printer : printers) {
                        writer.print("," + (entry.getValue().getOrDefault(printer, false) ? "V" : "X"));
                    }
                    writer.println();
                }
            }
        }
    }
}
