#include <utils.hpp>

void add1bil(std::size_t& x) {
    for (std::size_t i = 0; i < 1000; ++i){
        add1mil(x);
    }
}

void add1mil(std::size_t& x) {
    for (std::size_t i = 0; i < 1000000; ++i){
        x += 1;
    }
}