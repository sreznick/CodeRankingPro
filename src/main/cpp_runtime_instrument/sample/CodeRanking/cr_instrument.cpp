#include <unistd.h>
#include <cstdio>
#include <string>

namespace CodeRank{
static FILE* trace_output;
static bool made_setup = false;
}

void
__attribute__((constructor))
trace_begin(void) {
    int pid = (int)getpid();
    CodeRank::trace_output = fopen(("trace_" + std::to_string(pid)).data(), "w");
    std::string path_start = "/proc/", path_end = "/maps";
    std::string path = path_start + std::to_string(pid) + path_end;
    FILE* load_data_file = fopen(path.data(), "r");
    char load_addr[100];
    fscanf(load_data_file, "%s", load_addr);
    load_addr[12] = 0;
    fprintf(CodeRank::trace_output, "%s\n", load_addr + 0);
    CodeRank::made_setup = true;
}

extern "C" {
    void __cyg_profile_func_enter(void* func, void* callsite);
    void __cyg_profile_func_exit(void* func, void* callsite);


    void __cyg_profile_func_enter(void* func, void* callsite) {
        if (CodeRank::made_setup)
            fprintf(CodeRank::trace_output, "%p %p\n", func, callsite);
    }
}
