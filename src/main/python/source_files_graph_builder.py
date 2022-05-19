# code finds all files with extension .cpp or .h in current directory recursively,
#   build graph of dependencies and write result to file in JSON format
# !!! assumption: all short (i.e. without '/') filenames are different
# vertex is string, consisting from one of those tuples:
#   (cpp_source, cpp_header) or (cpp_source, None) or (None, cpp_header)
# graph is dict, which by vertex get list of adjacent vertices
#
# ideas: 1. specify output file by command line argument
#        2. make option write full filenames to graph vertices
#        3. think on the issue about the same filenames in other directories
#        4. optimize O(n^2) -> O(n) in some places

import os
import json


def is_cpp_source(file):  # file should be a full path
    return file.endswith(".cpp") and file.count("cmake-build-debug") == 0


def is_any_cpp_header(file):
    return file.endswith(".h")


def is_cpp_header(file):  # file should be a full path
    return is_any_cpp_header(file) and file.count("cmake-build-debug") == 0


def is_include(line):
    return line.startswith("#include")


def add_dependencies_from_file(tmp_gr, filename, from_vertex, vertex_by_header):
    with open(filename, 'r') as file:
        for line in file.readlines():
            line.strip()
            if is_include(line):
                parent_header = line[line.rfind(' ') + 2: -2]
                if is_any_cpp_header(parent_header) and \
                        parent_header in vertex_by_header.keys():
                    to_vertex = vertex_by_header[parent_header]
                    connected = tmp_gr.get(from_vertex, set())
                    connected.add(to_vertex)
                    tmp_gr[from_vertex] = connected


sources = {}
headers = {}
for subdir, dirs, files in os.walk("."):
    for filename in files:
        full_filename = os.path.join(subdir, filename)
        if is_cpp_source(full_filename):
            sources[filename] = full_filename
        if is_cpp_header(full_filename):
            headers[filename] = full_filename

vertices = []
for file in sources:
    needed_header = file[:-4] + ".h"
    if needed_header in headers:
        vertices.append((file, needed_header))
    else:
        vertices.append((file, None))

for file in headers:
    needed_source = file[:-2] + ".cpp"
    if (needed_source, file) not in vertices:
        vertices.append((None, file))

vertex_by_header = {}
for s, h in vertices:
    if h is not None:
        vertex_by_header[h] = (s, h)

tmp_gr = {}

for s, h in vertices:
    if s is not None:
        add_dependencies_from_file(tmp_gr, sources[s], (s, h), vertex_by_header)
    if h is not None:
        add_dependencies_from_file(tmp_gr, headers[h], (s, h), vertex_by_header)

gr = {}
for k in tmp_gr:
    dependents = []
    for depended in tmp_gr[k]:
        dependents.append(str(depended))
    gr[str(k)] = dependents

output = open("output.txt", 'w')

print(json.dumps(gr, indent=4), file=output)

output.close()
