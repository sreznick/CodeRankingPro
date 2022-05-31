#include <utils.hpp>
#include <iostream>
#include <cstdlib>

std::size_t foo(std::size_t x) {
    add1bil(x);
    add1mil(x);
    return x;
}

void drop(std::size_t& x) {
    if (x < 20000) return;
    x -= 17000;
    drop(x);
}

std::size_t bar(std::size_t x) {
    std::size_t y = foo(x);
    drop(y);
    return y;
}

int main(int argc, char** argv) {
    std::cout << bar(std::atoi(argv[1])) << std::endl;
}