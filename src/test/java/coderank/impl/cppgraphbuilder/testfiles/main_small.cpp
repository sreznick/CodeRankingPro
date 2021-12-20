
#include "a.h"


class A {
public:
	int foo(int x) {
		return x + 16;
	}
};



int f(int x){
	A obj;
	int y = obj.foo(x);
	return y / 42;
}


int main() {
	int res = f(84);
	return 0;
}
