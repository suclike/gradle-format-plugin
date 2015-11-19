package com.github.youribonnaffe.gradle.format
/**
 * From https://github.com/krasa/EclipseCodeFormatter
 *
 * @author Vojtech Krasa
 */
public class ImportSorterAdapter {
    public static final int START_INDEX_OF_IMPORTS_PACKAGE_DECLARATION = 7;
    public static final String N = "\n";

    private List<String> importsOrder;

    public ImportSorterAdapter(List<String> importsOrder) {
        this.importsOrder = new ArrayList<String>(importsOrder);
    }

    public ImportSorterAdapter(InputStream importsOrderAsConfigurationFile) {
        this.importsOrder = importsOrderAsConfigurationFile.readLines().
                findAll() { !it.startsWith('#') }.
                collectEntries {
                    def (idx, packageName) = it.split("=")
                    [(idx): packageName]
                }.sort().values() as List<String>
    }

    public String sortImports(String document) {
        // parse file
        Scanner scanner = new Scanner(document);
        int firstImportLine = 0;
        int lastImportLine = 0;
        int line = 0;
        List<String> imports = new ArrayList<String>();
        while (scanner.hasNext()) {
            line++;
            String next = scanner.nextLine();
            if (next == null) {
                break;
            }
            while (next.startsWith("import ")) {
                int i = next.indexOf(".");
                if (isNotValidImport(i)) {
                    break;
                }
                if (firstImportLine == 0) {
                    firstImportLine = line;
                }
                lastImportLine = line;
                int endIndex = next.indexOf(";");
                String importedPackage = next.substring(START_INDEX_OF_IMPORTS_PACKAGE_DECLARATION,
                        endIndex != -1 ? endIndex : next.length())
                imports.add(importedPackage);
                if (endIndex != -1 && endIndex + 1 < next.length())
                    next = next.substring(endIndex + 1)
                else
                    next = ""
            }
        }

        List<String> sortedImports = ImportsSorter.sort(imports, importsOrder);
        return applyImportsToDocument(document, firstImportLine, lastImportLine, sortedImports);
    }

    private static String applyImportsToDocument(final String document, int firstImportLine, int lastImportLine,
                                          List<String> strings) {
        Scanner scanner;
        boolean importsAlreadyAppended = false;
        scanner = new Scanner(document);
        int currentLine = 0;
        final StringBuilder sb = new StringBuilder();
        while (scanner.hasNext()) {
            currentLine++;
            String next = scanner.nextLine();
            if (next == null) {
                break;
            }
            if (currentLine >= firstImportLine && currentLine <= lastImportLine) {
                if (!importsAlreadyAppended) {
                    for (String string : strings) {
                        sb.append(string);
                    }
                }
                importsAlreadyAppended = true;
            } else {
                append(sb, next);
            }
        }
        return sb.toString();
    }

    private static void append(StringBuilder sb, String next) {
        sb.append(next);
        sb.append(N);
    }

    private static boolean isNotValidImport(int i) {
        return i <= START_INDEX_OF_IMPORTS_PACKAGE_DECLARATION;
    }

}