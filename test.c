#include<stdio.h>
#define sum(x,y) ((x)+(y))

int main()
{
	int x,y;
	x = 8;
	y = 10;
	printf("%d",sum(x+y,x>y?x*x:y*y));
}
