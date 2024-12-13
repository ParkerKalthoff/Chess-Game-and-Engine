with open('opening_book_raw.txt', 'r') as file:
    input_file_txt = file.read()

infoStrs = input_file_txt.split(";")

infoStrs = [s for s in infoStrs if "KQkq -" in s]

output_file_txt = ";".join(infoStrs)

with open('opening_book_raw.txt', 'w') as file:
    file.write(output_file_txt)