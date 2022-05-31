EXE = main
CXX = g++
# add flags (-finstrument-functions-exclude-file-list=/usr/include is optional and needed to prevent from STL functions tracing)
CXX_FLAGS = -std=c++17 -Wall -Iinclude -g -finstrument-functions  -finstrument-functions-exclude-file-list=/usr/include
OBJECTS = obj/main.o obj/utils.o
# this object file can be built outside the Makefile
OBJECT_INSTR = obj/cr_instrument.o


$(EXE): $(OBJECTS) $(OBJECT_INSTR) # new dependency
# link that object file
	$(CXX) -o $@ $(OBJECTS) $(OBJECT_INSTR) 

$(OBJECTS): obj/%.o: src/%.cpp | obj
	$(CXX) $(CXX_FLAGS) -c $< -o $@

# how to build that object file
$(OBJECT_INSTR): CodeRanking/cr_instrument.cpp | obj
	$(CXX) -std=c++17 -c CodeRanking/cr_instrument.cpp -o $(OBJECT_INSTR)

obj:
	mkdir -p obj

clean :
	$(RM) $(EXE)
	$(RM) -rf obj

.PHONY: all clean