import json

with open('opening_book.json', 'r') as file:
    input_json = json.load(file)

 

# Generate Java code
def generate_java_file(json_data, output_file):
    with open(output_file, "w") as java_file:
        # Java file header
        java_file.write("import java.util.*;\n\n")
        java_file.write("public class ChessOpeningBook {\n\n")
        java_file.write("    public static Map<String, List<AbstractMap.SimpleEntry<String, Integer>>> getOpeningBook() {\n")
        java_file.write("        Map<String, List<AbstractMap.SimpleEntry<String, Integer>>> openingBook = new HashMap<>();\n\n")

        # Populate the HashMap
        for fen, moves in json_data.items():
            java_file.write(f"        openingBook.put(\"{fen}\", Arrays.asList(\n")
            move_entries = []
            for move, count in moves:
                move_entries.append(f"            new AbstractMap.SimpleEntry<>(\"{move}\", {count})")
            java_file.write(",\n".join(move_entries) + "\n")
            java_file.write("        ));\n\n")

        # Closing the method and class
        java_file.write("        return openingBook;\n")
        java_file.write("    }\n")
        java_file.write("}\n")

# Run the script
generate_java_file(input_json, "ChessOpeningBook.java")
