import json

with open('opening_book.json', 'r') as file:
    data = json.load(file)

with open("opening_book.txt", "w") as output_file:
    for key, value in data.items():
        for item in value:
            output_file.write(f"{key}: {item[0]}: {item[1]};\n")