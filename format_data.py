import os
import re

for entry in os.scandir('./data/'):
	entry: os.DirEntry
	with open(entry.path, 'r+') as fp:
		# read an store all lines into list
		lines = fp.readlines()
		# move file pointer to the beginning of a file
		fp.seek(0)
		# truncate the file
		fp.truncate()

		# start writing lines
		# iterate line and line number
		for number, line in enumerate(lines):
			# delete line number 5 and 8
			# note: list index start from 0
			if not re.match(r'[A-Z]+', line) and line.split():
				split = (" ".join(line.split())).split()
				fp.write(f'{split[1]} {split[2]}\n')
