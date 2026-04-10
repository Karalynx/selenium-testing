package org.junit;

public class Tuple<A, B> {
	public A first;
	public B second;
	
	Tuple(A first, B second) {
		this.first = first;
		this.second = second;
	}
}
